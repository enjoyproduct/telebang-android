package com.neo2.telebang.base;

import android.os.Bundle;

/**
 * Created by Billy on 11/2/16.
 */

public abstract class BaseAppTabFragment extends StdFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setSelectedFragment() {

    }

    @Override
    public String getTagText() {
        return "";
    }
}
