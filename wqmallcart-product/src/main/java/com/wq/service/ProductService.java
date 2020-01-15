package com.wq.service;

import com.wq.common.PageResult;
import com.wq.entity.ProductInfo;
import com.wq.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Title: ProductService
 * @Description:
 * @Author: 魏秦
 * @Date: 2019/12/27  16:05
 */
@Service
public class ProductService {
    @Resource
    private ProductMapper productMapper;
    public PageResult queryAllProduct(Integer page, Integer rows) {
        PageResult result=new PageResult();
        int total=productMapper.queryTotal();
        result.setTotal(total);
        int start=(page-1)*rows;
        List<ProductInfo> productInfoList=productMapper.queryAllProduct(start,rows);
        result.setRows(productInfoList);
        return result;
    }

}
