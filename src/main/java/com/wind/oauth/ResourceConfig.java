package com.wind.oauth;

import com.wind.define.roleType;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
        http
                .authorizeRequests().antMatchers("/me/**")
                .hasAnyAuthority(roleType.user.toString(), roleType.admin.toString(), roleType.root.toString())
                .antMatchers("/rest/**")
                .hasAnyAuthority(roleType.admin.toString(), roleType.root.toString())
                .anyRequest().permitAll()
                .and().csrf().disable();
    }
}
