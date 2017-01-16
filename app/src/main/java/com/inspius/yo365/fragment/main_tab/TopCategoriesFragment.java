package com.inspius.yo365.fragment.main_tab;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.inspius.yo365.R;
import com.inspius.yo365.adapter.GridAllCategoryAdapter;
import com.inspius.yo365.adapter.ListTopCategoryAdapter;
import com.inspius.yo365.base.BaseAppTabFragment;
import com.inspius.yo365.fragment.VideoListByCategoryFragment;
import com.inspius.yo365.listener.AdapterActionListener;
import com.inspius.yo365.model.CategoryJSON;
import com.inspius.yo365.model.DataCategoryJSON;
import com.inspius.yo365.service.AppSession;
import com.inspius.yo365.widget.GridDividerDecoration;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.grid.BasicGridLayoutManager;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Billy on 11/3/16.
 */

public class TopCategoriesFragment extends BaseAppTabFragment implements AdapterActionListener {
    @BindView(R.id.all_categories_recycler_view)
    UltimateRecyclerView allCategoriesRecyclerView;

    @BindView(R.id.urvTopCategories)
    UltimateRecyclerView topCategoriesRecyclerView;

    private BasicGridLayoutManager mGridLayoutManager;
    private GridAllCategoryAdapter mAdapterAllCategory = null;
    private int columns = 2;

    private ListTopCategoryAdapter mAdapterTopCategory;

    @Override
    public int getLayout() {
        return R.layout.fragment_app_categories_top;
    }

    @Override
    public void onInitView() {

        // init RecyclerView
        initAllCategoriesRecyclerView();
        initTopCategoriesRecyclerView();

        // init data
        initData();
    }

    void initAllCategoriesRecyclerView() {
        allCategoriesRecyclerView.setHasFixedSize(false);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            allCategoriesRecyclerView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        // All Category
        boolean includeEdge = true;
        int spacing = getResources().getDimensionPixelSize(R.dimen.divider_grid_video);

        allCategoriesRecyclerView.addItemDecoration(
                new GridDividerDecoration(columns, spacing, includeEdge));

        mAdapterAllCategory = new GridAllCategoryAdapter(new ArrayList<CategoryJSON>());
        mAdapterAllCategory.setAdapterActionListener(this);

        mGridLayoutManager = new BasicGridLayoutManager(getContext(), columns, mAdapterAllCategory);
        allCategoriesRecyclerView.setLayoutManager(mGridLayoutManager);
        allCategoriesRecyclerView.setAdapter(mAdapterAllCategory);
    }

    void initTopCategoriesRecyclerView() {
        topCategoriesRecyclerView.setHasFixedSize(false);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            topCategoriesRecyclerView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        mAdapterTopCategory = new ListTopCategoryAdapter();
        mAdapterTopCategory.setAdapterActionListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        topCategoriesRecyclerView.setLayoutManager(linearLayoutManager);
        topCategoriesRecyclerView.setAdapter(mAdapterTopCategory);
    }

    void initData() {
        DataCategoryJSON data = AppSession.getInstance().getCategoryData();

        for (CategoryJSON model : data.listCategory) {
            if (model.enable == 1)
                mAdapterAllCategory.insert(model);
        }

        if (data.listIdTopCategory != null) {
            for (long id : data.listIdTopCategory)
                for (CategoryJSON model : data.listCategory)
                    if (model.id == id) {
                        if (model.enable == 1)
                            mAdapterTopCategory.insert(model);
                        break;
                    }
        }
    }

    @Override
    public void onItemClickListener(int position, Object model) {
        CategoryJSON categoryJSON = (CategoryJSON) model;
        mHostActivity.addFragment(VideoListByCategoryFragment.newInstance(categoryJSON));
    }
}
