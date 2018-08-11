package com.wind.web.controller.rest;

import com.wind.mybatis.pojo.Car;
import com.wind.web.common.ExtendController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/car")
public class CarRest extends ExtendController<Car> {
}
