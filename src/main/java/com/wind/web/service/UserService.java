package com.wind.web.service;

import com.wind.common.MD5;
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
@Component("userDetailsService")
public class UserService extends BaseService<User> implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = new User();
        user.setUsername(userName);
        user = mapper.selectOne(user);
        return new SecurityUser(user);
    }

    public Optional<User> selectByName(String username) {
        User user = new User();
        user.setUsername(username);
        return Optional.ofNullable(mapper.selectOne(user));
    }

    @Transactional
    @Override
    public boolean add(User user) {
        String md5 = MD5.getMD5(user.getPassword());
        user.setPassword(md5);
        return mapper.insertUseGeneratedKeys(user) > 0;
    }

    @Transactional
    @Override
    public boolean modifyById(User user) {
        User original = mapper.selectByPrimaryKey(user);
        if (!user.getPassword().equals(original.getPassword()))
        {
            String md5 = MD5.getMD5(user.getPassword());
            user.setPassword(md5);
        }
        return mapper.updateByPrimaryKey(user) > 0;
    }
}