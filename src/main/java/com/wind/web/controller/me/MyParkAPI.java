package com.wind.web.controller.me;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.wind.common.Constant;
import com.wind.common.SecurityUser;
import com.wind.common.StringUnicodeSerializer;
import com.wind.config.WxPayProperties;
import com.wind.define.carType;
import com.wind.mybatis.pojo.Car;
import com.wind.mybatis.pojo.CarFee;
import com.wind.mybatis.pojo.Fee;
import com.wind.mybatis.pojo.Park;
import com.wind.web.common.QueryParameter;
import com.wind.web.common.QueryParameterMethod;
import com.wind.web.common.QueryParameterType;
import com.wind.web.controller.rest.CarRest;
import com.wind.web.controller.rest.CarFeeRest;
import com.wind.web.controller.third.ParkThirdAPI;
import com.wind.web.controller.wx.WxPayAPI;
import com.wind.web.service.CarFeeService;
import com.wind.web.service.CarService;
import com.wind.web.service.FeeService;
import com.wind.web.service.ParkService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.*;

import static com.wind.common.Constant.EMPTY_STRING;

@RestController
@EnableConfigurationProperties(WxPayProperties.class)
@RequestMapping("/me/park")
public class MyParkAPI {
    @Autowired
    private WxPayProperties wxPayProperties;

    @Autowired
    private CarService carService;
    @Autowired
    protected CarFeeService carFeeService;
    @Autowired
    protected FeeService feeService;
    @Autowired
    private ParkService parkService;

    @Autowired
    protected CarRest carRest;
    @Autowired
    protected CarFeeRest carFeeRest;
    @Autowired
    protected WxPayAPI wxPayAPI;

    @ApiOperation(value = "绑定车牌")
    @PostMapping("/add_car")
    public ResponseEntity<?> addCar(@ApiParam("车牌号") @RequestParam("number") String number) {
        OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = ((SecurityUser) auth.getPrincipal()).getId();
        Car car = new Car();
        car.setUserId(currentUserId);
        car.setCarNumber(number);
        car.setCarType(number.length() == 7 ? carType.MOTOR_VEHICLE.toString() : carType.ELECTRIC_VEHICLE.toString());
        return carRest.post(car);
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
        return carRest.delete(parameters);
    }

    @ApiOperation(value = "我的车牌列表")
    @GetMapping("/car_list")
    public ResponseEntity<?> car_list() throws Exception {
        OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = ((SecurityUser) auth.getPrincipal()).getId();
        QueryParameter[] parameters = new QueryParameter[]{
                new QueryParameter("userId", QueryParameterMethod.EQUAL, currentUserId.toString(), QueryParameterType.LONG)
        };
        return carRest.search(parameters, EMPTY_STRING, Constant.ALL_PAGE);
    }

    @ApiOperation(value = "支付停车费")
    @PostMapping("/pay")
    public ResponseEntity<?> pay(HttpServletRequest httpRequest,
                                 @ApiParam("停车记录") @RequestParam("carFee") Long carFeeId) throws Exception {
        CarFee carFee = carFeeService.selectByID(carFeeId).get();
        Park park = parkService.selectByID(carFee.getParkId()).get();
        Fee fee = feeService.selectByID(park.getFeeId()).get();
        BigDecimal money = ParkThirdAPI.calculateMoney(fee, carFee.getInTime(), new Date());
        money = money.multiply(BigDecimal.TEN).multiply(BigDecimal.TEN).add(BigDecimal.ONE);
        Dictionary<String, String> map = new Hashtable<>();
        if (money == BigDecimal.ZERO) {
            map.put("money", "0");
        } else {
            OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
            WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
            request.setOpenid(((SecurityUser) auth.getPrincipal()).getOpenId());
            request.setDeviceInfo("ma");
            request.setBody(park.getName());
            request.setDetail(park.getName() + "-停车费");
            request.setOutTradeNo(Long.toString(new Date().getTime()) + '-' + new Random().nextInt(1000) + '-' + fee.getId().toString());
            request.setFeeType("CNY");
            request.setTotalFee(money.intValue());
            request.setSpbillCreateIp(httpRequest.getLocalAddr());
            request.setNotifyUrl("https://app.lhzh.tech/wx/pay/parseOrderNotifyResult");
            request.setTradeType("JSAPI");
            WxPayUnifiedOrderResult result = wxPayAPI.unifiedOrder(request);
            map.put("nonceStr", result.getNonceStr());
            String timeStamp = Long.toString(new Date().getTime() / 1000);
            map.put("timeStamp", timeStamp);
            String pkg = "prepay_id=" + result.getPrepayId();
            map.put("package", pkg);
            map.put("signType", "MD5");
            String paySignString = String.format("appId=%s&nonceStr=%s&package=%s&signType=MD5&timeStamp=%s&key=%s",
                    wxPayProperties.getAppId(), result.getNonceStr(), pkg, timeStamp, wxPayProperties.getMchKey());
            String paySign = new BigInteger(1, MessageDigest.getInstance("MD5").digest(paySignString.getBytes())).toString(16);
            map.put("paySign", paySign);
        }
        return ResponseEntity.status(HttpStatus.OK).body(map);
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
        return carFeeRest.search(parameters, EMPTY_STRING, page);
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
        return carFeeRest.search(parameters, EMPTY_STRING, page);
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
            ObjectMapper mapper = new ObjectMapper();
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
}
