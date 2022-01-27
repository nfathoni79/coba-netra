package id.nfathoni.cobanetra.service;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TwitterApi {
    private static final String TAG = "TwitterApi";
    private SearchTweetsListener searchTweetsListener;

    public void setSearchTweetListener(SearchTweetsListener listener) {
        this.searchTweetsListener = listener;
    }

    public void searchTweets(String query) {
        RequestParams params = new RequestParams();
        params.put("query", query);

        TwitterClient.get("/2/tweets/search/recent", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    JSONObject meta = response.getJSONObject("meta");
                    int resultCount = meta.getInt("result_count");

                    if (resultCount >= 1) {
                        JSONArray data = response.getJSONArray("data");
                        JSONObject firstResult = data.getJSONObject(0);
                        String firstText = firstResult.getString("text");
                        Log.i(TAG, firstText);
                        searchTweetsListener.onSuccess(statusCode, firstText);
                    } else {
                        searchTweetsListener.onFailure(statusCode, "No result");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.toString());
                    searchTweetsListener.onFailure(statusCode, "Unable to parse JSON");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e(TAG, errorResponse.toString());
                searchTweetsListener.onFailure(statusCode, "Something wrong");
            }
        });
    }
}
