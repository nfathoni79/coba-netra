package id.nfathoni.cobanetra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import id.nfathoni.cobanetra.adapter.ProductAdapter;
import id.nfathoni.cobanetra.databinding.ActivityCobaBinding;
import id.nfathoni.cobanetra.model.Product;
import id.nfathoni.cobanetra.service.CobaApi;

public class CobaActivity extends AppCompatActivity implements CobaApi.GetProductsListener {

    private static final String TAG = "CobaActivity";
    private ActivityCobaBinding binding;
    private final CobaApi cobaApi = new CobaApi();
    private ProductAdapter mProductAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCobaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Coba");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mProductAdapter = new ProductAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        binding.rvCoba.setLayoutManager(manager);
        binding.rvCoba.setAdapter(mProductAdapter);

        cobaApi.setGetProductsListener(this);
        getProducts();

        binding.srlCoba.setOnRefreshListener(() -> {
            Log.i(TAG, "Refresh from swipe gesture");
            getProducts();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_coba, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh) {
            Log.i(TAG, "Refresh from menu item");
            getProducts();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onGetProductsSuccess(int statusCode, List<Product> products) {
        binding.srlCoba.setRefreshing(false);
        mProductAdapter.setProducts(products);
    }

    @Override
    public void onGetProductsFailure(int statusCode, String message) {
        binding.srlCoba.setRefreshing(false);
        showErrorDialog(message);
    }

    private void showErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Gagal")
                .setMessage(message)
                .setPositiveButton("Tutup", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getProducts() {
        binding.srlCoba.setRefreshing(true);
        cobaApi.getProducts();
    }
}