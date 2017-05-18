package com.neo2.telebang.fragment.main_tab.home_tab;

import com.inspius.coreapp.widget.InspiusHackyViewPager;
import com.neo2.telebang.R;
import com.neo2.telebang.app.TabSetting;
import com.neo2.telebang.base.BaseAppTabFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import butterknife.BindView;

/**
 * Created by Billy on 11/3/16.
 */

public class HomeTabFragment extends BaseAppTabFragment {
    @BindView(R.id.viewpager)
    InspiusHackyViewPager viewpager;

    @BindView(R.id.viewpagertab)
    SmartTabLayout viewpagerTab;

    private FragmentPagerItemAdapter adapter;

    @Override
    public int getLayout() {
        return R.layout.fragment_home_tab;
    }

    @Override
    public void onInitView() {
        final FragmentPagerItems pages = new FragmentPagerItems(mContext);
        for (int titleResId : TabSetting.CUSTOM_HOME_TAB.tabs()) {
            FragmentPagerItem fragmentPagerItem = TabSetting.CUSTOM_HOME_TAB.getFragmentPagerItem(mContext, titleResId);
            pages.add(fragmentPagerItem);
        }

        adapter = new FragmentPagerItemAdapter(getChildFragmentManager(), pages);

        viewpager.setAdapter(adapter);
        viewpagerTab.setViewPager(viewpager);

        viewpager.setLocked(false);
    }
}
