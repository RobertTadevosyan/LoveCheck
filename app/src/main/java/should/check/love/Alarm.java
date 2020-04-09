package should.check.love;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.HashMap;

import should.check.love.main.ui.MainActivity;

public class Alarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification();
    }

    public void setAlarm(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        if (am != null) {
            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 1000 * 60 * 60 * 4, pi); // Millisec * Second * Minute
        }
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(sender);
        }
    }


    private void showNotification() {
        Intent intent = new Intent(LoveApp.Companion.getInstance(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(LoveApp.Companion.getInstance(), 0, intent, 0);
        HashMap<String, String> map = PrefsUtils.INSTANCE.getLastScore();
        String f_n = map.get(PrefsUtils.F_N);
        if (f_n != null && !f_n.isEmpty()) {
            createNotificationChannel();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(LoveApp.Companion.getInstance(), "LoveCheckChannel")
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(f_n)
                    .setContentText(map.get(PrefsUtils.S_N))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(BitmapFactory.decodeResource(LoveApp.Companion.getInstance().getResources(), R.drawable.ic_notification)))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setSmallIcon(R.drawable.ic_notification_transparent);
                builder.setColor(LoveApp.Companion.getInstance().getResources().getColor(R.color.colorAccent));
            } else {
                builder.setSmallIcon(R.drawable.ic_notification);
            }
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(LoveApp.Companion.getInstance());
            notificationManager.notify(1001, builder.build());
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = LoveApp.Companion.getInstance().getString(R.string.app_name);
            String description = LoveApp.Companion.getInstance().getString(R.string.invite_friends);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("LoveCheckChannel", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = LoveApp.Companion.getInstance().getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
