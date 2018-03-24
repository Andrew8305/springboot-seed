package com.wind.web.api;

import com.wind.mybatis.pojo.Auth;
import com.wind.web.ExtendController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController extends ExtendController<Auth>{
}
