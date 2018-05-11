package me.flyleft.mybatis.page.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author flyleft
 * @date 2018/5/9
 */
@NoArgsConstructor
@Getter
@Setter
@Table(name = "mybatis_role")
public class Role {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String level;

    public Role(String name, String level) {
        this.name = name;
        this.level = level;
    }
}
