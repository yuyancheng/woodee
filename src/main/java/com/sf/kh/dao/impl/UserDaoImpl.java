package com.sf.kh.dao.impl;

import static code.ponfee.commons.util.MessageFormats.format;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.sf.kh.dao.IUserDao;
import com.sf.kh.dao.mapper.UserMapper;
import com.sf.kh.dao.mapper.UserReceiveDtoMapper;
import com.sf.kh.dao.mapper.UserRoleMapper;
import com.sf.kh.dto.UserReceiveDto;
import com.sf.kh.model.User;
import com.sf.kh.model.UserDept;
import com.sf.kh.util.Constants;

import code.ponfee.commons.jedis.JedisClient;
import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.PageHandler;

/**
 * The IUserDao implementation class
 *
 * @author Ponfee
 */
@Repository
public class UserDaoImpl implements IUserDao {

    private static final int CACHE_USER_TM = 86400;
    private static final String KEY_PREFIX = "user:cache:";
    private static final String UNAME_KEY_PREFIX = KEY_PREFIX + "unm:#{uname}";

    private @Resource UserMapper mapper;
    private @Resource UserRoleMapper userRolemapper;
    private @Resource JedisClient jedis;
    
    private @Resource UserReceiveDtoMapper userReceiveDtoMapper;

    @Override
    public int batchAdd(List<User> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return 0;
        }
        return mapper.batchInsert(userList);
    }

    @Override
    public int update(User user) {
        int rows = mapper.update(user);
        invalidUserCache(user.getId());
        return rows;
    }

    @Override
    public int changeStatus(long uid, long updateBy, int status) {
        int rows = mapper.changeStatus(uid, updateBy, status);
        invalidUserCache(uid);
        return rows;
    }

    @Override
    public int logicDelete(long[] uids, long updateBy) {
        int rows = mapper.logicDelete(uids, updateBy);
        if (rows >= Constants.ONE_ROW_AFFECTED) {
            for (long uid : uids) {
                invalidUserCache(uid);
            }
        }
        return rows;
    }

    @Override
    public boolean checkUsernameIsExists(String username) {
        return mapper.checkUsernameIsExists(username);
    }

    @Override
    public User getByUsername(String username) {
        String key = format(UNAME_KEY_PREFIX, username);
        User user = jedis.valueOps().getObject(key, User.class, CACHE_USER_TM);
        if (user == null) {
            user = mapper.getByUsername(username);
            if (user != null) {
                user.setRoles(userRolemapper.queryByUserId(user.getId()));
                jedis.valueOps().setObject(key, user, CACHE_USER_TM);
            }
        }
        return user;
    }

    @Override public User getById(Long userId) {
        return mapper.getById(userId);
    }

    @Override
    public Page<Map<String, Object>> query4page(Map<String, ?> params) {
        PageHandler.NORMAL.handle(params);
        return new Page<>(mapper.query4list(params));
    }

    // -----------------------------------------------用户单位
    @Override
    public int batchInsertUserDept(List<UserDept> list) {
        int rows = mapper.batchInsertUserDept(list);
        for (UserDept ud : list) {
            invalidUserCache(ud.getUserId());
        }
        return rows;
    }

    @Override
    public int deleteUserDept(Long userId) {
        int rows = mapper.deleteUserDept(userId);
        invalidUserCache(userId);
        return rows;
    }

    @Override
    public int updateUserDept(UserDept userDept) {
        int rows = mapper.updateUserDept(userDept);
        invalidUserCache(userDept.getUserId());
        return rows;
    }

    private void invalidUserCache(long userId) {
        User user = mapper.getById(userId);
        if (user != null) {
            jedis.keysOps().del(format(UNAME_KEY_PREFIX, user.getUsername()));
        }
    }
    
    @Override
    public UserReceiveDto getUserInfoByDeptId(Long deptId) {
    	UserReceiveDto result = new UserReceiveDto();
    	UserReceiveDto temp = userReceiveDtoMapper.getUserInfoByDeptId(deptId);
    	if(null == temp){
    		return null;
    	}
    	result.setProvinceName(this.isNullReturn(temp.getProvinceName()));
    	result.setCityName(this.isNullReturn(temp.getCityName()));
    	result.setAreaName(this.isNullReturn(temp.getAreaName()));
    	result.setAddressDetail(this.isNullReturn(temp.getAddressDetail()));
    	result.setNickName(this.isNullReturn(temp.getNickName()));
    	result.setMobilePhone(this.isNullReturn(temp.getMobilePhone()));
    	return result;
    }

    @Override
    public int getDeptUserCount(Long deptId) {
        return mapper.getDeptUserCount(deptId);
    }

    private String isNullReturn(String temp){
    	if(null == temp){
    		return "";
    	}else{
    		return temp;
    	}
    }
}
