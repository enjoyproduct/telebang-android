package com.neo2.telebang.fragment.main_tab;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.neo2.telebang.R;
import com.neo2.telebang.adapter.ListVideoAdapter1;
import com.neo2.telebang.api.APIResponseListener;
import com.neo2.telebang.api.RPC;
import com.neo2.telebang.app.AppConstant;
import com.neo2.telebang.base.BaseAppTabFragment;
import com.neo2.telebang.helper.AppUtil;
import com.neo2.telebang.listener.AdapterVideoActionListener;
import com.neo2.telebang.model.CustomerJSON;
import com.neo2.telebang.model.VideoJSON;
import com.neo2.telebang.model.VideoModel;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Billy on 11/3/16.
 */

public class AppTrendingFragment extends BaseAppTabFragment implements AdapterVideoActionListener {
    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView ultimateRecyclerView;

    @BindView(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView avloadingIndicatorView;

    private LinearLayoutManager linearLayoutManager;
    private ListVideoAdapter1 mAdapter = null;
    int pageNumber;

    @Override
    public int getLayout() {
        return R.layout.fragment_app_trending;
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
        ultimateRecyclerView.setHasFixedSize(false);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            ultimateRecyclerView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        ultimateRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).sizeResId(R.dimen.divider_height_list_video_1).color(R.color.divider_color_list_video).build());

        mAdapter = new ListVideoAdapter1();
        mAdapter.setAdapterActionListener(this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        ultimateRecyclerView.setLayoutManager(linearLayoutManager);

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
        ultimateRecyclerView.reenableLoadmore();
        pageNumber = 1;
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

        RPC.getTrendingVideos(pageNumber, new APIResponseListener() {
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
        if (avloadingIndicatorView != null)
            avloadingIndicatorView.hide();

        if (ultimateRecyclerView != null)
            ultimateRecyclerView.setRefreshing(false);
    }
}
