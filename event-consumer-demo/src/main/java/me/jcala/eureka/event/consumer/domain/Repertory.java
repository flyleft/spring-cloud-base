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

    private String type;

    private Long num;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public Repertory() {
    }

    public Repertory(String type) {
        this.type = type;
    }
}
