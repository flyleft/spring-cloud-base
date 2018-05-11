package me.flyleft.mybatis.page.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import me.flyleft.mybatis.page.domain.Role;
import me.flyleft.mybatis.page.domain.User;
import me.flyleft.mybatis.page.domain.UserDto;


/**
 * @author flyleft
 * @date 2018/5/9
 */
public interface RoleService {

    Page<Role> listUserRoles(Long userId, PageRequest pageRequest);

    Page<UserDto> listUsers(Long roleId, PageRequest pageRequest);

}
