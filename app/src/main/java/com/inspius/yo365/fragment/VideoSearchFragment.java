package com.inspius.yo365.fragment;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inspius.yo365.R;
import com.inspius.yo365.adapter.ListVideoAdapter2;
import com.inspius.yo365.api.APIResponseListener;
import com.inspius.yo365.api.RPC;
import com.inspius.yo365.base.BaseAppSlideFragment;
import com.inspius.yo365.greendao.DBKeyword;
import com.inspius.yo365.helper.AppUtil;
import com.inspius.yo365.listener.AdapterVideoActionListener;
import com.inspius.yo365.manager.DatabaseManager;
import com.inspius.yo365.model.VideoJSON;
import com.inspius.yo365.model.VideoModel;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cuneyt.example.com.tagview.Models.TagClass;
import cuneyt.example.com.tagview.Tag.OnTagClickListener;
import cuneyt.example.com.tagview.Tag.OnTagDeleteListener;
import cuneyt.example.com.tagview.Tag.Tag;
import cuneyt.example.com.tagview.Tag.TagView;

/**
 * A placeholder fragment containing a simple view.
 */
public class VideoSearchFragment extends BaseAppSlideFragment implements AdapterVideoActionListener {
    public static final String TAG = VideoSearchFragment.class.getSimpleName();

    public static VideoSearchFragment newInstance() {
        VideoSearchFragment fragment = new VideoSearchFragment();
        return fragment;
    }

    @BindView(R.id.tvn_title_video_list)
    TextView tvnTitleVideoList;

    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView ultimateRecyclerView;

    @BindView(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView avloadingIndicatorView;

    @BindView(R.id.edtSearchField)
    EditText edtSearchField;

    @BindView(R.id.linear_keywords)
    LinearLayout linearKeywords;

    @BindView(R.id.linearLayoutNoComment)
    LinearLayout linearLayoutNoComment;

    @BindView(R.id.tag_group)
    TagView tagGroup;

    private LinearLayoutManager linearLayoutManager;
    private ListVideoAdapter2 mAdapter = null;
    int pageNumber;
    String currentKeyword = "";

    @Override
    public int getLayout() {
        return R.layout.fragment_video_search;
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public void onInitView() {
        // init RecyclerView
        initRecyclerView();

        // initRecentKeyword
        initRecentKeyword();

        // init search field
        edtSearchField.requestFocus();
        edtSearchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {

                    String keyword = edtSearchField.getText().toString();
                    if (!currentKeyword.equalsIgnoreCase(keyword)) {
                        pageNumber = 1;
                        requestGetVideos(keyword);

                        if (!TextUtils.isEmpty(keyword))
                            DatabaseManager.getInstance().insertKeyword(keyword);
                    }
                }
                return false;
            }
        });

        // load data
        pageNumber = 1;
        requestGetVideos(currentKeyword);
    }

    void initRecentKeyword() {
        tagGroup.setOnTagClickListener(new OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int position) {
                edtSearchField.setText(tag.text);
                edtSearchField.setSelection(tag.text.length());//to set cursor position

                pageNumber = 1;
                requestGetVideos(tag.text);
            }
        });

        List<DBKeyword> listKeyword = DatabaseManager.getInstance().getListKeyword(10);
        if (listKeyword.isEmpty()) {
            linearKeywords.setVisibility(View.GONE);
        } else {
            linearKeywords.setVisibility(View.VISIBLE);

            String text = "";
            ArrayList<Tag> tags = new ArrayList<>();
            Tag tag;
            for (DBKeyword dbKeyword : listKeyword) {
                if (TextUtils.isEmpty(text) || dbKeyword.getKeyword().toLowerCase().startsWith(text.toLowerCase())) {
                    tag = new Tag(dbKeyword.getKeyword());
                    tag.radius = 10f;
                    tag.layoutColor = (Color.parseColor("#BBBBBB"));
                    tag.isDeletable = false;

                    tags.add(tag);
                }
            }
            tagGroup.addTags(tags);
        }
    }

    void initRecyclerView() {
        ultimateRecyclerView.setHasFixedSize(false);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            ultimateRecyclerView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        ultimateRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).sizeResId(R.dimen.divider_height_list_video_2).color(Color.TRANSPARENT).build());

        mAdapter = new ListVideoAdapter2();
        mAdapter.setAdapterActionListener(this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        ultimateRecyclerView.setLayoutManager(linearLayoutManager);

        // reload data
//        ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshVideoList();
//            }
//        });

        // setting load more Recycler View
        ultimateRecyclerView.reenableLoadmore();
        ultimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                requestGetVideos(null);
            }
        });
        ultimateRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClickListener(VideoModel model) {
        startActivity(AppUtil.getIntentVideoDetail(mContext, model, false));
    }

    @Override
    public void onPlayClickListener(VideoModel model) {
        startActivity(AppUtil.getIntentVideoDetail(mContext, model, true));
    }

    void requestGetVideos(String keyword) {
        if (pageNumber <= 1) {
            pageNumber = 1;
            startAnimLoading();
            mAdapter.clear();
        }

        linearLayoutNoComment.setVisibility(View.GONE);

        if (TextUtils.isEmpty(keyword)) {
            tvnTitleVideoList.setText(getString(R.string.suggest_video));
            RPC.getMostVideos(pageNumber, new APIResponseListener() {
                @Override
                public void onError(String message) {
                    stopAnimLoading();
                }

                @Override
                public void onSuccess(Object results) {
                    stopAnimLoading();

                    List<VideoJSON> data = (List<VideoJSON>) results;
                    if (data == null || data.isEmpty())
                        return;

                    pageNumber++;
                    updateDataProduct(data);
                }
            });
        } else {
            tvnTitleVideoList.setText(String.format("Search Results For: %s", keyword));
            currentKeyword = keyword;

            RPC.getVideosByKeyword(keyword, pageNumber, new APIResponseListener() {
                @Override
                public void onError(String message) {
                    stopAnimLoading();
                }

                @Override
                public void onSuccess(Object results) {
                    stopAnimLoading();

                    List<VideoJSON> data = (List<VideoJSON>) results;
                    if (data == null || data.isEmpty()) {
                        if (pageNumber == 1) {
                            linearLayoutNoComment.setVisibility(View.VISIBLE);
                        }
                        return;
                    }

                    pageNumber++;
                    updateDataProduct(data);
                }
            });
        }
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

    @OnClick(R.id.imvHeaderBack)
    void doBack() {
        onBackPressed();
    }

}
