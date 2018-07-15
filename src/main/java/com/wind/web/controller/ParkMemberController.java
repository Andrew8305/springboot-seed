package com.wind.web.controller;

import com.wind.mybatis.pojo.ParkMember;
import com.wind.web.common.ExtendController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/park_member")
public class ParkMemberController extends ExtendController<ParkMember> {
}
