package com.poc.demo.util;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;


import java.util.HashMap;
import java.util.Map;

@Component
public class Webservice {

    private static Map<String, WebClient> WEB_CLIENT_STORE = new HashMap<>();
    public WebClient getWebClientInstance(String endPointName) {
        WebClient wc = null;
        try {
            if (WEB_CLIENT_STORE.get(endPointName) == null) {
                createWebClient(endPointName);
            }
            wc = WEB_CLIENT_STORE.get(endPointName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wc;
    }
    private void createWebClient(String endPointName) {
        try {
               String host="https://cwypla-821.bell.corp.bce.ca:8443";

           // String endpoint = appProps.getServers().getWebService().getEndPoints().get(endPointName);
            String authType = "BASIC";
            String baseUrl = host + "/";
            WebClient wc = null;
            if (authType != null && authType.equalsIgnoreCase("BASIC")) {
                String auth = "Basic c3ZjLWVmYXN0LWFkbWluOkJiODNPRzRoMmw=";
                wc = WebClient.builder().clientConnector(getCustomClientHttpConnector()).baseUrl(baseUrl)
                        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .defaultHeader("Authorization", auth).exchangeStrategies(getExchangeStrategies()).build();
            } else {
                wc = WebClient.builder().clientConnector(getCustomClientHttpConnector()).baseUrl(baseUrl)
                        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .exchangeStrategies(getExchangeStrategies()).build();
            }
            WEB_CLIENT_STORE.put(endPointName, wc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private ExchangeStrategies getExchangeStrategies() {
        return ExchangeStrategies.builder().codecs(configurer -> configurer.defaultCodecs()
                        .maxInMemorySize(Integer.parseInt("100000000")))
                .build();
    }
    private ClientHttpConnector getCustomClientHttpConnector() {
        ClientHttpConnector httpConnector = null;
        try {
            SslContext sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            // Allow Self-Signed SSL Certificates
            HttpClient httpClient = HttpClient.create().secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));

            httpConnector = new ReactorClientHttpConnector(httpClient);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpConnector;
    }
}
