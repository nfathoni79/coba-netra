package id.nfathoni.cobanetra.service;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class TwitterClient {
    private static final String BASE_URL = "https://api.twitter.com";
    private static final String BEARER_TOKEN = "AAAAAAAAAAAAAAAAAAAAAJ49YgEAAAAA49uNj9EMrTV1e5bBWf0CBUUFtRU%3D8hPz4nWN4TPoDG7WY4TDB5UXmdUbSjSIa0zsHUZ0dnbYrJebrw";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("Authorization", "Bearer " + BEARER_TOKEN);
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
