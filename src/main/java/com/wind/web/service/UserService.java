package com.wind.web.service;

import com.wind.mybatis.pojo.User;
import com.wind.web.BaseService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService extends BaseService<User> {

    public Optional<User> selectByName(String username) {
        User user = new User();
        user.setUsername(username);
        return Optional.ofNullable(mapper.selectOne(user));
    }

    public Optional<User> selectByPhone(String phone) {
        User user = new User();
        user.setPhone(phone);
        return Optional.ofNullable(mapper.selectOne(user));
    }

    public Optional<User> selectByEmail(String email) {
        User user = new User();
        user.setEmail(email);
        return Optional.ofNullable(mapper.selectOne(user));
    }

    public Optional<User> selectByOpenId(String openId) {
        User user = new User();
        user.setOpenId(openId);
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
        if (!user.getPassword().equals(original.getPassword())) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (!encoder.matches(user.getPassword(), original.getPassword())) {
                String crypt = encoder.encode(user.getPassword());
                user.setPassword(crypt);
            }
        }
        return mapper.updateByPrimaryKey(user) > 0;
    }
}