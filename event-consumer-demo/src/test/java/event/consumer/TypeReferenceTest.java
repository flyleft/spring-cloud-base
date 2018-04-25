package event.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import io.choerodon.core.event.EventPayload;
import io.choerodon.event.consumer.exception.CannotFindTypeReferenceException;
import me.jcala.eureka.event.consumer.domain.Person;
import me.jcala.eureka.event.consumer.domain.PersonPayload;
import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * @author flyleft
 * @date 2018/4/12
 */
public class TypeReferenceTest {

    @Test
    public void testTypeReference() throws Exception {
        PersonPayload personPayload = new PersonPayload("sdsdsd", 23);
        personPayload.setList(Arrays.asList("288282", "sdsdsds", "lllsooosd"));
        personPayload.setPersonList(Arrays.asList(new Person("7777df", 10), new Person("sjkkdsd8838", 89)));
        EventPayload<PersonPayload> eventPayload = new EventPayload<>();
        eventPayload.setData(personPayload);
        eventPayload.setUuid("sdsd");
        eventPayload.setBusinessType("sdsdddddddd");
        String jsonString = JSON.toJSONString(eventPayload);
        EventPayload payload = JSON.parseObject(jsonString, getRealType());
        System.out.println(payload.getData().getClass());
    }

    private TypeReference<EventPayload> getRealType() throws Exception {
        Class<?> claz = PersonService.class;
        Method[] methods = claz.getMethods();
        Type type = methods[0].getGenericParameterTypes()[0];

        return getTypeReference(methods[0]);
    }
    public static TypeReference<EventPayload> getTypeReference(final Method method) throws CannotFindTypeReferenceException {
        Type[] types = new Type[1];
        Type paramType = method.getGenericParameterTypes()[0];
        if (paramType instanceof ParameterizedType) {
            ParameterizedType newType = (ParameterizedType) paramType;
            types[0] = newType.getActualTypeArguments()[0];
        }
        if (types[0] == null) {
            throw new CannotFindTypeReferenceException(paramType.getTypeName());
        }
        final ParameterizedType trueType = ParameterizedTypeImpl.make(EventPayload.class, types, EventPayload.class.getDeclaringClass());
        return new TypeReference<EventPayload>() {
            @Override
            public Type getType() {
                return trueType;
            }
        };
    }


}
