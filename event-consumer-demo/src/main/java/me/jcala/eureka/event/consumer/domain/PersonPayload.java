package me.jcala.eureka.event.consumer.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author flyleft
 * @date 2018/4/10
 */
public class PersonPayload {

    private String name;

    private int age;

    private List<String> list;

    private List<Person> personList;

    private Map<String, Person> map = new HashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public PersonPayload() {
    }



    public PersonPayload(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "PersonPayload{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", list=" + list +
                ", personList=" + personList +
                ", map=" + map +
                '}';
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }

    public Map<String, Person> getMap() {
        return map;
    }

    public void setMap(Map<String, Person> map) {
        this.map = map;
    }
}
