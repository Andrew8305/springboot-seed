package com.wind.web.controller;

import com.wind.mybatis.pojo.Payment;
import com.wind.web.common.ExtendController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController extends ExtendController<Payment> {
}
