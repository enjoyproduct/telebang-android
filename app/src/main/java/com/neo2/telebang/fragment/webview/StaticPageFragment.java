package com.neo2.telebang.fragment.webview;

import android.os.Bundle;

import com.neo2.telebang.R;
import com.neo2.telebang.base.BaseAppSlideActivityInterface;
import com.neo2.telebang.helper.AppUtil;

/**
 * A placeholder fragment containing a simple view.
 */
public class StaticPageFragment extends WebViewFragment {
    public static final String TAG = StaticPageFragment.class.getSimpleName();
    protected BaseAppSlideActivityInterface mAppActivity;

    public static StaticPageFragment newInstance(String headerName, String urlContent) {
        StaticPageFragment fragment = new StaticPageFragment();
        fragment.headerName = headerName;
        fragment.urlContent = urlContent;
        fragment.urlShare = urlContent;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!(getActivity() instanceof BaseAppSlideActivityInterface)) {
            throw new ClassCastException("Hosting activity must implement BaseAppSlideActivityInterface");
        } else {
            mAppActivity = (BaseAppSlideActivityInterface) getActivity();
        }
    }

    @Override
    public void onInitView() {
        super.onInitView();

        imvHeaderLeft.setImageResource(R.drawable.ic_action_menu);
    }

    @Override
    void onShareClicked() {
        startActivity(AppUtil.shareApp(mContext));
    }

    @Override
    void doHeaderLeft() {
        mAppActivity.toggleDrawer();
    }
}
