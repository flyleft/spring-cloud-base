package me.flyleft.mybatis.page.controller;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import lombok.extern.slf4j.Slf4j;
import me.flyleft.mybatis.page.domain.Role;
import me.flyleft.mybatis.page.domain.User;
import me.flyleft.mybatis.page.domain.UserDto;
import me.flyleft.mybatis.page.domain.UserRole;
import me.flyleft.mybatis.page.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author flyleft
 * @date 2018/5/7
 */
@RestController
@Slf4j
public class RoleController {

    @Autowired
    private RoleService roleService;


    @GetMapping("/roles")
    public Page<Role> listUserRoles(
            @RequestParam("user_id") Long userId,
            @SortDefault(value = "id", direction = Sort.Direction.ASC) PageRequest pageRequest) {
        return roleService.listUserRoles(userId, pageRequest);
    }

    @GetMapping("/users")
    public Page<UserDto> listUsers(
            @RequestParam(value = "role_id", required = false) Long roleId,
            @SortDefault(value = "id", direction = Sort.Direction.ASC) PageRequest pageRequest) {
        return roleService.listUsers(roleId, pageRequest);
    }

}
