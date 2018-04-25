package event.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.HashMap;
import java.util.Map;

/**
 * @author flyleft
 * @date 2018/4/12
 */
public class JacksonTest {

    private ObjectMapper mapper = new ObjectMapper();
    @Test
    public void testType() throws Exception {
        PersonPayload personPayload = new PersonPayload("sdsdsd",23);
        personPayload.setList(Arrays.asList("dkkdkkdkf","dfdfdf","dfdf"));
        personPayload.setPersonList(Arrays.asList(new Person("kskdksdkkdkd",222)));
        Map<String, Person> personMap = new HashMap<>();
        personMap.put("kkkkk", new Person("kskdksdkkdkd",2222022));
        personPayload.setMap(personMap);
        EventPayload<PersonPayload> eventPayload = new EventPayload<>();
        eventPayload.setData(personPayload);
        eventPayload.setUuid("sdsd");
        eventPayload.setBusinessType("person");

        String json = mapper.writeValueAsString(eventPayload);
        System.out.println(json);
        EventPayload<PersonPayload> eventPayload1 = mapper.readValue(json,  new TypeReference<EventPayload<PersonPayload>>(){});
        System.out.println(eventPayload1.getData());
    }

    @Test
    public void testType2() throws Exception {
        PersonPayload personPayload = new PersonPayload("sdsdsd",23);
        personPayload.setList(Arrays.asList("dkkdkkdkf","dfdfdf","dfdf"));
        personPayload.setPersonList(Arrays.asList(new Person("kskdksdkkdkd",222)));
        Map<String, Person> personMap = new HashMap<>();
        personMap.put("kkkkk", new Person("kskdksdkkdkd",2222022));
        personPayload.setMap(personMap);
        EventPayload<PersonPayload> eventPayload = new EventPayload<>();
        eventPayload.setData(personPayload);
        eventPayload.setUuid("sdsd");
        eventPayload.setBusinessType("person");
        Method method = PersonService.class.getMethods()[0];
        TypeReference typeReference = getTypeReference(method);
        String json = mapper.writeValueAsString(eventPayload);
        System.out.println(json);
        EventPayload<PersonPayload> eventPayload1 = mapper.readValue(json,  typeReference);
        System.out.println(eventPayload1.getData());
    }

    public static TypeReference getTypeReference(final Method method) throws CannotFindTypeReferenceException {
        Type[] types = new Type[1];
        Type paramType = method.getGenericParameterTypes()[0];
        if (paramType instanceof ParameterizedType) {
            ParameterizedType newType = (ParameterizedType)paramType;
            types[0] = newType.getActualTypeArguments()[0];
        }
        if (types[0] == null) {
            throw new CannotFindTypeReferenceException(paramType.getTypeName());
        }
        final ParameterizedType type = ParameterizedTypeImpl.make(EventPayload.class, types, EventPayload.class.getDeclaringClass());
        return new TypeReference<EventPayload>() {
            @Override
            public Type getType() {
                return type;
            }
        };
    }

}
