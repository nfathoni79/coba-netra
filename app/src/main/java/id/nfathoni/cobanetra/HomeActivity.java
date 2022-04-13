package id.nfathoni.cobanetra;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import id.nfathoni.cobanetra.databinding.ActivityHomeBinding;
import id.nfathoni.cobanetra.util.PrefUtil;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Home");
        }

        setModeView();

        binding.tvModeChange.setOnClickListener(view ->
                startActivity(new Intent(this, ModeActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setModeView();
    }

    private void setModeView() {
        boolean isSatMode = PrefUtil.getBoolean(
                this, getString(R.string.key_mode), false);

        if (isSatMode) {
            binding.ivModeIcon.setImageResource(R.drawable.ic_round_satellite_alt_24);
            binding.tvModeName.setText(R.string.label_mode_sat);
        } else {
            binding.ivModeIcon.setImageResource(R.drawable.ic_round_signal_cellular_alt_24);
            binding.tvModeName.setText(R.string.label_mode_cell);
        }
    }
}