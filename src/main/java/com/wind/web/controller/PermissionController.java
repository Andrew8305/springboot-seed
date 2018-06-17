package com.wind.web.controller;

import com.wind.web.ExtendController;
import com.wind.mybatis.pojo.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permission")
public class PermissionController extends ExtendController<Permission> {
}
