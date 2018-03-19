package com.wind.web.service;

import com.wind.common.MD5;
import com.wind.mybatis.mapper.UserMapper;
import com.wind.mybatis.pojo.User;
import com.wind.common.SecurityUser;
import com.wind.web.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class UserService extends BaseService<User> implements UserDetailsService {

    @Autowired
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = new User();
        user.setUsername(userName);
        user = userMapper.selectOne(user);
        return new SecurityUser(user);
    }

    public Optional<User> selectByName(String username) {
        User user = new User();
        user.setUsername(username);
        return Optional.ofNullable(userMapper.selectOne(user));
    }

    @Transactional
    @Override
    public boolean add(User user) {
        String md5 = MD5.getMD5(user.getPassword());
        user.setPassword(md5);
        return userMapper.insertUseGeneratedKeys(user) > 0;
    }

    @Transactional
    @Override
    public boolean modifyById(User user) {
        User original = userMapper.selectByPrimaryKey(user);
        if (!user.getPassword().equals(original.getPassword()))
        {
            String md5 = MD5.getMD5(user.getPassword());
            user.setPassword(md5);
        }
        return userMapper.updateByPrimaryKey(user) > 0;
    }
}