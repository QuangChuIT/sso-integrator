package com.bsc.sso.authentication.http;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;

public class CloseableCallback implements FutureCallback<HttpResponse> {

    private CloseableHttpAsyncClient closeableHttpAsyncClient;

    public CloseableCallback(CloseableHttpAsyncClient closeableHttpAsyncClient) {
        this.closeableHttpAsyncClient = closeableHttpAsyncClient;
    }

    @Override
    public void completed(HttpResponse httpResponse) {
        close();
    }

    @Override
    public void failed(Exception e) {
        close();
    }

    @Override
    public void cancelled() {
        close();
    }

    private void close() {
        try {
            closeableHttpAsyncClient.close();
        } catch (Exception e) {
            _log.error(e);
        }
    }

    private Log _log = LogFactory.getLog(CloseableCallback.class);
}
