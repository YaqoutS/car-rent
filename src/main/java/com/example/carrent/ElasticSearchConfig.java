package com.example.carrent;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;

//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.client.ClientConfiguration;
//import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.TrustManagerFactory;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.security.KeyManagementException;
//import java.security.KeyStore;
//import java.security.KeyStoreException;
//import java.security.NoSuchAlgorithmException;
//import java.security.cert.Certificate;
//import java.security.cert.CertificateException;
//import java.security.cert.CertificateFactory;
//import java.util.Base64;
//import java.util.Base64.Decoder.*;


@Configuration
public class ElasticSearchConfig {
    @Bean
    public RestClient getRestClient() {
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200)).build();
        return restClient;
    }

    @Bean
    public  ElasticsearchTransport getElasticsearchTransport() {
        ObjectMapper customMapper = new ObjectMapper();
        customMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return new RestClientTransport(
                getRestClient(), new JacksonJsonpMapper(customMapper));
    }

    @Bean
    public ElasticsearchClient getElasticsearchClient(){
        ElasticsearchClient client = new ElasticsearchClient(getElasticsearchTransport());
        return client;
    }
}

//** Elasticsearch configuration when security is enabled **// it isn't working well

//@Configuration
//public class ElasticSearchConfig extends ElasticsearchConfiguration {
//
//    //@Value("${spring.elasticsearch.client.certificate}")
//    private String certificateBase64 = "8c390bf95e12281c4a806770c8ed53aa6f80cd912399b67d2099527410a5e3a5";
//
//    @Override
//    public ClientConfiguration clientConfiguration() {
//        final ClientConfiguration clientConfiguration;
//        try {
//            clientConfiguration = ClientConfiguration.builder()
//                    .connectedTo("localhost:9200")
//                    .usingSsl(getSSLContext())
//                    .withBasicAuth("elastic", "nIFU+N*q9Qa6X4oJ-D9u")
//                    .build();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        return clientConfiguration;
//    }
//
//    private SSLContext getSSLContext() throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
//        byte[] decode = Base64.getDecoder().decode(certificateBase64);
//
//        CertificateFactory cf = CertificateFactory.getInstance("X.509");
//
//        Certificate ca;
//        try (InputStream certificateInputStream = new ByteArrayInputStream(decode)) {
//            ca = cf.generateCertificate(certificateInputStream);
//        }
//
//        String keyStoreType = KeyStore.getDefaultType();
//        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
//        keyStore.load(null, null);
//        keyStore.setCertificateEntry("ca", ca);                                             // here is the problem
//
//        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//        TrustManagerFactory tmf =
//                TrustManagerFactory.getInstance(tmfAlgorithm);
//        tmf.init(keyStore);
//
//        SSLContext context = SSLContext.getInstance("TLS");
//        context.init(null, tmf.getTrustManagers(), null);
//        return context;
//    }
//
//}