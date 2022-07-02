package id.nfathoni.cobanetra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import id.nfathoni.cobanetra.databinding.ActivityNotifBinding;

public class NotifActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "channel_coba";
    private ActivityNotifBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotifBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Notification");
        }

        createNotificationChannel();

        Intent intent = new Intent(this, this.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Intent snoozeIntent = new Intent(this, this.getClass());
        snoozeIntent.setAction("Action Snooze");
        snoozeIntent.putExtra("extra_notification_id", 0);
        PendingIntent snoozePendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_mood_24)
                .setContentTitle("Coba Notifikasi")
                .setContentText("Mencoba teks di notifikasi ini. Menambahkan teks yang lebih panjang untuk melebihi satu baris.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_round_signal_cellular_alt_24, "Snooze", snoozePendingIntent)
                .setAutoCancel(true);

        binding.btNotifShow.setOnClickListener(view -> {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            int notificationId = 0;
            notificationManager.notify(notificationId, builder.build());
        });
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Coba Channel";
            String description = "Deskripsi coba channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system ; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}