package com.inspius.yo365.fragment.customer;

import android.content.DialogInterface;
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
import com.inspius.yo365.R;
import com.inspius.yo365.api.APIResponseListener;
import com.inspius.yo365.base.BaseLoginFragment;
import com.inspius.yo365.fragment.SlideMenuFragment;
import com.inspius.yo365.helper.DialogUtil;
import com.inspius.yo365.helper.Validation;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Billy on 11/2/16.
 */

public class RegisterFragment extends BaseLoginFragment {
    public static final String TAG = SlideMenuFragment.class.getSimpleName();

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @BindView(R.id.edtUsername)
    EditText edtUsername;

    @BindView(R.id.edtEmail)
    EditText edtEmail;

    @BindView(R.id.edtPassword)
    EditText edtPassword;

    @BindView(R.id.edtPasswordVerify)
    EditText edtPasswordVerify;

    @BindView(R.id.cbnTerm)
    CheckBox cbnTerm;

    CallbackManager callbackManager;

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_customer_register;
    }

    @Override
    public void onInitView() {
        edtPasswordVerify.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    doRegister();
                }
                return false;
            }
        });

        registerCallbackFacebook();
    }

    @OnClick(R.id.btnSubmit)
    void doRegister() {
        String username = edtUsername.getText().toString();
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        String passwordVerify = edtPasswordVerify.getText().toString();

        String validMessage = Validation.isValidUsername(username);
        if (!TextUtils.isEmpty(validMessage)) {
            DialogUtil.showMessageErrorForm(mContext, validMessage);
            return;
        }

        validMessage = Validation.isValidEmail(email);
        if (!TextUtils.isEmpty(validMessage)) {
            DialogUtil.showMessageErrorForm(mContext, validMessage);
            return;
        }

        validMessage = Validation.isValidPassword(password, passwordVerify);
        if (!TextUtils.isEmpty(validMessage)) {
            DialogUtil.showMessageErrorForm(mContext, validMessage);
            return;
        }

        if (!cbnTerm.isChecked()) {
            DialogUtil.showMessageErrorForm(mContext, "Please agree to the Terms and Conditions");
            return;
        }

        mLoginActivity.showLoading(getString(R.string.msg_loading));
        mCustomerManager.callRegister(username, email, password, new APIResponseListener() {
            @Override
            public void onError(String message) {
                mLoginActivity.hideLoading();
                DialogUtil.showMessageBox(mContext, message);
            }

            @Override
            public void onSuccess(Object results) {
                mLoginActivity.hideLoading();

                DialogUtil.showMessageBox(mContext, getString(R.string.msg_register_success), false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mLoginActivity.onLoginSuccess();
                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.btnFacebook)
    void doFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"));
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

    @OnClick(R.id.imvHeaderBack)
    void doBack() {
        onBackPressed();
    }
}