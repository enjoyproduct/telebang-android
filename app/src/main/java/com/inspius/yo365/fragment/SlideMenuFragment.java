package com.inspius.yo365.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inspius.coreapp.InspiusFragment;
import com.inspius.coreapp.helper.InspiusIntentUtils;
import com.inspius.yo365.R;
import com.inspius.yo365.adapter.SlideMenuAdapter;
import com.inspius.yo365.app.AppConfig;
import com.inspius.yo365.app.AppConstant;
import com.inspius.yo365.app.MenuSetting;
import com.inspius.yo365.base.BaseAppSlideFragment;
import com.inspius.yo365.fragment.webview.StaticPageFragment;
import com.inspius.yo365.helper.AppUtil;
import com.inspius.yo365.helper.ImageUtil;
import com.inspius.yo365.listener.AdapterActionListener;
import com.inspius.yo365.listener.CustomerListener;
import com.inspius.yo365.model.CustomerJSON;
import com.inspius.yo365.model.MenuModel;
import com.inspius.yo365.modules.news.MNewsSlideFragment;
import com.inspius.yo365.modules.upload_video.MUploadVideoFragment;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class SlideMenuFragment extends BaseAppSlideFragment implements AdapterActionListener, CustomerListener {
    public static final String TAG = SlideMenuFragment.class.getSimpleName();

    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView ultimateRecyclerView;

    @BindView(R.id.imvAvatar)
    ImageView imvAvatar;

    @BindView(R.id.tvn_customer_name)
    TextView tvnCustomerName;

    @BindView(R.id.tvn_customer_email)
    TextView tvnCustomerEmail;

    private SlideMenuAdapter menuAdapter;
    private List<MenuModel> listMenuItem;
    private AppConstant.MENU_TYPE menuTypeActive;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listMenuItem = new ArrayList<>();
        listMenuItem.addAll(MenuSetting.getInstance().getListMenuAtSlideMenu());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_slide_menu;
    }

    @Override
    public void onInitView() {
        ultimateRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).sizeResId(R.dimen.divider_height_list_menu).colorResId(R.color.divider_color_list_menu).build());

        menuAdapter = new SlideMenuAdapter(listMenuItem, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        ultimateRecyclerView.setLayoutManager(linearLayoutManager);
        ultimateRecyclerView.setAdapter(menuAdapter);

        AppConstant.MENU_TYPE typeActive = MenuSetting.getInstance().getDefaultTypeMenuActive();
        actionWithMenuType(typeActive);

        initCustomerInfo();
    }

    @Override
    public void onResume() {
        mCustomerManager.subscribeStateLogin(this);
        super.onResume();
    }

    @Override
    public void onDestroy() {
        menuTypeActive = null;
        mCustomerManager.unSubscribeStateLogin(this);
        super.onDestroy();
    }

    @Override
    public void onCustomerProfileChanged(CustomerJSON customer) {
        initCustomerInfo();
    }

    @Override
    public void onCustomerLoggedIn(CustomerJSON customer) {
        initCustomerInfo();
    }

    @Override
    public void onCustomerLogout() {

    }

    void initCustomerInfo() {
        tvnCustomerName.setText(mCustomerManager.getCustomerJSON().getFullName());
        tvnCustomerEmail.setText(mCustomerManager.getCustomerJSON().email);

        ImageLoader.getInstance().displayImage(mCustomerManager.getCustomerJSON().avatar, imvAvatar, ImageUtil.optionsImageAvatar);
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public void onItemClickListener(int position, Object model) {
        mAppActivity.toggleDrawer();

        MenuModel slideMenuModel = (MenuModel) model;
        actionWithMenuType(slideMenuModel.type);
    }

    private void actionWithMenuType(final AppConstant.MENU_TYPE type) {
        if (type == null || menuTypeActive == type)
            return;

        InspiusFragment fragment = null;
        switch (type) {
            case HOME:
                fragment = AppFragment.newInstance(0);
                break;

            case CATEGORIES:
                fragment = AppFragment.newInstance(1);
                break;

            case WATCH_LIST:
                fragment = AppFragment.newInstance(3);
                break;

            case DOWNLOAD:
                fragment = DownloadListFragment.newInstance();
                break;

            case SHARE:
                startActivity(AppUtil.shareApp(mContext));
                break;

            case MY_ACCOUNT:
                fragment = SettingFragment.newInstance();
                break;

            case ABOUT_US:
                fragment = StaticPageFragment.newInstance("About Us", AppConfig.URL_PAGE_ABOUT);
                break;

            case FEEDBACK:
                startActivity(InspiusIntentUtils.openPlayStore(mContext));
                break;

            case TERM:
                fragment = StaticPageFragment.newInstance("Terms & Privacy Policy", AppConfig.URL_PAGE_TERM);
                break;

            case HELP:
                fragment = StaticPageFragment.newInstance("Help", AppConfig.URL_PAGE_HELP);
                break;

            case NEWS:
                fragment = MNewsSlideFragment.newInstance();

                break;

            case UPLOAD_VIDEO:
                fragment = MUploadVideoFragment.newInstance();

                break;
        }

        if (fragment == null)
            return;

        addFragment(type, fragment);
    }

    private void addFragment(AppConstant.MENU_TYPE type, InspiusFragment fragment) {
        if (fragment != null) {
            menuTypeActive = type;
            for (MenuModel slideMenuModel : listMenuItem) {
                if (slideMenuModel.type == menuTypeActive) {
                    slideMenuModel.isActive = true;
                } else
                    slideMenuModel.isActive = false;
            }

            menuAdapter.notifyDataSetChanged();

            mHostActivity.clearBackStack();
            mHostActivity.addFragment(fragment, true);
        }
    }

    @OnClick(R.id.imvSetting)
    void doSetting() {
        mAppActivity.toggleDrawer();
        addFragment(null, SettingFragment.newInstance());
    }
}
