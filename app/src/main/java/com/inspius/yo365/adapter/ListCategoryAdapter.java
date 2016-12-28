package com.inspius.yo365.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inspius.yo365.R;
import com.inspius.yo365.listener.AdapterActionListener;
import com.inspius.yo365.model.CategoryJSON;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListCategoryAdapter extends UltimateViewAdapter<ListCategoryAdapter.HolderGirdCell> {
    private List<CategoryJSON> mItems;
    AdapterActionListener listener;

    private DisplayImageOptions options;

    public ListCategoryAdapter() {
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
    public HolderGirdCell onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        HolderGirdCell vh = new HolderGirdCell(v, true);
        return vh;
    }

    @Override
    public void onBindViewHolder(final HolderGirdCell holder, final int position) {
        final CategoryJSON model = getItem(position);
        if (model != null) {
            holder.tvnName.setText(model.name);

            ImageLoader.getInstance().displayImage(model.image, holder.imvBackground, options);
            ImageLoader.getInstance().displayImage(model.icon, holder.imvIcon, options);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClickListener(position, model);
                }
            });
        }
    }

    public void add(List<CategoryJSON> listData) {
        mItems.addAll(listData);
        notifyDataSetChanged();
    }

    public void insert(CategoryJSON model) {
        insertInternal(mItems, model, getAdapterItemCount());
    }

    public void clear() {
        clearInternal(mItems);
    }

    @Override
    public int getAdapterItemCount() {
        return mItems.size();
    }

    @Override
    public HolderGirdCell newFooterHolder(View view) {
        return null;
    }

    @Override
    public HolderGirdCell newHeaderHolder(View view) {
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

    public class HolderGirdCell extends UltimateRecyclerviewViewHolder {
        @BindView(R.id.imvBackground)
        ImageView imvBackground;

        @BindView(R.id.imvIcon)
        ImageView imvIcon;

        @BindView(R.id.tvnName)
        TextView tvnName;

        public HolderGirdCell(View itemView, boolean isItem) {
            super(itemView);

            if (isItem)
                ButterKnife.bind(this, itemView);
        }
    }

    public CategoryJSON getItem(int position) {
        if (customHeaderView != null)
            position--;
        if (position < mItems.size())
            return mItems.get(position);
        else return null;
    }
}