package id.nfathoni.cobanetra.service;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class NetraClient {

    private static final String BASE_URL = "http://192.168.4.1";
    private static final AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler handler) {
        client.setProxy(BASE_URL, 80);
        client.get(getAbsoluteUrl(url), params, handler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler handler) {
        client.setProxy(BASE_URL, 80);
//        client.setTimeout(10000);
        client.post(getAbsoluteUrl(url), params, handler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
