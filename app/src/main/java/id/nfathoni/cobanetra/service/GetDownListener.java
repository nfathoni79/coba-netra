package id.nfathoni.cobanetra.service;

import java.util.List;

public interface GetDownListener {
    void onGetDownSuccess(int statusCode, List<String> messages);
    void onGetDownFailure(int statusCode, String message);
}
