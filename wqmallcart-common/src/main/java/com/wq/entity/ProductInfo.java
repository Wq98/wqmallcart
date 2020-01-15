package com.wq.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Title: ProductInfo
 * @Description:
 * @Author: 魏秦
 * @Date: 2019/12/27  15:52
 */
@Data
public class ProductInfo {
    /***
     * 商品信息
     */
    private String productId;
    private String productName;
    private Double productPrice;
    private String productCategory;
    private String productImgurl;
    private Integer productNum;
    private String productDescription;
    private Date createTime;
    private Date modifyTime;
}
