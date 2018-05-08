package com.wind.oauth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        AuthenticationManager manager = super.authenticationManagerBean();
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
        http
                .authorizeRequests().antMatchers("/h2/**", "/static/**", "/druid/**").permitAll()
                .antMatchers("/swagger**", "/swagger-resources/**", "/webjars/**", "/v2/**").permitAll()
                .antMatchers("/login/**", "/oauth**", "/logout/**").permitAll()
                //.anyRequest().authenticated()
                //.and().formLogin().permitAll()   // uncomment when using authorization_code
                .and().csrf().disable();
    }
}