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

    private final String username;
    private final boolean enabled;
    private final boolean accountNonExpired;
    private final boolean credentialsNonExpired;
    private final boolean accountNonLocked;
    private final Set<GrantedAuthority> authorities;

    public SecurityUser(User user) {
        if (user != null) {
            setId(user.getId());
            username = user.getUsername();
            setPassword(user.getPassword());
            setRegisterdate(user.getRegisterdate());
            setAuthority(user.getAuthority());
            setEnabled(user.getEnabled());
            this.enabled = user.getEnabled();
            this.accountNonExpired = user.getEnabled();
            this.credentialsNonExpired = user.getEnabled();
            this.accountNonLocked = user.getEnabled();
            this.authorities = new HashSet<>();
            this.authorities.add(new SimpleGrantedAuthority(user.getAuthority()));

        } else {
            throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
        }
    }

}
