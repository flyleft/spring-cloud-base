package me.flyleft.mybatis.page.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import me.flyleft.mybatis.page.domain.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author flyleft
 * @date 2018/5/9
 */
public interface RoleMapper extends BaseMapper<Role> {

    @Select({"SELECT * FROM mybatis_role " ,
            "LEFT JOIN mybatis_user_role ON mybatis_user_role.role_id = mybatis_role.id ",
            "WHERE mybatis_user_role.user_id = #{user_id}"})
    List<Role> selectRolesByUserId(@Param("user_id") Long userId);

}
