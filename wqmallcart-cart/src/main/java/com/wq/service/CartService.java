package com.wq.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wq.common.UUIDUtil;
import com.wq.entity.CartInfo;
import com.wq.entity.ProductInfo;
import com.wq.mapper.CartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Title: CartService
 * @Description:
 * @Author: 魏秦
 * @Date: 2019/12/31  11:19
 */
@Service
public class CartService {
    @Resource
    private CartMapper cartMapper;
    @Resource
    private ShardedJedisPool pool;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    /***
     * 根据用户手机号查询购物车信息
     * @param userPhone
     * @return
     */
    public List<CartInfo> queryCartList(String userPhone) {
        ShardedJedis jedis=pool.getResource();
        List<CartInfo> list=new ArrayList<CartInfo>();
        Set<String> keys=stringRedisTemplate.keys(userPhone+"*");
        for (String key : keys) {
            CartInfo cartInfo=JSON.parseObject(jedis.get(key),CartInfo.class);
            list.add(cartInfo);
        }
        return list;
    }

    /***
     * 商品加购
     * @param cartInfo
     */
    public void saveOne(CartInfo cartInfo) {
        ShardedJedis jedis=pool.getResource();
        Integer count = 1;
        ProductInfo productInfo=new ProductInfo();
        productInfo=cartMapper.queryProductById(cartInfo.getProductId());
        if (!jedis.exists(cartInfo.getUserPhone()+cartInfo.getProductId())) {
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            format.format(date);
            cartInfo.setCartId(UUIDUtil.getUUID());
            cartInfo.setProductImage(productInfo.getProductImgurl());
            cartInfo.setProductName(productInfo.getProductName());
            cartInfo.setProductPrice(productInfo.getProductPrice());
            cartInfo.setCreateTime(date);
            cartInfo.setRequestNum(1);
        //    cartMapper.saveOne(cartInfo);
            jedis.set(cartInfo.getUserPhone()+cartInfo.getProductId(),JSON.toJSONString(cartInfo));
        } else {
            CartInfo cartInfo1=JSON.parseObject(jedis.get(cartInfo.getUserPhone()+cartInfo.getProductId()),CartInfo.class);
            cartInfo1.setRequestNum(cartInfo1.getRequestNum()+count);
         //   this.updateProductNum(cartInfo.getProductId(), cartInfo.getUserPhone(), cartInfo.getRequestNum());
        //    CartInfo cartInfo1=cartMapper.queryCartExist(cartInfo.getUserPhone(),cartInfo.getProductId());
            jedis.set(cartInfo1.getUserPhone()+cartInfo1.getProductId(),JSON.toJSONString(cartInfo1));
        }
    }

    public List<CartInfo> calcPrice(String userPhone, String productId) {
        ShardedJedis jedis=pool.getResource();
        List<CartInfo> list=new ArrayList<CartInfo>();
        CartInfo cartInfo=JSON.parseObject(jedis.get(userPhone+productId),CartInfo.class);
        list.add(cartInfo);
        return list;
    }

    /***
     * 删除某个购物车项
     * @param key
     * @return
     */
    /*public boolean removeCartProduct(String key) {
        System.out.println("key:  "+key);
        System.out.println("redisTemplate:  "+stringRedisTemplate.delete(key));
        if(stringRedisTemplate.delete(key)==true){
            return true;
        }else{
            return false;
        }
    }*/

    /***
     * 删除多个购物车项
     * @param cartIds
     */
    /*public void removeProductList(String[] cartIds) {
        cartMapper.removeProductList(cartIds);
    }*/

    /***
     * 根据用户手机号和商品ID查询购物车是否已经存在
     * @param productId
     * @param userPhone
     * @return
     */
   /* public Integer checkProductExist(String productId, String userPhone) {
        return cartMapper.checkProductExist(productId, userPhone);
    }*/

    /***
     * 根据商品ID、手机号、加购数量更新购物车项数据
     * @param productId
     * @param userPhone
     * @param requestNum
     * @return
     */
    /*public boolean updateProductNum(String productId, String userPhone, Integer requestNum) {
        return cartMapper.updateProductNum(productId, userPhone, requestNum);
    }*/
}
