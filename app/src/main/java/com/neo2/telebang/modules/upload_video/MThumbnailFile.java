package com.neo2.telebang.modules.upload_video;

import android.graphics.Bitmap;

/**
 * Created by Billy on 1/12/17.
 */

public class MThumbnailFile {
    private String mimeType;
    private String name;
    private Bitmap bitmap;

    public MThumbnailFile(String name, String mimeType, Bitmap bitmap) {
        this.mimeType = mimeType;
        this.name = name;
        this.bitmap = bitmap;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getName() {
        return name;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
