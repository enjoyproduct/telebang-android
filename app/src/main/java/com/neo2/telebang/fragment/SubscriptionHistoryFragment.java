package com.neo2.telebang.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.neo2.telebang.R;
import com.neo2.telebang.adapter.ListVideoAdapter1;
import com.neo2.telebang.api.APIResponseListener;
import com.neo2.telebang.api.RPC;
import com.neo2.telebang.base.BaseAppSlideFragment;
import com.neo2.telebang.fragment.home_slide.HomeFragment_2;
import com.neo2.telebang.fragment.home_slide.HomeFragment_3;
import com.neo2.telebang.helper.DialogUtil;
import com.neo2.telebang.helper.TimeUtil;
import com.neo2.telebang.manager.CustomerManager;
import com.neo2.telebang.model.SubscriptionJSON;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubscriptionHistoryFragment extends BaseAppSlideFragment {
    public static final String TAG = SubscriptionHistoryFragment.class.getSimpleName();

    ListView listView;
    ArrayList<SubscriptionJSON> arrayList;
    SubscriptionAdapter adapter;

    AVLoadingIndicatorView avloadingIndicatorView;

    public static SubscriptionHistoryFragment newInstance() {
        SubscriptionHistoryFragment fragment = new SubscriptionHistoryFragment();
        return fragment;
    }

    @Override
    public String getTagText() {
        return TAG;
    }


    @Override
    public int getLayout() {
        return R.layout.fragment_subscription_history;
    }

    @Override
    public void onInitView() {
        arrayList = new ArrayList<>();

        getSubscriptions();
        avloadingIndicatorView = (AVLoadingIndicatorView)getView().findViewById(R.id.avloadingIndicatorView);

        listView = (ListView) getView().findViewById(R.id.listview);
        adapter = new SubscriptionAdapter();
        listView.setAdapter(adapter);

    }
    @OnClick(R.id.imvHeaderMenu)
    void doMenu() {
        mAppActivity.toggleDrawer();
    }
    void getSubscriptions() {

        startAnimLoading();
        RPC.getSubscriptions(CustomerManager.getInstance().getAccountID(), new APIResponseListener() {
            @Override
            public void onError(String message) {
                stopAnimLoading();
                DialogUtil.showMessageBox(getActivity(), message);
            }

            @Override
            public void onSuccess(Object results) {
                stopAnimLoading();
                arrayList = (ArrayList<SubscriptionJSON>) results;
                adapter.notifyDataSetChanged();
            }
        });
    }
    class SubscriptionAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = getActivity().getLayoutInflater().inflate(R.layout.item_subscription, null);
            }
            TextView tvTime = (TextView) view.findViewById(R.id.tv_time);
            TextView tvAmount = (TextView) view.findViewById(R.id.tv_amount);
            TextView tvCardNumber = (TextView) view.findViewById(R.id.tv_card_number);

            SubscriptionJSON subscriptionJSON = (SubscriptionJSON) getItem(position);
            tvTime.setText(TimeUtil.getDateFormat(subscriptionJSON.time));
            tvAmount.setText(subscriptionJSON.amount);
            tvCardNumber.setText(subscriptionJSON.card_number);
            return view;
        }
    }
    void startAnimLoading() {
        if (avloadingIndicatorView != null)
            avloadingIndicatorView.smoothToShow();
    }

    void stopAnimLoading() {
        if (avloadingIndicatorView != null && avloadingIndicatorView.isShown())
            avloadingIndicatorView.smoothToHide();


    }
}
