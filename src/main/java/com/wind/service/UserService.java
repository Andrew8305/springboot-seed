package com.wind.service;

import com.github.pagehelper.PageHelper;
import com.wind.common.Constant;
import com.wind.common.MD5;
import com.wind.mybatis.mapper.UserMapper;
import com.wind.mybatis.pojo.User;
import com.wind.common.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = new User();
        user.setUsername(userName);
        user = userMapper.selectOne(user);
        return new SecurityUser(user);
    }

    public Optional<User> getUserByID(Long id) {
        return Optional.ofNullable(userMapper.selectByPrimaryKey(id));
    }

    public Optional<User> getUserByName(String name) {
        User user = new User();
        user.setUsername(name);
        return Optional.ofNullable(userMapper.selectOne(user));
    }

    public List<User> getAll(int page) {
        PageHelper.startPage(page, Constant.PAGE_SIZE);
        return userMapper.selectAll();
    }

    public List<User> getAll(String type, String value, int page) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike(type, "%" + value + "%");
        PageHelper.startPage(page, Constant.PAGE_SIZE);
        return userMapper.selectByExample(example);
    }

    public int getCount() {
        int count = userMapper.selectCount(new User());
        return count;
    }

    public int getCount(String type, String value) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike(type, "%" + value + "%");
        int count = userMapper.selectCountByExample(example);
        return count;
    }

    @Transactional
    public boolean addUser(User user) {
        String md5 = MD5.getMD5(user.getPassword());
        user.setPassword(md5);
        return userMapper.insertUseGeneratedKeys(user) > 0;
    }

    @Transactional
    public boolean modifyUserById(User user) {
        User original = userMapper.selectByPrimaryKey(user);
        if (!user.getPassword().equals(original.getPassword()))
        {
            String md5 = MD5.getMD5(user.getPassword());
            user.setPassword(md5);
        }
        return userMapper.updateByPrimaryKey(user) > 0;
    }

    @Transactional
    public boolean deleteUserById(Long id) {
        return userMapper.deleteByPrimaryKey(id) > 0;
    }
}