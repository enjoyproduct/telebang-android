package com.neo2.telebang.base;

/**
 * Created by Billy on 12/9/15.
 */
public interface BaseLoginActivityInterface extends StdActivityInterface {
    void updateHeaderTitle(String headerTitle);

    void onLoginSuccess();

    void onCancelLogin();
}
