package com.neo2.telebang.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.neo2.telebang.R;
import com.neo2.telebang.helper.ImageUtil;
import com.neo2.telebang.listener.AdapterActionListener;
import com.neo2.telebang.model.SeriesJSON;
import com.marshalchen.ultimaterecyclerview.UltimateGridLayoutAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GridSeriesAdapter extends UltimateGridLayoutAdapter<SeriesJSON, GridSeriesAdapter.HolderGirdCell> {
    private List<SeriesJSON> mItems;
    AdapterActionListener listener;

    public GridSeriesAdapter(List<SeriesJSON> items) {
        super(items);
        this.mItems = items;
    }

    public void setAdapterActionListener(AdapterActionListener listener) {
        this.listener = listener;
    }

    public void clear() {
        removeAll();
//        for (int i = getAdapterItemCount() - 1; i > 0; i--)
//            removeAt(i);
    }

    @Override
    protected void bindNormal(GridSeriesAdapter.HolderGirdCell holder, final SeriesJSON model, final int position) {
        if (model != null) {
            holder.tvnName.setText(model.title);

            ImageLoader.getInstance().displayImage(model.thumbnail, holder.imvThumbnail, ImageUtil.optionsImageDefault);
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
        return R.layout.item_series;
    }

    @Override
    protected GridSeriesAdapter.HolderGirdCell newViewHolder(View view) {
        return new HolderGirdCell(view, true);
    }

    @Override
    protected void withBindHolder(GridSeriesAdapter.HolderGirdCell holder, SeriesJSON data, int position) {

    }

    public SeriesJSON getItem(int position) {
        if (customHeaderView != null)
            position--;

        if (position < mItems.size())
            return mItems.get(position);

        else return null;
    }

    public class HolderGirdCell extends UltimateRecyclerviewViewHolder {
        @BindView(R.id.tvnName)
        TextView tvnName;

        @BindView(R.id.imvThumbnail)
        ImageView imvThumbnail;

        public HolderGirdCell(View itemView, boolean isItem) {
            super(itemView);

            if (isItem) {
                ButterKnife.bind(this, itemView);
            }
        }
    }
}