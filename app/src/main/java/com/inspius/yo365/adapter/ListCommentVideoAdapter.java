package com.inspius.yo365.adapter;

/**
 * Created by Billy on 11/7/16.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inspius.yo365.R;
import com.inspius.yo365.helper.ImageUtil;
import com.inspius.yo365.listener.AdapterActionListener;
import com.inspius.yo365.model.CommentModel;
import com.inspius.yo365.widget.CalloutLink;
import com.inspius.yo365.widget.Hashtag;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListCommentVideoAdapter extends UltimateViewAdapter<ListCommentVideoAdapter.HolderListCell> {
    private List<CommentModel> mItems;
    AdapterActionListener listener;

    private Context mContext;

    public ListCommentVideoAdapter(Context mContext) {
        this.mContext = mContext;
        this.mItems = new ArrayList<>();
    }

    public void setAdapterActionListener(AdapterActionListener listener) {
        this.listener = listener;
    }

    @Override
    public HolderListCell onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video_comment, parent, false);
        HolderListCell vh = new HolderListCell(v, true);
        return vh;
    }

    @Override
    public void onBindViewHolder(final HolderListCell holder, final int position) {
        final CommentModel model = getItem(position);
        if (model != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClickListener(position, model);
                }
            });

            holder.tvnUserName.setText(model.getFullName());
            holder.tvnTime.setText(model.getCreateAt());

            ImageLoader.getInstance().displayImage(model.getUserAvatar(), holder.imvAvatar, ImageUtil.optionsImageAvatar);

            String comments = model.getCommentContent();
            ArrayList<int[]> hashtagSpans = getSpans(comments, '#');
            ArrayList<int[]> calloutSpans = getSpans(comments, '@');

            SpannableString commentsContent =
                    new SpannableString(comments);

            for (int i = 0; i < hashtagSpans.size(); i++) {
                int[] span = hashtagSpans.get(i);
                int hashTagStart = span[0];
                int hashTagEnd = span[1];

                commentsContent.setSpan(new Hashtag(mContext),
                        hashTagStart,
                        hashTagEnd, 0);
            }

            for (int i = 0; i < calloutSpans.size(); i++) {
                int[] span = calloutSpans.get(i);
                int calloutStart = span[0];
                int calloutEnd = span[1];

                commentsContent.setSpan(new CalloutLink(mContext),
                        calloutStart,
                        calloutEnd, 0);
            }

            holder.tvnContent.setMovementMethod(LinkMovementMethod.getInstance());
            holder.tvnContent.setText(commentsContent);

        }
    }

    public ArrayList<int[]> getSpans(String body, char prefix) {
        ArrayList<int[]> spans = new ArrayList<int[]>();

        Pattern pattern = Pattern.compile(prefix + "\\w+");
        Matcher matcher = pattern.matcher(body);

        // Check all occurrences
        while (matcher.find()) {
            int[] currentSpan = new int[2];
            currentSpan[0] = matcher.start();
            currentSpan[1] = matcher.end();
            spans.add(currentSpan);
        }

        return spans;
    }

    public void insertFirst(CommentModel data) {
        mItems.add(0, data);
        notifyDataSetChanged();
    }

    public void insert(List<CommentModel> listData) {
        mItems.addAll(listData);
        notifyDataSetChanged();
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getAdapterItemCount() {
        return mItems.size();
    }

    @Override
    public HolderListCell newFooterHolder(View view) {
        return null;
    }

    @Override
    public HolderListCell newHeaderHolder(View view) {
        return null;
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

    }

    public class HolderListCell extends UltimateRecyclerviewViewHolder {
        @BindView(R.id.tvnContent)
        TextView tvnContent;

        @BindView(R.id.tvnUserName)
        TextView tvnUserName;

        @BindView(R.id.tvnTime)
        TextView tvnTime;

        @BindView(R.id.imvAvatar)
        ImageView imvAvatar;

        public HolderListCell(View itemView, boolean isItem) {
            super(itemView);

            if (isItem) {
                ButterKnife.bind(this, itemView);
            }
        }
    }

    public CommentModel getItem(int position) {
        if (customHeaderView != null)
            position--;
        if (position < mItems.size())
            return mItems.get(position);
        else return null;
    }
}