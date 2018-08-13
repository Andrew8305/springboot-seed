package com.wind.web.controller.me;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.wind.common.Constant;
import com.wind.common.SecurityUser;
import com.wind.common.StringUnicodeSerializer;
import com.wind.config.WxPayProperties;
import com.wind.define.carType;
import com.wind.define.roleType;
import com.wind.mybatis.pojo.*;
import com.wind.web.common.QueryParameter;
import com.wind.web.common.QueryParameterMethod;
import com.wind.web.common.QueryParameterType;
import com.wind.web.controller.rest.CarRest;
import com.wind.web.controller.rest.CarFeeRest;
import com.wind.web.controller.third.ParkThirdAPI;
import com.wind.web.controller.wx.WxPayAPI;
import com.wind.web.service.*;
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
    private PaymentService paymentService;

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

    @ApiOperation(value = "预估停车费")
    @GetMapping("/pay/price")
    public ResponseEntity<?> pay(@ApiParam("停车记录") @RequestParam("carFee") Long carFeeId) throws Exception {
        CarFee carFee = carFeeService.selectByID(carFeeId).get();
        Park park = parkService.selectByID(carFee.getParkId()).get();
        Fee fee = feeService.selectByID(park.getFeeId()).get();
        BigDecimal money = ParkThirdAPI.calculateMoney(fee, carFee.getInTime(), new Date());
        return ResponseEntity.status(HttpStatus.OK).body(money.toString());
    }


    @ApiOperation(value = "支付停车费")
    @PostMapping(value = "/pay")
    public ResponseEntity<?> pay(HttpServletRequest httpRequest,
                                 @ApiParam("停车记录") @RequestParam("carFee") Long carFeeId) throws Exception {
        OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
        SecurityUser securityUser = (SecurityUser) (auth.getPrincipal());
        CarFee carFee = carFeeService.selectByID(carFeeId).get();
        if (carFee.getPaymentTime() != null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Already Paid");
        }
        Park park = parkService.selectByID(carFee.getParkId()).get();
        Fee fee = feeService.selectByID(park.getFeeId()).get();
        BigDecimal money = ParkThirdAPI.calculateMoney(fee, carFee.getInTime(), new Date());
        money = money.multiply(BigDecimal.TEN).multiply(BigDecimal.TEN);
        Dictionary<String, String> map = new Hashtable<>();
        if (money == BigDecimal.ZERO) {
            carFee.setPaymentAmount(money);
            carFee.setPaymentTime(new Date());
            carFee.setPaymentMode("免费");
            carFee.setUserId(securityUser.getId());
            carFeeService.modifyById(carFee);
            map.put("money", "0");
        } else {
            if (securityUser.getId() < 15) {
                money = BigDecimal.ONE;
            }
            request.setOpenid(securityUser.getOpenId());
            request.setDeviceInfo("ma");
            request.setBody(park.getName());
            request.setDetail(carFee.getCarNumber() + "-停车费");
            request.setFeeType("CNY");
            request.setTotalFee(money.intValue());
            request.setSpbillCreateIp(httpRequest.getLocalAddr());
            request.setTradeType("JSAPI");
            // save db
            Payment payment = new Payment();
            payment.setBody(request.getBody());
            payment.setDetail(request.getDetail());
            payment.setTotalFee(money.intValue());
            payment.setFeeType(request.getFeeType());
            payment.setTradeType(request.getTradeType());
            payment.setIp(request.getSpbillCreateIp());
            paymentService.add(payment);
            // post wx request
            request.setNotifyUrl(String.format("https://app.lhzh.tech/third/park/pay_callback/1/%d/%d/%d", payment.getId(), carFeeId, securityUser.getId()));
            request.setOutTradeNo(String.format("1-%d-%d-%d-%d", securityUser.getId(), payment.getId(), carFeeId, new Random().nextInt(9999)));
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
            map.put("money", money.toString());
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
