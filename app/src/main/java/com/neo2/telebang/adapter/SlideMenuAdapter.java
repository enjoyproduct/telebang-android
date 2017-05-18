package com.neo2.telebang.adapter;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.neo2.telebang.R;
import com.neo2.telebang.listener.AdapterActionListener;
import com.neo2.telebang.model.MenuModel;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SlideMenuAdapter extends UltimateViewAdapter<SlideMenuAdapter.ViewHolder> {
    private List<MenuModel> slideMenuList;
    AdapterActionListener listener;

    public SlideMenuAdapter(List<MenuModel> stringList, AdapterActionListener listener) {
        this.slideMenuList = stringList;
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MenuModel slideMenuModel = getItem(position);
        if (slideMenuModel != null) {
            holder.tvnMenuTitle.setText(slideMenuModel.title);

            ImageLoader.getInstance().displayImage(slideMenuModel.getIconPath(), holder.imvMenuIcon);

            if (slideMenuModel.showLine)
                holder.viewLine.setVisibility(View.VISIBLE);
            else
                holder.viewLine.setVisibility(View.GONE);

//            if (slideMenuModel.isActive)
//                holder.onItemSelected();
//            else
//                holder.onItemClear();
            holder.getView().setSelected(slideMenuModel.isActive);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClickListener(position, slideMenuModel);
                }
            });
        }
    }

    @Override
    public int getAdapterItemCount() {
        return slideMenuList.size();
    }

    @Override
    public ViewHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public ViewHolder newHeaderHolder(View view) {
        return null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_slide_menu, parent, false);
        ViewHolder vh = new ViewHolder(v, true);
        return vh;
    }


    @Override
    public long generateHeaderId(int position) {
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        super.onItemMove(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        super.onItemDismiss(position);
    }

    public class ViewHolder extends UltimateRecyclerviewViewHolder {
        @BindView(R.id.tvnMenuTitle)
        TextView tvnMenuTitle;

        @BindView(R.id.imvMenuIcon)
        ImageView imvMenuIcon;

        @BindView(R.id.viewLine)
        View viewLine;

        public ViewHolder(View itemView, boolean isItem) {
            super(itemView);

            if (isItem)
                ButterKnife.bind(this, itemView);
        }

        @Override
        public void onItemSelected() {
//            itemView.setBackgroundColor(Color.parseColor("#2b343d"));
//            viewLine.setBackgroundColor(Color.parseColor("#cc3333"));
//            tvnMenuTitle.setTypeface(null, Typeface.BOLD);
            getView().setSelected(true);
        }

        @Override
        public void onItemClear() {
            getView().setSelected(false);
//            tvnMenuTitle.setTypeface(null, Typeface.NORMAL);
//            itemView.setBackgroundColor(Color.TRANSPARENT);
//            viewLine.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public MenuModel getItem(int position) {
        if (position < slideMenuList.size())
            return slideMenuList.get(position);
        else
            return null;
    }
}