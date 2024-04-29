package com.poc.demo.config;

import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;


public class CustomTrustManager implements X509TrustManager {
    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
        // Accept all certificates
    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
        // Accept all certificates
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
