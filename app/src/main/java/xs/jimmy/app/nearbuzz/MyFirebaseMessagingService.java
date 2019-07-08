package xs.jimmy.app.nearbuzz;

import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private LocalBroadcastManager broadcaster;

    @Override
    public void onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e(TAG,remoteMessage.getData().toString());
        Intent intent = new Intent("MyData");
        if (remoteMessage.getNotification() != null) {
            intent.putExtra("title", remoteMessage.getNotification().getTitle());
            intent.putExtra("body", remoteMessage.getNotification().getBody());
        }
        broadcaster.sendBroadcast(intent);
    }
}
