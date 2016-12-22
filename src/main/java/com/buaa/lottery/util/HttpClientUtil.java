package com.buaa.lottery.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

@SuppressWarnings("deprecation")
public class HttpClientUtil {
    HttpClient httpclient;
    HttpResponse response;
    
    public HttpClientUtil() {
        this.httpclient = new DefaultHttpClient();
        this.httpclient.getParams().setParameter("http.protocol.cookie-policy", "compatibility");
    }
    
    public HttpClientUtil(int connect_timeout, int so_timeout) {
        this.httpclient = new DefaultHttpClient();
        this.httpclient.getParams().setParameter("http.protocol.cookie-policy", "compatibility");
        this.httpclient.getParams().setParameter("http.connection.timeout",
                new Integer(connect_timeout));
        this.httpclient.getParams().setParameter("http.socket.timeout", new Integer(so_timeout));
    }
    
    @SuppressWarnings("rawtypes")
    public String post(String url, String str, Map<String, String> heads) throws Exception {
        this.httpclient.getConnectionManager().closeIdleConnections(30L, TimeUnit.SECONDS);
        HttpPost httppost = new HttpPost(url);
        for (Map.Entry head : heads.entrySet()) {
            httppost.setHeader((String) head.getKey(), (String) head.getValue());
        }
        StringEntity reqEntity = new StringEntity(str);
        httppost.setEntity(reqEntity);
        this.response = this.httpclient.execute(httppost);
        HttpEntity httpEntity = this.response.getEntity();
        String html = null;
        if (httpEntity != null) {
            html = EntityUtils.toString(httpEntity);
            httpEntity.consumeContent();
        }
        return html;
    }
    
    public void close() throws IOException {
        this.httpclient.getConnectionManager().shutdown();
    }
    
    /**
     * 封装http访问头
     * 
     * @return
     */
    private static Map<String, String> getHeads() {
        Map<String, String> heads = new HashMap<String, String>();
        heads.put("User-Agent", "xlive-2.10.30");
        heads.put("v", "2");
        heads.put("deviceId", "KSYUN_WEBSITE");
        return heads;
    }
    
    public static String ks3ApiPost(String url, String postStr) {
        HttpClientUtil client = new HttpClientUtil(10000, 10000);
        
        String retStr = null;
        try {
            retStr = client.post(url, postStr, getHeads());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return retStr;
    }
}