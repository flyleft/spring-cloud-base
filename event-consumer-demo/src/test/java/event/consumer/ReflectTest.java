package event.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author flyleft
 * @date 2018/4/10
 */
public class ReflectTest {

    private ObjectMapper mapper = new ObjectMapper();

    private Type getRealType() {
        Class<?> claz = PersonService.class;
        Method[] methods = claz.getMethods();
        Type type =  methods[0].getGenericParameterTypes()[0];
        if (type instanceof ParameterizedType) {
            ParameterizedType newType = (ParameterizedType)type;
            Type realType = newType.getActualTypeArguments()[0];
            return realType;
        }
        return null;
    }

    @Test
    public void jsonTest () throws Exception{
        Type[] types = new Type[1];
        types[0] = getRealType();
        final ParameterizedTypeImpl type = ParameterizedTypeImpl.make(EventPayload.class, types, EventPayload.class.getDeclaringClass());
        TypeReference typeReference = new TypeReference<EventPayload>() {
            @Override
            public Type getType() {
                return type;
            }
        };
        PersonPayload personPayload = new PersonPayload("sdsdsd",23);
        EventPayload<PersonPayload> eventPayload = new EventPayload<>();
        eventPayload.setT(personPayload);
        eventPayload.setUuid("sdsd");
        eventPayload.setType("sdsdddddddd");
        String json = mapper.writeValueAsString(eventPayload);
        EventPayload<?> eventPayload1 = mapper.readValue(json, typeReference);
        System.out.println(eventPayload1);
    }

}
