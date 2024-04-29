package com.poc.demo.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.demo.util.Webservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Component
public class ElasticsearchWebClient {

    private static final String ELASTICSEARCH_URL = "https://cwypla-821.bell.corp.bce.ca:8443";
    private static final String INDEX_NAME = "vwbbm_mdm116_gk_detail_uat_v2_test";
    private static final int PAGE_SIZE = 5000;
    private static final int NUM_PAGES = 20; // Number of pages to fetch (10 pages of 10,000 records each)
    static String endpoint = "bbm-efastdb-prod";
    @Autowired
    private Webservice webService;

    public    Map<String, Object> callelk() {

        WebClient wc = webService.getWebClientInstance(endpoint);
        Map<String, Object> allRecords = new HashMap<>();
        for (int i = 0; i < NUM_PAGES; i++) {

            int from = i * PAGE_SIZE;
            Map<String, Object> pageRecords = fetchPage(wc, INDEX_NAME, from, PAGE_SIZE);
            allRecords.putAll(pageRecords);

        }

        // Print or process the allRecords map
        //allRecords.forEach((id, record) -> System.out.println("ID: " + id + ", Record: " + record));
        return allRecords;
    }

    private static Map<String, Object> fetchPage(WebClient wx, String index, int from, int size) {
        String result=null;
        try {
            String query = "{\"query\":{\"match_all\":{}},\"size\":" + size + ",\"from\":" + from + "}";
            Mono<String> response = wx.post()
                    .uri(index + "/_search?scroll=5m")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(BodyInserters.fromValue(query))
                    .retrieve()
                    .bodyToMono(String.class);
              result = response.block();

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return parseResponse(result);
    }

    private static Map<String, Object> parseResponse(String response) {
        Map<String, Object> result = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> jsonResponse = mapper.readValue(response, Map.class);
            if (jsonResponse.containsKey("hits")) {
                Map<String, Object> hits = (Map<String, Object>) jsonResponse.get("hits");
                if (hits.containsKey("hits")) {
                    for (Map<String, Object> hit : (Iterable<Map<String, Object>>) hits.get("hits")) {
                        String id = (String) hit.get("_id");
                        Map<String, Object> source = (Map<String, Object>) hit.get("_source");
                        result.put(id, source);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
