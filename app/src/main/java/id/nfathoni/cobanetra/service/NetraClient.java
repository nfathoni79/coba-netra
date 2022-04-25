package id.nfathoni.cobanetra.service;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.HttpEntity;

public class NetraClient {

    private static final String BASE_URL = "http://10.10.10.1";
    private static final AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler handler) {
//        client.setProxy(BASE_URL, 80);
        client.get(getAbsoluteUrl(url), params, handler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler handler) {
//        client.setProxy(BASE_URL, 80);
//        client.setTimeout(10000);
//        client.setURLEncodingEnabled(true);
        client.post(getAbsoluteUrl(url), params, handler);
    }

    public static void post(Context context, String url, HttpEntity entity, String contentType, AsyncHttpResponseHandler handler) {
//        client.setTimeout(30000);
        client.post(context, getAbsoluteUrl(url), entity, contentType, handler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
