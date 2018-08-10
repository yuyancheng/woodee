package com.sf.kh.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.sf.kh.model.User;
import com.sf.kh.model.UserDept;

/**
 * The user mapper of mybatis
 *
 * @author Ponfee
 */
public interface UserMapper {

    int batchInsert(List<User> userList);

    int update(User user);

    int changeStatus(@Param("id") long id, 
                     @Param("updateBy") long updateBy, 
                     @Param("status") int status);

    int logicDelete(@Param("uids") long[] uids, 
                    @Param("updateBy") long updateBy);

    boolean checkUsernameIsExists(String username);

    User getByUsername(String username);

    User getById(long id);

    List<Map<String, Object>> query4list(Map<String, ?> params);

    // -----------------------------------------------用户单位
    int batchInsertUserDept(List<UserDept> list);

    int deleteUserDept(Long userId);

    int updateUserDept(UserDept userDept);

    int getDeptUserCount(Long deptId);

}
