package com.wind.web.controller;

import com.wind.mybatis.pojo.CarFee;
import com.wind.web.common.ExtendController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/car_fee")
public class CarFeeController extends ExtendController<CarFee> {
}
