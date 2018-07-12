package com.wind.web.controller;

import com.wind.mybatis.pojo.Department;
import com.wind.web.common.ExtendController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/department")
public class DepartmentController extends ExtendController<Department>{
}
