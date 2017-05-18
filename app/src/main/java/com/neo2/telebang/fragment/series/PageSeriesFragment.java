package com.neo2.telebang.fragment.series;

import android.os.Bundle;

import com.neo2.telebang.R;
import com.neo2.telebang.adapter.GridSeriesAdapter;
import com.neo2.telebang.api.APIResponseListener;
import com.neo2.telebang.api.RPC;
import com.neo2.telebang.app.AppConstant;
import com.neo2.telebang.base.BaseAppTabFragment;
import com.neo2.telebang.listener.AdapterActionListener;
import com.neo2.telebang.model.SeriesJSON;
import com.neo2.telebang.widget.GridDividerDecoration;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.grid.BasicGridLayoutManager;
import com.ogaclejapan.smarttablayout.utils.v4.Bundler;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Billy on 11/3/16.
 */

public class PageSeriesFragment extends BaseAppTabFragment implements AdapterActionListener {
    public static Bundle arguments(AppConstant.SERIES_TYPE type) {
        return new Bundler().putSerializable(AppConstant.KEY_TYPE, type).get();
    }

    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView ultimateRecyclerView;

    @BindView(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView avloadingIndicatorView;

    private BasicGridLayoutManager mGridLayoutManager;
    private GridSeriesAdapter mAdapter = null;
    private int columns = 2;
    private int pageNumber;
    AppConstant.SERIES_TYPE type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.type = (AppConstant.SERIES_TYPE) getArguments().getSerializable(AppConstant.KEY_TYPE);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_home_page;
    }

    @Override
    public void onInitView() {

        // init RecyclerView
        initRecyclerView();

        // load data
        startAnimLoading();
        refreshVideoList();
    }

    void initRecyclerView() {
        boolean includeEdge = true;
        int spacing = getResources().getDimensionPixelSize(R.dimen.divider_grid_video);

        ultimateRecyclerView.addItemDecoration(
                new GridDividerDecoration(columns, spacing, includeEdge));

        mAdapter = new GridSeriesAdapter(new ArrayList<SeriesJSON>());
        mAdapter.setAdapterActionListener(this);

        mGridLayoutManager = new BasicGridLayoutManager(getContext(), columns, mAdapter);
        ultimateRecyclerView.setLayoutManager(mGridLayoutManager);

        ultimateRecyclerView.setHasFixedSize(true);

        // reload data
//        ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshVideoList();
//            }
//        });

        // setting load more Recycler View
        ultimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                requestGetVideos();
            }
        });
        ultimateRecyclerView.setAdapter(mAdapter);
    }

    void refreshVideoList() {
        pageNumber = 1;
        ultimateRecyclerView.reenableLoadmore();
        requestGetVideos();
    }

    @Override
    public void onItemClickListener(int position, Object model) {
        SeriesJSON seriesJSON = (SeriesJSON) model;
        mHostActivity.addFragment(VideoListBySeriesFragment.newInstance(seriesJSON));
    }

    void requestGetVideos() {
        if (pageNumber <= 1) {
            pageNumber = 1;

            mAdapter.clear();
        }

        RPC.getSeries(type == AppConstant.SERIES_TYPE.COMPLETED, pageNumber, new APIResponseListener() {
            @Override
            public void onError(String message) {
                stopAnimLoading();
            }

            @Override
            public void onSuccess(Object results) {
                stopAnimLoading();

                List<SeriesJSON> data = (List<SeriesJSON>) results;
                if (data == null)
                    return;

                if (data.size() < AppConstant.LIMIT_SERIES_LIST) {
                    ultimateRecyclerView.disableLoadmore();
                }
                pageNumber++;
                mAdapter.insert(data);
            }
        });
    }

    void startAnimLoading() {
        if (avloadingIndicatorView != null)
            avloadingIndicatorView.smoothToShow();
    }

    void stopAnimLoading() {
        if (avloadingIndicatorView != null)
            avloadingIndicatorView.smoothToHide();

        if (ultimateRecyclerView != null)
            ultimateRecyclerView.setRefreshing(false);
    }
}
