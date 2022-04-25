package id.nfathoni.cobanetra.service;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import id.nfathoni.cobanetra.model.UpMessage;

public class NetraApi {

    private static final String TAG = "NetraApi";
    private SendUpListener sendUpListener;
    private GetPendingUpListener getPendingUpListener;
    private GetDownListener getDownListener;

    public NetraApi() { }

    public void setSendUpListener(SendUpListener listener) {
        sendUpListener = listener;
    }

    public void setGetPendingUpListener(GetPendingUpListener listener) {
        getPendingUpListener = listener;
    }

    public void setGetDownListener(GetDownListener listener) {
        getDownListener = listener;
    }

    public void sendUp(Context context, String payload) {
        RequestParams params = new RequestParams();
        params.put("payload", payload);
        params.put("dataType", 1);

        StringEntity stringEntity = null;

        try {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("payload", payload);
            jsonParams.put("dataType", 1);
            stringEntity = new StringEntity(jsonParams.toString());
            stringEntity.setContentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
        }

        NetraClient.post(context, "/v1/outbox", stringEntity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    String message = response.getString("message");
                    sendUpListener.onSendUpSuccess(statusCode, message);
                } catch (JSONException e) {
                    Log.e(TAG, e.toString());
                    sendUpListener.onSendUpFailure(statusCode, e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e(TAG, "Status: " + statusCode);

                if (errorResponse != null) {
                    sendUpListener.onSendUpFailure(statusCode, errorResponse.toString());
                } else {
                    sendUpListener.onSendUpFailure(statusCode, throwable.getMessage());
                }
            }
        });
    }

    public void getPendingUp() {
        NetraClient.get("/v1/outbox", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                List<UpMessage> upMessages = new ArrayList<>();

                try {
                    for (int i = 0; i <= response.length(); i++) {
                        JSONObject jsonMessage = response.getJSONObject(i);
                        String id = jsonMessage.getString("id");
                        String message = jsonMessage.getString("message");
                        int status = jsonMessage.getInt("status");
                        upMessages.add(new UpMessage(id, message, status));

                        getPendingUpListener.onGetPendingUpSuccess(statusCode, upMessages);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.toString());
                    getPendingUpListener.onGetPendingUpFailure(statusCode, e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                if (errorResponse != null) {
                    getPendingUpListener.onGetPendingUpFailure(statusCode, errorResponse.toString());
                } else {
                    getPendingUpListener.onGetPendingUpFailure(statusCode, throwable.getMessage());
                }
            }
        });
    }

    public void getDown() {
        NetraClient.get("/down", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                List<String> downMessages = new ArrayList<>();

                try {
                    for (int i = 0; i <+ response.length(); i++) {
                        JSONObject jsonMessage = response.getJSONObject(i);
                        String message = jsonMessage.getString("message");
                        downMessages.add(message);

                        getDownListener.onGetDownSuccess(statusCode, downMessages);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.toString());
                    getDownListener.onGetDownFailure(statusCode, e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                getDownListener.onGetDownFailure(statusCode, responseString);
            }
        });
    }
}
