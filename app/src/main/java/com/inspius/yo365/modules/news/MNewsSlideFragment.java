package com.inspius.yo365.modules.news;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.TextView;

import com.inspius.coreapp.widget.InspiusHackyViewPager;
import com.inspius.yo365.R;
import com.inspius.yo365.app.TabSetting;
import com.inspius.yo365.base.BaseAppSlideFragment;
import com.inspius.yo365.fragment.VideoSearchFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class MNewsSlideFragment extends BaseAppSlideFragment {
    public static final String TAG = MNewsSlideFragment.class.getSimpleName();

    public static MNewsSlideFragment newInstance() {
        MNewsSlideFragment fragment = new MNewsSlideFragment();
        return fragment;
    }

    @BindView(R.id.viewpager)
    InspiusHackyViewPager viewpager;

    @BindView(R.id.viewpagertab)
    SmartTabLayout viewpagerTab;

    @BindView(R.id.tvnHeaderTitle)
    TextView tvnHeaderTitle;

    private FragmentPagerItemAdapter adapter;

    @Override
    public int getLayout() {
        return R.layout.m_fragment_news_slide;
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public void onInitView() {
        final FragmentPagerItems pages = new FragmentPagerItems(mContext);
        for (int titleResId : TabSetting.CUSTOM_NEWS_TAB.tabs()) {
            FragmentPagerItem fragmentPagerItem = TabSetting.CUSTOM_NEWS_TAB.getFragmentPagerItem(mContext, titleResId);
            pages.add(fragmentPagerItem);
        }

        adapter = new FragmentPagerItemAdapter(getChildFragmentManager(), pages);

        viewpager.setAdapter(adapter);
        viewpagerTab.setViewPager(viewpager);

        viewpager.setLocked(false);

        viewpagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateHeaderTitle(pages.get(position).getTitle().toString());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @OnClick(R.id.imvHeaderMenu)
    void doShowMenu() {
        mAppActivity.toggleDrawer();
    }

    @OnClick(R.id.imvHeaderSearch)
    void doShowSearch() {
        mHostActivity.addFragment(VideoSearchFragment.newInstance());
    }

    void updateHeaderTitle(String name) {
        if (TextUtils.isEmpty(name))
            name = getString(R.string.app_name);

        tvnHeaderTitle.setText(name);
    }
}
