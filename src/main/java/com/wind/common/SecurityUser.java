package com.wind.common;

import com.wind.mybatis.pojo.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;


@Data
public class SecurityUser implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private String authType;
    private String email;
    private String phone;
    private String openId;
    private String unionId;
    private String sessionKey;
    private String city;
    private String province;
    private String country;
    private String nickname;
    private Short gender;
    private String language;
    private String avatarUrl;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean accountNonLocked;
    private Set<GrantedAuthority> authorities;

    public SecurityUser(User user) {
        if (user != null) {
            setId(user.getId());
            setUsername(user.getUsername());
            setPassword(user.getPassword());
            setEnabled(user.getEnabled());
            setAuthType(user.getAuthType());
            setEmail(user.getEmail());
            setPhone(user.getPhone());
            setOpenId(user.getOpenId());
            setUnionId(user.getUnionId());
            setSessionKey(user.getSessionKey());
            setCity(user.getCity());
            setProvince(user.getProvince());
            setCountry(user.getCountry());
            setNickname(user.getNickname());
            setGender(user.getGender());
            setLanguage(user.getLanguage());
            setAvatarUrl(user.getAvatarUrl());
            setAccountNonExpired(user.getEnabled());
            setCredentialsNonExpired(user.getEnabled());
            setAccountNonLocked(user.getEnabled());
            setAuthorities(new HashSet<>());
            getAuthorities().add(new SimpleGrantedAuthority(user.getAuthority()));
        } else {
            throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
        }
    }

}
