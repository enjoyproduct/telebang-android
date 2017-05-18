package com.neo2.telebang.fragment.customer;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.EditText;

import com.neo2.telebang.R;
import com.neo2.telebang.api.APIResponseListener;
import com.neo2.telebang.api.RPC;
import com.neo2.telebang.base.BaseLoginFragment;
import com.neo2.telebang.fragment.SlideMenuFragment;
import com.neo2.telebang.helper.DialogUtil;
import com.neo2.telebang.helper.Validation;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Billy on 11/2/16.
 */

public class ForgotPasswordFragment extends BaseLoginFragment {
    public static final String TAG = SlideMenuFragment.class.getSimpleName();

    public static ForgotPasswordFragment newInstance() {
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        return fragment;
    }

    @BindView(R.id.edtEmail)
    EditText edtEmail;

    @Override
    public int getLayout() {
        return R.layout.fragment_customer_forgot_password;
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public void onInitView() {

    }

    @OnClick(R.id.btnSubmit)
    void doRegister() {
        String email = edtEmail.getText().toString();

        String validMessage = Validation.isValidEmail(email);
        if (!TextUtils.isEmpty(validMessage)) {
            DialogUtil.showMessageErrorForm(mContext, validMessage);
            return;
        }

        mLoginActivity.showLoading(getString(R.string.msg_loading));
        RPC.requestForgotPassword(email, new APIResponseListener() {
            @Override
            public void onError(String message) {
                mLoginActivity.hideLoading();
                DialogUtil.showMessageBox(mContext, message);
            }

            @Override
            public void onSuccess(Object results) {
                mLoginActivity.hideLoading();

                DialogUtil.showMessageBox(mContext, getString(R.string.msg_forgot_pass_success), false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mHostActivity.popBackStack();
                    }
                });
            }
        });
    }

    @OnClick(R.id.imvHeaderBack)
    void doBack() {
        onBackPressed();
    }
}
