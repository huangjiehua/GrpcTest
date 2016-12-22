package com.buaa.lottery.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpUtil {
    
    private static final int CONNECTION_TIMEOUT = 1000;
    private static final int SOCKET_TIME = 1000;
    
    private static HttpPost getPostMethod(String url, String data,
            Map<String, String> header) {
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(SOCKET_TIME)
                .setConnectTimeout(CONNECTION_TIMEOUT).build();
        if(header != null) {
            Iterator<String> it = header.keySet().iterator();
            while (it.hasNext()) {
                String itString = it.next();
                httpPost.addHeader(itString, header.get(itString));
            }
        }
        StringEntity se = new StringEntity(data, "utf-8");
        httpPost.setConfig(requestConfig);
        httpPost.setEntity(se);
        return httpPost;
    }
    
    public static String request(String url, String data,
            Map<String, String> headers) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String result = null;
        CloseableHttpResponse response = null;
        InputStream input = null;
        HttpPost postMethod = getPostMethod(url, data, headers);
        try {
            response = httpclient.execute(postMethod);
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity);
                return result;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                if (response != null) {
                    response.close();
                }
                postMethod.abort();
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
}
