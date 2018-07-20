package com.wind.web.api;

import com.wind.mybatis.pojo.*;
import com.wind.web.common.QueryParameter;
import com.wind.web.common.QueryParameterMethod;
import com.wind.web.common.QueryParameterType;
import com.wind.web.service.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.wind.common.Constant.EMPTY_STRING;

@CommonsLog
@RestController
@RequestMapping("/api/park")
public class ParkAPI {

    ParkMemberService parkMemberService = new ParkMemberService();
    ParkService parkService = new ParkService();
    CarFeeService carFeeService = new CarFeeService();

    @ApiOperation(value = "车辆待入场申请")
    @GetMapping("/car_in_pre")
    public ResponseEntity<?> carInPre(
            @RequestParam("parkId") String parkId,
            @RequestParam("car") String carNumber) {
        int code = -1;
        String message = EMPTY_STRING;
        Date now = new Date();
        Optional<Park> optionalPark = parkService.selectByID(Long.parseLong(parkId));
        if (optionalPark.isPresent()){
            List<ParkMember> memberList = parkMemberService.selectAll(
                    new QueryParameter("parkId", QueryParameterMethod.EQUAL, parkId, QueryParameterType.LONG),
                    new QueryParameter("carNumber", QueryParameterMethod.EQUAL, carNumber, QueryParameterType.STRING));
            if (memberList.size() > 0) {
                ParkMember member = memberList.get(0);
                if (member.getPaymentAmount().intValue() == 0) {
                    code = 1;
                    message = "不收费车辆";
                } else {
                    Date endDate = member.getEndDate();
                    if (endDate.after(now)) {
                        Integer days = (int) ((endDate.getTime() - now.getTime()) / (1000 * 3600 * 24));
                        code = 2;
                        message = days.toString();
                    }
                }

            } else if ( optionalPark.get().getIsMemberOnly()) {
                code = 4;
                message = "非小区车辆";
            } else {
                CarFee instance = new CarFee();
                instance.setCarNumber(carNumber);
                instance.setParkId(Long.parseLong(parkId));
                instance.setInTime(new Date());
                carFeeService.add(instance);
                code = 3;
                message = "按次收费车辆";
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(String.format("{\"code\":%d, \"msg\":\"%s\",\"recId\":recId }", code, message));
    }

    @ApiOperation(value = "车辆已入场通知")
    @GetMapping("/car_in_post")
    public ResponseEntity<?> carInPost(
            @RequestParam("parkId") String parkId,
            @RequestParam("recId") String recId,
            @RequestParam("gate") String gate,
            @RequestParam("car") String car,
            @RequestParam("imgUrl") String imgUrl,
            @RequestParam("optType") Short optType,
            @RequestParam(name = "optUser", required = false) String optUser
    ) {
        return ResponseEntity.status(HttpStatus.OK).body("{\"code\":0, \"msg\":\"欢迎入场\"}");
    }

    @ApiOperation(value = "车辆待出场申请")
    @GetMapping("/car_out_pre")
    public ResponseEntity<?> carOutPre(
            @RequestParam("parkId") String parkId,
            @RequestParam("car") String car) {
        return ResponseEntity.status(HttpStatus.OK).body("{ \"code\":1 ,\"recId\":recId }");
    }

    @ApiOperation(value = "车辆已出场通知")
    @GetMapping("/car_out_post")
    public ResponseEntity<?> carOutPost(
            @RequestParam("parkId") String parkId,
            @RequestParam("recId") String recId,
            @RequestParam("gate") String gate,
            @RequestParam("car") String car,
            @RequestParam("imgUrl") String imgUrl,
            @RequestParam("optType") Short optType,
            @RequestParam("inCash") Long inCash,
            @RequestParam(name = "feeType", required = false) Short feeType,
            @RequestParam(name = "sTime", required = false) Long sTime,
            @RequestParam(name = "eTime", required = false) Long eTime,
            @RequestParam(name = "optUser", required = false) String optUser
    ) {
        return ResponseEntity.status(HttpStatus.OK).body("{ \"code\":0, \"msg\":\"一路顺风\"}");
    }


    @ApiOperation(value = "子库入场申请")
    @GetMapping("/car_in_sub")
    public ResponseEntity<?> carInSub(
            @RequestParam("parkId") String parkId,
            @RequestParam("car") String car,
            @RequestParam("subId") String subId) {
        return ResponseEntity.status(HttpStatus.OK).body("{ \"code\":1 ,\"msg\":\"欢迎入场\" }");
    }
}
