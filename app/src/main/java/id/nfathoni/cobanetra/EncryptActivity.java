package id.nfathoni.cobanetra;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import id.nfathoni.cobanetra.databinding.ActivityEncryptBinding;
import id.nfathoni.cobanetra.util.AesUtil;
import id.nfathoni.cobanetra.util.HexUtil;

public class EncryptActivity extends AppCompatActivity {

    private static final String TAG = "EncryptActivity";
    private ActivityEncryptBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEncryptBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Encrypt");
        }

        binding.btEncProcess.setOnClickListener(view -> process());
    }

    private void process() {
        if (binding.etEncOriginal.getText() != null && binding.etEncSecret.getText() != null) {
            String original = binding.etEncOriginal.getText().toString();
            String secret = binding.etEncSecret.getText().toString();

//            String ivB64 = AesUtil.generateIvB64();
//            binding.tvEncIv.setText(ivB64);

            String encrypted = AesUtil.ecbEncrypt(original, secret);

            if (encrypted != null) {
                binding.tvEncEncrypted.setText(encrypted);
                Log.d(TAG, "Encrypted: " + encrypted);

                String hexed = HexUtil.stringToHex(encrypted);
                binding.tvEncHexed.setText(hexed);
                Log.d(TAG, "Hexed: " + hexed);

                String stringed = HexUtil.hexToString(hexed);
                binding.tvEncStringed.setText(stringed);
                Log.d(TAG, "Stringed: " + stringed);

                String decrypted = AesUtil.ecbDecrypt(stringed, secret);
                binding.tvEncDecrypted.setText(decrypted);
                Log.d(TAG, "Decrypted: " + decrypted);
            }
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}