package com.inspius.yo365.fragment;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inspius.yo365.R;
import com.inspius.yo365.adapter.ListCommentVideoAdapter;
import com.inspius.yo365.api.APIResponseListener;
import com.inspius.yo365.api.AppRestClient;
import com.inspius.yo365.api.RPC;
import com.inspius.yo365.app.AppConstant;
import com.inspius.yo365.base.StdFragment;
import com.inspius.yo365.helper.AppUtil;
import com.inspius.yo365.helper.DialogUtil;
import com.inspius.yo365.listener.AdapterActionListener;
import com.inspius.yo365.model.CommentJSON;
import com.inspius.yo365.model.CommentModel;
import com.inspius.yo365.model.VideoModel;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Billy on 12/1/15.
 */
public class VideoCommentFragment extends StdFragment implements AdapterActionListener {
    public static final String TAG = VideoCommentFragment.class.getSimpleName();

    public static VideoCommentFragment newInstance(VideoModel videoModel) {
        VideoCommentFragment fragment = new VideoCommentFragment();
        fragment.videoModel = videoModel;
        return fragment;
    }

    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView ultimateRecyclerView;

    @BindView(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView avloadingIndicatorView;

    @BindView(R.id.edtCommentText)
    EditText edtCommentText;

    @BindView(R.id.tvnHeaderTitle)
    TextView tvnHeaderTitle;

    @BindView(R.id.linearLayoutNoComment)
    LinearLayout linearLayoutNoComment;

    private VideoModel videoModel;

    private LinearLayoutManager linearLayoutManager;
    private ListCommentVideoAdapter mAdapter = null;

    int pageNumber;

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_video_comment;
    }

    @Override
    public void onInitView() {
        tvnHeaderTitle.setText(videoModel.getTitle());

        ultimateRecyclerView.setHasFixedSize(false);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            ultimateRecyclerView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        ultimateRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).sizeResId(R.dimen.divider_height_list_comment).colorResId(R.color.divider_color_list_comment).build());

        mAdapter = new ListCommentVideoAdapter(mContext);
        mAdapter.setAdapterActionListener(this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        ultimateRecyclerView.setLayoutManager(linearLayoutManager);

        ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ultimateRecyclerView.reenableLoadmore();
                pageNumber = 1;
                requestGetComments();
            }
        });
        // setting load more Recycler View
        ultimateRecyclerView.reenableLoadmore();
        ultimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                requestGetComments();
            }
        });
        ultimateRecyclerView.setAdapter(mAdapter);

        startAnimLoading();

        pageNumber = 1;
        requestGetComments();
    }

    @OnClick(R.id.imvHeaderBack)
    void doBack() {
        getActivity().finish();
    }

    @Override
    public void onItemClickListener(int position, Object model) {
        //mHostTabInterface.addFragment(VideoDetailFragment.newInstance((VideoModel) model, false), true);
    }

    void startAnimLoading() {
        if (avloadingIndicatorView != null)
            avloadingIndicatorView.smoothToShow();
    }

    void stopAnimLoading() {
        if (avloadingIndicatorView != null)
            avloadingIndicatorView.smoothToHide();
    }

    void requestGetComments() {
        linearLayoutNoComment.setVisibility(View.GONE);
        RPC.getVideoComments(videoModel.getVideoId(), pageNumber, new APIResponseListener() {
            @Override
            public void onError(String message) {
                stopAnimLoading();
            }

            @Override
            public void onSuccess(Object results) {
                stopAnimLoading();

                if (ultimateRecyclerView == null)
                    return;

                ultimateRecyclerView.setRefreshing(false);

                if (pageNumber <= 1)
                    mAdapter.clear();


                List<CommentJSON> data = (List<CommentJSON>) results;
                if (data == null || data.isEmpty()) {
                    ultimateRecyclerView.disableLoadmore();

                    if (pageNumber <= 1)
                        linearLayoutNoComment.setVisibility(View.VISIBLE);
                } else {
                    List<CommentModel> listComment = new ArrayList<>();
                    for (CommentJSON commentJSON : data)
                        listComment.add(new CommentModel(commentJSON));

                    mAdapter.insert(listComment);
                }
            }
        });
    }

    @OnClick(R.id.imvPostComment)
    void insertComment() {
        AppUtil.hideKeyBoard(getActivity());

        linearLayoutNoComment.setVisibility(View.GONE);
        if (!mCustomerManager.isLogin()) {
            DialogUtil.showMessageBox(mContext, getString(R.string.msg_request_login));
            return;
        }

        String comment = edtCommentText.getText().toString();
        if (!TextUtils.isEmpty(comment)) {
            final ProgressDialog loadingDialog = DialogUtil.showLoading(mContext, getString(R.string.msg_loading));

            RPC.postCommentVideo(mCustomerManager.getAccountID(), videoModel.getVideoId(), comment, new APIResponseListener() {
                @Override
                public void onError(String message) {
                    DialogUtil.hideLoading(loadingDialog);

                    DialogUtil.showMessageBox(mContext, message);
                }

                @Override
                public void onSuccess(Object results) {
                    DialogUtil.hideLoading(loadingDialog);

                    CommentJSON commentJSON = (CommentJSON) results;
                    if (commentJSON != null)
                        mAdapter.insertFirst(new CommentModel(commentJSON));

                    edtCommentText.setText("");
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        AppRestClient.cancelRequestsByTAG(AppConstant.RELATIVE_URL_INSERT_COMMENT);
        AppRestClient.cancelRequestsByTAG(AppConstant.RELATIVE_URL_GET_VIDEO_COMMENTS);
    }
}
