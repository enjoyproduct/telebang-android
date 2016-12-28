package com.inspius.yo365.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.TextView;

import com.inspius.yo365.R;
import com.inspius.yo365.activity.LoginActivity;
import com.inspius.yo365.api.APIResponseListener;
import com.inspius.yo365.api.RPC;
import com.inspius.yo365.app.AppConfig;
import com.inspius.yo365.base.StdFragment;
import com.inspius.yo365.model.DataCategoryJSON;
import com.inspius.yo365.service.AppSession;

import butterknife.BindView;

/**
 * A placeholder fragment containing a simple view.
 */
public class SplashFragment extends StdFragment {
    public static final String TAG = SplashFragment.class.getSimpleName();
    boolean isDestroy = false;

    public static SplashFragment newInstance() {
        SplashFragment fragment = new SplashFragment();
        return fragment;
    }

    @BindView(R.id.tvnVersion)
    TextView tvnVersion;

    @Override
    public int getLayout() {
        return R.layout.fragment_splash;
    }

    @Override
    public void onResume() {
        super.onResume();

        isDestroy = false;
    }

    @Override
    public void onInitView() {
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);

            tvnVersion.setText(String.format(getString(R.string.fm_version), pInfo.versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        getCategories();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(AppConfig.SPLASH_DURATION);
                    if (getActivity() == null || isDestroy)
                        return;


                    nextScreen();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    void nextScreen() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public boolean onBackPressed() {
        return true;
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

    void getCategories() {
        if (getActivity() == null || isDestroy)
            return;

        RPC.getCategories(new APIResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onSuccess(Object results) {
                DataCategoryJSON data = (DataCategoryJSON) results;
                AppSession.getInstance().setCategoryData(data);
            }
        });
    }
}
