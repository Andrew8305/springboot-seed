package com.wind.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wind.common.SecurityUser;
import com.wind.mybatis.pojo.*;
import com.wind.web.service.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class IndexController {

    @Autowired
    private UserService userService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private PermissionService permissionService;

    @ApiOperation(value = "获取个人详情")
    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo() throws Exception{
        OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        SecurityUser principal = (SecurityUser) auth.getPrincipal();
        ObjectMapper mapper = new ObjectMapper();
        if (principal.getId() != null) {
            User user = userService.selectByID(principal.getId()).get();
            List<Department> departments = departmentService.selectAllByUserId(user.getId());
            List<Permission> permissions = permissionService.selectAllByUserId(user.getId());
            return ResponseEntity.status(HttpStatus.OK)
                    .header("permissions",  mapper.writeValueAsString(permissions))
                    .header("departments",  mapper.writeValueAsString(departments))
                    .body(user);
        } else if ("wx_app".equals(principal.getAuthType())) {
            User user = userService.selectByOpenId(principal.getOpenId()).get();
            List<Department> departments = departmentService.selectAllByUserId(user.getId());
            List<Permission> permissions = permissionService.selectAllByUserId(user.getId());
            return ResponseEntity.status(HttpStatus.OK)
                    .header("permissions",  mapper.writeValueAsString(permissions))
                    .header("departments",  mapper.writeValueAsString(departments))
                    .body(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }
}
