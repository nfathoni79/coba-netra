package id.nfathoni.cobanetra;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import id.nfathoni.cobanetra.databinding.ActivityLogbookBinding;
import id.nfathoni.cobanetra.model.Logbook;
import id.nfathoni.cobanetra.service.NetraApi;
import id.nfathoni.cobanetra.service.SendUpListener;
import id.nfathoni.cobanetra.util.AesUtil;
import id.nfathoni.cobanetra.util.HexUtil;

public class LogbookActivity extends AppCompatActivity implements SendUpListener {

    private static final String TAG = "LogbookActivity";
    private ActivityLogbookBinding binding;
    private final NetraApi netraApi = new NetraApi();

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogbookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Tambah Logbook");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        netraApi.setSendUpListener(this);

        mProgressDialog = createProgressDialog();

        Logbook logbook = new Logbook();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String dateString = formatter.format(logbook.getCreatedAt());
        binding.tvLogDate.setText(dateString);

        binding.tvLogLat.setText(String.format(Locale.getDefault(), "%.6f", logbook.getLat()));
        binding.tvLogLon.setText(String.format(Locale.getDefault(), "%.6f", logbook.getLon()));
        binding.rgLogHasfish.check(R.id.rb_log_hasfish_no);

        binding.btLogSubmit.setOnClickListener(view -> {
            showProgressDialog();
            String delimitedString = logbook.getDelimitedString();
            String encrypted = AesUtil.ecbEncrypt(delimitedString, "kunci1234");
            Log.d(TAG, "Encrypted: " + encrypted);

            if (encrypted != null) {
                String hexed = HexUtil.stringToHex(encrypted);
                Log.d(TAG, "Hexed: " + hexed);
                netraApi.sendUp(Long.toString(logbook.getEpochCreatedAt()), hexed);
            } else {
                dismissProgressDialog();
                showErrorDialog("Something wrong in encryption");
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onSendUpSuccess(int statusCode, String payload) {
        dismissProgressDialog();
        showSuccessDialog(payload);
    }

    @Override
    public void onSendUpFailure(int statusCode, String message) {
        dismissProgressDialog();
        showErrorDialog(message);
    }

    private ProgressDialog createProgressDialog() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Mohon ditunggu");
        dialog.setCancelable(false);

        return dialog;
    }

    private void showProgressDialog() {
        mProgressDialog.show();
    }

    private void dismissProgressDialog() {
        mProgressDialog.dismiss();
    }

    private void showErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Gagal")
                .setMessage(message)
                .setPositiveButton("Tutup", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showSuccessDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Berhasil")
                .setMessage(message)
                .setPositiveButton("Tutup", (dialog, id) -> finish());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}