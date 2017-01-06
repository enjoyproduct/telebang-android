package com.inspius.yo365.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.TextView;

import com.inspius.coreapp.helper.InspiusSharedPrefUtils;
import com.inspius.yo365.R;
import com.inspius.yo365.activity.AppSlideActivity;
import com.inspius.yo365.activity.IntroActivity;
import com.inspius.yo365.activity.LoginActivity;
import com.inspius.yo365.api.APIResponseListener;
import com.inspius.yo365.api.RPC;
import com.inspius.yo365.app.AppConfig;
import com.inspius.yo365.app.AppConstant;
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

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(AppConfig.SPLASH_DURATION);
                    if (getActivity() == null || isDestroy)
                        return;


                    getCategories();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    void getCategories() {
        if (getActivity() == null || isDestroy)
            return;

        RPC.getCategories(new APIResponseListener() {
            @Override
            public void onError(String message) {
                autoLogin();
            }

            @Override
            public void onSuccess(Object results) {
                DataCategoryJSON data = (DataCategoryJSON) results;
                AppSession.getInstance().setCategoryData(data);

                autoLogin();
            }
        });
    }

    void autoLogin() {
        if (getActivity() == null || isDestroy)
            return;

        mCustomerManager.callAutoLoginRequest(new APIResponseListener() {
            @Override
            public void onError(String message) {
                nextScreen();
            }

            @Override
            public void onSuccess(Object results) {
                nextScreen();
            }
        });
    }

    void nextScreen() {
        Intent intent = null;
        if (AppConfig.IS_SHOW_INTRO) {
            boolean isFirstOpenApp = InspiusSharedPrefUtils.getBooleanFromPrefs(mContext, AppConstant.KEY_FIRST_OPEN_APP, true);
            if (isFirstOpenApp) {
                intent = new Intent(mContext, IntroActivity.class);
                InspiusSharedPrefUtils.saveToPrefs(mContext, AppConstant.KEY_FIRST_OPEN_APP, false);
            }
        }

        if (intent == null) {
            if (mCustomerManager.isLogin()) {
                intent = new Intent(mContext, AppSlideActivity.class);
            } else {
                intent = new Intent(mContext, LoginActivity.class);
            }
        }

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
}
