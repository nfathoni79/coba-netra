package id.nfathoni.cobanetra.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.nfathoni.cobanetra.databinding.ItemProductBinding;
import id.nfathoni.cobanetra.model.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> products;

    public ProductAdapter() {
        this.products = new ArrayList<>();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setProducts(List<Product> products) {
        this.products = products;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getBinding().tvProductName.setText(products.get(position).getName());
        holder.getBinding().tvProductDetail.setText(products.get(position).getDetail());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductBinding binding;

        public ViewHolder(ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemProductBinding getBinding() {
            return binding;
        }
    }
}
