package com.wind.web;

import com.wind.mybatis.pojo.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ExtendController<T> extends BaseController<T> {

    @Autowired
    public BaseService<User> userService;
    public BaseService<Group> groupService;
    public BaseService<Authority> authorityService;

    public QueryResult unionQueryUser(String type, String value, int page, String column) throws Exception {
        QueryResult result = new QueryResult();
        if ("".equals(type)) {
            result = query(page, column);
            result.setUserList(userService.selectAll(result.getIds()));
        } else {
            result.setUserList(userService.selectAll(type, value));
            List<Long> ids = userService.getIds(result.getUserList());
            result.setList(service.selectAll(column, ids, page));
            result.setCount(service.getCount(column, ids));
        }
        return result;
    }

    private QueryResult query(String type, String value, int page, String column) throws Exception {
        QueryResult result = new QueryResult();
        result.setList(service.selectAll(type, value, page));
        result.setCount(service.getCount(type, value));
        result.setIds(service.getRelatedIds(result.getList(), column));
        return result;
    }

    private QueryResult query(int page, String column) throws Exception {
        QueryResult result = new QueryResult();
        result.setList(service.selectAll(page));
        result.setCount(service.getCount());
        result.setIds(service.getRelatedIds(result.getList(), column));
        return result;
    }

    @Data
    @NoArgsConstructor
    public class QueryResult {
        private List<User> userList = new ArrayList<>();
        private List<Group> groupList = new ArrayList<>();
        private List<Authority> authorityList = new ArrayList<>();
        private List<Long> ids = new ArrayList<>();
        private int count = 0;
        private List<T> list = new ArrayList<>();
    }
}
