package com.wind.web.controller;

import com.wind.mybatis.pojo.Car;
import com.wind.web.common.ExtendController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/car")
public class CarController extends ExtendController<Car> {
}
