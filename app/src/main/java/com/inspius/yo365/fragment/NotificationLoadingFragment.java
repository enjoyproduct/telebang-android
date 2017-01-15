package com.inspius.yo365.fragment;

import android.content.DialogInterface;

import com.inspius.yo365.R;
import com.inspius.yo365.api.APIResponseListener;
import com.inspius.yo365.api.RPC;
import com.inspius.yo365.app.AppConstant;
import com.inspius.yo365.base.StdFragment;
import com.inspius.yo365.helper.AppUtil;
import com.inspius.yo365.helper.DialogUtil;
import com.inspius.yo365.model.VideoJSON;
import com.inspius.yo365.model.VideoModel;
import com.inspius.yo365.modules.news.MNewsJSON;
import com.inspius.yo365.modules.news.MNewsModel;
import com.inspius.yo365.modules.news.MNewsNotificationFragment;
import com.inspius.yo365.modules.news.MNewsRPC;

/**
 * A placeholder fragment containing a simple view.
 */
public class NotificationLoadingFragment extends StdFragment {
    public static final String TAG = NotificationLoadingFragment.class.getSimpleName();
    boolean isDestroy = false;

    public static NotificationLoadingFragment newInstance(AppConstant.NOTIFICATION_TYPE contentType, int contentID) {
        NotificationLoadingFragment fragment = new NotificationLoadingFragment();
        fragment.contentType = contentType;
        fragment.contentID = contentID;
        return fragment;
    }

    AppConstant.NOTIFICATION_TYPE contentType;
    int contentID;

    @Override
    public int getLayout() {
        return R.layout.fragment_notification_loading;
    }

    @Override
    public void onResume() {
        super.onResume();

        isDestroy = false;
    }

    @Override
    public void onInitView() {
        if (contentID <= 0) {
            onBackPressed();
            return;
        }

        if (contentType == AppConstant.NOTIFICATION_TYPE.VIDEO) {
            requestGetVideoDetail(contentID);
        } else if (contentType == AppConstant.NOTIFICATION_TYPE.NEWS) {
            requestGetNewsDetail(contentID);
        }
    }

    @Override
    public boolean onBackPressed() {
        getActivity().finish();
        return false;
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroy = true;
    }

    void requestGetVideoDetail(int videoID) {
        RPC.getVideoDetailByID(videoID, new APIResponseListener() {
            @Override
            public void onError(String message) {
                if (isDestroy)
                    return;

                DialogUtil.showMessageBox(mContext, message, false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
            }

            @Override
            public void onSuccess(Object results) {
                if (isDestroy)
                    return;

                VideoJSON videoJSON = (VideoJSON) results;
                startActivity(AppUtil.getIntentVideoDetail(mContext, new VideoModel(videoJSON), true));
                getActivity().finish();
            }
        });
    }

    void requestGetNewsDetail(int newID) {
        MNewsRPC.getNewsByID(newID, new APIResponseListener() {
            @Override
            public void onError(String message) {
                if (isDestroy)
                    return;

                DialogUtil.showMessageBox(mContext, message, false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
            }

            @Override
            public void onSuccess(Object results) {
                if (isDestroy)
                    return;

                MNewsJSON newsJSON = (MNewsJSON) results;
                mHostActivity.addFragment(MNewsNotificationFragment.newInstance(new MNewsModel(newsJSON)));
            }
        });
    }
}
