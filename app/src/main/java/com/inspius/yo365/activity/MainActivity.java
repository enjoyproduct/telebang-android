package com.inspius.yo365.activity;

import android.os.Bundle;

import com.inspius.coreapp.helper.InspiusUtils;
import com.inspius.yo365.R;
import com.inspius.yo365.base.StdActivity;
import com.inspius.yo365.fragment.SplashFragment;

import butterknife.ButterKnife;

public class MainActivity extends StdActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_app);
        ButterKnife.bind(this);

        initFragment();

        InspiusUtils.printHashKey(this);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.id.container;
    }

    public void initFragment() {
        addFragment(SplashFragment.newInstance());
    }
}
