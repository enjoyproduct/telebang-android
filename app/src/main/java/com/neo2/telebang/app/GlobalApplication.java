package com.neo2.telebang.app;

import android.content.Context;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.inspius.coreapp.InspiusApplication;
import com.inspius.coreapp.config.InspiusConfig;
import com.inspius.coreapp.helper.InspiusUtils;
import com.neo2.telebang.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.graphics.Bitmap;
import android.support.multidex.MultiDex;

/**
 * Created by Billy on 9/3/15.
 */
public class GlobalApplication extends InspiusApplication {
    /**
     * Log or request TAG
     */
    public static final String TAG = GlobalApplication.class.getSimpleName();
    private static GlobalApplication mInstance;

    @Override
    public InspiusConfig.Environment getEnvironment() {
        return AppConfig.ENVIRONMENT;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        MultiDex.install(getBaseContext());
        mInstance = this;
        initImageLoader(mAppContext);

        // facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static synchronized GlobalApplication getInstance() {
        return mInstance;
    }

    public static Context getAppContext() {
        return mAppContext;
    }


    private void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(512 * 1024 * 1024); // 128 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.diskCacheExtraOptions(480, 320, null);

        if (InspiusUtils.isProductionEnvironment())
            config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }


}
