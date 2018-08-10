package com.sf.kh.util;

import code.ponfee.commons.jce.CryptoProvider;
import code.ponfee.commons.jce.digest.DigestUtils;
import code.ponfee.commons.jce.passwd.BCrypt;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.model.ResultCode;

/**
 * The common utility
 * 
 * @author 01367825
 */
public class CommonUtils {

    // RSA加密组件
    private static final CryptoProvider RSA_CRYPTOR = CryptoProvider.rsaPrivateKeyProvider(
        "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJNdfWIuGIykj+blQJiD/m90Ve7s"
      + "5YXN8Up453C3nJJdGMIQbMqpybc2GBRxjwgul2rBZNrLQQvWzNOhhTKyjVdhwYNDpzCzffS6vTMk"
      + "MQfwBULhn7OcNorppNAmIDsnfx45enaEEwIDsgLkeAQL7+C98ZVifVgQi1MA9tFXabjtAgMBAAEC"
      + "gYA4RCb+mLkY4jBrqbbbRPKeHZ4+BsewKizqEXXU7NZkcBVm65yOWPvC722MSLujFR8NTOA0Rg0E"
      + "9zPnzjEsZU5fquN/fXSxii72t0DAp9TKCiYrgaBqyyk9aN9wJt4fPfAwojSLe2218V6wjEmi73Jh"
      + "+dSRXdMlaaRmcW3uQByvIQJBAOqVtncmAU4mXbx6NR6+MCtlckukzxGxaTQXdMJ7c2pls8GTy7D9"
      + "93ES2U0rp5Roo3dwsR20byVG81NG/qg6zZkCQQCg0XCHluo6ZjM7hyOfec0MCre840QZXpEWpFrn"
      + "v5qMrwhKpjpexMdOSQBUzsyFjRjfb1q8IS1KzV7kd8Cd3RJ1AkAhnF3OaIWbWufnT2M5CmsAbMKZ"
      + "j/2TPWYjbpDuJJ3+yp+cxr5Sl7DSZK753Z1fKDbzsBPQe7/JCYdnkS5/kB1RAkEAn2yhOrlgQD6a"
      + "oXOp2+m6XSqu1UETfHbkLGcIe1/VzYujE6XQjaxTzIbQHkLYO7kRpEbW01Ose9A2NxWETLshFQJB"
      + "AJifdD1Y2PKbd6Kxyr1UYF15ohSUoxX+/pkZu8YafUVNX4tkhvsRTglAQrACT1IoKk3hxg+fXA0y"
      + "lL7YQBOI8EU="
    );

    /**
     * Crypt password as BCrypt hashed
     * 
     * @param password the password of user input
     * @return password bcrypt hashed
     */
    public static String cryptPassword(String password) {
        // 密码sha1后再进行bcrypt
        return BCrypt.create(DigestUtils.sha1(password), 2);
    }

    /**
     * Checks the password
     * 
     * @param password the password of user input
     * @param hashed   the hashed password of t_user.password value
     * @return
     */
    public static boolean checkPassword(String password, String hashed) {
        return BCrypt.check(DigestUtils.sha1(password), hashed);
    }

    /**
     * Returns the passowrd plain with RSA decrypt
     * 
     * @param password the password RSA encrypted
     * @return the plain password
     */
    public static String decryptPassword(String password) {
        try {
            return RSA_CRYPTOR.decrypt(password);
        } catch (Exception e) {
            throw new IllegalArgumentException("密码无效");
        }
    }

    /**
     * Checks the database operator for create update delete result
     * 
     * @param affectedRows the affected row number
     * @return the status of Result code
     */
    public static Result<Void> oneRowAffected(int affectedRows) {
        if (affectedRows == Constants.ONE_ROW_AFFECTED) {
            return Result.SUCCESS;
        } else {
            return Result.failure(ResultCode.OPS_CONFLICT, "操作失败");
        }
    }


    public static void main(String[] args) {
        String psw = "khPSW@bj123";

        System.out.println(cryptPassword(psw));
        System.out.println(checkPassword(psw, "$2a$02$xvfIZE578h0tK1pBl-c-oA$wsncywIYJWDrSsHD04XyTafrl2ocQWnP"));
    }
}
