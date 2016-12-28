package com.inspius.yo365.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inspius.yo365.R;
import com.inspius.yo365.greendao.DBWishListVideo;
import com.inspius.yo365.listener.AdapterActionListener;
import com.inspius.yo365.listener.AdapterVideoActionListener;
import com.inspius.yo365.model.VideoModel;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WishListVideoAdapter extends UltimateViewAdapter<WishListVideoAdapter.HolderListCell> {
    private List<DBWishListVideo> mItems;
    private AdapterActionListener listener;

    private DisplayImageOptions options;

    public WishListVideoAdapter() {
        this.mItems = new ArrayList<>();

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.no_image_default)
                .showImageForEmptyUri(R.drawable.no_image_default)
                .showImageOnFail(R.drawable.no_image_default)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
    }

    public void setAdapterActionListener(AdapterActionListener listener) {
        this.listener = listener;
    }

    @Override
    public HolderListCell onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wish_list, parent, false);
        HolderListCell vh = new HolderListCell(v, true);
        return vh;
    }

    @Override
    public void onBindViewHolder(final HolderListCell holder, final int position) {
        final DBWishListVideo model = getItem(position);
        if (model != null) {
            holder.tvnName.setText(model.getVideoName());
            holder.tvnCreateAt.setText(model.getVideoCreateAt());
            holder.tvnCategory.setText(model.getVideoCategory());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClickListener(position, model);
                }
            });

            ImageLoader.getInstance().displayImage(model.getVideoThumbnail(), holder.imvThumbnail, options);
        }
    }

    public void add(List<DBWishListVideo> listData) {
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
        @BindView(R.id.imvThumbnail)
        ImageView imvThumbnail;

        @BindView(R.id.tvnName)
        TextView tvnName;

        @BindView(R.id.tvnCreateAt)
        TextView tvnCreateAt;

        @BindView(R.id.tvnCategory)
        TextView tvnCategory;

        public HolderListCell(View itemView, boolean isItem) {
            super(itemView);

            if (isItem)
                ButterKnife.bind(this, itemView);
        }
    }

    public DBWishListVideo getItem(int position) {
        if (customHeaderView != null)
            position--;
        if (position < mItems.size())
            return mItems.get(position);
        else return null;
    }
}