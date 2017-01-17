package com.inspius.yo365.app;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inspius.yo365.R;
import com.inspius.yo365.fragment.main_tab.AppCategoriesFragment;
import com.inspius.yo365.fragment.main_tab.AppHomeFragment;
import com.inspius.yo365.fragment.main_tab.AppTrendingFragment;
import com.inspius.yo365.fragment.main_tab.AppWatchListFragment;
import com.inspius.yo365.fragment.main_tab.TopCategoriesFragment;
import com.inspius.yo365.fragment.main_tab.home_tab.HomeLatestFragment;
import com.inspius.yo365.fragment.main_tab.home_tab.HomeMostViewFragment;
import com.inspius.yo365.fragment.main_tab.home_tab.HomeTabFragment;
import com.inspius.yo365.modules.upload_video.MAppUploadVideoFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;

/**
 * Created by Billy on 11/3/16.
 */

public enum TabSetting {
    CUSTOM_MAIN_TAB() {
        @Override
        public int[] tabs() {
            return new int[]{
                    R.string.menu_home,
                    R.string.menu_categories,
                    R.string.menu_trending,
                    R.string.menu_watch_list,
                    //R.string.menu_upload
            };
        }

        @Override
        public FragmentPagerItem getFragmentPagerItem(Context context, int titleResId) {
            FragmentPagerItem fragmentPagerItem = null;

            switch (titleResId) {
                case R.string.menu_home:
                    if (AppConfig.HOME_SCREEN == AppConstant.YO_SCREEN.DEFAULT || AppConfig.HOME_SCREEN == AppConstant.YO_SCREEN.HOME_1)
                        fragmentPagerItem = FragmentPagerItem.of(context.getString(titleResId), AppHomeFragment.class);
                    else if (AppConfig.HOME_SCREEN == AppConstant.YO_SCREEN.HOME_2) {
                        fragmentPagerItem = FragmentPagerItem.of(context.getString(titleResId), HomeTabFragment.class);
                    }
                    break;
                case R.string.menu_categories:
                    if (AppConfig.VIDEO_CATEGORIES_SCREEN == AppConstant.YO_SCREEN.DEFAULT || AppConfig.VIDEO_CATEGORIES_SCREEN == AppConstant.YO_SCREEN.VIDEO_CATEGORIES_1)
                        fragmentPagerItem = FragmentPagerItem.of(context.getString(titleResId), AppCategoriesFragment.class);
                    else
                        fragmentPagerItem = FragmentPagerItem.of(context.getString(titleResId), TopCategoriesFragment.class);

                    break;
                case R.string.menu_trending:
                    fragmentPagerItem = FragmentPagerItem.of(context.getString(titleResId), AppTrendingFragment.class);
                    break;
                case R.string.menu_watch_list:
                    fragmentPagerItem = FragmentPagerItem.of(context.getString(titleResId), AppWatchListFragment.class);
                    break;

//                case R.string.menu_upload:
//                    fragmentPagerItem = FragmentPagerItem.of(context.getString(titleResId), MAppUploadVideoFragment.class);
//                    break;

                default:
                    fragmentPagerItem = FragmentPagerItem.of(context.getString(titleResId), AppHomeFragment.class);
                    break;
            }

            return fragmentPagerItem;
        }

        @Override
        public void setup(SmartTabLayout layout) {
            super.setup(layout);

            final LayoutInflater inflater = LayoutInflater.from(layout.getContext());
            final Resources res = layout.getContext().getResources();

            layout.setCustomTabView(new SmartTabLayout.TabProvider() {
                @Override
                public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
                    View view = inflater.inflate(R.layout.custom_tab_icon, container,
                            false);

                    ImageView icon = (ImageView) view.findViewById(R.id.imvIcon);
                    TextView name = (TextView) view.findViewById(R.id.tvnName);

                    switch (position) {
                        case 0:
                            icon.setImageDrawable(res.getDrawable(R.drawable.ic_tab_home));
                            name.setText(res.getString(R.string.menu_home));
                            break;
                        case 1:
                            icon.setImageDrawable(res.getDrawable(R.drawable.ic_tab_categories));
                            name.setText(res.getString(R.string.menu_categories));
                            break;
                        case 2:
                            icon.setImageDrawable(res.getDrawable(R.drawable.ic_tab_trending));
                            name.setText(res.getString(R.string.menu_trending));
                            break;
                        case 3:
                            icon.setImageDrawable(res.getDrawable(R.drawable.ic_tab_watch_list));
                            name.setText(res.getString(R.string.menu_watch_list));
                            break;
//                        case 4:
//                            icon.setImageDrawable(res.getDrawable(R.drawable.ic_tab_upload));
//                            name.setText(res.getString(R.string.menu_upload));
//                            break;

                        default:
                            icon.setImageDrawable(res.getDrawable(R.drawable.ic_tab_home));
                            name.setText(res.getString(R.string.app_name));
                    }
                    return view;
                }
            });
        }
    }, CUSTOM_HOME_TAB() {
        @Override
        public int[] tabs() {
            return new int[]{
                    R.string.home_latest,
                    R.string.home_most_view
            };
        }

        @Override
        public FragmentPagerItem getFragmentPagerItem(Context context, int titleResId) {
            FragmentPagerItem fragmentPagerItem = null;

            switch (titleResId) {
                case R.string.home_latest:
                    fragmentPagerItem = FragmentPagerItem.of(context.getString(titleResId), HomeLatestFragment.class);
                    break;
                case R.string.home_most_view:
                    fragmentPagerItem = FragmentPagerItem.of(context.getString(titleResId), HomeMostViewFragment.class);
                    break;

                default:
                    fragmentPagerItem = FragmentPagerItem.of(context.getString(titleResId), HomeLatestFragment.class);
                    break;
            }

            return fragmentPagerItem;
        }

        @Override
        public void setup(SmartTabLayout layout) {
            super.setup(layout);
        }
    };

    public void setup(final SmartTabLayout layout) {
        //Do nothing.
    }

    public int[] tabs() {
        return new int[]{};
    }

    public FragmentPagerItem getFragmentPagerItem(Context context, int titleResId) {
        return null;
    }
}
