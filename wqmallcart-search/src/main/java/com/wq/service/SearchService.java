package com.wq.service;

import com.alibaba.fastjson.JSON;
import com.wq.common.TypeTransformMapper;
import com.wq.entity.ProductInfo;
import com.wq.mapper.SearchMapper;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.noggit.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Title: SearchService
 * @Description:
 * @Author: 魏秦
 * @Date: 2020/1/6  17:36
 */
@Service
public class SearchService {
    @Resource
    private SearchMapper searchMapper;
    @Autowired
    private TransportClient client;


    /***
     * 創建索引
     */
    public void createIndex() {
        IndicesAdminClient admin=client.admin().indices();
        IndicesExistsRequestBuilder requestBuilder=admin.prepareExists("emprod");
        IndicesExistsResponse response=requestBuilder.get();
        if(!response.isExists()){
            admin.prepareCreate("emprod").get();
        }
        List<ProductInfo> proList=searchMapper.queryAll();
        for(ProductInfo productInfo : proList){
            try {
                String proJson= TypeTransformMapper.OM.writeValueAsString(productInfo);
                IndexRequestBuilder request=client.prepareIndex("emprod","product",productInfo.getProductId());
                request.setSource(proJson, XContentType.JSON).get();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public List<ProductInfo> queryProduct(String keyWord) {
        List<ProductInfo> productInfoList= new ArrayList<ProductInfo>();
        MatchQueryBuilder queryBuilder=QueryBuilders.matchQuery("productName",keyWord);
        SearchRequestBuilder requestBuilder=client.prepareSearch("emprod").setQuery(queryBuilder);
        SearchResponse response=requestBuilder.get();
        SearchHits topHits=response.getHits();
        SearchHit[] hits=topHits.getHits();
        for (SearchHit hit: hits) {
            try {
                String pJson=hit.getSourceAsString();
                ProductInfo productInfo=TypeTransformMapper.OM.readValue(pJson,ProductInfo.class);
                productInfoList.add(productInfo);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return productInfoList;
    }

    private static final String  SOLR_URL ="http://localhost:8983/solr/test_core";
    private HttpSolrClient httpSolrClient = new HttpSolrClient.Builder(SOLR_URL).build();
    private static final String PRODUCT_NAME_FIELD = "product_name";
    private static final String PRODUCT_IMGURL_FIELD = "product_imgurl";
    private static final String PRODUCT_PRICE_FIELD = "product_price";

    public List<ProductInfo> searchProduct(String keyword,String hlPer, String hlPos) throws IOException, SolrServerException {
        List<ProductInfo> productInfoList=new ArrayList<>();
        ProductInfo productInfo=new ProductInfo();
        SolrDocumentList list = new SolrDocumentList();
        SolrQuery query=new SolrQuery("product_name:"+keyword);
        query.setHighlight(true);
        query.setHighlightSimplePre(hlPer);
        query.setHighlightSimplePost(hlPos);
        query.set("hl.fl",PRODUCT_NAME_FIELD);
        QueryResponse response=httpSolrClient.query(query);
        SolrDocumentList solrDocumentList = response.getResults();
        Map<String, Map<String, List<String>>> map = response.getHighlighting();
        for(SolrDocument solrDocument :solrDocumentList) {
            String str=map.get(solrDocument.getFieldValue("id")).get("product_name").toString();
            String newStr=str.substring(1,str.length()-1);
            solrDocument.setField("product_name",newStr);
            String toJSON = JSONUtil.toJSON(solrDocument);
            productInfo = JSON.parseObject(toJSON, ProductInfo.class);
            productInfoList.add(productInfo);
        }
    //    Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
       /* Map<String, Map<String, List<String>>> map = response.getHighlighting();
        for(int i=0;i<solrDocumentList.size();i++){
            SolrDocument solrDocument = solrDocumentList.get(i);
            solrDocument.setField("product_name",map.get(solrDocument.getFieldValue("id")).get("product_name"));
            list.add(solrDocument);
            productInfo = JSON.parseObject(String.valueOf(list), ProductInfo.class);
            productInfoList.add(productInfo);
        }*/

           /* for (Map.Entry<String, Map<String, List<String>>> entry : response.getHighlighting().entrySet()) {
                if (entry.getValue().containsKey(PRODUCT_NAME_FIELD)) {
                    List<String> contentList = entry.getValue().get(PRODUCT_NAME_FIELD);
                    System.out.println("contentList:  " + contentList);
                    if(contentList.size() > 0) {

                            productInfo.setProductName(contentList.get(0));
                            System.out.println("cList:  " + contentList.get(0));

                    }
                }
                productInfoList.add(productInfo);
            }*/
        return productInfoList;
    }
}
