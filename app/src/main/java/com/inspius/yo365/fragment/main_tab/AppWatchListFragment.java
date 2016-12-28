package com.inspius.yo365.fragment.main_tab;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.inspius.yo365.R;
import com.inspius.yo365.adapter.WishListVideoAdapter;
import com.inspius.yo365.api.APIResponseListener;
import com.inspius.yo365.api.RPC;
import com.inspius.yo365.base.BaseAppTabFragment;
import com.inspius.yo365.fragment.VideoDetailFragment;
import com.inspius.yo365.greendao.DBWishListVideo;
import com.inspius.yo365.helper.AppUtil;
import com.inspius.yo365.helper.DialogUtil;
import com.inspius.yo365.listener.AdapterActionListener;
import com.inspius.yo365.manager.DatabaseManager;
import com.inspius.yo365.model.VideoJSON;
import com.inspius.yo365.model.VideoModel;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Billy on 11/3/16.
 */

public class AppWatchListFragment extends BaseAppTabFragment implements AdapterActionListener {
    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView ultimateRecyclerView;

    @BindView(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView avloadingIndicatorView;

    @BindView(R.id.linearLayoutNoComment)
    LinearLayout linearLayoutNoComment;

    private LinearLayoutManager linearLayoutManager;
    private WishListVideoAdapter mAdapter = null;
    int pageNumber;

    @Override
    public int getLayout() {
        return R.layout.fragment_app_watch;
    }

    @Override
    public void onInitView() {

        // init RecyclerView
        initRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();

        // load data
        startAnimLoading();
        refreshVideoList();
    }

    void initRecyclerView() {
        ultimateRecyclerView.setHasFixedSize(false);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            ultimateRecyclerView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        ultimateRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).sizeResId(R.dimen.divider_height_list_video_2).color(Color.TRANSPARENT).build());

        mAdapter = new WishListVideoAdapter();
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
        pageNumber = 1;
        requestGetVideos();
    }

    @Override
    public void onItemClickListener(int position, Object model) {
        DBWishListVideo videoModel = (DBWishListVideo) model;
        getVideoDetail(videoModel.getVideoID());
    }

    void requestGetVideos() {
        linearLayoutNoComment.setVisibility(View.GONE);
        if (pageNumber <= 1) {
            pageNumber = 1;

            mAdapter.clear();
        }

        List<DBWishListVideo> data = DatabaseManager.getInstance().listVideoAtWishList(pageNumber, mCustomerManager.getAccountID());
        stopAnimLoading();

        if (data == null || data.isEmpty() || mAdapter == null) {
            if (pageNumber <= 1)
                linearLayoutNoComment.setVisibility(View.VISIBLE);

            return;
        }

        pageNumber++;
        mAdapter.add(data);
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

    void getVideoDetail(int videoID) {
        final ProgressDialog loadingDialog = DialogUtil.showLoading(mContext, getString(R.string.msg_loading_content));
        RPC.getVideoDetailByID(videoID, new APIResponseListener() {
            @Override
            public void onError(String message) {
                DialogUtil.hideLoading(loadingDialog);
                DialogUtil.showMessageBox(mContext, message);
            }

            @Override
            public void onSuccess(Object results) {
                DialogUtil.hideLoading(loadingDialog);

                VideoJSON videoJSON = (VideoJSON) results;
                startActivity(AppUtil.getIntentVideoDetail(mContext, new VideoModel(videoJSON), false));
            }
        });
    }
}
