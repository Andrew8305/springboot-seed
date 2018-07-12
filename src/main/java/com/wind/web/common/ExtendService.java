package com.wind.web.common;

import tk.mybatis.mapper.entity.Example;

import java.util.List;

public class ExtendService<T> extends BaseService<T> {
    /**
     * 根据用户ID获取用户关联信息列表（限制用户关联表使用）
     *
     * @param userId 用户ID
     * @return 实例列表
     */
    public List<T> selectAllByUserId(Long userId) {
        Example example = new Example(getActualClass());
        example.setOrderByClause("id desc");
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        return mapper.selectByExample(example);
    }
}
