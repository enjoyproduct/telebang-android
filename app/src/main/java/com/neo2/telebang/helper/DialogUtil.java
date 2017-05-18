package com.neo2.telebang.helper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by Billy on 11/23/15.
 */
public class DialogUtil {
    public static void showMessageBox(Context context, String message) {
        showMessageBox(context, message, true, null);
    }

    public static void showMessageBox(Context context, String message, boolean isBackPress, DialogInterface.OnClickListener listener) {
        if (message == null) {
            message = "";
        }

        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setMessage(message);
        b.setPositiveButton("CLOSE", listener);
        b.setCancelable(isBackPress);
        AlertDialog d = b.show();

        ((TextView) d.findViewById(android.R.id.message)).setGravity(Gravity.CENTER);
    }

    public static void showMessageYesNo(Context context, String message, DialogInterface.OnClickListener listener) {
        if (message == null) {
            message = "";
        }

        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setMessage(message);
        b.setPositiveButton("Yes", listener);
        b.setNegativeButton("No", null);
        AlertDialog d = b.show();

        ((TextView) d.findViewById(android.R.id.message)).setGravity(Gravity.CENTER);
    }

    public static void showMessageErrorForm(Context context, String message) {
        if (message == null) {
            message = "";
        }

        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setMessage(message);
        b.setPositiveButton("CLOSE", null);
        AlertDialog d = b.show();

        ((TextView) d.findViewById(android.R.id.message)).setGravity(Gravity.CENTER);
    }

    public static ProgressDialog showLoading(Context mContext, String message) {
        try {
            ProgressDialog loadingDialog = new ProgressDialog(mContext);
            loadingDialog.setCancelable(false);
            if (!TextUtils.isEmpty(message))
                loadingDialog.setMessage(message);

            loadingDialog.show();

            return loadingDialog;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void hideLoading(ProgressDialog loadingDialog) {
        try {
            if (loadingDialog != null && loadingDialog.isShowing())
                loadingDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
