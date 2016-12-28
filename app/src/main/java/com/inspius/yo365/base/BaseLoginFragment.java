package com.inspius.yo365.base;

import android.os.Bundle;

/**
 * Created by Billy on 12/3/15.
 */
public abstract class BaseLoginFragment extends StdFragment {
    protected BaseLoginActivityInterface mLoginActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getContext();

        if (!(getActivity() instanceof BaseLoginActivityInterface)) {
            throw new ClassCastException("Hosting activity must implement BaseLoginActivityInterface");
        } else {
            mLoginActivity = (BaseLoginActivityInterface) getActivity();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        mLoginActivity.hideKeyBoard();
    }
}
