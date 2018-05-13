package com.wind.web.service;

import com.wind.mybatis.pojo.User;
import com.wind.common.SecurityUser;
import com.wind.web.BaseService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class UserService extends BaseService<User>{

    public Optional<User> selectByName(String username) {
        User user = new User();
        user.setUsername(username);
        return Optional.ofNullable(mapper.selectOne(user));
    }

    @Transactional
    @Override
    public boolean add(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String crypt = encoder.encode(user.getPassword());
        user.setPassword(crypt);
        return mapper.insertUseGeneratedKeys(user) > 0;
    }

    @Transactional
    @Override
    public boolean modifyById(User user) {
        User original = mapper.selectByPrimaryKey(user);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(user.getPassword(), original.getPassword()))
        {
            String crypt = encoder.encode(user.getPassword());
            user.setPassword(crypt);
        }
        return mapper.updateByPrimaryKey(user) > 0;
    }
}