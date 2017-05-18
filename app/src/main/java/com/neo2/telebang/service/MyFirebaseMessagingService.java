package com.neo2.telebang.service;


import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.neo2.telebang.activity.MainActivity;
import com.neo2.telebang.app.AppConstant;
import com.neo2.telebang.app.GlobalApplication;
import com.neo2.telebang.helper.DialogUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Belal on 03/11/16.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = "Yo365";
        String message = "New notification";

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            title = remoteMessage.getNotification().getTitle();
            message = remoteMessage.getNotification().getBody();
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            if (remoteMessage.getData().containsKey(AppConstant.KEY_NOTIFICATION_TITLE)) {
                title = remoteMessage.getData().get(AppConstant.KEY_NOTIFICATION_TITLE);
            }

            if (remoteMessage.getData().containsKey(AppConstant.KEY_NOTIFICATION_MESSAGE)) {
                message = remoteMessage.getData().get(AppConstant.KEY_NOTIFICATION_MESSAGE);
            }

            if (remoteMessage.getData().containsKey(AppConstant.KEY_NOTIFICATION_CONTENT_ID)) {
                String contentID = remoteMessage.getData().get(AppConstant.KEY_NOTIFICATION_CONTENT_ID);
                String contentType = remoteMessage.getData().get(AppConstant.KEY_NOTIFICATION_CONTENT_TYPE);
                String imageUrl = remoteMessage.getData().get(AppConstant.KEY_NOTIFICATION_IMAGE);

                sendPushNotification(title, message, contentType, contentID, imageUrl);
            }
        }
    }

    //this method will display the notification
    //We are passing the JSONObject that is received from
    //firebase cloud messaging
    private void sendPushNotification(String title, String message, String type, String id, String imageUrl) {
        //creating MyNotificationManager object
        MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());

        //creating an intent for the notification
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(AppConstant.KEY_NOTIFICATION_CONTENT_ID, id);
        intent.putExtra(AppConstant.KEY_NOTIFICATION_CONTENT_TYPE, type);

        message = String.format("%s - %s - %s", message, type, id);
        //if there is no image
        if (imageUrl == null || imageUrl.equals("null") || imageUrl.isEmpty()) {
            //displaying small notification
            mNotificationManager.showSmallNotification(title, message, intent);
        } else {
            //if there is an image
            //displaying a big notification
            mNotificationManager.showBigNotification(title, message, imageUrl, intent);
        }
    }

}