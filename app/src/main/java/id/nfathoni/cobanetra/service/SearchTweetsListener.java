package id.nfathoni.cobanetra.service;

public interface SearchTweetsListener {
    void onSuccess(int statusCode, String searchResult);
    void onFailure(int statusCode, String message);
}
