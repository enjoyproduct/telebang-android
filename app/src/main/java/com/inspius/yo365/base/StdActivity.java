package com.inspius.yo365.base;

import android.os.Bundle;

import com.inspius.coreapp.InspiusActivity;

/**
 * Created by Billy on 11/2/16.
 */

public abstract class StdActivity extends InspiusActivity implements StdActivityInterface {
    StdActivityInterface baseActivityInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        baseActivityInterface = new StdActivityImplement(this);
    }

    @Override
    public void showLoading(String message) {
        baseActivityInterface.showLoading(message);
    }

    @Override
    public void hideLoading() {
        baseActivityInterface.hideLoading();
    }

    @Override
    public void hideKeyBoard() {
        baseActivityInterface.hideKeyBoard();
    }
}
