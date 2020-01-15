package com.wq.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Title: CartInfo
 * @Description:
 * @Author: 魏秦
 * @Date: 2019/12/27  15:55
 */
@Data
public class CartInfo {
    /**
     * 购物车信息
     */
    private String cartId;
    private String userPhone;
    private String productId;
    private String productImage;
    private String productName;
    private Double productPrice;
    private Integer requestNum;
    private Date createTime;
    private Date modifyTime;
}
