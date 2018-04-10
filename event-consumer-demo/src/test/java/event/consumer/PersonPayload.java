package event.consumer;

/**
 * @author flyleft
 * @date 2018/4/10
 */
public class PersonPayload {

    private String name;

    private int age;

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
                '}';
    }
}
