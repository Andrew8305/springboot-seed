package com.wind.web;

import com.wind.common.PaginatedResult;
import com.wind.exception.ResourceNotFoundException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class BaseController<T> {

    @Autowired
    protected BaseService<T> service;

    @ApiOperation(value = "分页查询实例")
    @GetMapping("/all/{page}")
    public ResponseEntity<?> search(
            @RequestParam(value = "type", required = false, defaultValue = "") String type,
            @RequestParam(value = "value", required = false, defaultValue = "") String value,
            @PathVariable int page) throws Exception{
        if ("".equals(type)) {
            return ResponseEntity
                    .ok(new PaginatedResult()
                            .setData(service.selectAll(page))
                            .setCurrentPage(page)
                            .setCount(service.getCount()));
        } else {
            return ResponseEntity
                    .ok(new PaginatedResult()
                            .setData(service.selectAll(type, value, page))
                            .setCurrentPage(page)
                            .setCount(service.getCount(type, value)));
        }
    }

    @ApiOperation(value = "根据ID获取实例")
    @GetMapping("/{id}")
    public ResponseEntity<?> selectById(@PathVariable Long id) {
        return service
                .selectByID(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException()
                        .setId(id));
    }

    @ApiOperation(value = "新增实例")
    @PostMapping
    public ResponseEntity<?> post(@RequestBody T instance) {
        service.add(instance);
        return ResponseEntity.accepted().body(instance);
    }

    @ApiOperation(value = "新增多个实例")
    @PostMapping("/list")
    public ResponseEntity<?> post(@RequestBody List<T> list) {
        int count = service.add(list);
        return ResponseEntity.accepted().body(count);
    }

    @ApiOperation(value = "根据ID修改实例")
    @PutMapping
    public ResponseEntity<?> put(@RequestBody T instance) {
        boolean result = service.modifyById(instance);
        if (result)
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(instance);
        else
            return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "根据ID删除实例")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        boolean result = service.deleteById(id);
        if (result)
            return ResponseEntity.accepted().build();
        else
            return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "删除所有实例")
    @DeleteMapping("/all")
    public ResponseEntity<?> deleteAll() {
        boolean result = service.deleteAll();
        if (result)
            return ResponseEntity.accepted().build();
        else
            return ResponseEntity.notFound().build();
    }
}
