package com.neo2.telebang.base;

import android.os.Bundle;

import com.inspius.coreapp.InspiusHostActivityInterface;

/**
 * Created by Billy on 11/2/16.
 */

public abstract class BaseTabFragment extends StdFragment {
    protected InspiusHostActivityInterface mHostTab;

    @Override
    public String getTagText() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!(this.getParentFragment() instanceof InspiusHostActivityInterface)) {
            throw new ClassCastException("Parent Fragment must implement InspiusHostActivityInterface");
        } else {
            this.mHostTab = (InspiusHostActivityInterface) this.getParentFragment();
        }
    }
}
