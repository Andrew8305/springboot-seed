package com.wind.web.controller;

import com.wind.web.ExtendController;
import com.wind.mybatis.pojo.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user_role")
public class UserRoleController extends ExtendController<UserRole> {
}
