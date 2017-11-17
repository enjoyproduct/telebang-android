package com.neo2.telebang.service;

import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.inspius.coreapp.helper.InspiusSharedPrefUtils;
import com.neo2.telebang.api.RPC;
import com.neo2.telebang.app.AppConstant;

/**
 * Created by Billy on 11/14/16.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        //saving the token on shared preferences
        InspiusSharedPrefUtils.saveToPrefs(getApplicationContext(), AppConstant.KEY_DEVICE_TOKEN, token);

        // Add custom implementation, as needed.
//        if (!TextUtils.isEmpty(token))
//            RPC.registerDeviceNotification(token, null);
    }
}
