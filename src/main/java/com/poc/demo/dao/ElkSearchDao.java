package com.poc.demo.dao;

import com.poc.demo.model.Searchquery;
import com.poc.demo.util.Webservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class ElkSearchDao {
    @Autowired
    private Webservice webService;
    public Object querySource(String queryString) {
     //   String reqId, String srcName, SearchQuery query, String url, String filterPath
        String endpoint="bbm-efastdb-prod";
        String url="vwbbm_mdm116_gk_detail_uat_v2_test";
    //    String filterPath= "hits.total,hits.total.value,hits.max_score,hits.hits._score,hits.hits._source,hits.hits.inner_hits";
     String filterPath="hits.total,hits.total.value,hits.max_score,hits.hits._score,hits.hits._source,hits.hits.inner_hits";
        WebClient wc = webService.getWebClientInstance(endpoint);
        Object resObj = null;
        System.out.println(wc);
//        resObj = wc.post().uri(url + "/_search?scroll=1m")
//                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).bodyValue(queryString)
//                .exchangeToMono(wcRes -> wcRes.bodyToMono(Map.class)).block();



        resObj = wc.post().uri(url + "/_search?filter_path=" + filterPath)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).bodyValue(queryString)
                .exchangeToMono(wcRes -> wcRes.bodyToMono(Map.class)).block();

//        resObj = wc.post().uri(url + "/_search?filter_path=" + filterPath).body(Mono.just(query), Searchquery.class)
//                .retrieve().bodyToMono(Map.class).block();
        return resObj;
    }
}
