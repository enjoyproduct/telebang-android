package com.neo2.telebang.activity;

import android.content.Intent;
import android.os.Bundle;

import com.neo2.telebang.R;
import com.neo2.telebang.app.AppConfig;
import com.neo2.telebang.base.BaseLoginActivityInterface;
import com.neo2.telebang.base.StdActivity;
import com.neo2.telebang.fragment.customer.LoginFragment;

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
        if(AppConfig.IS_REQUIRE_LOGIN){
            Intent intent = new Intent(this, AppSlideActivity.class);
            startActivity(intent);
            finish();
        }else{
            finish();
        }
    }

    @Override
    public void onCancelLogin() {
        finish();
    }

    @Override
    public void handleBackPressInThisActivity() {
        onCancelLogin();
    }
}
