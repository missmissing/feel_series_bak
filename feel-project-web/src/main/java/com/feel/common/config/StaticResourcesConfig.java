package com.feel.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * @author yuweijun 2017-04-11
 */

@Configuration
@PropertySource({"file:${catalina.home}/conf/application.properties", "file:${catalina.home}/conf/mq-client.properties"})
public class StaticResourcesConfig {

    /**
     * <bean id="resourceVersion" class="com.lyancafe.wechat.util.ResourcePathExposer" init-method="init">
     * <property name="environmentMode" value="${wechat.environment.mode}"/>
     * <property name="gaEnableStatus" value="${wechat.ga.enable.status}"/>
     * <property name="mtaEnableStatus" value="${wechat.mta.enable.status}"/>
     * </bean>
     */
    /**
     * java 方式配置，效果类似上面注释掉的 xml配置
     * 代表 容易托管了 类型为 ResourcePathExposer，名字为resourcePathExpoSer的bean，假设 此bean内有autowire，也会去注入（systemConfig）
     * @param environment
     * @return
     */
    @Bean(initMethod = "init")
    public ResourcePathExposer resourcePathExpoSer(Environment environment) {
        String environmentMode = environment.getProperty("wechat.environment.mode");
        String gaEnableStatus = environment.getProperty("wechat.ga.enable.status");
        String mtaEnableStatus = environment.getProperty("wechat.mta.enable.status");

        System.out.println("========"+environment);

        ResourcePathExposer resourcePathExposer = new ResourcePathExposer();
        resourcePathExposer.setEnvironmentMode(environmentMode);
        resourcePathExposer.setGaEnableStatus(Integer.parseInt(gaEnableStatus));
        resourcePathExposer.setMtaEnableStatus(Integer.parseInt(mtaEnableStatus));
        return resourcePathExposer;
    }
}
