package com.wq.mapper;

import com.wq.entity.ProductInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Title: ProductMapper
 * @Description:
 * @Author: 魏秦
 * @Date: 2019/12/27  16:05
 */
public interface ProductMapper {
    /***
     * 分页查询商品
     * @param start
     * @param rows
     * @return
     */
    List<ProductInfo> queryAllProduct(@Param("start") int start, @Param("rows")Integer rows);

    /***
     * 查询商品总量
     * @return
     */
    int queryTotal();

}
