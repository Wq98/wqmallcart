package com.wq.mapper;

import com.wq.entity.ProductInfo;

import java.util.List;

/**
 * @Title: SearchMapper
 * @Description:
 * @Author: 魏秦
 * @Date: 2020/1/6  17:36
 */
public interface SearchMapper {
    /***
     * 查询所有商品
     * @return
     */
    List<ProductInfo> queryAll();
}
