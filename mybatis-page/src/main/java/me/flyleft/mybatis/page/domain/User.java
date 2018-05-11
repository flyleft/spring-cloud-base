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
@Table(name = "mybatis_user")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
