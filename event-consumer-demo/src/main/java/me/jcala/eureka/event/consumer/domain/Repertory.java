package me.jcala.eureka.event.consumer.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author flyleft
 * @date 2018/4/9
 */
@Entity
@Table(name = "repertory_tb")
public class Repertory {

    @Id
    @GeneratedValue
    private Long id;

    private String itemType;

    private Long num;

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public Repertory() {
    }

    public Repertory(String itemType) {
        this.itemType = itemType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Repertory{" +
                "id=" + id +
                ", itemType='" + itemType + '\'' +
                ", num=" + num +
                '}';
    }
}
