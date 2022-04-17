package id.nfathoni.cobanetra.service;

public interface SendUpListener {
    void onSendUpSuccess(int statusCode, String payload);
    void onSendUpFailure(int statusCode, String message);
}
