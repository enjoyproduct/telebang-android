package com.neo2.telebang.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inspius.coreapp.helper.InspiusIntentUtils;
import com.neo2.telebang.R;
import com.neo2.telebang.adapter.DownloadListVideoAdapter;
import com.neo2.telebang.base.BaseAppSlideFragment;
import com.neo2.telebang.greendao.DBVideoDownload;
import com.neo2.telebang.helper.DialogUtil;
import com.neo2.telebang.listener.AdapterDownloadActionListener;
import com.neo2.telebang.manager.DatabaseManager;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Billy on 11/3/16.
 */

public class DownloadListFragment extends BaseAppSlideFragment implements AdapterDownloadActionListener {
    public static final String TAG = DownloadListFragment.class.getSimpleName();

    public static DownloadListFragment newInstance() {
        DownloadListFragment fragment = new DownloadListFragment();
        return fragment;
    }

    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView ultimateRecyclerView;

    @BindView(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView avloadingIndicatorView;

    @BindView(R.id.linearLayoutNoComment)
    LinearLayout linearLayoutNoComment;

    @BindView(R.id.tvnHeaderTitle)
    TextView tvnHeaderTitle;

    private LinearLayoutManager linearLayoutManager;
    private DownloadListVideoAdapter mAdapter = null;
    private List<DBVideoDownload> mData;

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_download_list;
    }

    @Override
    public void onInitView() {
        tvnHeaderTitle.setText(getString(R.string.menu_my_download));
        // init RecyclerView
        initRecyclerView();

        // load data
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
        mData = new ArrayList<>();
        mAdapter = new DownloadListVideoAdapter(mData);
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
//        ultimateRecyclerView.reenableLoadmore();
//        ultimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
//            @Override
//            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
//                requestGetVideos();
//            }
//        });
        ultimateRecyclerView.setAdapter(mAdapter);
    }

    void refreshVideoList() {
        mAdapter.clearInternal(mData);
        requestGetVideos();
    }

    @Override
    public void onItemClickListener(int position) {
        DBVideoDownload model = mAdapter.getItem(position);
        Intent intent = InspiusIntentUtils.openVideo(model.getVideoPath());
        startActivity(intent);
    }

    @Override
    public void onItemDeleteClickListener(final int position) {
        DialogUtil.showMessageYesNo(mContext, getString(R.string.msg_delete_video), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DBVideoDownload model = mAdapter.getItem(position);
                DatabaseManager.getInstance().deleteVideoDownloadByID(model.getId());

                File file = new File(model.getVideoPath());
                if (file != null) {
                    boolean deleted = file.delete();
                }

                mAdapter.removeInternal(mData, position);

                if (mAdapter.getItemCount() <= 0)
                    linearLayoutNoComment.setVisibility(View.VISIBLE);
            }
        });
    }

    void requestGetVideos() {
        startAnimLoading();
        linearLayoutNoComment.setVisibility(View.GONE);

        List<DBVideoDownload> listVideo = DatabaseManager.getInstance().getVideosDownload();
        stopAnimLoading();

        if (listVideo == null || listVideo.isEmpty() || mAdapter == null) {
            linearLayoutNoComment.setVisibility(View.VISIBLE);

            return;
        }

        mAdapter.insert(listVideo);
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

    @OnClick(R.id.imvHeaderMenu)
    void doShowMenu() {
        mAppActivity.toggleDrawer();
    }

    @OnClick(R.id.imvHeaderSearch)
    void doShowSearch() {

    }
}
