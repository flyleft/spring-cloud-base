package me.flyleft.mybatis.page.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import me.flyleft.mybatis.page.domain.Role;
import me.flyleft.mybatis.page.domain.UserDto;
import me.flyleft.mybatis.page.mapper.RoleMapper;
import me.flyleft.mybatis.page.mapper.UserMapper;
import me.flyleft.mybatis.page.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author flyleft
 * @date 2018/5/9
 */
@Service
public class RoleServiceImpl implements RoleService {

    private UserMapper userMapper;

    private RoleMapper roleMapper;

    private UserRoleMapper userRoleMapper;

    @Autowired
    public RoleServiceImpl(UserMapper userMapper, RoleMapper roleMapper, UserRoleMapper userRoleMapper) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.userRoleMapper = userRoleMapper;
    }

    @Override
    public Page<Role> listUserRoles(Long userId, PageRequest page) {

        return PageHelper.doPage(page.getPage(), page.getSize(), () -> roleMapper.selectRolesByUserId(userId));
    }

    @Override
    public Page<UserDto> listUsers(Long roleId, PageRequest page) {
        return  PageHelper.doPageAndSort(page, () -> userMapper.selectRolesByUserId());
    }

}
