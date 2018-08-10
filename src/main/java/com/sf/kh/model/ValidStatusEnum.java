package com.sf.kh.model;

/**
 * @Auther: 01378178
 * @Date: 2018/6/14 17:23
 * @Description:
 */

public enum ValidStatusEnum {

    VALID(1), INVALID(2);

    int code;

    ValidStatusEnum(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }


}