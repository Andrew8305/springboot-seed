package com.wind.service;

import com.github.pagehelper.PageHelper;
import com.wind.common.Constant;
import com.wind.mybatis.mapper.UserMapper;
import com.wind.mybatis.pojo.User;
import com.wind.common.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = new User();
        user.setAccount(userName);
        user = userMapper.selectOne(user);
        return new SecurityUser(user);
    }

    public Optional<User> getUserByID(Long id) {
        return Optional.ofNullable(userMapper.selectByPrimaryKey(id));
    }

    public Optional<User> getUserByName(String name) {
        User user = new User();
        user.setAccount(name);
        return Optional.ofNullable(userMapper.selectOne(user));
    }

    public List<User> getAll(int page) {
        PageHelper.startPage(page, Constant.PAGE_SIZE);
        return userMapper.selectAll();
    }

    public int getCount() {
        int count = userMapper.selectCount(new User());
        // count = (count % Constant.PAGE_SIZE == 0) ? (count / Constant.PAGE_SIZE) : (count / Constant.PAGE_SIZE + 1);
        return count;
    }

    @Transactional
    public boolean addUser(User user) {
        return userMapper.insertUseGeneratedKeys(user) > 0;
    }

    @Transactional
    public boolean modifyUserById(User user) {
        return userMapper.updateByPrimaryKey(user) > 0;
    }

    @Transactional
    public boolean deleteUserById(Long id) {
        return userMapper.deleteByPrimaryKey(id) > 0;
    }
}