package com.wind.web.controller.rest;

import com.wind.mybatis.pojo.Payment;
import com.wind.web.common.ExtendController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/payment")
public class PaymentRest extends ExtendController<Payment> {
}
