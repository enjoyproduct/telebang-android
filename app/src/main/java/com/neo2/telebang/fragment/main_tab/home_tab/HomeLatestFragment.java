package com.neo2.telebang.fragment.main_tab.home_tab;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;

import com.neo2.telebang.R;
import com.neo2.telebang.activity.AppSlideActivity;
import com.neo2.telebang.adapter.GridVideoAdapter;
import com.neo2.telebang.api.APIResponseListener;
import com.neo2.telebang.api.RPC;
import com.neo2.telebang.app.AppConstant;
import com.neo2.telebang.base.BaseAppTabFragment;
import com.neo2.telebang.helper.AppUtil;
import com.neo2.telebang.listener.AdapterVideoActionListener;
import com.neo2.telebang.model.CustomerJSON;
import com.neo2.telebang.model.VideoJSON;
import com.neo2.telebang.model.VideoModel;
import com.neo2.telebang.widget.GridDividerDecoration;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.grid.BasicGridLayoutManager;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Billy on 11/3/16.
 */

public class HomeLatestFragment extends BaseAppTabFragment implements AdapterVideoActionListener {
    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView ultimateRecyclerView;

    @BindView(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView avloadingIndicatorView;

    private BasicGridLayoutManager mGridLayoutManager;
    private GridVideoAdapter mAdapter = null;
    private int columns = 2;
    private int pageNumber;

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

        mAdapter = new GridVideoAdapter();
        mAdapter.setAdapterActionListener(this);

        mGridLayoutManager = new BasicGridLayoutManager(getContext(), columns, mAdapter);
        ultimateRecyclerView.setLayoutManager(mGridLayoutManager);

        ultimateRecyclerView.setHasFixedSize(true);

        // reload data
        ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshVideoList();
            }
        });

        // setting load more Recycler View
        ultimateRecyclerView.reenableLoadmore();
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
    public void onItemClickListener(VideoModel model) {
        startActivity(AppUtil.getIntentVideoDetail(mContext, model, false));
    }

    @Override
    public void onPlayClickListener(VideoModel model) {



            startActivity(AppUtil.getIntentVideoDetail(mContext, model, true));

    }

    @Override
    public void onUserClickListener(CustomerJSON model) {

    }

    void requestGetVideos() {
        if (pageNumber <= 1) {
            pageNumber = 1;

            mAdapter.clear();
        }

        RPC.getLatestVideos(pageNumber, new APIResponseListener() {
            @Override
            public void onError(String message) {
                stopAnimLoading();
            }

            @Override
            public void onSuccess(Object results) {
                stopAnimLoading();

                List<VideoJSON> data = (List<VideoJSON>) results;
                if (data == null)
                    return;

                if (data.size() < AppConstant.LIMIT_VIDEOS_HOMES) {
                    ultimateRecyclerView.disableLoadmore();
                }

                pageNumber++;
                updateDataProduct(data);
            }
        });
    }

    void updateDataProduct(List<VideoJSON> data) {
        List<VideoModel> listVideo = new ArrayList<>();
        for (VideoJSON productModel : data) {
            VideoModel vModel = new VideoModel(productModel);
            listVideo.add(vModel);
        }

        mAdapter.add(listVideo);
    }

    void startAnimLoading() {
        if (avloadingIndicatorView != null)
            avloadingIndicatorView.smoothToShow();
    }

    void stopAnimLoading() {
        if (avloadingIndicatorView != null && avloadingIndicatorView.isShown())
            avloadingIndicatorView.smoothToHide();

        if (ultimateRecyclerView != null)
            ultimateRecyclerView.setRefreshing(false);
    }
}
