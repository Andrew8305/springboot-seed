package com.wind.web.controller.rest;

import com.wind.mybatis.pojo.Fee;
import com.wind.web.common.ExtendController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/fee")
public class FeeRest extends ExtendController<Fee> {
}
