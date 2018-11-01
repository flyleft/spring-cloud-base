package me.flyleft.eureka.client.instance;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.shared.Applications;
import javassist.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class EurekaListenerHandler {

    private final Set<String> serviceInstanceIds = Collections.synchronizedSet(new HashSet<>());

    private static EurekaListenerHandler instance = new EurekaListenerHandler();

    private EurekaInstanceObservable observable = new EurekaInstanceObservable();

    private static final String APPLICATION_PATH = "com.netflix.discovery.shared.Application";

    private EurekaListenerHandler() {
    }

    public static EurekaListenerHandler getInstance() {
        return instance;
    }

    public void eurekaAddInstance(final InstanceInfo instanceInfo) {
        if (instanceInfo.getStatus() == InstanceInfo.InstanceStatus.UP
                && !serviceInstanceIds.contains(instanceInfo.getId())) {
            serviceInstanceIds.add(instanceInfo.getId());
            observable.sendEvent(new CloudInstanceChangePayload(instanceInfo));
        }
    }

    public void eurekaRemoveInstance(final InstanceInfo instanceInfo) {
        if (instanceInfo.getStatus() == InstanceInfo.InstanceStatus.DOWN
                && serviceInstanceIds.contains(instanceInfo.getId())) {
            serviceInstanceIds.remove(instanceInfo.getId());
            observable.sendEvent(new CloudInstanceChangePayload(instanceInfo));
        }
    }

    public EurekaInstanceObservable getObservable() {
        return observable;
    }

    public void start() {
        try {
            ClassPool classPool = new ClassPool(true);
            //添加com.netflix.discovery包的扫描路径
            ClassClassPath classPath = new ClassClassPath(Applications.class);
            classPool.insertClassPath(classPath);
            //获取要修改Application类
            CtClass ctClass = classPool.get(APPLICATION_PATH);
            //获取addInstance方法
            CtMethod addInstanceMethod = ctClass.getDeclaredMethod("addInstance");
            //修改addInstance方法
            addInstanceMethod.setBody("{instancesMap.put($1.getId(), $1);"
                    + "synchronized (instances) {me.flyleft.eureka.client.instance.EurekaListenerHandler.getInstance().eurekaAddInstance($1);" +
                    "instances.remove($1);instances.add($1);isDirty = true;}}");
            //获取removeInstance方法
            CtMethod removeInstanceMethod = ctClass.getDeclaredMethod("removeInstance");
            //修改removeInstance方法
            removeInstanceMethod.setBody("{me.flyleft.eureka.client.instance.EurekaListenerHandler.getInstance().eurekaRemoveInstance($1);this.removeInstance($1, true);}");
            //覆盖原有的Application类
            ctClass.toClass();
            //使用类加载器重新加载Application类
            classPool.getClassLoader().loadClass(APPLICATION_PATH);
            Class.forName(APPLICATION_PATH);
        } catch (Exception e) {
            throw new EurekaEventAddException(e);
        }

    }
}
