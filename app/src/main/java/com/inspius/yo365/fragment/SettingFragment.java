package com.inspius.yo365.fragment;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inspius.coreapp.helper.InspiusIntentUtils;
import com.inspius.yo365.R;
import com.inspius.yo365.activity.LoginActivity;
import com.inspius.yo365.api.APIResponseListener;
import com.inspius.yo365.app.AppConstant;
import com.inspius.yo365.base.BaseAppSlideFragment;
import com.inspius.yo365.fragment.customer.ChangePasswordFragment;
import com.inspius.yo365.fragment.customer.ChangeProfileFragment;
import com.inspius.yo365.helper.AppUtil;
import com.inspius.yo365.helper.DialogUtil;
import com.inspius.yo365.helper.ImageUtil;
import com.inspius.yo365.model.ImageFileModel;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingFragment extends BaseAppSlideFragment {
    public static final String TAG = SettingFragment.class.getSimpleName();

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @BindView(R.id.tvnHeaderTitle)
    TextView tvnHeaderTitle;

    @BindView(R.id.tvnVip)
    TextView tvnVip;

    @BindView(R.id.imvHeaderSearch)
    ImageView imvHeaderSearch;

    @BindView(R.id.imgAvatar)
    ImageView imgAvatar;

    @Override
    public int getLayout() {
        return R.layout.fragment_setting;
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public void onInitView() {
        tvnHeaderTitle.setText(getString(R.string.menu_setting));
        imvHeaderSearch.setVisibility(View.GONE);

        if (mCustomerManager.isPremiumAccount())
            tvnVip.setVisibility(View.VISIBLE);
        else
            tvnVip.setVisibility(View.GONE);

        ImageLoader.getInstance().displayImage(mCustomerManager.getCustomerJSON().avatar, imgAvatar, ImageUtil.optionsImageAvatar);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConstant.REQUEST_ALBUM_PIC) {
            if (resultCode == Activity.RESULT_OK) {
                ImageFileModel imageObj = ImageUtil.getImageFileFromUri(mContext, data.getData());
                requestChangeAvatar(imageObj);
            }
        }
    }

    @OnClick(R.id.imvHeaderMenu)
    void doMenu() {
        mAppActivity.toggleDrawer();
    }

    @OnClick(R.id.linerEditAccount)
    void doChangeProfile() {
        mHostActivity.addFragment(ChangeProfileFragment.newInstance());
    }

    @OnClick(R.id.linerChangePass)
    void doChangePassword() {
        mHostActivity.addFragment(ChangePasswordFragment.newInstance());
    }

    @OnClick(R.id.linerChangeAvatar)
    void doChangeAvatar() {
        if (!AppUtil.verifyStoragePermissions(getActivity()))
            return;
        Intent intent = InspiusIntentUtils.pickImage();
        startActivityForResult(intent, AppConstant.REQUEST_ALBUM_PIC);
    }

    @OnClick(R.id.btnLogout)
    void doLogout() {
        mCustomerManager.callLogout(new APIResponseListener() {
            @Override
            public void onError(String message) {
                DialogUtil.showLoading(mContext, message);
            }

            @Override
            public void onSuccess(Object results) {
                startActivity(new Intent(mContext, LoginActivity.class));
                getActivity().finish();
            }
        });
    }

    void requestChangeAvatar(ImageFileModel imageFileModel) {
        mAppActivity.showLoading("Updating avatar...");
        mCustomerManager.callChangeAvatar(imageFileModel, new APIResponseListener() {
            @Override
            public void onError(String message) {
                mAppActivity.hideLoading();
                DialogUtil.showMessageBox(getContext(), message);
            }

            @Override
            public void onSuccess(Object results) {
                mAppActivity.hideLoading();
                ImageLoader.getInstance().displayImage(mCustomerManager.getCustomerJSON().avatar, imgAvatar, ImageUtil.optionsImageAvatar);
            }
        });
    }
}
