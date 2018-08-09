package com.wind.web.controller.me;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.wind.common.Constant;
import com.wind.common.SecurityUser;
import com.wind.common.StringUnicodeSerializer;
import com.wind.define.carType;
import com.wind.mybatis.pojo.*;
import com.wind.web.common.QueryParameter;
import com.wind.web.common.QueryParameterMethod;
import com.wind.web.common.QueryParameterType;
import com.wind.web.controller.CarController;
import com.wind.web.controller.CarFeeController;
import com.wind.web.service.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.ser.CustomSerializerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.wind.common.Constant.EMPTY_STRING;

@RestController
@RequestMapping("/me")
public class MeController {

    @Autowired
    private UserService userService;
    @Autowired
    private CarService carService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    protected CarFeeService carFeeService;
    @Autowired
    private ParkService parkService;

    @Autowired
    protected CarController carController;
    @Autowired
    protected CarFeeController carFeeController;

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
        return carController.search(parameters, EMPTY_STRING, Constant.ALL_PAGE);
    }

    @ApiOperation(value = "我的已缴费记录列表")
    @GetMapping("/car_fee_paid_list/{page}")
    public ResponseEntity<?> car_fee_list(@ApiParam("车牌号") @RequestParam(name = "number", required = false, defaultValue = EMPTY_STRING) String number,
                                          @ApiParam("停车场编号") @RequestParam(name = "parkId", required = false, defaultValue = "0") Long parkId,
                                          @ApiParam("页数") @PathVariable("page") Integer page) throws Exception {
        QueryParameter[] parameters;
        OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = ((SecurityUser) auth.getPrincipal()).getId();
        if (number.equals(EMPTY_STRING) && parkId == 0) {
            parameters = new QueryParameter[]{
                    new QueryParameter("userId", QueryParameterMethod.EQUAL, currentUserId.toString(), QueryParameterType.LONG)
            };
        } else if (number.equals(EMPTY_STRING)) {
            parameters = new QueryParameter[]{
                    new QueryParameter("parkId", QueryParameterMethod.EQUAL, parkId.toString(), QueryParameterType.LONG),
                    new QueryParameter("userId", QueryParameterMethod.EQUAL, currentUserId.toString(), QueryParameterType.LONG)
            };
        } else {
            parameters = new QueryParameter[]{
                    new QueryParameter("carNumber", QueryParameterMethod.EQUAL, number, QueryParameterType.STRING),
                    new QueryParameter("userId", QueryParameterMethod.EQUAL, currentUserId.toString(), QueryParameterType.LONG)
            };
        }
        return carFeeController.search(parameters, EMPTY_STRING, page);
    }

    @ApiOperation(value = "我的未缴费记录列表")
    @GetMapping("/car_fee_unpaid_list/{page}")
    public ResponseEntity<?> car_fee_list(
            @ApiParam("页数") @PathVariable("page") Integer page) throws Exception {
        OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = ((SecurityUser) auth.getPrincipal()).getId();
        List<Car> carList = carService.selectAll(
                new QueryParameter[]{new QueryParameter("userId", QueryParameterMethod.EQUAL, currentUserId.toString(), QueryParameterType.LONG)});
        String cars = EMPTY_STRING;
        for (int i = 0; i < carList.size(); i++) {
            cars += carList.get(i).getCarNumber();
            if (i != carList.size() - 1)
                cars += ",";
        }
        QueryParameter[] parameters = new QueryParameter[]{
                new QueryParameter("carNumber", QueryParameterMethod.IN, cars, QueryParameterType.ARRAY),
                new QueryParameter("userId", QueryParameterMethod.IS_NULL, EMPTY_STRING, QueryParameterType.STRING)};
        return carFeeController.search(parameters, EMPTY_STRING, page);
    }

    @ApiOperation(value = "最近一次停车记录")
    @GetMapping(value = "/last_car_fee", produces = "application/json; charset=utf-8")
    public ResponseEntity<?> car_fee_list() throws Exception {
        OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = ((SecurityUser) auth.getPrincipal()).getId();
        List<Car> carList = carService.selectAll(
                new QueryParameter[]{new QueryParameter("userId", QueryParameterMethod.EQUAL, currentUserId.toString(), QueryParameterType.LONG)});
        String cars = EMPTY_STRING;
        for (int i = 0; i < carList.size(); i++) {
            cars += carList.get(i).getCarNumber();
            if (i != carList.size() - 1)
                cars += ",";
        }
        QueryParameter[] parameters = new QueryParameter[]{
                new QueryParameter("carNumber", QueryParameterMethod.IN, cars, QueryParameterType.ARRAY),
                new QueryParameter("userId", QueryParameterMethod.IS_NULL, EMPTY_STRING, QueryParameterType.STRING)};
        List<CarFee> carFeeList = carFeeService.selectTop(1, parameters);
        if (carFeeList.size() != 1) {
            parameters = new QueryParameter[]{
                    new QueryParameter("userId", QueryParameterMethod.EQUAL, currentUserId.toString(), QueryParameterType.LONG)
            };
            carFeeList = carFeeService.selectTop(1, parameters);
        }
        if (carFeeList.size() == 1) {
            Park park = parkService.selectByID(carFeeList.get(0).getParkId()).get();
            ObjectMapper mapper= new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            SimpleModule module = new SimpleModule();
            module.addSerializer(String.class, new StringUnicodeSerializer());
            mapper.registerModule(module);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return ResponseEntity.status(HttpStatus.OK).header("park", mapper.writeValueAsString(park)).body(carFeeList.get(0));
        } else {
            return ResponseEntity.ok().build();
        }
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
