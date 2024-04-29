package com.poc.demo.dao;

import com.poc.demo.util.Webservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ElkScroll {

    private static final String ELASTICSEARCH_URL = "https://cwypla-821.bell.corp.bce.ca:8443";
    private static final String INDEX_NAME = "vwbbm_mdm116_gk_detail_uat_v2_test";
    private static final int PAGE_SIZE = 5000;
    private static final int NUM_PAGES = 20; // Number of pages to fetch (10 pages of 10,000 records each)
    static String endpoint = "bbm-efastdb-prod";
    @Autowired
    private Webservice webService;


    public List< List<Map<String, Object>>> fetchDataWithScrollApi() {
        int batchSize = 10000;
        String scrollId = "";
        WebClient wc = webService.getWebClientInstance(endpoint);
       List<List<Map<String, Object>>>  a=new ArrayList<>();
    int c=0;
        while (true) {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("scroll", "1m");
            requestBody.put("size", batchSize);
            if (!scrollId.isEmpty()) {
                requestBody.put("scroll_id", scrollId);
            }
            Map<String, Object> response=new HashMap<>();
        if(c==0) {
            String queryString = "{\"size\":10000,\"query\":{\"match_all\":{}}}";

            Mono<Map> responseMono = wc.post()
                    .uri(INDEX_NAME + "/_search?scroll=5m")
                    .body(BodyInserters.fromValue(queryString))
                    .retrieve()
                    .bodyToMono(Map.class);
            response= responseMono.block();
            c++;
        }
        else{
           String q = "{\"size\":10000,\"scroll_id\":\"" + scrollId + "\",\"query\":{\"match_all\":{}}}";

            Mono<Map> responseMono = wc.post()
                    .uri(INDEX_NAME + "/_search?scroll=5m")
                    .bodyValue(q)
                    .retrieve()
                    .bodyToMono(Map.class);
            response= responseMono.block();
        }
//            String scroll_id=null;
//            String queryString = "{\"size\":10000,\"scroll_id\":" + scroll_id + ",\"query\":{\"match_all\":{}}}";


            if (response == null || !response.containsKey("hits")) {
                break;
            }

            List<Map<String, Object>> hits = (List<Map<String, Object>>) ((Map<String, Object>) response.get("hits")).get("hits");
            if (hits.isEmpty()) {
                break;
            }
a.add( hits);
            scrollId = (String) response.get("_scroll_id");

            // Process the hits
//            for (Map<String, Object> hit : hits) {
//                // Process each hit
//            }
        }
 return a;
        // Close the scroll
//        wc.delete()
//                .uri("/_search/scroll")
//                .body(BodyInserters.fromValue("{\"scroll_id\": [\"" + scrollId + "\"]}"))
//                .retrieve()
//                .bodyToMono(Void.class)
//                .block();
    }


    public List<Map<String, Object>> fetch(List<String> data){
        List<Map<String, Object>> reslist=new ArrayList<>();
        WebClient wc = webService.getWebClientInstance(endpoint);
        for(int i=0;i<20;i++){
            int start=i*5000;
            int end=start+5000;
            List<String> sublist=data.subList(start,end);
//            Map<String, Object> queryMap = new HashMap<>();
//            queryMap.put("size", 10000);
//            Map<String, Object> termsMap = new HashMap<>();
//            Map<String, Object> qmap  = new HashMap<>();
//            termsMap.put("id", sublist);
//            qmap.put("terms", termsMap);
//            queryMap.put("query", qmap);
//            System.out.println(queryMap);


            Map<String, Object> termsmap = new HashMap<>();
            termsmap.put("id", sublist);
            Map<String, Object> t = new HashMap<>();
            t.put("terms",termsmap);
            List< Map<String, Object>> mustnotmap = new ArrayList<Map<String,Object>>();
            mustnotmap.add(t);
            Map<String, Object> boolmap = new HashMap<>();
            boolmap.put("must_not", mustnotmap);
            Map<String, Object> qmap = new HashMap<>();
            qmap.put("bool", boolmap);
            Map<String, Object> query = new HashMap<>();
            query.put( "query", qmap);
            query.put("size","10000");
            Map<String, Object> pageRecords = fetchPage(wc, INDEX_NAME,query);
            System.out.println(pageRecords);
            reslist.add(pageRecords);
        }
        return reslist;
    }

    private static Map<String, Object> fetchPage(WebClient wc, String index, Map<String, Object> queryMap){
        Object resObj=null;
        resObj = wc.post().uri(INDEX_NAME + "/_search" )
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).bodyValue(queryMap)
                .exchangeToMono(wcRes -> wcRes.bodyToMono(Map.class)).block();
    Map<String,Object> res= (Map<String, Object>) resObj;
return res;
    }
}
