package com.wq.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: JedisPoolConfig
 * @Description:
 * @author: 魏秦
 * @date: 2019/11/26  9:13
 */
@Configuration
@ConfigurationProperties(prefix = "wqblog.redis")
public class JedisPoolConfig {
   private  List<String> nodes;
   private  Integer maxTotal;
   private  Integer maxIdle;
   private Integer minIdle;

    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    public Integer getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }

    public Integer getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    /***
     * 初始化方法
     * 读取配置文件中的属性
     * @return
     */
    @Bean
    public ShardedJedisPool initShardedJedisPool(){
        //读取配置文件的节点信息
        List<JedisShardInfo> jedisShardInfos=new ArrayList<JedisShardInfo>();
        for (String node:nodes) {
            String hostIp=node.split(":")[0];
            Integer port=Integer.parseInt(node.split(":")[1]);
            jedisShardInfos.add(new JedisShardInfo(hostIp,port));
        }
        //配置jedis连接池信息
        GenericObjectPoolConfig config=new GenericObjectPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        ShardedJedisPool shardedJedisPool=new ShardedJedisPool(config,jedisShardInfos);
        return shardedJedisPool;
    }
}
