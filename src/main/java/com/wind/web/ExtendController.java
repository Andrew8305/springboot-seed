package com.wind.web;

import com.wind.common.PaginatedResult;
import com.wind.common.SpringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ExtendController<T> extends BaseController<T> {

    public PaginatedResult relatedResult(PaginatedResult result) throws Exception {
        List<T> list = (List<T>) result.getData();
        Field[] fs = getActualClass().getDeclaredFields();
        for (Field f : fs) {
            String name = f.getName();
            if (name.length() > 2 && name.toLowerCase().endsWith("id")) {
                // 获取ID列表
                Field idField = getActualClass().getDeclaredField(name);
                idField.setAccessible(true);
                List<Long> ids = new ArrayList<>();
                for (T instance : list) {
                    Long e = Long.parseLong(idField.get(instance).toString());
                    if (!ids.contains(e)) {
                        ids.add(e);
                    }
                }
                // 获取ID对应Service
                String typeName = f.getName().substring(0, name.length() - 2);
                Object relatedService =  SpringUtil.getBean(typeName + "Service");
                // 获取ids查询方法
                Method selectMethod = relatedService.getClass().getDeclaredMethod("selectAll", List.class);
                Object relatedResult = selectMethod.invoke(relatedService, ids);
                result.getDictionary().put(typeName, relatedResult);
            }
        }
        return result;
    }
}
