package com.wind.common;

import com.wind.mybatis.pojo.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;


@Getter
@ToString
public class SecurityUser extends User implements UserDetails {

    public SecurityUser(User user) {
        if (user != null) {
            setId(user.getId());
            setUsername(user.getUsername());
            setAuthType(user.getAuthType());
            setEmail(user.getEmail());
            setPhone(user.getPhone());
            setOpenId(user.getOpenId());
            setUnionId(user.getUnionId());
            setSessionKey(user.getSessionKey());
            setNickname(user.getNickname());
            setAvatarUrl(user.getAvatarUrl());
            setAuthority(user.getAuthority());
            setCity(user.getCity());
            setProvince(user.getProvince());
            setCountry(user.getCountry());
            setGender(user.getGender());
            setLanguage(user.getLanguage());
            setRegisterDate(user.getRegisterDate());
            setEnabled(user.getEnabled());
        } else {
            throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
        }
    }

    @Override
    public boolean isEnabled() {
        return getEnabled();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> result = new ArrayList<>();
        result.add(new SimpleGrantedAuthority(this.getAuthority()));
        return result;
    }

    @Override
    public boolean isAccountNonExpired() {
        return getEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return getEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return getEnabled();
    }

}
