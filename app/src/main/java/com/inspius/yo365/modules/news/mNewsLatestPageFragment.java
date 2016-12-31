package com.inspius.yo365.modules.news;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.inspius.yo365.R;
import com.inspius.yo365.adapter.ListNewsAdapter;
import com.inspius.yo365.adapter.WishListVideoAdapter;
import com.inspius.yo365.api.APIResponseListener;
import com.inspius.yo365.api.RPC;
import com.inspius.yo365.base.BaseAppTabFragment;
import com.inspius.yo365.greendao.DBWishListVideo;
import com.inspius.yo365.helper.AppUtil;
import com.inspius.yo365.helper.DialogUtil;
import com.inspius.yo365.listener.AdapterActionListener;
import com.inspius.yo365.manager.DatabaseManager;
import com.inspius.yo365.model.NewsJSON;
import com.inspius.yo365.model.NewsModel;
import com.inspius.yo365.model.VideoJSON;
import com.inspius.yo365.model.VideoModel;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Billy on 11/3/16.
 */

public class MNewsLatestPageFragment extends BaseAppTabFragment implements AdapterActionListener {
    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView ultimateRecyclerView;

    @BindView(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView avloadingIndicatorView;

    private LinearLayoutManager linearLayoutManager;
    private ListNewsAdapter mAdapter = null;
    int pageNumber;

    @Override
    public int getLayout() {
        return R.layout.m_fragment_news_slide_page;
    }

    @Override
    public void onInitView() {

        // init RecyclerView
        initRecyclerView();

        // load data
        startAnimLoading();
        refreshVideoList();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    void initRecyclerView() {
        ultimateRecyclerView.setHasFixedSize(false);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            ultimateRecyclerView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        ultimateRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).sizeResId(R.dimen.divider_height_list_video_2).color(Color.TRANSPARENT).build());

        mAdapter = new ListNewsAdapter();
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
                requestGetData();
            }
        });
        ultimateRecyclerView.setAdapter(mAdapter);
    }

    void refreshVideoList() {
        pageNumber = 1;
        requestGetData();
    }

    @Override
    public void onItemClickListener(int position, Object model) {
    }

    void requestGetData() {
        if (pageNumber <= 1) {
            pageNumber = 1;

            mAdapter.clear();
        }

        RPC.getNews(pageNumber, new APIResponseListener() {
            @Override
            public void onError(String message) {
                stopAnimLoading();
            }

            @Override
            public void onSuccess(Object results) {
                stopAnimLoading();

                List<NewsJSON> data = (List<NewsJSON>) results;
                if (data == null || data.isEmpty())
                    return;

                List<NewsModel> listNews = new ArrayList<>();
                for (NewsJSON news : data)
                    listNews.add(new NewsModel(news));

                pageNumber++;
                mAdapter.add(listNews);
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
