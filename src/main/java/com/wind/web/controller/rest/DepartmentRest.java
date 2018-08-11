package com.wind.web.controller.rest;

import com.wind.mybatis.pojo.Department;
import com.wind.web.common.ExtendController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/department")
public class DepartmentRest extends ExtendController<Department>{
}
