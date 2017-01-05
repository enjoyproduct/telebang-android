package com.inspius.yo365.modules.news;

import android.view.View;

import com.inspius.yo365.R;
import com.inspius.yo365.api.APIResponseListener;
import com.inspius.yo365.base.BaseAppTabFragment;
import com.inspius.yo365.listener.AdapterActionListener;
import com.inspius.yo365.widget.GridDividerDecoration;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.grid.BasicGridLayoutManager;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Billy on 11/3/16.
 */

public class MNewsCategoriesPageFragment extends BaseAppTabFragment implements AdapterActionListener {
    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView ultimateRecyclerView;

    @BindView(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView avloadingIndicatorView;

    private BasicGridLayoutManager mGridLayoutManager;
    private int columns = 3;
    private MListNewsCategoriesAdapter mAdapter = null;

    @Override
    public int getLayout() {
        return R.layout.m_fragment_news_categories_slide_page;
    }

    @Override
    public void onInitView() {
        // init RecyclerView
        initRecyclerView();
        // load data
        requestGetData();
    }

    void initRecyclerView() {
        ultimateRecyclerView.setHasFixedSize(false);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            ultimateRecyclerView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        boolean includeEdge = true;
        int spacing = getResources().getDimensionPixelSize(R.dimen.divider_grid_news_categories);
        ultimateRecyclerView.addItemDecoration(
                new GridDividerDecoration(columns, spacing, includeEdge));
        mAdapter = new MListNewsCategoriesAdapter();
        mAdapter.setAdapterActionListener(this);

        mGridLayoutManager = new BasicGridLayoutManager(getContext(), columns, mAdapter);
        ultimateRecyclerView.setLayoutManager(mGridLayoutManager);

        ultimateRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClickListener(int position, Object model) {
        MNewsCategoryJSON categoryJSON = (MNewsCategoryJSON) model;
        mHostActivity.addFragment(MNewsListByCategoryFragment.newInstance(categoryJSON));
    }

    void requestGetData() {
        startAnimLoading();
        MNewsRPC.getNewsCategories(new APIResponseListener() {
            @Override
            public void onError(String message) {
                stopAnimLoading();
            }

            @Override
            public void onSuccess(Object results) {
                stopAnimLoading();

                List<MNewsCategoryJSON> data = (List<MNewsCategoryJSON>) results;
                if (data == null || data.isEmpty())
                    return;

                mAdapter.add(data);
            }
        });
    }

    void startAnimLoading() {
        if (avloadingIndicatorView != null)
            avloadingIndicatorView.smoothToShow();
    }

    void stopAnimLoading() {
        if (avloadingIndicatorView != null)
            avloadingIndicatorView.hide();

        if (ultimateRecyclerView != null)
            ultimateRecyclerView.setRefreshing(false);
    }
}
