package com.feel.common.interceptor;

import com.feel.common.interceptor.annotation.PreventDuplicatePostToken;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author yuweijun 2016-08-23
 */
public class PostTokenInterceptor implements HandlerInterceptor {

    private static final String POST_TOKEN = "postToken";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            PreventDuplicatePostToken annotation = method.getAnnotation(PreventDuplicatePostToken.class);
            if (annotation != null) {
                if ("GET".equals(request.getMethod().toUpperCase())) {
                    setPostToken(request);
                    return true;
                } else if ("POST".equals(request.getMethod().toUpperCase())) {
                    HttpSession session = request.getSession(false);
                    if (session == null) {
                        return false;
                    }
                    synchronized (session) {
                        if (isRepeatSubmit(request)) {
                            response.sendRedirect(request.getContextPath() + "/index.html");
                            response.setStatus(301);
                            return false;
                        }
                        // 相同session的一个用户提交反馈之后，立即删除token，重复提交的被取消
                        session.removeAttribute(POST_TOKEN);
                    }
                    return true;
                }
            }
        }
        return true;
    }

    void setPostToken(HttpServletRequest request) {
    	HttpSession session = request.getSession(false);
    	if (session == null) {
    		//favicon.ico结尾的不拦截（http://127.0.0.1:8090/wechat/favicon.ico）
    		if ("favicon.ico".equals(request.getRequestURL().substring(request.getRequestURL().length()-11)))	{
    			return;
    		}
    		session = request.getSession(true);
    	}
    	if (session != null) {
    		session.setAttribute(POST_TOKEN, UUID.randomUUID().toString());
    	}
    }

    private boolean isRepeatSubmit(HttpServletRequest request) {
        String serverToken = (String) request.getSession(false).getAttribute(POST_TOKEN);
        if (serverToken == null) {
            return true;
        }
        String clientToken = request.getParameter(POST_TOKEN);
        if (clientToken == null) {
            return true;
        }
        if (!serverToken.equals(clientToken)) {
            return true;
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
