package com.inspius.yo365.base;

import android.os.Bundle;

/**
 * Created by Billy on 12/3/15.
 */
public abstract class BaseAppSlideFragment extends StdFragment {
    protected BaseAppSlideActivityInterface mAppActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getContext();

        if (!(getActivity() instanceof BaseAppSlideActivityInterface)) {
            throw new ClassCastException("Hosting activity must implement BaseAppSlideActivityInterface");
        } else {
            mAppActivity = (BaseAppSlideActivityInterface) getActivity();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        mAppActivity.hideKeyBoard();
    }
}
