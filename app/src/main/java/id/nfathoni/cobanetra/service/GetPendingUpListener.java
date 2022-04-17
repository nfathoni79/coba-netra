package id.nfathoni.cobanetra.service;

import java.util.List;

import id.nfathoni.cobanetra.model.UpMessage;

public interface GetPendingUpListener {
    void onGetPendingUpSuccess(int statusCode, List<UpMessage> upMessages);
    void onGetPendingUpFailure(int statusCode, String message);
}
