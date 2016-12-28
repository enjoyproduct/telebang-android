package com.inspius.yo365.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inspius.yo365.app.GlobalApplication;
import com.inspius.yo365.manager.CustomerManager;
import com.inspius.coreapp.InspiusFragment;

import butterknife.ButterKnife;

/**
 * Created by Billy on 12/3/15.
 */
public abstract class StdFragment extends InspiusFragment {
    protected Context mContext;
    protected GlobalApplication mApplication;
    protected CustomerManager mCustomerManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getContext();
        mApplication = GlobalApplication.getInstance();
        mCustomerManager = CustomerManager.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = createPersistentView(inflater, container, savedInstanceState, getLayout());
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!hasInitializedRootView) {
            hasInitializedRootView = true;

            onInitView();
        }
    }

    @Override
    public boolean onBackPressed() {
        return mHostActivity.popBackStack();
    }

    public abstract int getLayout();

    public abstract void onInitView();
}
