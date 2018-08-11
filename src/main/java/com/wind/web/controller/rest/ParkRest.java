package com.wind.web.controller.rest;

import com.wind.mybatis.pojo.Park;
import com.wind.web.common.ExtendController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/park")
public class ParkRest extends ExtendController<Park> {
}
