package com.neo2.telebang.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.neo2.telebang.R;
import com.neo2.telebang.api.APIResponseListener;
import com.neo2.telebang.api.RPC;
import com.neo2.telebang.app.AppConstant;
import com.neo2.telebang.helper.DialogUtil;
import com.neo2.telebang.helper.TimeUtil;
import com.neo2.telebang.manager.CustomerManager;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import butterknife.BindView;
import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.exceptions.ExpiredAccessCodeException;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class SubscriptionActivity extends AppCompatActivity {
    EditText mEditCardNum;
    EditText mEditCVC;
    EditText mEditExpiryMonth;
    EditText mEditExpiryYear;

    Card card;
    private Charge charge;
    private Transaction transaction;

    AVLoadingIndicatorView avloadingIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        mEditCardNum = (EditText)findViewById(R.id.edit_card_number);
        mEditCVC = (EditText)findViewById(R.id.edit_cvc);
        mEditExpiryMonth = (EditText)findViewById(R.id.edit_expiry_month);
        mEditExpiryYear = (EditText)findViewById(R.id.edit_expiry_year);
        avloadingIndicatorView = (AVLoadingIndicatorView)findViewById(R.id.avloadingIndicatorView);

        Button mButtonPerformTransaction = (Button) findViewById(R.id.button_perform_transaction);
        //set click listener
        mButtonPerformTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validate form
                validateCardForm();
                //check card validity
                if (card != null && card.isValid()) {
                    startAnimLoading();

                    try {
                        startAFreshCharge();
                    } catch (Exception e) {
                        DialogUtil.showMessageBox(SubscriptionActivity.this, String.format("An error occured hwile charging card: %s %s", e.getClass().getSimpleName(), e.getMessage()));
                    }

                }
            }
        });
    }
    private void startAFreshCharge() {
        // initialize the charge
        charge = new Charge();
        // Perform transaction/initialize on our server to get an access code
        // documentation: https://developers.paystack.co/reference#initialize-a-transaction

        new fetchAccessCodeFromServer().execute(AppConstant.PAYSTACK_BACKEND_URL+"/new-access-code");

    }

    /**
     * Method to validate the form, and set errors on the edittexts.
     */
    private void validateCardForm() {
        //validate fields
        String cardNum = mEditCardNum.getText().toString().trim();

        if (isEmpty(cardNum)) {
            mEditCardNum.setError("Empty card number");
            return;
        }

        //build card object with ONLY the number, update the other fields later
        card = new Card.Builder(cardNum, 0, 0, "").build();
        if (!card.validNumber()) {
            mEditCardNum.setError("Invalid card number");
            return;
        }

        //validate cvc
        String cvc = mEditCVC.getText().toString().trim();
        if (isEmpty(cvc)) {
            mEditCVC.setError("Empty cvc");
            return;
        }
        //update the cvc field of the card
        card.setCvc(cvc);

        //check that it's valid
        if (!card.validCVC()) {
            mEditCVC.setError("Invalid cvc");
            return;
        }

        //validate expiry month;
        String sMonth = mEditExpiryMonth.getText().toString().trim();
        int month = -1;
        try {
            month = Integer.parseInt(sMonth);
        } catch (Exception ignored) {
        }

        if (month < 1) {
            mEditExpiryMonth.setError("Invalid month");
            return;
        }

        card.setExpiryMonth(month);

        String sYear = mEditExpiryYear.getText().toString().trim();
        int year = -1;
        try {
            year = Integer.parseInt(sYear);
        } catch (Exception ignored) {
        }

        if (year < 1) {
            mEditExpiryYear.setError("invalid year");
            return;
        }

        card.setExpiryYear(year);

        //validate expiry
        if (!card.validExpiryDate()) {
            mEditExpiryMonth.setError("Invalid expiry");
            mEditExpiryYear.setError("Invalid expiry");
        }
    }
    private void chargeCard() {
        transaction = null;
        PaystackSdk.chargeCard(this, charge, new Paystack.TransactionCallback() {
            // This is called only after transaction is successful
            @Override
            public void onSuccess(Transaction transaction) {
//                stopAnimLoading();

                SubscriptionActivity.this.transaction = transaction;
                Toast.makeText(SubscriptionActivity.this, transaction.getReference(), Toast.LENGTH_LONG).show();
                new verifyOnServer().execute(transaction.getReference());
                updateSubscribe("");
            }

            // This is called only before requesting OTP
            // Save reference so you may send to server if
            // error occurs with OTP
            // No need to dismiss dialog
            @Override
            public void beforeValidate(Transaction transaction) {
                SubscriptionActivity.this.transaction = transaction;
                Toast.makeText(SubscriptionActivity.this, transaction.getReference(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                // If an access code has expired, simply ask your server for a new one
                // and restart the charge instead of displaying error
                SubscriptionActivity.this.transaction = transaction;
                if (error instanceof ExpiredAccessCodeException) {
                    SubscriptionActivity.this.startAFreshCharge();
                    SubscriptionActivity.this.chargeCard();
                    return;
                }

                stopAnimLoading();

                if (transaction.getReference() != null) {
                    Toast.makeText(SubscriptionActivity.this, transaction.getReference() + " concluded with error: " + error.getMessage(), Toast.LENGTH_LONG).show();
//                    mTextError.setText(String.format("%s  concluded with error: %s %s", transaction.getReference(), error.getClass().getSimpleName(), error.getMessage()));
                    new verifyOnServer().execute(transaction.getReference());
                } else {
                    Toast.makeText(SubscriptionActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
//                    mTextError.setText(String.format("Error: %s %s", error.getClass().getSimpleName(), error.getMessage()));
                }
            }

        });
    }
    private class fetchAccessCodeFromServer extends AsyncTask<String, Void, String> {
        private String error;

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                charge.setAccessCode(result);
                charge.setCard(card);
                chargeCard();
            } else {
//                SubscriptionActivity.this.mTextBackendMessage.setText(String.format("There was a problem getting a new access code form the backend: %s", error));
                DialogUtil.showMessageBox(SubscriptionActivity.this, error);
                stopAnimLoading();
            }
        }

        @Override
        protected String doInBackground(String... ac_url) {
            try {
                URL url = new URL(ac_url[0]);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                url.openStream()));

                String inputLine;
                inputLine = in.readLine();
                in.close();
                return inputLine;
            } catch (Exception e) {
                error = e.getClass().getSimpleName() + ": " + e.getMessage();
            }
            return null;
        }
    }

    private class verifyOnServer extends AsyncTask<String, Void, String> {
        private String reference;
        private String error;

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
//                SubscriptionActivity.this.mTextBackendMessage.setText(String.format("Gateway response: %s", result));
                    DialogUtil.showMessageBox(SubscriptionActivity.this, result);
            } else {
//                SubscriptionActivity.this.mTextBackendMessage.setText(String.format("There was a problem verifying %s on the backend: %s ", this.reference, error));
                DialogUtil.showMessageBox(SubscriptionActivity.this, error);
                stopAnimLoading();
            }
        }

        @Override
        protected String doInBackground(String... reference) {
            try {
                this.reference = reference[0];
                URL url = new URL(AppConstant.PAYSTACK_BACKEND_URL + "/verify/" + this.reference);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                url.openStream()));

                String inputLine;
                inputLine = in.readLine();
                in.close();
                return inputLine;
            } catch (Exception e) {
                error = e.getClass().getSimpleName() + ": " + e.getMessage();
            }
            return null;
        }
    }
    void updateSubscribe(String paystack_auth_code) {
        CustomerManager.getInstance().callUpdateSubscription(paystack_auth_code, TimeUtil.getCurrentTimeStamp(), new APIResponseListener() {
            @Override
            public void onError(String message) {
                stopAnimLoading();
//                DialogUtil.showMessageBox(SubscriptionActivity.this, message);
            }

            @Override
            public void onSuccess(Object results) {
                stopAnimLoading();
                setResult(RESULT_OK);
                finish();
            }
        });
    }
    void startAnimLoading() {
        if (avloadingIndicatorView != null)
            avloadingIndicatorView.smoothToShow();
    }

    void stopAnimLoading() {
        if (avloadingIndicatorView != null && avloadingIndicatorView.isShown())
            avloadingIndicatorView.smoothToHide();

    }
    private boolean isEmpty(String s) {
        return s == null || s.length() < 1;
    }
}
