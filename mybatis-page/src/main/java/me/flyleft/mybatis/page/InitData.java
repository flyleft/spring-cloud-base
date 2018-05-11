package me.flyleft.mybatis.page;

import me.flyleft.mybatis.page.domain.Role;
import me.flyleft.mybatis.page.domain.User;
import me.flyleft.mybatis.page.domain.UserRole;
import me.flyleft.mybatis.page.mapper.RoleMapper;
import me.flyleft.mybatis.page.mapper.UserMapper;
import me.flyleft.mybatis.page.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

/**
 * @author flyleft
 * @date 2018/5/9
 */
@Component
@Transactional
public class InitData {

    private UserMapper userMapper;

    private RoleMapper roleMapper;

    private UserRoleMapper userRoleMapper;

    @Autowired
    public InitData(UserMapper userMapper, RoleMapper roleMapper, UserRoleMapper userRoleMapper) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.userRoleMapper = userRoleMapper;
    }

    @PostConstruct
    public void initData() {
        User user1 = new User("one", "one");
        User user2 = new User("two", "two");
        User user3 = new User("three", "three");
        User user4 = new User("44", "44");
        User user5 = new User("55", "55");
        User user6 = new User("66", "66");
        User user7 = new User("77", "77");
        User user8 = new User("88", "88");
        User user9 = new User("99", "99");
        User user10 = new User("1010", "1010");
        User user11 = new User("1111", "1111");

        userMapper.insertSelective(user1);
        userMapper.insertSelective(user2);
        userMapper.insertSelective(user3);
        userMapper.insertSelective(user4);
        userMapper.insertSelective(user5);
        userMapper.insertSelective(user6);
        userMapper.insertSelective(user7);
        userMapper.insertSelective(user8);
        userMapper.insertSelective(user9);
        userMapper.insertSelective(user10);
        userMapper.insertSelective(user11);

        Role role1 = new Role("sitAdmin","sit");
        Role role2 = new Role("orgAdmin","org");
        Role role3 = new Role("projectAdmin","project");
        roleMapper.insertSelective(role1);
        roleMapper.insertSelective(role2);
        roleMapper.insertSelective(role3);

        UserRole userRole1 = new UserRole(user1.getId(), role1.getId());
        UserRole userRole2 = new UserRole(user1.getId(), role3.getId());
        UserRole userRole3 = new UserRole(user2.getId(), role2.getId());
        UserRole userRole4 = new UserRole(user2.getId(), role3.getId());
        UserRole userRole5 = new UserRole(user3.getId(), role1.getId());
        UserRole userRole6 = new UserRole(user3.getId(), role2.getId());
        UserRole userRole7 = new UserRole(user3.getId(), role3.getId());
        userRoleMapper.insertSelective(userRole1);
        userRoleMapper.insertSelective(userRole2);
        userRoleMapper.insertSelective(userRole3);
        userRoleMapper.insertSelective(userRole4);
        userRoleMapper.insertSelective(userRole5);
        userRoleMapper.insertSelective(userRole6);
        userRoleMapper.insertSelective(userRole7);
    }

}
