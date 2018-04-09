package test;

/**
 * @author flyleft
 * @date 2018/3/19
 */
public interface Yyy<T> {

    default public T dtoToEntity(T dto) {
        return null;
    }

}
