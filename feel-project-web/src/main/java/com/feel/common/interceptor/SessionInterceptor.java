package com.feel.common.interceptor;

import com.feel.common.interceptor.annotation.SecurityForInternalTest;
import com.feel.common.metadata.Constants;
import com.feel.common.util.ServletUtils;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

/**
 * @author raynor
 *         <p>
 *         2016年8月15日
 */
public class SessionInterceptor extends HandlerInterceptorAdapter {

    private final static String SESSION_KEY = "sessionId";

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {
        MDC.remove(SESSION_KEY);
    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) throws Exception {

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null) {
            // favicon.ico结尾的不拦截（http://127.0.0.1:8090/wechat/favicon.ico）
            String favicon = request.getRequestURL().substring(request.getRequestURL().length() - 11);
            if ("favicon.ico".equals(favicon)) {
                return false;
            }
            session = request.getSession(true);
        }
        String requestedSessionId = session.getId();
        if (!Strings.isNullOrEmpty(requestedSessionId)) {
            MDC.put(SESSION_KEY, requestedSessionId + "-" + getCustomerId(request));
        }

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            // TestController user validation
            SecurityForInternalTest annotation = method.getAnnotation(SecurityForInternalTest.class);
            if (annotation != null) {
                Object developer = session.getAttribute("developer");
                if (developer == null) {
                    response.sendRedirect(request.getContextPath() + "/test/login");
                    response.setStatus(301);
                    return false;
                }
            }
        }

        return true;
    }

    private String getCustomerId(HttpServletRequest request) {
        String value = getSessionName(request, Constants.COOKIE_CUSTOMERID);
        if (StringUtils.isNotEmpty(value)) {
            return value;
        }
        return getCookieName(request, Constants.COOKIE_CUSTOMERID);
    }

    private String getCookieName(HttpServletRequest request, String cookieName) {
        Cookie cookie = ServletUtils.getCookie(request, cookieName);
        return cookie == null ? "" : cookie.getValue();
    }

    private String getSessionName(HttpServletRequest request, String sessionName) {
        HttpSession session = request.getSession();
        return (session == null || session.getAttribute(sessionName) == null) ? "" : session.getAttribute(sessionName).toString();
    }

}
