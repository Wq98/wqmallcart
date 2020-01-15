package com.wq.mapper;

import com.wq.entity.CartInfo;
import com.wq.entity.ProductInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Title: CartMapper
 * @Description:
 * @Author: 魏秦
 * @Date: 2019/12/31  11:18
 */
@Service
public interface CartMapper {
    /**
     * 购物车加购
     *
     * @param cartInfo
     */
    void saveOne(CartInfo cartInfo);

    /***
     * 购物车列表
     * @param userPhone
     * @return
     */
    List<CartInfo> queryCartList(String userPhone);

    /***
     * 删除购物车某个商品
     * @param cartId
     * @return
     */
    boolean removeCartProduct(String cartId);

    /***
     * 批量删除购物车数据
     * @param cartIds
     */
    void removeProductList(String[] cartIds);

    /***
     * 查询商品是否已加购
     * @param productId
     * @param userPhone
     * @return
     */
    Integer checkProductExist(String productId, String userPhone);

    /***
     * 如果商品已经加购过则数量增加
     * @param productId
     * @param userPhone
     * @param requestNum
     * @return
     */
    boolean updateProductNum(String productId, String userPhone, Integer requestNum);

    /***
     * 根据用户账号和商品ID查商品加购信息
     * @param userPhone
     * @param productId
     * @return
     */
    CartInfo queryCartExist(String userPhone, String productId);

    /***
     * 根据商品id查询商品信息
     * @param productId
     * @return
     */
    ProductInfo queryProductById(String productId);
}
