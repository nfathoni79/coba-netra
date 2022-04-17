package id.nfathoni.cobanetra;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import id.nfathoni.cobanetra.databinding.ActivityPendingBinding;
import id.nfathoni.cobanetra.model.UpMessage;
import id.nfathoni.cobanetra.service.GetPendingUpListener;
import id.nfathoni.cobanetra.service.NetraApi;

public class PendingActivity extends AppCompatActivity implements GetPendingUpListener {

    private ActivityPendingBinding binding;
    private final NetraApi netraApi = new NetraApi();
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPendingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Pending Messages");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mProgressDialog = createProgressDialog();

        netraApi.setGetPendingUpListener(this);
        showProgressDialog();
        netraApi.getPendingUp();

        List<UpMessage> upMessages = new ArrayList<>();
        upMessages.add(new UpMessage("abc", "cobamessageabc", 0));
        upMessages.add(new UpMessage("def", "cobamessagedef", 1));
        upMessages.add(new UpMessage("ghi", "cobamessageghi", 2));

        binding.tvPendList.setText(getMessagesString(upMessages));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onGetPendingUpSuccess(int statusCode, List<UpMessage> upMessages) {
        dismissProgressDialog();
        binding.tvPendList.setText(getMessagesString(upMessages));
    }

    @Override
    public void onGetPendingUpFailure(int statusCode, String message) {
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

    private String getMessagesString(List<UpMessage> upMessages) {
        StringBuilder builder = new StringBuilder();

        for (UpMessage message : upMessages) {
            builder.append("ID: ").append(message.getId()).append("\n");
            builder.append("Message: ").append(message.getMessage()).append("\n");
            builder.append("Status: ").append(message.getStatus()).append("\n\n");
        }

        return builder.toString();
    }
}