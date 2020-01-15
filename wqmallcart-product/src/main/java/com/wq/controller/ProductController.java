package com.wq.controller;

import com.wq.common.PageResult;
import com.wq.common.SysResult;
import com.wq.entity.ProductInfo;
import com.wq.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Title: ProductController
 * @Description:
 * @Author: 魏秦
 * @Date: 2019/12/27  16:05
 */
@RestController
@Api(tags = {"商品相关操作API"})
@RequestMapping("/product")
public class ProductController {
    @Resource
    private ProductService productService;
    @ApiOperation(value = "显示所有商品信息")
    @RequestMapping("queryAllProduct")
    public PageResult queryAllProduct(Integer page, Integer rows){
        PageResult result= productService.queryAllProduct(page,rows);
        return result;
    }

}
