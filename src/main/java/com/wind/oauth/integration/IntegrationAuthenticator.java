package com.wind.oauth.integration;

import com.wind.mybatis.pojo.User;
import com.wind.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class IntegrationAuthenticator {

    @Autowired
    private UserService userService;

    public User authenticate(IntegrationAuthentication integrationAuthentication) {
        return userService.selectByName(integrationAuthentication.getUsername()).get();
    }

    /**
     * 进行预处理
     *
     * @param integrationAuthentication
     */
    public void prepare(IntegrationAuthentication integrationAuthentication) {
    }

    /**
     * 判断是否支持集成认证类型
     *
     * @param integrationAuthentication
     * @return
     */
    public boolean support(IntegrationAuthentication integrationAuthentication) {
        return StringUtils.isEmpty(integrationAuthentication.getAuthType());
    }

    /**
     * 认证结束后执行
     *
     * @param integrationAuthentication
     */
    public void complete(IntegrationAuthentication integrationAuthentication) {
    }

}