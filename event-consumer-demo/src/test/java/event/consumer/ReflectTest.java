package event.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.choerodon.core.event.EventPayload;
import me.jcala.eureka.event.consumer.domain.MoneyPayload;
import me.jcala.eureka.event.consumer.domain.PersonPayload;
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
        eventPayload.setData(personPayload);
        eventPayload.setUuid("sdsd");
        eventPayload.setBusinessType("sdsdddddddd");
        String json = mapper.writeValueAsString(eventPayload);
        System.out.println(json);
        EventPayload<?> eventPayload1 = null;
        try {
            eventPayload1 = JSON.parseObject("ssd", EventPayload.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(eventPayload1);
    }

    @Test
    public void payload() throws Exception{
        MoneyPayload moneyPayload = new MoneyPayload();
        moneyPayload.setAccount(10L);
        moneyPayload.setMoneyFromUserId(20L);
        moneyPayload.setMoneyToUserId(22L);
        EventPayload<MoneyPayload> payload = new EventPayload<>();
        payload.setUuid("s82822992999sd");
        payload.setBusinessType("money");
        payload.setData(moneyPayload);
        String json = mapper.writeValueAsString(payload);
        System.out.println(json);
        EventPayload<?> data =  mapper.readValue(json,  new TypeReference<EventPayload<MoneyPayload>>(){});
        System.out.println(data.getData());
    }

    public  String getBusinessType (String message) {
        String businessType = null;
        try {
            businessType = mapper.readTree(message).get("businessType").toString();
            businessType = businessType.substring(1, businessType.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return businessType;
    }


}
