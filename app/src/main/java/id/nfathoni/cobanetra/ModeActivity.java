package id.nfathoni.cobanetra;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import id.nfathoni.cobanetra.databinding.ActivityModeBinding;
import id.nfathoni.cobanetra.util.PrefUtil;

public class ModeActivity extends AppCompatActivity {

    private static final String NETRAHUB_PREFIX = "\"nx";

    private ActivityModeBinding binding;
    private WifiManager mWifiManager;
    private boolean mIsSatMode = false;

    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                if (result.containsValue(false)) {
                    showRationaleDialog();
                } else {
                    checkNetrahubSsid(getWifiSsid());
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Ubah Mode Internet");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        mIsSatMode = PrefUtil.getBoolean(
                this, getString(R.string.key_mode), false);

        if (mIsSatMode) {
            binding.rgMode.check(R.id.rb_mode_sat);
        } else {
            binding.rgMode.check(R.id.rb_mode_cell);
        }

        setModeInfo(mIsSatMode);

        binding.rgMode.setOnCheckedChangeListener((radioGroup, id) -> {
            mIsSatMode = id == R.id.rb_mode_sat;
            setModeInfo(mIsSatMode);
        });

        binding.tvWifiOpen.setOnClickListener(view ->
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)));

        binding.btModeApply.setOnClickListener(view -> {
            if (mIsSatMode) {
                boolean isCoarseLocationGranted = ContextCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED;
                boolean isFineLocationGranted = ContextCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED;

                if (isCoarseLocationGranted && isFineLocationGranted) {
                    checkNetrahubSsid(getWifiSsid());
                } else if (shouldShowRequestPermissionRationale(
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        || shouldShowRequestPermissionRationale(
                                Manifest.permission.ACCESS_FINE_LOCATION)) {
                    showRationaleDialog();
                } else {
                    requestPermissionLauncher.launch(new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION});
                }
            } else {
                PrefUtil.setBoolean(this, getString(R.string.key_mode), false);
                finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void setModeInfo(boolean isSatMode) {
        if (isSatMode) {
            binding.tvModeInfo.setText(R.string.info_mode_sat);
            binding.clModeStep.setVisibility(View.VISIBLE);
        } else {
            binding.tvModeInfo.setText(R.string.info_mode_cell);
            binding.clModeStep.setVisibility(View.INVISIBLE);
        }
    }

    private String getWifiSsid() {
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();

        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            return wifiInfo.getSSID();
        }

        return "";
    }

    private void showRationaleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Izin Akses")
                .setMessage("FishOn membutuhkan akses lokasi agar dapat membaca keadaan WiFi")
                .setPositiveButton("Izinkan", (dialog, id) ->
                        requestPermissionLauncher.launch(new String[]{
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION}))
                .setNegativeButton("Tolak", (dialog, id) -> { });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void checkNetrahubSsid(String wifiSsid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (wifiSsid.startsWith(NETRAHUB_PREFIX)) {
            PrefUtil.setBoolean(this, getString(R.string.key_mode), true);

            builder.setTitle("Berhasil")
                    .setMessage("Berhasil tersambung ke WiFi " + wifiSsid)
                    .setPositiveButton("Tutup", (dialog, id) -> finish());

        } else {
            PrefUtil.setBoolean(this, getString(R.string.key_mode), false);

            builder.setTitle("Gagal")
                    .setMessage("Gagal tersambung ke WiFi NetraHub. Periksa sambungan WiFi Anda.")
                    .setPositiveButton("Tutup", null);
        }

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}