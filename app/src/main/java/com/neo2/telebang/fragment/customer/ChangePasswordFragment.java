package com.neo2.telebang.fragment.customer;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.neo2.telebang.R;
import com.neo2.telebang.api.APIResponseListener;
import com.neo2.telebang.base.BaseAppSlideFragment;
import com.neo2.telebang.helper.DialogUtil;
import com.neo2.telebang.helper.Validation;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChangePasswordFragment extends BaseAppSlideFragment {
    public static final String TAG = ChangePasswordFragment.class.getSimpleName();

    public static ChangePasswordFragment newInstance() {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        return fragment;
    }

    @BindView(R.id.tvnHeaderTitle)
    TextView tvnHeaderTitle;

    @BindView(R.id.edtOldPassword)
    EditText edtOldPassword;

    @BindView(R.id.edtNewPassword)
    EditText edtNewPassword;

    @BindView(R.id.edtReNewPassword)
    EditText edtReNewPassword;

    @Override
    public int getLayout() {
        return R.layout.fragment_customer_change_pass;
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public void onInitView() {
        tvnHeaderTitle.setText(getString(R.string.change_password));
        edtReNewPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    doSubmit();
                }
                return false;
            }
        });
    }

    @OnClick(R.id.imvHeaderBack)
    void doBack() {
        onBackPressed();
    }

    @OnClick(R.id.btnSubmit)
    void doSubmit() {
        String oldPassword = edtOldPassword.getText().toString();
        String newPassword = edtNewPassword.getText().toString();
        String reNewPasswordVerify = edtReNewPassword.getText().toString();

        String validMessage = Validation.isValidPassword(oldPassword);
        if (!TextUtils.isEmpty(validMessage)) {
            DialogUtil.showMessageErrorForm(mContext, validMessage);
            return;
        }

        validMessage = Validation.isValidPassword(newPassword, reNewPasswordVerify);
        if (!TextUtils.isEmpty(validMessage)) {
            DialogUtil.showMessageErrorForm(mContext, validMessage);
            return;
        }
        mAppActivity.showLoading(getString(R.string.msg_loading));
        mCustomerManager.callChangePassword(oldPassword, newPassword, new APIResponseListener() {
            @Override
            public void onError(String message) {
                mAppActivity.hideLoading();
                DialogUtil.showMessageBox(mContext, message);
            }

            @Override
            public void onSuccess(Object results) {
                mAppActivity.hideLoading();
                DialogUtil.showMessageBox(getContext(), getString(R.string.msg_change_pass_success), false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
            }
        });
    }
}
