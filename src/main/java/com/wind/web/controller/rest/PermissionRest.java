package com.wind.web.controller.rest;

import com.wind.web.common.ExtendController;
import com.wind.mybatis.pojo.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/permission")
public class PermissionRest extends ExtendController<Permission> {
}
