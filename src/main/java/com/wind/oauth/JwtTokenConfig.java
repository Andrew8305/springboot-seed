package com.wind.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@ConditionalOnProperty(prefix = "oauth2", name = "storeType", havingValue = "jwt", matchIfMissing = true)
public class JwtTokenConfig{

    @Autowired
    private OAuth2Properties oAuth2Properties;

    /**
     * 使用jwtTokenStore存储token
     * @return
     */
    @Bean
    public TokenStore jwtTokenStore(){
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * 用于生成jwt
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setSigningKey(oAuth2Properties.getJwtSigningKey());
        return accessTokenConverter;
    }
}