package com.wind.web.controller.api;

import com.wind.mybatis.pojo.*;
import com.wind.web.common.QueryParameter;
import com.wind.web.common.QueryParameterMethod;
import com.wind.web.common.QueryParameterType;
import com.wind.web.service.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;

import static com.wind.common.Constant.EMPTY_STRING;

@CommonsLog
@RestController
@RequestMapping("/api/park")
public class ParkAPI {

    @Autowired
    ParkMemberService parkMemberService;

    @Autowired
    ParkService parkService;

    @Autowired
    CarFeeService carFeeService;

    @Autowired
    FeeService feeService;

    @ApiOperation(value = "车辆待入场申请")
    @GetMapping("/car_in_pre")
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
                    if (member.getPaymentAmount().intValue() == 0 && memberList.get(0).getEndDate().after(now)) {
                        code = 1;
                        message = "不收费车辆";
                    } else {
                        Integer days = (int) ((member.getEndDate().getTime() - now.getTime()) / (1000 * 3600 * 24));
                        code = 2;
                        message = days.toString();
                    }
                } else if (optionalPark.get().getIsMemberOnly()) {
                    code = 4;
                    message = "非小区车辆";
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
    @GetMapping("/car_in_post")
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
    @GetMapping("/car_out_pre")
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
                CarFee fee = feeList.get(0);
                long recId = fee.getId();
                long inUTC = fee.getInTime().getTime() / 1000;
                long outUTC = (new Date()).getTime() / 1000;
                if (fee.getOutTime() != null) {
                    code = -3;
                    message = "无入场记录";
                    return ResponseEntity.status(HttpStatus.OK).body(String.format("{ \"code\":%d ,\"msg\":\"%s\" }", code, message));
                } else {
                    if (fee.getCode() == 1) {
                        code = 1;
                        fee.setOutComment("不收费小区车");
                        carFeeService.modifyById(fee);
                        return ResponseEntity.status(HttpStatus.OK).body(String.format("{ \"code\":%d ,\"recId\":%d }", code, recId));
                    } else if (fee.getCode() == 2) {  // 小区月租车
                        List<ParkMember> memberList = parkMemberService.selectTop(1,
                                new QueryParameter("parkId", QueryParameterMethod.EQUAL, parkId.toString(), QueryParameterType.LONG),
                                new QueryParameter("carNumber", QueryParameterMethod.EQUAL, carNumber, QueryParameterType.STRING));
                        if (memberList.size() > 0) {
                            ParkMember member = memberList.get(0);
                            Integer days = (int) ((member.getEndDate().getTime() - now.getTime()) / (1000 * 3600 * 24));
                            if (fee.getPaymentTime() == null && member.getEndDate().before(now)) {
                                code = 3;
                                fee.setOutComment("会员过期收费");
                                carFeeService.modifyById(fee);
                                BigDecimal money = calculateMoney(parkFee, fee.getInTime(), new Date());
                                long rentStartUTC = (new Date()).getTime() / (1000 * 3600 * 24);
                                long rentEndUTC = rentStartUTC + 31;  // 按31天计算月租
                                return ResponseEntity.status(HttpStatus.OK).body(
                                        String.format("{\"code\":%d,\"recId\":%d\n" +
                                                        "\"inTime\":%d,\n" +
                                                        "\"oweDate\": %d\n" +
                                                        "\"fee1\":{\"sTime\": %d,\n" +
                                                        "\"eTime\": %d,\n" +
                                                        "\"inCash\":%.2f}\n" +
                                                        "\"fee2\": {\"sDate\":%d,\n" +
                                                        "\"eDate\":%d,\n" +
                                                        "\"inCash\":%.2f}\n" +
                                                        "}", code, recId, inUTC,
                                                days, inUTC, outUTC, money,
                                                rentStartUTC, rentEndUTC,
                                                parkFee.getPerMonth()));
                            } else {
                                code = 2;
                                fee.setOutComment("会员没有过期或已经缴费");
                                carFeeService.modifyById(fee);
                                return ResponseEntity.status(HttpStatus.OK).body(
                                        String.format("{ \"code\":%d, \"recId\":%d ,\"oweDate\":%d }", code, recId, days));
                            }
                        } else {
                            message = "无会员记录";
                            return ResponseEntity.status(HttpStatus.OK).body(String.format("{ \"code\":%d ,\"msg\":\"%s\" }", code, message));
                        }
                    } else if (fee.getCode() == 3 || fee.getCode() == 4) {
                        BigDecimal money = calculateMoney(parkFee, fee.getInTime(), new Date());
                        if (money.equals(BigDecimal.ZERO)) {
                            code = 1;
                            fee.setOutComment("免费出场");
                            carFeeService.modifyById(fee);
                            return ResponseEntity.status(HttpStatus.OK).body(String.format("{ \"code\":%d ,\"recId\":%d }", code, recId));
                        } else if (fee.getPaymentTime() == null) {
                            code = 4;
                            fee.setOutComment("未提前移动支付");
                            carFeeService.modifyById(fee);
                            return ResponseEntity.status(HttpStatus.OK).body(
                                    String.format("{\"code\":%d,\"recId\":%d\n" +
                                            "\"inTime\":%d,\n" +
                                            "\"fee1\":{\"sTime\":%d,\n" +
                                            "\"eTime\":%d,\n" +
                                            "\"inCash\":%.2f}\n" +
                                            "}", code, recId, inUTC, inUTC, outUTC, money));
                        } else if ((fee.getPaymentTime().getTime() - now.getTime()) / (1000 * 60) < 15) {
                            code = 1;
                            fee.setOutComment("已经提前移动支付，15分钟内出场");
                            carFeeService.modifyById(fee);
                            return ResponseEntity.status(HttpStatus.OK).body(String.format("{ \"code\":%d ,\"recId\":%d }", code, recId));
                        } else { // 已经提前移动支付，超时出场
                            BigDecimal payedMoney = fee.getPaymentAmount();
                            if (money.compareTo(payedMoney) == 0) {
                                code = 1;
                                fee.setOutComment("已经提前移动支付，超时出场，但已支付金额能够弥补超时费用");
                                carFeeService.modifyById(fee);
                                return ResponseEntity.status(HttpStatus.OK).body(String.format("{ \"code\":%d ,\"recId\":%d }", code, recId));
                            } else {
                                code = 4;
                                fee.setOutComment("已经提前移动支付，超时出场，重新计算计费起始时间");
                                carFeeService.modifyById(fee);
                                return ResponseEntity.status(HttpStatus.OK).body(
                                        String.format("{\"code\":%d,\"recId\":%d\n" +
                                                        "\"inTime\":%d,\n" +
                                                        "\"fee1\":{\"sTime\":%d,\n" +
                                                        "\"eTime\":%d,\n" +
                                                        "\"inCash\":%.2f}\n" +
                                                        "}", code, recId, inUTC, fee.getPaymentTime().getTime() / 1000,
                                                outUTC, payedMoney.subtract(money)));
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
    @GetMapping("/car_out_post")
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
                fee.setCash(new BigDecimal(inCash).divide(new BigDecimal(100)));
                fee.setPaymentTime(new Date());
                fee.setCashType(feeType);
                if (feeType == 1 && inCash > 0) { // 续租收现
                    ParkMember member = new ParkMember();
                    member.setCarNumber(carNumber);
                    member.setParkId(parkId);
                    member.setOperator(optUser);
                    member.setComment("停车场手动续租");
                    member.setStartDate(new Date(sTime * 1000 * 24 * 3600));
                    member.setEndDate(new Date(eTime * 1000 * 24 * 3600));
                    member.setPaymentAmount(fee.getCash());
                    parkMemberService.add(member);
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
        } else if ((endTime.getTime() - startTime.getTime()) / (1000 * 60) < fee.getFreeMinutes()) {
            // zero
        } else {
            ArrayList<String> parameters = new ArrayList<>(Arrays.asList(fee.getParameters().split("\\|")));
            long hours = (endTime.getTime() - startTime.getTime()) / (1000 * 3600) + 1;
            if (parameters.contains("per_hour")) {
                money = new BigDecimal(hours).multiply(fee.getPerHour());
            } else if (parameters.contains("per_time")) {
                money = fee.getPerTime();
            } else if (parameters.contains("differential_pricing")) {
                // TODO: differential_pricing
            }
            if (parameters.contains("limit_per_time") && fee.getLimitPerTime().compareTo(money) == -1) {
                money = fee.getLimitPerTime();
            } else if (parameters.contains("limit_per_day")) {
                long days = hours / 24;
                long residualHours = hours - days * 24;
                BigDecimal residualMoney = new BigDecimal(residualHours).multiply(fee.getPerHour());
                if (fee.getLimitPerDay().compareTo(residualMoney) == -1) {
                    residualMoney = fee.getLimitPerDay();
                }
                money = (new BigDecimal(days).multiply(fee.getLimitPerDay())).add(residualMoney);
            }
        }
        return money;
    }
}
