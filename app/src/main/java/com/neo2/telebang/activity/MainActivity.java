package com.neo2.telebang.activity;

import android.content.Intent;
import android.os.Bundle;

import com.inspius.coreapp.helper.InspiusUtils;
import com.neo2.telebang.R;
import com.neo2.telebang.app.AppConstant;
import com.neo2.telebang.base.StdActivity;
import com.neo2.telebang.fragment.NotificationLoadingFragment;
import com.neo2.telebang.fragment.SplashFragment;
import com.neo2.telebang.helper.ExceptionHandler;

import butterknife.ButterKnife;
import co.paystack.android.PaystackSdk;

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
        ///set exception handler
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

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
