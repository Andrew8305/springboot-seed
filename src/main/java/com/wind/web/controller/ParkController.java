package com.wind.web.controller;

import com.wind.mybatis.pojo.Park;
import com.wind.web.common.ExtendController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/park")
public class ParkController extends ExtendController<Park> {
}
