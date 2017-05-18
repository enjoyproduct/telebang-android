package com.neo2.telebang.fragment.series;

import android.text.TextUtils;
import android.widget.TextView;

import com.inspius.coreapp.widget.InspiusHackyViewPager;
import com.neo2.telebang.R;
import com.neo2.telebang.app.AppConstant;
import com.neo2.telebang.base.BaseAppSlideFragment;
import com.neo2.telebang.fragment.VideoSearchFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Billy on 11/3/16.
 */

public class SeriesSlideFragment extends BaseAppSlideFragment {
    public static final String TAG = SeriesSlideFragment.class.getSimpleName();

    public static SeriesSlideFragment newInstance() {
        SeriesSlideFragment fragment = new SeriesSlideFragment();
        return fragment;
    }

    @BindView(R.id.tvnHeaderTitle)
    TextView tvnHeaderTitle;

    @BindView(R.id.viewpager)
    InspiusHackyViewPager viewpager;

    @BindView(R.id.viewpagertab)
    SmartTabLayout viewpagerTab;

    private FragmentPagerItemAdapter adapter;

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_series_slide;
    }

    @Override
    public void onInitView() {
        updateHeaderTitle(getString(R.string.menu_series));
        final FragmentPagerItems pages = new FragmentPagerItems(mContext);
        if (getActivity() != null && isAdded()) {
            pages.add(FragmentPagerItem.of(getString(R.string.menu_on_air), PageSeriesFragment.class, PageSeriesFragment.arguments(AppConstant.SERIES_TYPE.ON_AIR)));
            pages.add(FragmentPagerItem.of(getString(R.string.menu_completed), PageSeriesFragment.class, PageSeriesFragment.arguments(AppConstant.SERIES_TYPE.COMPLETED)));

            adapter = new FragmentPagerItemAdapter(
                    getChildFragmentManager(), pages);
            viewpager.setAdapter(adapter);
            viewpagerTab.setViewPager(viewpager);
            viewpager.setLocked(false);
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
