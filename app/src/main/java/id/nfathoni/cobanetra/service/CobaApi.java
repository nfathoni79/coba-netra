package id.nfathoni.cobanetra.service;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import id.nfathoni.cobanetra.model.Product;

public class CobaApi {

    private static final String TAG = "CobaApi";
    private GetProductsListener getProductsListener;

    public CobaApi() { }

    public void setGetProductsListener(GetProductsListener listener) {
        getProductsListener = listener;
    }

    public void getProducts() {
        CobaClient.get("/api/products-no-auth", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    JSONArray jsonProducts = response.getJSONArray("data");
                    List<Product> products = new ArrayList<>();

                    for (int i = 0; i < jsonProducts.length(); i++) {
                        JSONObject jsonProduct = jsonProducts.getJSONObject(i);
                        products.add(new Product(
                                jsonProduct.getInt("id"),
                                jsonProduct.getString("name"),
                                jsonProduct.getString("detail")));
                    }

                    getProductsListener.onGetProductsSuccess(statusCode, products);
                } catch (JSONException e) {
                    Log.e(TAG, "JSONException: " + e);
                    getProductsListener.onGetProductsFailure(statusCode, e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                if (errorResponse != null) {
                    Log.e(TAG, "Error Response: " + errorResponse);
                    getProductsListener.onGetProductsFailure(statusCode, errorResponse.toString());
                } else {
                    Log.e(TAG, "Throwable: " + throwable.getMessage());
                    getProductsListener.onGetProductsFailure(statusCode, throwable.getMessage());
                }
            }
        });
    }

    public interface GetProductsListener {
        void onGetProductsSuccess(int statusCode, List<Product> products);
        void onGetProductsFailure(int statusCode, String message);
    }
}
