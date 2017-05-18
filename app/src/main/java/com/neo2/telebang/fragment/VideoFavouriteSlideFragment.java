package com.neo2.telebang.fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neo2.telebang.R;
import com.neo2.telebang.adapter.WishListVideoAdapter;
import com.neo2.telebang.api.APIResponseListener;
import com.neo2.telebang.api.RPC;
import com.neo2.telebang.app.AppConstant;
import com.neo2.telebang.base.BaseAppSlideFragment;
import com.neo2.telebang.greendao.DBWishListVideo;
import com.neo2.telebang.helper.AppUtil;
import com.neo2.telebang.helper.DialogUtil;
import com.neo2.telebang.listener.AdapterActionListener;
import com.neo2.telebang.listener.CustomerListener;
import com.neo2.telebang.manager.DatabaseManager;
import com.neo2.telebang.model.CustomerJSON;
import com.neo2.telebang.model.VideoJSON;
import com.neo2.telebang.model.VideoModel;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Billy on 11/3/16.
 */

public class VideoFavouriteSlideFragment extends BaseAppSlideFragment implements AdapterActionListener, CustomerListener {
    public static final String TAG = VideoFavouriteSlideFragment.class.getSimpleName();

    public static VideoFavouriteSlideFragment newInstance() {
        VideoFavouriteSlideFragment fragment = new VideoFavouriteSlideFragment();
        return fragment;
    }

    @BindView(R.id.tvnHeaderTitle)
    TextView tvnHeaderTitle;

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
    public String getTagText() {
        return TAG;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_video_favourite_slide;
    }

    @Override
    public void onInitView() {
        updateHeaderTitle(getString(R.string.menu_favourite));
        stopAnimLoading();
        // init RecyclerView
        initRecyclerView();
    }

    @OnClick(R.id.imvHeaderMenu)
    void doShowMenu() {
        mAppActivity.toggleDrawer();
    }

    @OnClick(R.id.imvHeaderSearch)
    void doShowSearch() {
        mHostActivity.addFragment(VideoSearchFragment.newInstance());
    }

    void updateHeaderTitle(String name) {
        if (TextUtils.isEmpty(name))
            name = getString(R.string.app_name);

        tvnHeaderTitle.setText(name);
    }

    @Override
    public void onResume() {
        super.onResume();

        // load data
        refreshVideoList();

        mCustomerManager.subscribeStateLogin(this);
    }

    @Override
    public void onDestroy() {
        mCustomerManager.unSubscribeStateLogin(this);
        super.onDestroy();
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
        ultimateRecyclerView.reenableLoadmore();
        requestGetVideos();
    }

    @Override
    public void onItemClickListener(int position, Object model) {
        DBWishListVideo videoModel = (DBWishListVideo) model;
        getVideoDetail(videoModel.getVideoID());
    }

    void requestGetVideos() {
        if (!mCustomerManager.isLogin()) {
            linearLayoutNoComment.setVisibility(View.VISIBLE);
            ultimateRecyclerView.setRefreshing(false);
            return;
        }

        linearLayoutNoComment.setVisibility(View.GONE);
        if (pageNumber <= 1) {
            pageNumber = 1;

            mAdapter.clear();
        }

        List<DBWishListVideo> data = DatabaseManager.getInstance().listVideoAtWishList(pageNumber, mCustomerManager.getAccountID());

        if (data == null || data.isEmpty() || mAdapter == null) {
            if (pageNumber <= 1)
                linearLayoutNoComment.setVisibility(View.VISIBLE);

            return;
        }
        ultimateRecyclerView.setRefreshing(false);
        if (data.size() < AppConstant.LIMIT_WISH_LIST)
            ultimateRecyclerView.disableLoadmore();

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

    @Override
    public void onCustomerLoggedIn(CustomerJSON customer) {
        refreshVideoList();
    }

    @Override
    public void onCustomerLogout() {
        linearLayoutNoComment.setVisibility(View.VISIBLE);
        mAdapter.clear();
    }

    @Override
    public void onCustomerProfileChanged(CustomerJSON customer) {

    }
}
