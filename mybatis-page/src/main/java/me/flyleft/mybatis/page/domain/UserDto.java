package me.flyleft.mybatis.page.domain;

import lombok.Data;

/**
 * @author flyleft
 * @date 2018/5/9
 */
@Data
public class UserDto {

    private Long id;

    private String username;

    private String password;

    private String name;

    private String level;

}
