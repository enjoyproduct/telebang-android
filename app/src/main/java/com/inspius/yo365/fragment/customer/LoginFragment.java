package com.inspius.yo365.fragment.customer;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.inspius.yo365.R;
import com.inspius.yo365.api.APIResponseListener;
import com.inspius.yo365.base.BaseLoginFragment;
import com.inspius.yo365.fragment.SlideMenuFragment;
import com.inspius.yo365.helper.DialogUtil;
import com.inspius.yo365.helper.Validation;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Billy on 11/2/16.
 */

public class LoginFragment extends BaseLoginFragment {
    public static final String TAG = SlideMenuFragment.class.getSimpleName();

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @BindView(R.id.edtUsername)
    EditText edtUserName;

    @BindView(R.id.edtPassword)
    EditText edtPassWord;

    @Override
    public int getLayout() {
        return R.layout.fragment_customer_login;
    }

    @Override
    public void onInitView() {
        edtUserName.setText(mCustomerManager.getUsername());
        edtPassWord.setText(mCustomerManager.getPassword());

        edtPassWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    doLogin();
                }
                return false;
            }
        });
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @OnClick(R.id.btnFacebook)
    void doFacebook() {

    }

    @OnClick(R.id.tvnRegister)
    void doRegister() {
        mHostActivity.addFragment(RegisterFragment.newInstance());
    }

    @OnClick(R.id.tvnForgotPass)
    void doForgotPassword() {
        mHostActivity.addFragment(ForgotPasswordFragment.newInstance());
    }

    @OnClick(R.id.btnLogin)
    void doLogin() {
        String user = edtUserName.getText().toString();
        String pass = edtPassWord.getText().toString();

        String validMessage = Validation.isValidUsername(user);
        if (!TextUtils.isEmpty(validMessage)) {
            DialogUtil.showMessageErrorForm(mContext, validMessage);
            return;
        }

        validMessage = Validation.isValidPassword(pass);
        if (!TextUtils.isEmpty(validMessage)) {
            DialogUtil.showMessageErrorForm(mContext, validMessage);
            return;
        }

        mLoginActivity.showLoading(getString(R.string.msg_loading));
        mCustomerManager.callLogin(user, pass, new APIResponseListener() {
            @Override
            public void onError(String message) {
                mLoginActivity.hideLoading();
                DialogUtil.showMessageBox(mContext, message);
            }

            @Override
            public void onSuccess(Object results) {
                mLoginActivity.hideLoading();
                mLoginActivity.onLoginSuccess();
            }
        });
    }
}
