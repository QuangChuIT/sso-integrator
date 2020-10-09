package com.bsc.sso.authentication.http;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SendRequest {

    public HttpResponse sendRequest(String message, String endpoint) throws GeneralSecurityException,
            ExecutionException, InterruptedException {
        return sendAsync(endpoint, message).get();
    }

    public HttpResponse sendRequest(String message, String endpoint, String token) throws GeneralSecurityException,
            ExecutionException, InterruptedException {
        return sendAsync(endpoint, message, token).get();
    }

    public HttpResponse getRequest(String endpoint, String token) throws GeneralSecurityException {
        return getAsync(endpoint, token);
    }

    public HttpResponse getRequest(String endpoint) throws GeneralSecurityException {
        return getAsync(endpoint);
    }

    /**
     * Gửi bất đồng bộ tin push sử dụng CloseableHttpAsyncClient
     *
     * @param message
     * @return
     * @throws GeneralSecurityException
     */
    private Future<HttpResponse> sendAsync(String endpoint, String message) throws GeneralSecurityException {
        try {
            HttpPost httpPost = preparePost(endpoint, message);
            CloseableHttpAsyncClient closeableHttpAsyncClient = HttpAsyncClients.createSystem();
            closeableHttpAsyncClient.start();

            return closeableHttpAsyncClient.execute(httpPost, new CloseableCallback(closeableHttpAsyncClient));
        } catch (Exception e) {
            log.error("send push async exception = " + e);
            throw new GeneralSecurityException(e);
        }
    }

    /**
     * Gửi bất đồng bộ tin push sử dụng CloseableHttpAsyncClient
     *
     * @param message
     * @return
     * @throws GeneralSecurityException
     */
    private Future<HttpResponse> sendAsync(String endpoint, String message, String token) throws GeneralSecurityException {
        try {
            HttpPost httpPost = preparePost(endpoint, message);
            httpPost.addHeader(new BasicHeader("Token", token));
            CloseableHttpAsyncClient closeableHttpAsyncClient = HttpAsyncClients.createSystem();
            closeableHttpAsyncClient.start();

            return closeableHttpAsyncClient.execute(httpPost, new CloseableCallback(closeableHttpAsyncClient));
        } catch (Exception e) {
            log.error("send push async exception = " + e);
            throw new GeneralSecurityException(e);
        }
    }

    private HttpResponse getAsync(String endpoint, String token) throws GeneralSecurityException {
        try {
            HttpGet httpGet = prepareGet(endpoint);
            httpGet.setHeader("Token", token);
            CloseableHttpClient httpClient = null;
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                    builder.build());
            httpClient = HttpClients.custom().setSSLSocketFactory(
                    sslConnectionSocketFactory).build();
            return httpClient.execute(httpGet);
        } catch (Exception e) {
            log.error("Error get async exception =" + e);
            throw new GeneralSecurityException(e);
        }
    }

    private HttpResponse getAsync(String endpoint) throws GeneralSecurityException {
        try {
            HttpGet httpGet = prepareGet(endpoint);
            CloseableHttpClient httpClient = HttpClients.createSystem();
            return httpClient.execute(httpGet);
        } catch (Exception e) {
            log.error("Error when get async exception " + e);
            throw new GeneralSecurityException(e);
        }
    }

    /**
     * Tạo http post với header và data push gửi đi
     *
     * @param message
     * @return
     */
    private HttpPost preparePost(String endpoint, String message) throws Exception {

        try {
            HttpPost httpPost = new HttpPost(endpoint);
            httpPost.addHeader(new BasicHeader("Content-Type", "application/json"));

            httpPost.setEntity(new StringEntity(message));
            return httpPost;
        } catch (Exception e) {
            throw new Exception(e);
        }

    }

    private HttpGet prepareGet(String endpoint) throws Exception {
        try {
            return new HttpGet(endpoint);
        } catch (Exception e) {
            throw new Exception("Error when call get endpoint" + endpoint + "in Hue Authenticator");
        }
    }

    private static final Log log = LogFactory.getLog(SendRequest.class);

    public static void main(String[] args) throws InterruptedException, ExecutionException, GeneralSecurityException, IOException {
        LoginUrlRequest loginUrlRequest = new LoginUrlRequest();
        loginUrlRequest.setSysUserName("IOC");
        loginUrlRequest.setSysPassword("wj9@i76!m2");
        loginUrlRequest.setParam1("79000003");
        loginUrlRequest.setParam2("param2");
        loginUrlRequest.setParam3("param3");
        loginUrlRequest.setReturnUri("http://quanly.hcm.edu.vn");

        String message = loginUrlRequest.toString();
        String endpoint = "https://wapi.hcm.edu.vn/ChuyenTruongMoRong/wapiquanly/loginsso";

        SendRequest sendRequest = new SendRequest();
        HttpResponse response = sendRequest.sendRequest(message, endpoint);
        String responseStr = EntityUtils.toString(response.getEntity());

        JSONObject payload = new JSONObject(responseStr);
        System.out.println(payload);

    }
}
