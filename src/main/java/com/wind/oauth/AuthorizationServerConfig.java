package com.wind.oauth;

import com.wind.config.DruidAutoConfig;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties(OAuth2Properties.class)
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {


    @Autowired
    private OAuth2Properties oAuth2Properties;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public TokenStore getTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenStore(getTokenStore())
                .tokenEnhancer(new CustomTokenEnhancer())
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }

    /**
     * 配置客户端
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        InMemoryClientDetailsServiceBuilder build = clients.inMemory();
        if (ArrayUtils.isNotEmpty(oAuth2Properties.getClients())) {
            for (OAuth2ClientProperties config : oAuth2Properties.getClients()) {
                String password = passwordEncoder.encode(config.getClientSecret());
                build.withClient(config.getClientId())
                        .secret(password)
                        .accessTokenValiditySeconds(config.getAccessTokenValiditySeconds())
                        .refreshTokenValiditySeconds(config.getRefreshTokenValiditySeconds())
                        .authorizedGrantTypes("refresh_token", "password", "authorization_code")
                        .scopes("all");
            }
        }
    }
}