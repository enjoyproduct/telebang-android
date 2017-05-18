package com.neo2.telebang.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.neo2.telebang.R;
import com.neo2.telebang.helper.ImageUtil;
import com.neo2.telebang.listener.AdapterActionListener;
import com.neo2.telebang.model.CategoryJSON;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListTopCategoryAdapter extends UltimateViewAdapter<ListTopCategoryAdapter.HolderGirdCell> {
    private List<CategoryJSON> mItems;
    AdapterActionListener listener;

    public ListTopCategoryAdapter() {
        this.mItems = new ArrayList<>();
    }

    public void setAdapterActionListener(AdapterActionListener listener) {
        this.listener = listener;
    }

    @Override
    public HolderGirdCell onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_top_category, parent, false);
        HolderGirdCell vh = new HolderGirdCell(v, true);
        return vh;
    }

    @Override
    public void onBindViewHolder(final HolderGirdCell holder, final int position) {
        final CategoryJSON model = getItem(position);
        if (model != null) {
            holder.tvnCategoryName.setText(model.name);
            ImageLoader.getInstance().displayImage(model.image, holder.imvCategory, ImageUtil.optionsImageDefault);

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
        @BindView(R.id.imv_category)
        ImageView imvCategory;

        @BindView(R.id.tvnCategoryName)
        TextView tvnCategoryName;

        public HolderGirdCell(View itemView, boolean isItem) {
            super(itemView);

            if (isItem) {
                ButterKnife.bind(this, itemView);
            }
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