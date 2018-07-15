package com.wind.web.controller;

import com.wind.mybatis.pojo.Fee;
import com.wind.web.common.ExtendController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fee")
public class FeeController extends ExtendController<Fee> {
}
