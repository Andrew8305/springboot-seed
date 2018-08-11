package com.wind.web.controller.rest;

import com.wind.mybatis.pojo.CarFee;
import com.wind.web.common.ExtendController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/car_fee")
public class CarFeeRest extends ExtendController<CarFee> {
}
