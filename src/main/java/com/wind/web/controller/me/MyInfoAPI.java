package com.wind.web.controller.me;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wind.common.SecurityUser;
import com.wind.mybatis.pojo.Department;
import com.wind.mybatis.pojo.Permission;
import com.wind.mybatis.pojo.User;
import com.wind.web.service.DepartmentService;
import com.wind.web.service.PermissionService;
import com.wind.web.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/me/info")
public class MyInfoAPI {

    @Autowired
    private UserService userService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private PermissionService permissionService;

    @ApiOperation(value = "获取个人详情")
    @GetMapping("/")
    public ResponseEntity<?> getInfo() throws Exception {
        OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        SecurityUser principal = (SecurityUser) auth.getPrincipal();
        ObjectMapper mapper = new ObjectMapper();
        if (principal.getId() != null) {
            User user = userService.selectByID(principal.getId()).get();
            List<Department> departments = departmentService.selectAllByUserId(user.getId());
            List<Permission> permissions = permissionService.selectAllByUserId(user.getId());
            return ResponseEntity.status(HttpStatus.OK)
                    .header("permissions", mapper.writeValueAsString(permissions))
                    .header("departments", mapper.writeValueAsString(departments))
                    .body(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @ApiOperation(value = "绑定微信个人信息" )
    @PutMapping("/bind_wx" )
    public ResponseEntity<?> bindUserInfo(@RequestBody Map<String, Object> params) {
        OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        SecurityUser principal = (SecurityUser) auth.getPrincipal();
        User user = userService.selectByID(principal.getId()).get();
        user.setNickname(params.get("nickName" ).toString());
        user.setGender(Short.parseShort(params.get("gender" ).toString()));
        user.setLanguage(params.get("language" ).toString());
        user.setCity(params.get("city" ).toString());
        user.setProvince(params.get("province" ).toString());
        user.setCountry(params.get("country" ).toString());
        user.setAvatarUrl(params.get("avatarUrl" ).toString());
        userService.modifyById(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @ApiOperation(value = "修改密码")
    @PutMapping("/change_password")
    public ResponseEntity<?> changePassword(
            @ApiParam("旧密码") @RequestParam("oldPassword") String oldPassword,
            @ApiParam("新密码") @RequestParam("newPassword") String newPassword
    ) {
        OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userService.selectByID(((SecurityUser) auth.getPrincipal()).getId());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (user.isPresent() && encoder.matches(oldPassword, user.get().getPassword())) {
            User instance = user.get();
            instance.setPassword(newPassword);
            userService.modifyById(instance);
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
