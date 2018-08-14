package com.wind.web.controller.third;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.wind.mybatis.pojo.*;
import com.wind.web.common.QueryParameter;
import com.wind.web.common.QueryParameterMethod;
import com.wind.web.common.QueryParameterType;
import com.wind.web.controller.wx.WxPayAPI;
import com.wind.web.service.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

import static com.wind.common.Constant.EMPTY_STRING;

@CommonsLog
@RestController
@RequestMapping("/third/park")
public class ParkThirdAPI {

    @Autowired
    ParkMemberService parkMemberService;

    @Autowired
    ParkService parkService;

    @Autowired
    CarFeeService carFeeService;

    @Autowired
    FeeService feeService;

    @Autowired
    protected WxPayAPI wxPayAPI;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/pay_callback/{type}/{id}/{rid}/{uid}")
    public ResponseEntity<?> parseOrderNotifyResult(@ApiParam("支付类型") @PathVariable("type") Integer type,
                                                    @ApiParam("主键") @PathVariable("id") long id,
                                                    @ApiParam("外键") @PathVariable("rid") long rid,
                                                    @ApiParam("用户id") @PathVariable("uid") long uid,
                                                    @RequestBody String xmlData) throws WxPayException {
        WxPayOrderNotifyResult result = wxPayAPI.parseOrderNotifyResult(xmlData);
        if (result.getReturnCode().equals("SUCCESS")) {
            if (type == 1) {
                Payment payment = paymentService.selectByID(id).get();
                payment.setBankType(result.getBankType());
                payment.setOutTradeNo(result.getOutTradeNo());
                payment.setTransactionNo(result.getTransactionId());
                payment.setComment(result.getReturnCode());
                paymentService.modifyById(payment);
                CarFee carFee = carFeeService.selectByID(rid).get();
                carFee.setPaymentAmount(new BigDecimal(payment.getTotalFee()).divide(new BigDecimal(100)));
                carFee.setPaymentTime(new Date());
                carFee.setPaymentMode("微信支付");
                carFee.setPaymentId(id);
                carFee.setUserId(uid);
                carFeeService.modifyById(carFee);
            } else {
                // todo: 会员充值
            }
            return ResponseEntity.status(HttpStatus.OK).body("SUCCESS");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("FAIL");
        }
    }


    @ApiOperation(value = "车辆待入场申请")
    @GetMapping(value = "/car_in_pre", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> carInPre(
            @RequestParam("parkId") Long parkId,
            @RequestParam("car") String carNumber) {
        int code = -1;
        long rec = 0;
        String message;
        try {
            Date now = new Date();
            Optional<Park> optionalPark = parkService.selectByID(parkId);
            if (optionalPark.isPresent()) {
                CarFee fee = new CarFee();
                fee.setCarNumber(carNumber);
                fee.setInTime(new Date());
                fee.setParkId(parkId);
                List<ParkMember> memberList = parkMemberService.selectTop(1,
                        new QueryParameter("parkId", QueryParameterMethod.EQUAL, parkId.toString(), QueryParameterType.LONG),
                        new QueryParameter("carNumber", QueryParameterMethod.EQUAL, carNumber, QueryParameterType.STRING));
                if (memberList.size() > 0) {
                    ParkMember member = memberList.get(0);
                    if (member.getEndDate().after(now)) {
                        Integer days = (int) ((member.getEndDate().getTime() - now.getTime()) / (1000 * 3600 * 24));
                        if (days > 30) {
                            code = 1;
                            message = "会员车辆";
                        } else {
                            code = 2;
                            message = days.toString();
                        }
                    } else if (optionalPark.get().getIsMemberOnly()) {
                        message = "非会员车辆";
                        code = 4;
                    } else {
                        message = "按次收费车辆";
                        code = 3;
                    }
                } else if (optionalPark.get().getIsMemberOnly()) {
                    code = 4;
                    message = "非会员车辆";
                } else {
                    code = 3;
                    message = "按次收费车辆";
                }
                fee.setCode(code);
                fee.setInComment(message);
                carFeeService.add(fee);
                rec = fee.getId();
            } else {
                code = -9;
                message = "无法识别小区";
            }
        } catch (Exception e) {
            message = e.getMessage();
            log.error(message);
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.OK).body(String.format("{\"code\":%d, \"msg\":\"%s\",\"recId\":%d }", code, message, rec));
    }

    @ApiOperation(value = "车辆已入场通知")
    @GetMapping(value = "/car_in_post", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> carInPost(
            @RequestParam("parkId") Long parkId,
            @RequestParam("recId") Long recId,
            @RequestParam("gate") String gate,
            @RequestParam("car") String car,
            @RequestParam("imgUrl") String imgUrl,
            @RequestParam("optType") Integer optType,
            @RequestParam(name = "optUser", required = false) String optUser
    ) {
        int code = -1;
        String message = EMPTY_STRING;
        try {
            List<CarFee> feeList = carFeeService.selectTop(1,
                    new QueryParameter("id", QueryParameterMethod.EQUAL, recId.toString(), QueryParameterType.LONG),
                    new QueryParameter("parkId", QueryParameterMethod.EQUAL, parkId.toString(), QueryParameterType.LONG),
                    new QueryParameter("carNumber", QueryParameterMethod.EQUAL, car, QueryParameterType.STRING));
            if (feeList.size() > 0) {
                CarFee fee = feeList.get(0);
                fee.setInTime(new Date());
                fee.setInOperationType(optType);
                fee.setInOperator(optUser);
                fee.setInImageUrl(imgUrl);
                fee.setInGate(gate);
                carFeeService.modifyById(fee);
                code = 0;
            } else {
                message = "记录不存在";
            }
        } catch (Exception e) {
            message = e.getMessage();
            log.error(message);
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.OK).body(String.format("{\"code\":%d, \"msg\":\"%s\"}", code, message));
    }

    @ApiOperation(value = "车辆待出场申请")
    @GetMapping(value = "/car_out_pre", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> carOutPre(
            @RequestParam("parkId") Long parkId,
            @RequestParam("car") String carNumber) {
        int code = -1;
        String message;
        Date now = new Date();
        try {
            Park park = parkService.selectByID(parkId).get();
            Fee parkFee = feeService.selectByID(park.getFeeId()).get();
            List<CarFee> feeList = carFeeService.selectTop(1,
                    new QueryParameter("parkId", QueryParameterMethod.EQUAL, parkId.toString(), QueryParameterType.LONG),
                    new QueryParameter("carNumber", QueryParameterMethod.EQUAL, carNumber, QueryParameterType.STRING));
            if (feeList.size() > 0) {
                CarFee carFee = feeList.get(0);
                long recId = carFee.getId();
                long inUTC = carFee.getInTime().getTime() / 1000;
                long outUTC = (new Date()).getTime() / 1000;
                if (carFee.getOutTime() != null) {
                    code = -3;
                    message = "无入场记录";
                    return ResponseEntity.status(HttpStatus.OK).body(String.format("{ \"code\":%d ,\"msg\":\"%s\" }", code, message));
                } else {
                    if (carFee.getCode() == 1 || carFee.getCode() == 2) {
                        code = 1;
                        carFee.setPaymentMode("会员");
                        carFee.setOutComment("会员车辆");
                        carFeeService.modifyById(carFee);
                        return ResponseEntity.status(HttpStatus.OK).body(String.format("{ \"code\":%d ,\"recId\":%d }", code, recId));
                    } else if (carFee.getCode() == 3 || carFee.getCode() == 4) {
                        BigDecimal money = calculateMoney(parkFee, carFee.getInTime(), new Date());
                        if (money.compareTo(BigDecimal.ZERO) == 0) {
                            code = 1;
                            carFee.setOutComment("免费期内出场");
                            carFeeService.modifyById(carFee);
                            return ResponseEntity.status(HttpStatus.OK).body(String.format("{ \"code\":%d ,\"recId\":%d }", code, recId));
                        } else if (carFee.getPaymentTime() == null) {
                            code = 4;
                            carFee.setOutComment("未提前移动支付");
                            carFeeService.modifyById(carFee);
                            return ResponseEntity.status(HttpStatus.OK).body(
                                    String.format("{\"code\":%d,\"recId\":%d\n" +
                                            "\"inTime\":%d,\n" +
                                            "\"fee1\":{\"sTime\":%d,\n" +
                                            "\"eTime\":%d,\n" +
                                            "\"inCash\":%d}\n" +
                                            "}", code, recId, inUTC, inUTC, outUTC, money.multiply(new BigDecimal(100)).intValue()));
                        } else if ((now.getTime() - carFee.getPaymentTime().getTime()) / (1000 * 60) < parkFee.getOutFreeMinutes()) {
                            code = 1;
                            carFee.setOutComment("已移动支付");
                            carFeeService.modifyById(carFee);
                            return ResponseEntity.status(HttpStatus.OK).body(String.format("{ \"code\":%d ,\"recId\":%d }", code, recId));
                        } else { // 已经提前移动支付，超时出场
                            BigDecimal payedMoney = carFee.getPaymentAmount();
                            if (money.compareTo(payedMoney) == 0) {
                                code = 1;
                                carFee.setOutComment("已移动支付，超时出场，无需补缴");
                                carFeeService.modifyById(carFee);
                                return ResponseEntity.status(HttpStatus.OK).body(String.format("{ \"code\":%d ,\"recId\":%d }", code, recId));
                            } else {
                                code = 4;
                                carFee.setOutComment("已移动支付，超时出场，需补缴");
                                carFeeService.modifyById(carFee);
                                return ResponseEntity.status(HttpStatus.OK).body(
                                        String.format("{\"code\":%d,\"recId\":%d\n" +
                                                        "\"inTime\":%d,\n" +
                                                        "\"fee1\":{\"sTime\":%d,\n" +
                                                        "\"eTime\":%d,\n" +
                                                        "\"inCash\":%d}\n" +
                                                        "}", code, recId, inUTC, carFee.getPaymentTime().getTime() / 1000,
                                                outUTC, (money.subtract(payedMoney)).multiply(new BigDecimal(100)).intValue()));
                            }
                        }
                    } else {
                        message = "code不正确";
                    }
                }
            } else {
                code = -3;
                message = "无入场记录";
                return ResponseEntity.status(HttpStatus.OK).body(String.format("{ \"code\":%d ,\"msg\":\"%s\" }", code, message));
            }
        } catch (Exception e) {
            code = -1;
            message = e.getMessage();
            log.error(message);
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.OK).body(String.format("{ \"code\":%d ,\"msg\":\"%s\" }", code, message));
    }

    @ApiOperation(value = "车辆已出场通知")
    @GetMapping(value = "/car_out_post", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<?> carOutPost(
            @RequestParam("parkId") Long parkId,
            @RequestParam("recId") Long recId,
            @RequestParam("gate") String gate,
            @RequestParam("car") String carNumber,
            @RequestParam("imgUrl") String imgUrl,
            @RequestParam("optType") Integer optType,
            @RequestParam("inCash") Long inCash,
            @RequestParam(name = "feeType", required = false) Integer feeType,
            @RequestParam(name = "sTime", required = false) Long sTime,
            @RequestParam(name = "eTime", required = false) Long eTime,
            @RequestParam(name = "optUser", required = false) String optUser
    ) {
        int code = -1;
        String message = EMPTY_STRING;
        try {
            List<CarFee> feeList = carFeeService.selectTop(1,
                    new QueryParameter("id", QueryParameterMethod.EQUAL, recId.toString(), QueryParameterType.LONG),
                    new QueryParameter("parkId", QueryParameterMethod.EQUAL, parkId.toString(), QueryParameterType.LONG),
                    new QueryParameter("carNumber", QueryParameterMethod.EQUAL, carNumber, QueryParameterType.STRING));
            if (feeList.size() > 0) {
                CarFee fee = feeList.get(0);
                fee.setOutTime(new Date());
                fee.setOutOperationType(optType);
                fee.setOutOperator(optUser);
                fee.setOutImageUrl(imgUrl);
                fee.setOutGate(gate);
                if (inCash > 0) {
                    if (fee.getPaymentTime() == null) {
                        fee.setPaymentAmount(new BigDecimal(inCash).divide(new BigDecimal(100)));
                        fee.setPaymentTime(new Date());
                        fee.setCash(fee.getPaymentAmount());
                        fee.setPaymentMode("现金");
                    } else {
                        fee.setCash(new BigDecimal(inCash).divide(new BigDecimal(100)));
                        fee.setComment("超时补缴");
                    }
                }
                carFeeService.modifyById(fee);
                code = 0;
            } else {
                message = "记录不存在";
            }
        } catch (Exception e) {
            message = e.getMessage();
            log.error(message);
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.OK).body(String.format("{\"code\":%d, \"msg\":\"%s\"}", code, message));
    }


    @ApiOperation(value = "子库入场申请")
    @GetMapping("/car_in_sub")
    public ResponseEntity<?> carInSub(
            @RequestParam("parkId") Long parkId,
            @RequestParam("car") String carNumber,
            @RequestParam("subId") Long subId) {
        return ResponseEntity.status(HttpStatus.OK).body("{ \"code\":1 ,\"msg\":\"欢迎入场\" }");
    }

    static public BigDecimal calculateMoney(Fee fee, Date startTime, Date endTime) {
        BigDecimal money = BigDecimal.ZERO;
        if (fee.getIsFree()) {
            // zero
        } else if ((endTime.getTime() - startTime.getTime()) / (1000 * 60) < fee.getInFreeMinutes()) {
            // zero
        } else {
            ArrayList<String> parameters = new ArrayList<>(Arrays.asList(fee.getParameters().split(",")));
            int hours = (int) ((endTime.getTime() - startTime.getTime()) / (1000 * 3600) + 1);
            money = getPricing(fee, parameters, hours);
            if (parameters.contains("limit_per_day")) {
                int days = hours / 24;
                int residualHours = hours - days * 24;
                BigDecimal residualMoney = getPricing(fee, parameters, residualHours);
                if (fee.getLimitPerDay().compareTo(residualMoney) == -1) {
                    residualMoney = fee.getLimitPerDay();
                }
                BigDecimal daysMoney = new BigDecimal(days).multiply(fee.getLimitPerDay());
                money = daysMoney.add(residualMoney);
            }
            if (parameters.contains("limit_per_time") && fee.getLimitPerTime().compareTo(money) == -1) {
                money = fee.getLimitPerTime();
            }
        }
        return money;
    }

    static private BigDecimal getPricing(Fee fee, ArrayList<String> parameters, int hours) {
        BigDecimal money = BigDecimal.ZERO;
        if (parameters.contains("per_hour")) {
            money = new BigDecimal(hours).multiply(fee.getPerHour());
        } else if (parameters.contains("per_time")) {
            money = fee.getPerTime();
        } else if (parameters.contains("differential_pricing")) {
            ArrayList<String> prices = new ArrayList<>(Arrays.asList(fee.getDifferentialPricing().split(",")));
            if (prices.size() != 24) {
                log.error(String.format("分区计费错误，规则必须等于24小时：%d", fee.getId()));
            } else {
                hours = hours > 24 ? 24 : hours;
                int value = Integer.parseInt(prices.get(hours - 1));
                money = BigDecimal.valueOf(value);
            }
        }
        return money;
    }
}
