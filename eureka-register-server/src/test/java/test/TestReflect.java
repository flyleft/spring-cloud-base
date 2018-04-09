package test;

import org.junit.Test;

import java.lang.reflect.Method;

/**
 * @author flyleft
 * @date 2018/3/19
 */
public class TestReflect {

    @Test
    public void testMethod() {
        Class my = My.class;
        Class myyy = Myyy.class;
        Method[] ymethods  = my.getMethods();
        for (Method method : ymethods) {
            Class returnt = method.getReturnType();
        }

        Method[] yymethods  = myyy.getDeclaredMethods();
        for (Method method : ymethods) {
            Class returnt = method.getReturnType();
        }
    }
}
