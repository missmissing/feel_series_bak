package com.feel.common.interceptor.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来控制表单post的重复提交问题:
 *
 * 在GET和POST方法中使用@PreventDuplicatePostToken, 在view页面上的表单里添加此postToken到hidden域里, 通过post提交到服务器
 * 服务器验证此postToken提交有效性并从session中删除
 *
 * @author yuweijun 2016-08-23
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface PreventDuplicatePostToken {
}
