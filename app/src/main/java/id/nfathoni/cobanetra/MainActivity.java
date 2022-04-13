package id.nfathoni.cobanetra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Objects;

import id.nfathoni.cobanetra.databinding.ActivityMainBinding;
import id.nfathoni.cobanetra.service.SearchTweetsListener;
import id.nfathoni.cobanetra.service.TwitterApi;

public class MainActivity extends AppCompatActivity implements SearchTweetsListener {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TwitterApi usage = new TwitterApi();
        usage.setSearchTweetListener(this);

        binding.tvResult.setText("");
        binding.btSearch.setOnClickListener(view -> {
            String query = Objects.requireNonNull(binding.etSearch.getText()).toString();
            usage.searchTweets(query);
        });

        binding.btToHome.setOnClickListener(view ->
                startActivity(new Intent(this, HomeActivity.class)));
    }

    @Override
    public void onSuccess(int statusCode, String searchResult) {
        binding.tvResult.setText(searchResult);
    }

    @Override
    public void onFailure(int statusCode, String message) {
        binding.tvResult.setText(message);
    }
}