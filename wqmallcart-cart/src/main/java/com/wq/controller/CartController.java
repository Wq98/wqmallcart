package com.wq.controller;

import com.wq.common.SysResult;
import com.wq.entity.CartInfo;
import com.wq.service.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;
import java.util.List;


/**
 * @Title: CartController
 * @Description:
 * @Author: 魏秦
 * @Date: 2019/12/31  11:18
 */
@RestController
@Api(tags = {"购物车相关操作API"})
@RequestMapping("/cart")
public class CartController {
    @Resource
    private CartService cartService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ShardedJedisPool pool;
    @ApiOperation(value = "从redis中获取加购信息",notes="需要传递cartInfo对象")
    @RequestMapping("saveOne")
    public SysResult saveOne(CartInfo cartInfo){
        try {
            cartService.saveOne(cartInfo);
            return SysResult.ok();
        }catch (Exception e){
            e.printStackTrace();
            return  SysResult.build(201,"",null);
        }
    }
    @ApiOperation(value = "显示购物车项",notes="需要传递userPhone参数")
    @RequestMapping("queryCartList{userPhone}")
   public List<CartInfo> queryCartList(String userPhone){
        return cartService.queryCartList(userPhone);
    }

    @ApiOperation(value = "计算总价",notes="需要传递userPhone和productId参数")
    @RequestMapping("calcPrice")
    public List<CartInfo> calcPrice(String userPhone,String productId){
        return cartService.calcPrice(userPhone,productId);
    }

    @ApiOperation(value = "删除某个购物车项",notes="需要传递cartInfo对象")
    @RequestMapping("removeCartProduct")
    public SysResult removeCartProduct(CartInfo cartInfo){
        String key=cartInfo.getUserPhone()+cartInfo.getProductId();
        if(stringRedisTemplate.delete(key)==true){
            return SysResult.ok();
        }else{
            return SysResult.build(201,"",null);
        }
    }
    @ApiOperation(value = "删除多个购物车项",notes="需要传递cartId数组")
    @RequestMapping("removeProductList")
    public SysResult removeProductList(String[] cartIds){
        try {
            for (String key: cartIds) {
                stringRedisTemplate.delete(key);
            }
            return SysResult.ok();
        }catch (Exception e){
            e.printStackTrace();
            return SysResult.build(201,"",null);
        }

    }
}
