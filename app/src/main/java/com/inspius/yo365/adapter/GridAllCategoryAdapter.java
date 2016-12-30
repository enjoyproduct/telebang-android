package com.inspius.yo365.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inspius.yo365.R;
import com.inspius.yo365.helper.ImageUtil;
import com.inspius.yo365.listener.AdapterActionListener;
import com.inspius.yo365.model.CategoryJSON;
import com.marshalchen.ultimaterecyclerview.UltimateGridLayoutAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GridAllCategoryAdapter extends UltimateGridLayoutAdapter<CategoryJSON, GridAllCategoryAdapter.HolderGirdCell> {
    private List<CategoryJSON> mItems;
    AdapterActionListener listener;


    public GridAllCategoryAdapter(List<CategoryJSON> items) {
        super(items);

        this.mItems = items;
    }

    public void setAdapterActionListener(AdapterActionListener listener) {
        this.listener = listener;
    }

    public void insert(CategoryJSON model) {
        insertInternal(mItems, model, getAdapterItemCount());
    }

    @Override
    protected void bindNormal(HolderGirdCell holder, CategoryJSON categoryJSON, final int position) {
        final CategoryJSON model = getItem(position);
        if (model != null) {
            holder.tvnName.setText(model.name);
//            float[] direction = new float[]{0.0f, -1.0f, 0.5f};
//            MaskFilter filter = new EmbossMaskFilter(direction, 0.8f, 15f, 1f);
//
//            holder.tvnName.getPaint().setMaskFilter(filter);

            ImageLoader.getInstance().displayImage(model.image, holder.imvBackground, ImageUtil.optionsImageDefault);
            ImageLoader.getInstance().displayImage(model.icon, holder.imvIcon, ImageUtil.optionsImageDefault);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClickListener(position, model);
                }
            });
        }
    }

    @Override
    protected int getNormalLayoutResId() {
        return R.layout.item_category;
    }

    @Override
    protected HolderGirdCell newViewHolder(View view) {
        return new HolderGirdCell(view, true);
    }

    @Override
    protected void withBindHolder(HolderGirdCell holder, CategoryJSON data, int position) {

    }

    public CategoryJSON getItem(int position) {
        if (customHeaderView != null)
            position--;
        if (position < mItems.size())
            return mItems.get(position);
        else return null;
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
}