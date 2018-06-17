package com.wind.web;

import com.wind.common.PaginatedResult;
import com.wind.exception.ResourceNotFoundException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BaseController<T> {

    @Autowired
    protected ExtendService<T> service;

    /**
     * 获取真实反射类型
     *
     * @return 反射类型
     */
    Class getActualClass() {
        Type type = getClass().getGenericSuperclass();

        // 判断是否泛型
        if (type instanceof ParameterizedType) {
            // 返回表示此类型实际类型参数的Type对象的数组
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            return (Class) types[0];  //将第一个泛型T对应的类返回
        } else {
            return (Class) type;//若没有给定泛型，则返回Object类
        }
    }

    List<Field> getRelatedFields() throws Exception {
        return getRelatedFields(null);
    }

    List<Field> getRelatedFields(Class type) throws Exception {
        if (type == null) {
            type = getActualClass();
        }
        List<Field> result = new ArrayList<>();
        Field[] fs = type.getDeclaredFields();
        for (Field f : fs) {
            String name = f.getName();
            if (name.length() > 2 && name.toLowerCase().endsWith("id")) {
                if (name != "openId" && name != "unionId") {
                    // 获取ID列表
                    Field idField = getActualClass().getDeclaredField(name);
                    idField.setAccessible(true);
                    result.add(idField);
                }
            }
        }
        return result;
    }

    /**
     * 关联查询
     *
     * @param result
     * @return 关联查询结果
     */
    protected PaginatedResult relatedResult(PaginatedResult result) throws Exception {
        return result;
    }

    @ApiOperation(value = "分页查询实例")
    @GetMapping("/all/{page}")
    public ResponseEntity<?> search(
            @RequestParam(value = "type", required = false, defaultValue = "") String type,
            @RequestParam(value = "value", required = false, defaultValue = "") String value,
            @RequestParam(value = "table", required = false, defaultValue = "") String table,
            @PathVariable int page) throws Exception {
        if ("".equals(type)) {
            return ResponseEntity
                    .ok(relatedResult(new PaginatedResult()
                            .setData(service.selectAll(page))
                            .setCurrentPage(page)
                            .setCount(service.getCount())));
        } else {
            if ("".equals(table)) {
                return ResponseEntity
                        .ok(relatedResult(new PaginatedResult()
                                .setData(service.selectAll(type, value, page))
                                .setCurrentPage(page)
                                .setCount(service.getCount(type, value))));
            } else {
                return ResponseEntity
                        .ok(relatedResult(new PaginatedResult()
                                .setData(service.selectRelatedAll(type, value, table, page))
                                .setCurrentPage(page)
                                .setCount(service.getCount(type, value, table))));
            }
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
