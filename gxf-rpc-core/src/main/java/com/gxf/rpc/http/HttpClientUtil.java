package com.gxf.rpc.http;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.util.Timeout;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

public class HttpClientUtil {

    private static CloseableHttpClient httpClient;

    static {
        SocketConfig socketConfig = SocketConfig.custom()
                .setSoTimeout(Timeout.ofMilliseconds(1000))
                .build();

        PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder
                .create()
                .setDefaultSocketConfig(socketConfig)
                .setMaxConnTotal(1000)
                .setMaxConnPerRoute(50)
                .build();

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(8000))
                .setResponseTimeout(Timeout.ofMilliseconds(8000))
                .setConnectTimeout(Timeout.ofMilliseconds(8000))
                .build();

        httpClient = HttpClients
                .custom()
                .disableContentCompression()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    public static CloseableHttpResponse get(String url) throws IOException {
        HttpGet request = new HttpGet(url);
        return httpClient.execute(request);
    }

    public static CloseableHttpResponse get(String url, Map<String, String> headers) throws IOException {
        HttpGet request = new HttpGet(url);
        BasicHeader[] head = mapToHeaders(headers);
        request.setHeaders(head);
        return httpClient.execute(request);
    }

    public static CloseableHttpResponse post(String url, HttpEntity httpEntity) throws IOException {
        HttpPost request = new HttpPost(url);
        request.setEntity(httpEntity);
        return httpClient.execute(request);
    }

    public static CloseableHttpResponse post(String url, Map<String, String> headers, HttpEntity httpEntity) throws IOException {
        HttpPost request = new HttpPost(url);
        BasicHeader[] head = mapToHeaders(headers);
        request.setHeaders(head);
        request.setEntity(httpEntity);
        return httpClient.execute(request);
    }

    public static BasicHeader[] mapToHeaders(Map<String, String> map) {
        if(map.isEmpty()){
            return null;
        }
        BasicHeader[] headers = new BasicHeader[map.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            headers[i++] = new BasicHeader(entry.getKey(), entry.getValue());
        }
        return headers;
    }

    public static void closeQuietly(Closeable is) {
        if (is != null) {
            try {
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
