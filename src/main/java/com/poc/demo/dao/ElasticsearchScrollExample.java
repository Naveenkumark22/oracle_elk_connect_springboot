package com.poc.demo.dao;

import com.poc.demo.util.Webservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Component
public class ElasticsearchScrollExample {
    private static final String ELASTICSEARCH_URL = "https://cwypla-821.bell.corp.bce.ca:8443";
    private static final String INDEX_NAME = "vwbbm_mdm116_sk_internal_detail_uat_v2";
    private static final String SCROLL_TIME = "20s"; // Scroll time
    private static final int PAGE_SIZE = 10000; // Page size
    String endpoint="bbm-efastdb-prod";
    @Autowired
    private   Webservice webService;


    public List<List<Map<String, Object>>> elkcall(){
        WebClient wc = webService.getWebClientInstance(endpoint);
        String scrollId = "";
        List<Map<String, Object>> finalres=new ArrayList<>();
        List<List<Map<String, Object>>> storehits=new ArrayList<>();
        //to store hits


        // Initial scroll request
        Mono<Map> scrollResponseMono = wc.post()
                .uri( INDEX_NAME + "/_search?scroll=" + SCROLL_TIME)
                .bodyValue("{\"size\": " + PAGE_SIZE + ", \"query\": {\"match_all\": {}}}")
                .retrieve()
                .bodyToMono(Map.class);

        Map<String, Object> scrollResponse = scrollResponseMono.block();
        scrollId = (String) scrollResponse.get("_scroll_id");
       // finalres.add(scrollResponse);
        int c=0;


        Map<String, Object> hitsInfo1 = (Map<String, Object>) scrollResponse.get("hits");
        List<Map<String, Object>> hits1 = (List<Map<String, Object>>) hitsInfo1.get("hits");
        storehits.add(hits1);
        // .bodyValue("{\"size\": " + PAGE_SIZE + ",\"scroll_id\": \"" + scrollId + "\", \"scroll\": \"" + SCROLL_TIME + "\"}")

        // Loop to fetch all records
        while (true) {
            Mono<Map>   scrollResponseMono1 = wc.post()
                    .uri("/_search/scroll")
                    .bodyValue("{\"scroll_id\": \"" + scrollId + "\", \"scroll\": \"" + SCROLL_TIME + "\"}")
                    .retrieve()
                    .bodyToMono(Map.class);
            System.out.println("--------------------"+ c++ +"-------------");
//            scrollResponse1 = scrollResponseMono.block();
//            scrollId = (String) scrollResponse1.get("_scroll_id");
            Map<String, Object> scrollResponse1 = scrollResponseMono1.block();
            finalres.add(scrollResponse1);
            // Process the current batch of hits
            System.out.println("Fetched " + PAGE_SIZE + " records");

//            Map<String, Object> hitsInfo = (Map<String, Object>) scrollResponse.get("hits");
//            Map<String, Object> totalHitsObject = (Map<String, Object>) hitsInfo.get("total");
//            Integer totalHits = (Integer) totalHitsObject.get("value");
//            List<Map<String, Object>> innerhits = (List<Map<String, Object>>) scrollResponse.get("hits");

            Map<String, Object> hitsInfo = (Map<String, Object>) scrollResponse1.get("hits");
            List<Map<String, Object>> hits = (List<Map<String, Object>>) hitsInfo.get("hits");
            storehits.add(hits);
// Now 'hits' contains the array of hits

 if(hits.isEmpty())
            {
                System.out.println("---------------------exit----------------------");
                break;
            }

//            if (totalHits == 0L) {
//                break; // Exit loop if no more hits
//            }
//          System.out.println(hits);
//            System.out.println(scrollResponse1);
//            System.out.println(scrollId);
        }
        // Clear the scroll context


        return  storehits;
         //Clear the scroll context
//        wc.delete()
//                .uri("/_search/scroll")
//                .bodyValue("{\"scroll_id\": [\"" + scrollId + "\"]}")
//                .retrieve()
//                .bodyToMono(Void.class)
//                .block();
    }


//    public long getindexsize()
//    {
//        WebClient wc = webService.getWebClientInstance(endpoint);
//
//    }

}
