package com.neo2.telebang.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.neo2.telebang.R;
import com.neo2.telebang.helper.ImageUtil;
import com.neo2.telebang.listener.AdapterVideoActionListener;
import com.neo2.telebang.model.VideoModel;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListVideoAdapter1 extends UltimateViewAdapter<ListVideoAdapter1.HolderListCell> {
    private List<VideoModel> mItems;
    private AdapterVideoActionListener listener;

    public ListVideoAdapter1() {
        this.mItems = new ArrayList<>();
    }

    public void setAdapterActionListener(AdapterVideoActionListener listener) {
        this.listener = listener;
    }

    @Override
    public HolderListCell onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video_list_1, parent, false);
        HolderListCell vh = new HolderListCell(v, true);
        return vh;
    }

    @Override
    public void onBindViewHolder(final HolderListCell holder, final int position) {
        final VideoModel model = getItem(position);
        if (model != null) {
            holder.tvnName.setText(model.getTitle());
            holder.tvnView.setText(model.getViewCounterStringFormat());
            holder.tvnTime.setText(model.getTimeRemain());
            holder.tvnCreateAt.setText(model.getUpdateAt());
            holder.tvnCategory.setText(model.getCategoryName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClickListener(model);
                }
            });

            holder.imvPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onPlayClickListener(model);
                }
            });

            ImageLoader.getInstance().displayImage(model.getThumbnail(), holder.imvThumbnail, ImageUtil.optionsImageDefault);
        }
    }

    public void add(List<VideoModel> listData) {
//        mItems.addAll(listData);
//        notifyDataSetChanged();
        insertInternal(listData, mItems);
    }

    public void clear() {
//        mItems.clear();
//        notifyDataSetChanged();
        clearInternal(mItems);
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
        @BindView(R.id.imvThumbnail)
        ImageView imvThumbnail;

        @BindView(R.id.imvPlay)
        ImageView imvPlay;

        @BindView(R.id.tvnName)
        TextView tvnName;

        @BindView(R.id.tvnView)
        TextView tvnView;

        @BindView(R.id.tvnTime)
        TextView tvnTime;

        @BindView(R.id.tvnCategory)
        TextView tvnCategory;

        @BindView(R.id.tvnCreateAt)
        TextView tvnCreateAt;

        public HolderListCell(View itemView, boolean isItem) {
            super(itemView);

            if (isItem)
                ButterKnife.bind(this, itemView);
        }
    }

    public VideoModel getItem(int position) {
        if (customHeaderView != null)
            position--;
        if (position < mItems.size())
            return mItems.get(position);
        else return null;
    }
}