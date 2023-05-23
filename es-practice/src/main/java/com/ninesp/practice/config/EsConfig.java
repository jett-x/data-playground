/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package com.ninesp.practice.config;

import static cn.hutool.core.text.StrPool.COLON;

import cn.hutool.core.text.CharSequenceUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import nl.altindag.ssl.SSLFactory;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ninesp
 * @date 2023/5/22
 */
@Configuration
public class EsConfig {

    @Resource
    private EsProperties esProperties;
    @Bean
    @ConditionalOnMissingBean
    public RestHighLevelClient restHighLevelClient() {
        // 处理地址
        String address = esProperties.getAddress();
        if (CharSequenceUtil.isEmpty(address)) {
            throw new  IllegalArgumentException("please config the es address");
        }
        if (!address.contains(COLON)) {
            throw new  IllegalArgumentException("the address must contains port and separate by ':'");
        }
        String schema = CharSequenceUtil.isEmpty(esProperties.getSchema())
            ? "http" : esProperties.getSchema();
        List<HttpHost> hostList = new ArrayList<>();
        Arrays.stream(esProperties.getAddress().split(","))
            .forEach(item -> hostList.add(new HttpHost(item.split(":")[0],
                Integer.parseInt(item.split(":")[1]), schema)));

        // 转换成 HttpHost 数组
        HttpHost[] httpHost = hostList.toArray(new HttpHost[]{});

        // 构建连接对象
        RestClientBuilder builder = RestClient.builder(httpHost);
        SSLFactory sslFactory = SSLFactory.builder().withUnsafeHostnameVerifier().withUnsafeTrustMaterial().build();
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            // 设置心跳时间,最大连接数,最大连接路由

            // 设置账号密码
            String username = esProperties.getUsername();
            String password = esProperties.getPassword();
            if (CharSequenceUtil.isNotEmpty(username) && CharSequenceUtil.isNotEmpty(password)) {
                final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(username, password));
                //禁用ssl
                httpClientBuilder.setSSLContext(sslFactory.getSslContext())
                    .setSSLHostnameVerifier(sslFactory.getHostnameVerifier());
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            }
            return httpClientBuilder;
        });

        // 设置超时时间之类的
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            return requestConfigBuilder;
        });

        return new RestHighLevelClient(builder);
    }
}
