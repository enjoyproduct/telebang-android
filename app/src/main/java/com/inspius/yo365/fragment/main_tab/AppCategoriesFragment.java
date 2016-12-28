package com.inspius.yo365.fragment.main_tab;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.inspius.yo365.R;
import com.inspius.yo365.adapter.GridAllCategoryAdapter;
import com.inspius.yo365.adapter.ListCategoryAdapter;
import com.inspius.yo365.adapter.ListVideoAdapter1;
import com.inspius.yo365.base.BaseAppTabFragment;
import com.inspius.yo365.fragment.VideoListByCategoryFragment;
import com.inspius.yo365.listener.AdapterActionListener;
import com.inspius.yo365.model.CategoryJSON;
import com.inspius.yo365.model.DataCategoryJSON;
import com.inspius.yo365.service.AppSession;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;

import butterknife.BindView;

/**
 * Created by Billy on 11/3/16.
 */

public class AppCategoriesFragment extends BaseAppTabFragment implements AdapterActionListener {
    @BindView(R.id.all_categories_recycler_view)
    UltimateRecyclerView ultimateRecyclerView;

    private ListCategoryAdapter mAdapter = null;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public int getLayout() {
        return R.layout.fragment_app_categories;
    }

    @Override
    public void onInitView() {

        // init RecyclerView
        initAllCategoriesRecyclerView();

        // init data
        initData();
    }

    void initAllCategoriesRecyclerView() {
        ultimateRecyclerView.setHasFixedSize(false);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            ultimateRecyclerView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        ultimateRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).sizeResId(R.dimen.divider_height_list_category).color(R.color.divider_color_list_category).build());

        mAdapter = new ListCategoryAdapter();
        mAdapter.setAdapterActionListener(this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        ultimateRecyclerView.setLayoutManager(linearLayoutManager);
        ultimateRecyclerView.setAdapter(mAdapter);
    }

    void initData() {
        DataCategoryJSON data = AppSession.getInstance().getCategoryData();

        for (CategoryJSON model : data.listCategory) {
            if (model.enable == 1)
                mAdapter.insert(model);
        }
    }

    @Override
    public void onItemClickListener(int position, Object model) {
        CategoryJSON categoryJSON = (CategoryJSON) model;
        mHostActivity.addFragment(VideoListByCategoryFragment.newInstance(categoryJSON));
    }
}
