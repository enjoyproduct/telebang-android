package com.neo2.telebang.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Billy on 11/2/16.
 */

public class StdActivityImplement implements StdActivityInterface {
    private ProgressDialog loadingDialog;
    private Activity mActivity;

    public StdActivityImplement(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void showLoading(String message) {
        if (mActivity.isFinishing())
            return;

        if (loadingDialog != null && loadingDialog.isShowing())
            return;

        try {
            if (loadingDialog == null) {
                loadingDialog = new ProgressDialog(mActivity);
                loadingDialog.setCancelable(false);
            }
            if (!TextUtils.isEmpty(message))
                loadingDialog.setMessage(message);

            loadingDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void hideLoading() {
        if (mActivity.isFinishing())
            return;

        try {
            if (loadingDialog != null && loadingDialog.isShowing())
                loadingDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void hideKeyBoard() {
        View view = mActivity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
