package com.neo2.telebang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inspius.coreapp.InspiusFragment;
import com.inspius.coreapp.helper.InspiusIntentUtils;
import com.neo2.telebang.R;
import com.neo2.telebang.activity.LoginActivity;
import com.neo2.telebang.adapter.SlideMenuAdapter;
import com.neo2.telebang.app.AppConfig;
import com.neo2.telebang.app.AppConstant;
import com.neo2.telebang.app.MenuSetting;
import com.neo2.telebang.base.BaseAppSlideFragment;
import com.neo2.telebang.fragment.home_slide.HomeFragment_1;
import com.neo2.telebang.fragment.home_slide.HomeFragment_2;
import com.neo2.telebang.fragment.home_slide.HomeFragment_3;
import com.neo2.telebang.fragment.series.SeriesSlideFragment;
import com.neo2.telebang.fragment.webview.StaticPageFragment;
import com.neo2.telebang.helper.AppUtil;
import com.neo2.telebang.helper.ImageUtil;
import com.neo2.telebang.listener.AdapterActionListener;
import com.neo2.telebang.listener.CustomerListener;
import com.neo2.telebang.model.CustomerJSON;
import com.neo2.telebang.model.MenuModel;
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

    @BindView(R.id.relativeLogin)
    RelativeLayout relativeLogin;

    @BindView(R.id.relativeProfile)
    RelativeLayout relativeProfile;

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
        initCustomerInfo();
    }

    void initCustomerInfo() {
        if (!mCustomerManager.isLogin()) {
            relativeLogin.setVisibility(View.VISIBLE);
            relativeProfile.setVisibility(View.GONE);

            return;
        }
        relativeLogin.setVisibility(View.GONE);
        relativeProfile.setVisibility(View.VISIBLE);

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

    public void actionWithMenuType(final AppConstant.MENU_TYPE type) {
        if (type == null || menuTypeActive == type)
            return;

        InspiusFragment fragment = null;
        switch (type) {
            case HOME_1:
                fragment = HomeFragment_1.newInstance();

                break;

            case HOME_2:
                fragment = HomeFragment_2.newInstance();

                break;

            case HOME_3:
                fragment = HomeFragment_3.newInstance();

                break;

            case CATEGORIES:
                if (AppConfig.VIDEO_CATEGORIES_SCREEN == AppConstant.YO_SCREEN.DEFAULT || AppConfig.VIDEO_CATEGORIES_SCREEN == AppConstant.YO_SCREEN.VIDEO_CATEGORIES_1 || AppConfig.VIDEO_CATEGORIES_SCREEN == AppConstant.YO_SCREEN.VIDEO_CATEGORIES_2)
                    fragment = VideoCategoriesSlideFragment.newInstance();
                else {
                    //fragment = MCategoriesSlideFragment.newInstance();
                }
                break;

            case WATCH_LIST:
                fragment = VideoFavouriteSlideFragment.newInstance();
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
//                fragment = MNewsSlideFragment.newInstance();
                fragment = StaticPageFragment.newInstance("News Module", AppConfig.URL_PAGE_NEWS);
                break;

            case SERIES:
                fragment = SeriesSlideFragment.newInstance();
                break;

            case UPLOAD_VIDEO:
                //fragment = MUploadVideoFragment.newInstance();
                //fragment = HomeFragment_1.newInstance(4);UPLOAD
                fragment = StaticPageFragment.newInstance("Upload Module", AppConfig.URL_PAGE_UPLOAD);
                break;
        }

        if (fragment == null)
            return;

        addFragment(type, fragment);
    }

    private void addFragment(AppConstant.MENU_TYPE type, InspiusFragment fragment) {
        if (fragment != null) {
            changeActiveMenu(type);
            mHostActivity.clearBackStack();
            mHostActivity.addFragment(fragment, true);
        }
    }

    private void changeActiveMenu(AppConstant.MENU_TYPE type) {
        menuTypeActive = type;
        for (MenuModel slideMenuModel : listMenuItem) {
            if (slideMenuModel.type == menuTypeActive) {
                slideMenuModel.isActive = true;
            } else
                slideMenuModel.isActive = false;
        }

        menuAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.imvSetting)
    void doSetting() {
        mAppActivity.toggleDrawer();
        addFragment(null, SettingFragment.newInstance());
    }

    @OnClick(R.id.relativeLogin)
    void doLogin() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);
    }
}
