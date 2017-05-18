package com.neo2.telebang.fragment.customer;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.neo2.telebang.R;
import com.neo2.telebang.api.APIResponseListener;
import com.neo2.telebang.base.BaseAppSlideFragment;
import com.neo2.telebang.helper.DialogUtil;
import com.neo2.telebang.helper.Validation;
import com.neo2.telebang.model.CustomerJSON;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChangeProfileFragment extends BaseAppSlideFragment {
    public static final String TAG = ChangeProfileFragment.class.getSimpleName();

    public static ChangeProfileFragment newInstance() {
        ChangeProfileFragment fragment = new ChangeProfileFragment();
        return fragment;
    }

    @BindView(R.id.tvnHeaderTitle)
    TextView tvnHeaderTitle;

    @BindView(R.id.edtFirstName)
    EditText edtFirstName;

    @BindView(R.id.edtLastName)
    EditText edtLastName;

    @BindView(R.id.edtEmail)
    EditText edtEmail;

    @BindView(R.id.edtPhone)
    EditText edtPhone;

    @BindView(R.id.edtAddress)
    EditText edtAddress;

    @BindView(R.id.edtCity)
    EditText edtCity;

    @BindView(R.id.edtCountry)
    EditText edtCountry;

    @BindView(R.id.edtZip)
    EditText edtZip;

    @Override
    public int getLayout() {
        return R.layout.fragment_customer_change_profile;
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public void onInitView() {
        tvnHeaderTitle.setText(getString(R.string.change_profile));

        CustomerJSON customerInfo = mCustomerManager.getCustomerJSON();
        if (customerInfo == null)
            return;

        if (!TextUtils.isEmpty(customerInfo.email))
            edtEmail.setText(customerInfo.email);

        if (!TextUtils.isEmpty(customerInfo.firstName))
            edtFirstName.setText(customerInfo.firstName);

        if (!TextUtils.isEmpty(customerInfo.lastName))
            edtLastName.setText(customerInfo.lastName);


        if (!TextUtils.isEmpty(customerInfo.phone))
            edtPhone.setText(customerInfo.phone);

        if (!TextUtils.isEmpty(customerInfo.address))
            edtAddress.setText(customerInfo.address);

        if (!TextUtils.isEmpty(customerInfo.city))
            edtCity.setText(customerInfo.city);

        if (!TextUtils.isEmpty(customerInfo.country))
            edtCountry.setText(customerInfo.country);

        if (!TextUtils.isEmpty(customerInfo.zip))
            edtZip.setText(customerInfo.zip);
    }

    @OnClick(R.id.imvHeaderBack)
    void doBack() {
        onBackPressed();
    }

    @OnClick(R.id.btnSubmit)
    void doSubmit() {
        CustomerJSON customerUpdate = new CustomerJSON();
        customerUpdate.firstName = edtFirstName.getText().toString();
        customerUpdate.lastName = edtLastName.getText().toString();
        customerUpdate.email = edtEmail.getText().toString();
        customerUpdate.phone = edtPhone.getText().toString();
        customerUpdate.city = edtCity.getText().toString();
        customerUpdate.country = edtCountry.getText().toString();
        customerUpdate.zip = edtZip.getText().toString();
        customerUpdate.address = edtAddress.getText().toString();

        String validMessage = Validation.isValidEmail(customerUpdate.email);
        if (!TextUtils.isEmpty(validMessage)) {
            DialogUtil.showMessageErrorForm(mContext, validMessage);
            return;
        }

        mAppActivity.showLoading(getString(R.string.msg_loading_content));
        mCustomerManager.callChangeProfile(customerUpdate, new APIResponseListener() {
            @Override
            public void onError(String message) {
                mAppActivity.hideLoading();
                DialogUtil.showMessageBox(getContext(), message);
            }

            @Override
            public void onSuccess(Object results) {
                mAppActivity.hideLoading();

                DialogUtil.showMessageBox(getContext(), getString(R.string.msg_update_acc_success), false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
            }
        });
    }
}
