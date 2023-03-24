package snow.app.padelbook.fcm;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import snow.app.padelbook.R;
import snow.app.padelbook.ui.NotificationActivity;


/**
 * Created by Snow-Dell-05 on 05-Feb-18.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    String chat_id = "";
    String name = "";
    String type = "";
    String body = "";
    int notificationcount = 0;
    int msgcount = 0;


    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param // messageBody FCM message body received.
     */

    Intent intent;
    private NotificationUtils notificationUtils;

    // [END receive_message]

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
   /* private void scheduleJob() {
        // [START dispatch_job]
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("my-job-tag")
                .build();
        dispatcher.schedule(myJob);
        // [END dispatch_job]
    }*/

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e(TAG, "DATA: " + remoteMessage);
        //  StaticValues.mNotificationBlinking = true;
        Map<String, String> mMap = remoteMessage.getData();

        //  Check if message contains a notification payload.
     /*   if (remoteMessage.getNotification() != null) {
            Log.wtf(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification("Hello working");
        }*/

        //   Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData().toString());
            //    handleNow(remoteMessage.getData());
            type = remoteMessage.getData().get("type");
            name = remoteMessage.getData().get("title");
            body = remoteMessage.getData().get("body");
            Log.e("type---", "--" + type);
            Log.e("title---", "--" + name);
            //   try {

            //startTmer();
            // handleNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"));
            //  handleNow(mMap);


            // testNiti("test");
//            } catch (Exception e) {
//                Log.e(TAG, "Exception: " + e.getMessage());
//            }

/*
            if (type.equals("1")) {
                startTmer(R.raw.noti_ring);
            }


            if (type.equals("5")) {
                startTmer(R.raw.broadcast_ring);
            }*/
        }
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.


        super.onMessageReceived(remoteMessage);


        Intent intent = new Intent(this, NotificationActivity.class);
        intent.putExtra("noti", "fromnoti");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
              pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        }else {
              pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        }

        String channelId = "Default";
       // final MediaPlayer mpsound = MediaPlayer.create(this, R.raw.noti_ring);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] pattern = {500, 500, 500, 500, 500, 500};
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
        builder.setSmallIcon(R.drawable.logo_new);

//        if (!type.equals("1") || !type.equals("5")) {
//            builder.setSound(alarmSound, AudioManager.STREAM_ALARM);
//        }
        // .setSound( Uri.parse("android.resource://"+ getPackageName()+"/"+R.raw.noti_ring))
        // .setVibrate(pattern)
        builder.setContentTitle(remoteMessage.getData().get("title"));
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getData().get("message")));
        builder.setContentText(remoteMessage.getData().get("message")).setAutoCancel(true).setContentIntent(pendingIntent);
        try {
            if(remoteMessage.getData().containsKey("largeIcon")) {
                URL url = new URL(remoteMessage.getData().get("largeIcon"));
                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                builder.setLargeIcon(getCircleBitmap(image));
            }
        } catch(IOException e) {
            System.out.println(e);
        }


        //  builder.setSound(Uri.parse("android.resource://"+ getPackageName()+"/"+R.raw.noti_ring));//Here is FILE_NAME is the name of file that you want to play


        // Vibrate if vibrate is enabled
        // notification.defaults |= Notification.DEFAULT_VIBRATE;

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_HIGH);

           /* if (!type.equals("1") || !type.equals("5")) {
                channel.setSound(alarmSound, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.noti_ring);
            }*/

            //channel.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.noti_ring), attributes); // This is IMPORTANT
            manager.createNotificationChannel(channel);

        }
/*
       mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener()          {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mp.start();
            }
        });*/

        manager.notify(0, builder.build());



        /*MediaPlayer mp= MediaPlayer.create(this, R.raw.noti_ring);
        mp.start();*/

        //  sendNotification( mMap );
    }

    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
       // String refreshedToken = FirebaseInstanceId.getInstance().getToken();
      //  Log.wtf(TAG, "Refreshed token: " + refreshedToken);
        Log.d("refresh", s);
        //  Saving reg id to shared preferences
        //    If you want to send messages to this application instance or
        //    manage this apps subscriptions on the server side, send the
        //    Instance ID token to your app server.


        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
      //  registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }




}
