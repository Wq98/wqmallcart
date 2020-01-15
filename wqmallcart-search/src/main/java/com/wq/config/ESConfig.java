package com.wq.config;

import lombok.Data;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.util.List;

/**
 * @Title: ESConfig
 * @Description:
 * @Author: 魏秦
 * @Date: 2020/1/6  17:36
 */
@Configuration
@ConfigurationProperties(prefix = "es")
public class ESConfig {
    private List<String> nodes;

    /***
     * 初始化方法
     * @return
     */
    @Bean
    public TransportClient initTransportClient() {
        Settings settings=Settings.builder()
                .put("client.transport.sniff", true)
                .put("cluster.name","wqelasticsearch")
                .build();
        TransportClient client = new PreBuiltTransportClient(settings);
        //解析nodes
        try {
            for (String node : nodes) {
                String host = node.split(":")[0];
                int port = Integer.parseInt(node.split(":")[1]);
                InetAddress esA1 = InetAddress.getByName(host);
                TransportAddress addressT1 = new TransportAddress(esA1, 9300);
                client.addTransportAddress(addressT1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return client;
    }

    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }
}
