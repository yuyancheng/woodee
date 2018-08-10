package com.sf.kh.dao;

import java.util.List;
import java.util.Map;

import com.sf.kh.dto.UserReceiveDto;
import com.sf.kh.model.User;
import com.sf.kh.model.UserDept;

import code.ponfee.commons.model.Page;

/**
 * The database operator interface for user table
 *
 * @author Ponfee
 */
public interface IUserDao {

    int batchAdd(List<User> userList) ;

    int update(User user);

    int changeStatus(long id, long updateBy, int status);

    int logicDelete(long[] uids, long updateBy);

    boolean checkUsernameIsExists(String username);

    User getByUsername(String username);

    User getById(Long userId);

    Page<Map<String, Object>> query4page(Map<String, ?> params);

    // -----------------------------------------------用户单位
    int batchInsertUserDept(List<UserDept> list);

    int deleteUserDept(Long userId);

    int updateUserDept(UserDept userDept);
    
    UserReceiveDto getUserInfoByDeptId(Long deptId);

    int getDeptUserCount(Long deptId);
}
