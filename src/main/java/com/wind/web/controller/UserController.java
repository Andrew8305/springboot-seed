package com.wind.web.controller;

import com.wind.common.*;
import com.wind.web.ExtendController;
import com.wind.web.service.*;
import com.wind.mybatis.pojo.User;
import io.swagger.annotations.*;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController extends ExtendController<User> {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "新增用户")
    @PostMapping
    public ResponseEntity<?> post(@RequestBody User user) {
        Optional<User> result = userService.selectByName(user.getUsername());

        if (!result.isPresent()) {
            userService.add(user);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(user.getId())
                    .toUri();
            return ResponseEntity.created(location).body(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @ApiOperation(value = "修改用户")
    @PutMapping
    public ResponseEntity<?> put(@RequestBody User user) {
        userService.modifyById(user);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(user);
    }

    @ApiOperation(value = "修改密码")
    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordForm form) {
        OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userService.selectByName(((SecurityUser) auth.getPrincipal()).getUsername());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (user.isPresent() && encoder.matches(form.oldPassword, user.get().getPassword())) {
            User instance = user.get();
            instance.setPassword(form.newPassword);
            userService.modifyById(instance);
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Data
    static class ChangePasswordForm {
        private String oldPassword;
        private String newPassword;
    }
}