package com.wind.oauth.integration;

import com.wind.common.SecurityUser;
import com.wind.mybatis.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class IntegrationUserDetailsService implements UserDetailsService {

    @Autowired
    private Collection<IntegrationAuthenticator> authenticators;

    @Override
    public SecurityUser loadUserByUsername(String username) throws UsernameNotFoundException {
        IntegrationAuthentication integrationAuthentication = IntegrationAuthenticationContext.get();
        //判断是否是集成登录
        if (integrationAuthentication == null) {
            integrationAuthentication = new IntegrationAuthentication();
        }
        integrationAuthentication.setUsername(username);
        User user = this.authenticate(integrationAuthentication);

        if(user == null){
            throw new UsernameNotFoundException("用户名错误");
        }

        return new SecurityUser(user);
    }

    private User authenticate(IntegrationAuthentication integrationAuthentication) {
        if (this.authenticators != null) {
            for (IntegrationAuthenticator authenticator : authenticators) {
                if (authenticator.support(integrationAuthentication)) {
                    return authenticator.authenticate(integrationAuthentication);
                }
            }
        }
        return null;
    }
}