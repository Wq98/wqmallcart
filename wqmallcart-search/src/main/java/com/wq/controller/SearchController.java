package com.wq.controller;


import com.wq.entity.ProductInfo;
import com.wq.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.solr.client.solrj.SolrServerException;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @Title: SearchController
 * @Description:
 * @Author: 魏秦
 * @Date: 2020/1/6  17:36
 */
@RestController
@Api(tags = {"搜索相关操作API"})
public class SearchController {
    @Resource
    private SearchService searchService;

    @ApiOperation(value = "创建索引")
    @RequestMapping("/createIndex")
    public String createIndex(){
        searchService.createIndex();
        return "success";
    }

    @ApiOperation(value = "全文搜索",notes="需要传递搜索参数")
    @RequestMapping("/search/queryProduct")
    public List<ProductInfo> queryProduct(String query){
        List<ProductInfo> list=searchService.queryProduct(query);
        return list;
    }
    
    @ApiOperation(value = "全文搜索",notes="需要传递搜索参数")
    @RequestMapping("/search/queryProductBySolr{keyword}")
    public List<ProductInfo> queryProductBySolr(String keyword) throws IOException, SolrServerException {
        List<ProductInfo> list=searchService.searchProduct(keyword,"<em><font color=\'brown\'>","</font></em>");
        return list;
    }
}
