package com.inspius.yo365.fragment.customer;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.inspius.coreapp.helper.Logger;
import com.inspius.yo365.R;
import com.inspius.yo365.api.APIResponseListener;
import com.inspius.yo365.base.BaseLoginFragment;
import com.inspius.yo365.helper.DialogUtil;
import com.inspius.yo365.helper.Validation;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Billy on 11/2/16.
 */

public class LoginFragment extends BaseLoginFragment {
    public static final String TAG = LoginFragment.class.getSimpleName();

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @BindView(R.id.edtUsername)
    EditText edtUserName;

    @BindView(R.id.edtPassword)
    EditText edtPassWord;

    @BindView(R.id.cbnRemember)
    CheckBox cbnRemember;

    CallbackManager callbackManager;

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

        registerCallbackFacebook();
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    void registerCallbackFacebook() {
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        requestLoginFacebook(loginResult.getAccessToken().getToken());
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        DialogUtil.showMessageBox(mContext, exception.getMessage());
                    }
                });
    }


    void requestLoginFacebook(String accessToken) {
        mLoginActivity.showLoading(getString(R.string.msg_loading_content));
        mCustomerManager.callLoginFacebook(accessToken, new APIResponseListener() {
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

    @OnClick(R.id.btnFacebook)
    void doFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"));
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

        boolean isRemember = cbnRemember.isChecked();

        mLoginActivity.showLoading(getString(R.string.msg_loading));
        mCustomerManager.callLogin(isRemember, user, pass, new APIResponseListener() {
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
