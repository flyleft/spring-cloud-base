package me.flyleft.mybatis.page.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import me.flyleft.mybatis.page.domain.User;
import me.flyleft.mybatis.page.domain.UserDto;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author flyleft
 * @date 2018/5/9
 */
public interface UserMapper extends BaseMapper<User> {

    @Select({"SELECT mybatis_user.id, ",
            "mybatis_user.username, ",
            "mybatis_user.password, ",
            "mybatis_role.name, ",
            "mybatis_role.level ",
            "FROM mybatis_user ",
            "LEFT JOIN mybatis_user_role ON mybatis_user_role.user_id = mybatis_user.id ",
            "LEFT JOIN mybatis_role ON mybatis_user_role.role_id = mybatis_role.id"
//            "WHERE mybatis_role.id = #{role_id}"
    })
    List<UserDto> selectRolesByUserId();

}
