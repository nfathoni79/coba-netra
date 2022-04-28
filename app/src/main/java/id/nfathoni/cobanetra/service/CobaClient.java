package id.nfathoni.cobanetra.service;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class CobaClient {

    private static final String BASE_URL = "http://192.168.1.11:8000";
    private static final AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url , RequestParams params, AsyncHttpResponseHandler handler) {
        client.get(getAbsoluteUrl(url), params, handler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
