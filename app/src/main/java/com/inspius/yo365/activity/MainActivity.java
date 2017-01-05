package com.inspius.yo365.activity;

import android.content.Intent;
import android.os.Bundle;

import com.inspius.coreapp.helper.InspiusUtils;
import com.inspius.yo365.R;
import com.inspius.yo365.app.AppConstant;
import com.inspius.yo365.base.StdActivity;
import com.inspius.yo365.fragment.NotificationLoadingFragment;
import com.inspius.yo365.fragment.SplashFragment;

import butterknife.ButterKnife;

public class MainActivity extends StdActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_app);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            if (isOpenNotification(getIntent())) {
                parseNotificationContent(getIntent());
            } else
                goToSplashScreen();
        }

        InspiusUtils.printHashKey(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (isOpenNotification(intent)) {
            parseNotificationContent(intent);
        } else
            finish();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.id.container;
    }

    public void parseNotificationContent(Intent intent) {
        AppConstant.NOTIFICATION_TYPE contentType = AppConstant.NOTIFICATION_TYPE.fromString(intent.getExtras().getString(AppConstant.KEY_NOTIFICATION_CONTENT_TYPE));
        String strContentID = intent.getExtras().getString(AppConstant.KEY_NOTIFICATION_CONTENT_ID);
        int contentID = 0;
        try {
            contentID = Integer.parseInt(strContentID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mHostActivity.addFragment(NotificationLoadingFragment.newInstance(contentType, contentID));
    }

    boolean isOpenNotification(Intent intent) {
        if (intent.getExtras() != null) {
            for (String key : intent.getExtras().keySet()) {
                if (key.equals(AppConstant.KEY_NOTIFICATION_CONTENT_ID))
                    return true;
            }
        }

        return false;
    }

    void goToSplashScreen() {
        addFragment(SplashFragment.newInstance());
    }
}
