package com.wind.web.api;

import com.wind.web.ExtendController;
import com.wind.mybatis.pojo.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
public class RoleController extends ExtendController<Role>{
}
