package com.sf.kh.auth;

import static code.ponfee.commons.model.ResultCode.BAD_REQUEST;
import static code.ponfee.commons.model.ResultCode.FORBIDDEN;
import static code.ponfee.commons.model.ResultCode.NOT_FOUND;
import static code.ponfee.commons.model.ResultCode.REDIRECT;
import static code.ponfee.commons.model.ResultCode.UNAUTHORIZED;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.ImmutableMap;
import com.sf.kh.model.User;
import com.sf.kh.service.IUserService;
import com.sf.kh.util.CommonUtils;
import com.sf.kh.util.WebContextHolder;
import com.sf.kh.web.CaptchaServlet;

import code.ponfee.commons.http.HttpParams;
import code.ponfee.commons.io.Files;
import code.ponfee.commons.jedis.JedisClient;
import code.ponfee.commons.limit.RequestLimiter;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.web.WebUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

/**
 * The Authorization Filter based Jwt, 
 * include authentication and url permission
 * 
 * @author 01367825
 */
public class JwtAuthorizationFilter extends AuthorizationFilter {

    private static Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    private static final String COOKIE_PATH = "/";

    private String loginAction; // the server login api
    private String logoutAction; // the server logout api
    private boolean loginWithCaptcha; // login whether use captcha
    private String successUrl; // the default location a user is sent after login

    private JwtManager jwtManager;
    private IUserService userService;
    private JedisClient jedisClient;

    /**
     * 预处理
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response)
        throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        WebUtils.cors(req, resp);

        // 跨域时会先发送一个option请求，则直接返回正常状态
        if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
            resp.setStatus(HttpStatus.OK.value());
            return false;
        }

        String requestURI = super.getPathWithinApplication(request);
        if (StringUtils.equals(this.loginAction, requestURI)) {
            return login(req, resp); // login process
        } else if (StringUtils.equals(this.logoutAction, requestURI)) {
            return logout(req, resp); // logout process
        }

        return super.preHandle(request, response);
    }

    /**
     * 权限认证
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, 
                                      Object mappedValue) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String requestURI = super.getPathWithinApplication(request);
        if ("/".equals(requestURI)
            || UrlPermissionMatcher.isNotMapping(requestURI)) {
            return true; // is spring mvc controller url mapping, no need login
        }

        // verify the jwt for authentication
        Jws<Claims> jws;
        try {
            jws = jwtManager.verify(WebUtils.getCookie(req, WebUtils.AUTH_COOKIE));
        } catch (Exception e) {
            // verify fail: unlogin or jwt expire
            logger.info("Verify jwt occur exception.", e);
            response(req, resp, getLoginUrl(), Result.failure(REDIRECT, "会话超时，请重新登录"));
            return false;
        }

        // verify the permission for authorization
        String username = jws.getBody().getSubject();
        User user = userService.getByUsername(username).getData();
        String fail = (user == null) 
                      ? "用户不存在" 
                      : user.isDeleted() 
                      ? "用户已删除" 
                      : user.getStatus() == User.STATUS_DISABLE 
                      ? "用户被锁定" 
                      : null;
        if (fail != null) {
            response(req, resp, super.getUnauthorizedUrl(), Result.failure(UNAUTHORIZED, fail));
            return false;
        }

        // check the user has spec url permission
        if (UrlPermissionMatcher.hasNotPermission(requestURI, user.getId())) {
            response(req, resp, super.getUnauthorizedUrl(), Result.failure(UNAUTHORIZED));
            return false;
        }

        // check whether renew jwt
        String renewJwt = (String) jws.getBody().get(JwtManager.RENEW_JWT);
        if (renewJwt != null) {
            WebUtils.addCookie(resp, WebUtils.AUTH_COOKIE, renewJwt, 
                               COOKIE_PATH, jwtManager.getJwtExpSeconds());
        }
        WebContextHolder.currentUser(user);

        return true;
    }

    /**
     * 权限认证失败后的处理
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        //return super.onAccessDenied(request, response);
        return false; // direct return false
    }

    /**
     * Do login Action
     * 
     * @param req
     * @param resp
     * @return
     * @throws IOException
     */
    private boolean login(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!verifyCaptcha(req, resp)) {
            response(req, resp, super.getLoginUrl(), Result.failure(BAD_REQUEST, "验证码错误"));
            return false;
        }

        String username = req.getParameter("username");
        if (StringUtils.isBlank(username)) {
            response(req, resp, super.getLoginUrl(), Result.failure(BAD_REQUEST, "用户名不能为空"));
            return false;
        }
        String password = req.getParameter("password");
        if (StringUtils.isBlank(password)) {
            response(req, resp, super.getLoginUrl(), Result.failure(BAD_REQUEST, "密码不能为空"));
            return false;
        }
        try {
            password = CommonUtils.decryptPassword(password);
        } catch (Exception ignored) {
            response(req, resp, super.getLoginUrl(), Result.failure(BAD_REQUEST, "密码无效"));
            return false;
        }

        // 校验是否登录频繁
        String loginTraceKey = "lg:fl:" + username;
        RequestLimiter limiter = RequestLimiter.create(jedisClient);
        if (limiter.countAction(loginTraceKey) > 5) {
            response(req, resp, super.getLoginUrl(), Result.failure(FORBIDDEN, "登录频繁，请稍后再试"));
            return false;
        }

        User user = userService.getByUsername(username).getData();
        if (user == null || !CommonUtils.checkPassword(password, user.getPassword())) {
            if (user != null) {
                // 防止密码枚举攻击
                limiter.traceAction(loginTraceKey, 60);
            }
            response(req, resp, super.getLoginUrl(), Result.failure(NOT_FOUND, "用户名或密码错误"));
            return false;
        }

        String fail = user.isDeleted() 
                      ? "用户已删除"
                      : user.getStatus() == User.STATUS_DISABLE
                      ? "用户被锁定" 
                      : null;
        if (fail != null) {
            response(req, resp, super.getLoginUrl(), Result.failure(FORBIDDEN, fail));
            return false;
        }

        // create a jwt
        String jwt = jwtManager.create(username);

        // revoke the oldness jwt
        jwtManager.revoke(WebUtils.getCookie(req, WebUtils.AUTH_COOKIE));

        // response the header cookie
        WebUtils.addCookie(resp, WebUtils.AUTH_COOKIE, jwt, COOKIE_PATH, 
                           jwtManager.getJwtExpSeconds());

        // 重置登录失败次数
        limiter.resetAction(loginTraceKey);

        response(req, resp, successUrl, Result.SUCCESS); // login success
        return false;
    }

    /**
     * Logout
     * 
     * @param req
     * @param resp
     * @return
     * @throws IOException
     */
    private boolean logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        jwtManager.revoke(WebUtils.getCookie(req, WebUtils.AUTH_COOKIE));
        WebUtils.delCookie(req, resp, WebUtils.AUTH_COOKIE);
        response(req, resp, getLoginUrl(), Result.SUCCESS); // logout success
        return false;
    }

    /**
     * Verify the login captcha
     * 
     * @param req
     * @param resp
     * @return
     */
    private boolean verifyCaptcha(HttpServletRequest req, HttpServletResponse resp) {
        if (!loginWithCaptcha) {
            return true;
        }

        String captcid = req.getParameter(CaptchaServlet.CAPTCID);
        String captcne = req.getParameter(CaptchaServlet.CAPTCNE);
        String captcha = req.getParameter(CaptchaServlet.CAPTCHA);

        return StringUtils.isNoneBlank(captcid, captcne, captcha)
            && RequestLimiter.verifyNonce(captcne, captcha, captcid)
            && RequestLimiter.create(jedisClient).checkCaptcha(captcid, captcha);
    }

    /**
     * Response for client
     * 
     * @param req
     * @param resp
     * @param redirectUrl  the redirect url
     * @param json         the json data if ajax request
     * @throws IOException
     */
    private void response(HttpServletRequest req, HttpServletResponse resp, 
                          String redirectUrl, Result<?> json) throws IOException {
        if (WebUtils.isAjax(req)) {
            WebUtils.respJson(resp, json);
        } else {
            String context = org.apache.shiro.web.util.WebUtils.getContextPath(req);
            redirectUrl = context + redirectUrl;
            String url = HttpParams.buildUrlPath(redirectUrl, Files.UTF_8, 
                                                 ImmutableMap.of("msg", json.getMsg()));
            resp.sendRedirect(resp.encodeRedirectURL(url));
        }
    }

    public void setLoginAction(String loginAction) {
        this.loginAction = loginAction;
    }

    public void setLogoutAction(String logoutAction) {
        this.logoutAction = logoutAction;
    }

    public void setLoginWithCaptcha(boolean loginWithCaptcha) {
        this.loginWithCaptcha = loginWithCaptcha;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public void setJwtManager(JwtManager jwtManager) {
        this.jwtManager = jwtManager;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    public void setJedisClient(JedisClient jedisClient) {
        this.jedisClient = jedisClient;
    }

}
