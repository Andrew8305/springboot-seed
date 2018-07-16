package com.wind.web.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/")
public class IndexController {

    @ApiOperation(value = "获取服务器时间")
    @GetMapping("/time")
    public ResponseEntity<?> getTime(){
        return ResponseEntity.status(HttpStatus.OK).body(new Date());
    }
}
