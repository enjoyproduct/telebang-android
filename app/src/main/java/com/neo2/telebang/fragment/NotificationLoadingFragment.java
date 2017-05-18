package com.neo2.telebang.fragment;

import android.content.DialogInterface;
import android.widget.TextView;

import com.neo2.telebang.R;
import com.neo2.telebang.api.APIResponseListener;
import com.neo2.telebang.api.RPC;
import com.neo2.telebang.app.AppConfig;
import com.neo2.telebang.app.AppConstant;
import com.neo2.telebang.base.StdFragment;
import com.neo2.telebang.helper.AppUtil;
import com.neo2.telebang.helper.DialogUtil;
import com.neo2.telebang.model.VideoJSON;
import com.neo2.telebang.model.VideoModel;

import butterknife.BindView;

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

    @BindView(R.id.tvnMessage)
    TextView tvnMessage;

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
        if (AppConfig.NEWS_MODULE != AppConstant.YO_MODULE.DEFAULT) {
//        MNewsRPC.getNewsByID(newID, new APIResponseListener() {
//            @Override
//            public void onError(String message) {
//                if (isDestroy)
//                    return;
//
//                DialogUtil.showMessageBox(mContext, message, false, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        onBackPressed();
//                    }
//                });
//            }
//
//            @Override
//            public void onSuccess(Object results) {
//                if (isDestroy)
//                    return;
//
//                MNewsJSON newsJSON = (MNewsJSON) results;
//                mHostActivity.addFragment(MNewsNotificationFragment.newInstance(new MNewsModel(newsJSON)));
//            }
//        });
        } else {
            DialogUtil.showMessageBox(mContext, "Please update NEWS MODULE for News notification", false, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onBackPressed();
                }
            });
        }
    }
}
