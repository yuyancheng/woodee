package com.sf.kh.exception;

import code.ponfee.commons.exception.BasicException;
import code.ponfee.commons.model.ResultCode;

/**
 * @Auther: 01378178
 * @Date: 2018/6/19 17:28
 * @Description:
 */
public class BusinessException extends BasicException {

    private static final long serialVersionUID = 3808428327292617923L;

    public BusinessException(Integer code) {
        super(code);
    }

    public BusinessException(Integer code, String message) {
        super(code, message);
    }

    public BusinessException(ResultCode code) {
        super(code.getCode(), code.getMsg());
    }

    public BusinessException(ResultCode code, String message) {
        super(code.getCode(), message);
    }
}
