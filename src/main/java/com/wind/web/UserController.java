package com.wind.web;

import com.wind.common.Constant;
import com.wind.common.PaginatedResult;
import com.wind.common.SecurityUser;
import com.wind.exception.ResourceNotFoundException;
import com.wind.mybatis.pojo.User;
import com.wind.service.UserService;
import io.swagger.annotations.*;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.Console;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "获取用户详情")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return userService
                .getUserByID(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException()
                        .setResourceName(Constant.RESOURCE_USER)
                        .setId(id));
    }

    @ApiOperation(value = "获取用户列表")
    @GetMapping("/all/{page}")
    public ResponseEntity<?> search(
            @RequestParam(value = "type", required = false, defaultValue = "") String type,
            @RequestParam(value = "value", required = false, defaultValue = "") String value,
            @PathVariable int page) {
        if ("".equals(type)) {
            return ResponseEntity
                    .ok(new PaginatedResult()
                            .setData(userService.getAll(page))
                            .setCurrentPage(page)
                            .setCount(userService.getCount()));
        } else {
            assert ("account".equals(type));
            return ResponseEntity
                    .ok(new PaginatedResult()
                            .setData(userService.getAll(type, value, page))
                            .setCurrentPage(page)
                            .setCount(userService.getCount(type, value)));
        }
    }

    @ApiOperation(value = "新增用户")
    @PostMapping
    public ResponseEntity<?> postUser(@RequestBody User user) {
        userService.addUser(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(user);
    }

    @ApiOperation(value = "修改用户")
    @PutMapping
    public ResponseEntity<?> putUser(@RequestBody User user) {
        assertUserExist(user.getId());

        userService.modifyUserById(user);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(user);
    }

    @ApiOperation(value = "修改密码")
    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody changePasswordForm form) {
        OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userService.getUserByName(((SecurityUser) auth.getPrincipal()).getUsername());
        if (user.isPresent() && user.get().getPassword().equals(form.oldPassword)) {
            User instance = user.get();
            instance.setPassword(form.newPassword);
            userService.modifyUserById(instance);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .build();
        } else {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        assertUserExist(id);

        boolean result = userService.deleteUserById(id);

        if (result)
            return ResponseEntity
                    .accepted()
                    .build();
        else
            return ResponseEntity
                    .notFound()
                    .build();

    }

    private void assertUserExist(Long id) {
        userService
                .getUserByID(id)
                .orElseThrow(() -> new ResourceNotFoundException()
                        .setResourceName(Constant.RESOURCE_USER)
                        .setId(id));
    }

    @Data
    static class changePasswordForm {
        private String oldPassword;
        private String newPassword;
    }
}