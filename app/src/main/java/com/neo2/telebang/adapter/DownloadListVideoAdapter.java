package com.neo2.telebang.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.neo2.telebang.R;
import com.neo2.telebang.app.GlobalApplication;
import com.neo2.telebang.greendao.DBVideoDownload;
import com.neo2.telebang.listener.AdapterDownloadActionListener;
import com.marshalchen.ultimaterecyclerview.SwipeableUltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DownloadListVideoAdapter extends SwipeableUltimateViewAdapter<DBVideoDownload> {
    private List<DBVideoDownload> mItems;
    private AdapterDownloadActionListener listener;

    public DownloadListVideoAdapter(List<DBVideoDownload> mData) {
        super(mData);
        this.mItems = mData;
    }

    public void setAdapterActionListener(AdapterDownloadActionListener listener) {
        this.listener = listener;
    }

    @Override
    protected void withBindHolder(UltimateRecyclerviewViewHolder holder, final DBVideoDownload model, final int position) {
        super.withBindHolder(holder, model, position);
        HolderListCell cell = (HolderListCell) holder;
        cell.tvnName.setText(model.getVideoName());
        cell.tvnCreateAt.setText(model.getVideoCreateAt());
        cell.tvnCategory.setText(model.getVideoCategory());

        Glide.with(GlobalApplication.getAppContext()).load(model.getVideoPath()).placeholder(R.drawable.no_image_default).into(cell.imvThumbnail);
        Glide.with(GlobalApplication.getAppContext()).load(model.getVideoPath()).placeholder(R.drawable.no_image_default).into(cell.trash);
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
    protected int getNormalLayoutResId() {
        return HolderListCell.layout;
    }

    @Override
    protected UltimateRecyclerviewViewHolder newViewHolder(View view) {
        final HolderListCell viewHolder = new HolderListCell(view, true);

        viewHolder.linearItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onItemClickListener(viewHolder.getLayoutPosition());
            }
        });

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onItemDeleteClickListener(viewHolder.getLayoutPosition());
            }
        });
        return viewHolder;
    }

    public class HolderListCell extends UltimateRecyclerviewViewHolder {
        public static final int layout = R.layout.item_video_download;

        @BindView(R.id.imvThumbnail)
        ImageView imvThumbnail;

        @BindView(R.id.tvnName)
        TextView tvnName;

        @BindView(R.id.tvnCreateAt)
        TextView tvnCreateAt;

        @BindView(R.id.tvnCategory)
        TextView tvnCategory;

        @BindView(R.id.delete)
        Button delete;

        @BindView(R.id.linear_item)
        LinearLayout linearItem;

        @BindView(R.id.trash)
        ImageView trash;

        public HolderListCell(View itemView, boolean isItem) {
            super(itemView);

            if (isItem)
                ButterKnife.bind(this, itemView);
        }
    }

    public DBVideoDownload getItem(int position) {
        if (customHeaderView != null)
            position--;
        if (position < mItems.size())
            return mItems.get(position);
        else return null;
    }
}