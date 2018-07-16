package com.wind.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wind.common.Constant;
import com.wind.common.SecurityUser;
import com.wind.define.carType;
import com.wind.mybatis.pojo.Car;
import com.wind.mybatis.pojo.Department;
import com.wind.mybatis.pojo.Permission;
import com.wind.mybatis.pojo.User;
import com.wind.web.common.QueryParameter;
import com.wind.web.common.QueryParameterMethod;
import com.wind.web.common.QueryParameterType;
import com.wind.web.service.DepartmentService;
import com.wind.web.service.PermissionService;
import com.wind.web.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/me")
public class MeController {

    @Autowired
    private UserService userService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private PermissionService permissionService;

    @Autowired
    protected CarController carController;

    @ApiOperation(value = "获取个人详情")
    @GetMapping("/info")
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

    @ApiOperation(value = "绑定车牌")
    @PostMapping("/add_car")
    public ResponseEntity<?> addCar(@ApiParam("车牌号") @RequestParam("number") String number) {
        OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = ((SecurityUser) auth.getPrincipal()).getId();
        Car car = new Car();
        car.setUserId(currentUserId);
        car.setCarNumber(number);
        car.setCarType(number.length() == 7 ? carType.MOTOR_VEHICLE.toString() : carType.ELECTRIC_VEHICLE.toString());
        return carController.post(car);
    }

    @ApiOperation(value = "取消绑定车牌")
    @DeleteMapping("/remove_car")
    public ResponseEntity<?> removeCar(@ApiParam("车牌号") @RequestParam("number") String number) {
        OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = ((SecurityUser) auth.getPrincipal()).getId();
        QueryParameter[] parameters = new QueryParameter[]{
                new QueryParameter("userId", QueryParameterMethod.EQUAL, currentUserId.toString(), QueryParameterType.LONG),
                new QueryParameter("carNumber", QueryParameterMethod.EQUAL, number, QueryParameterType.STRING)
        };
         return carController.delete(parameters);
    }

    @ApiOperation(value = "我的车牌列表")
    @GetMapping("/car_list")
    public ResponseEntity<?> car_list() throws Exception {
        OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = ((SecurityUser) auth.getPrincipal()).getId();
        QueryParameter[] parameters = new QueryParameter[]{
                new QueryParameter("userId", QueryParameterMethod.EQUAL, currentUserId.toString(), QueryParameterType.LONG)
        };
        return carController.search(parameters, Constant.EMPTY_STRING, Constant.ALL_PAGE);
    }

    @ApiOperation(value = "修改密码")
    @PutMapping("/change_password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordForm form) {
        OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userService.selectByID(((SecurityUser) auth.getPrincipal()).getId());
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
