package com.inspius.yo365.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.inspius.coreapp.helper.InspiusIntentUtils;
import com.inspius.yo365.R;
import com.inspius.yo365.activity.VideoDetailActivity;
import com.inspius.yo365.app.AppConfig;
import com.inspius.yo365.app.AppConstant;
import com.inspius.yo365.model.VideoModel;

/**
 * Created by Billy on 12/27/16.
 */

public class AppUtil {
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static Intent shareApp(Context mContext) {
        String appPackageName = mContext.getPackageName();
        String urlAppOnStore = "https://play.google.com/store/apps/details?id=" + appPackageName;
        return InspiusIntentUtils.shareText(mContext.getString(R.string.app_name), urlAppOnStore);
    }

    public static Intent getIntentVideoDetail(Context mContext, VideoModel videoModel, boolean isAutoPlay) {
        Intent intent = null;
        switch (AppConfig.VIDEO_DETAIL_MODULE) {
            case VIDEO_DETAIL_JW:
                intent = getIntentVideoDetailJW(mContext, videoModel.getVideoType());
                break;

            default:
                intent = getIntentVideoDetailDefault(mContext);
                break;
        }

        intent.putExtra(AppConstant.KEY_BUNDLE_AUTO_PLAY, isAutoPlay);
        intent.putExtra(AppConstant.KEY_BUNDLE_VIDEO, videoModel);

        return intent;
    }

    private static Intent getIntentVideoDetailDefault(Context mContext) {
        Intent intent = new Intent(mContext, VideoDetailActivity.class);
        return intent;
    }

    private static Intent getIntentVideoDetailJW(Context mContext, AppConstant.VIDEO_TYPE videoType) {
        Intent intent = null;
//        switch (videoType) {
//            case YOUTUBE:
//                intent = new Intent(mContext, MYoutubeVideoDetailActivity.class);
//                break;
//
//            case UPLOAD:
////                intent = new Intent(mContext, MJWVideoDetailActivity.class);
//                intent = new Intent(mContext, MExoDetailActivity.class);
//                break;
//
//            case MP3:
//                intent = new Intent(mContext, MMusicDetailActivity.class);
//                break;
//
//            default:
//                intent = new Intent(mContext, MWebVideoDetailActivity.class);
//                break;
//        }

        return intent;
    }

    /**
     * Checks if the app has permission to write to device storage
     * <p/>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static boolean verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );

            return false;
        }

        return true;
    }

    public static void hideKeyBoard(Activity mActivity) {
        View view = mActivity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
