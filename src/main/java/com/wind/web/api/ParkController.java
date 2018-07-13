package com.wind.web.api;

import io.swagger.annotations.ApiOperation;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CommonsLog
@RestController
@RequestMapping("/park")
public class ParkController {

    @ApiOperation(value = "车辆待入场申请")
    @GetMapping("/car_in_pre")
    public ResponseEntity<?> carInPre(
            @RequestParam("parkId") String parkId,
            @RequestParam("car") String car) {
        return ResponseEntity.status(HttpStatus.OK).body("{\"code\":2, \"msg\":\"19835\",\"recId\":recId }");
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
