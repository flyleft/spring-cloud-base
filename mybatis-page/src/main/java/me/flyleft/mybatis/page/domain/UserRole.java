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
@Table(name = "mybatis_user_role")
public class UserRole {

    @Id
    @GeneratedValue
    private Long id;

    private Long userId;

    private Long roleId;

    public UserRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
