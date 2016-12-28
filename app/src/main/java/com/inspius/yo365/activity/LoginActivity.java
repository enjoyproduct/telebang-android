package com.inspius.yo365.activity;

import android.content.Intent;
import android.os.Bundle;

import com.inspius.yo365.R;
import com.inspius.yo365.base.BaseLoginActivityInterface;
import com.inspius.yo365.base.StdActivity;
import com.inspius.yo365.fragment.customer.LoginFragment;

import butterknife.ButterKnife;

public class LoginActivity extends StdActivity implements BaseLoginActivityInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_app);
        ButterKnife.bind(this);

        initFragment();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.id.container;
    }

    public void initFragment() {
        addFragment(LoginFragment.newInstance());
    }

    @Override
    public void updateHeaderTitle(String headerTitle) {

    }

    @Override
    public void onLoginSuccess() {
        Intent intent = new Intent(this, AppSlideActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCancelLogin() {

    }
}
