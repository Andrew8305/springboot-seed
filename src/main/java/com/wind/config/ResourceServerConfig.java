package com.wind.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/v2/**","/swagger**", "/druid/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/user/{id}").permitAll()
                    .antMatchers("/user/password").authenticated()
                    .antMatchers("/user/**").hasAuthority("admin")
                    .anyRequest().permitAll();
        }
}