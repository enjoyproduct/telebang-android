package com.neo2.telebang.helper;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.neo2.telebang.R;
import com.neo2.telebang.model.ImageFileModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;

/**
 * Created by Admin on 15/4/2016.
 */
public class ImageUtil {
    public static DisplayImageOptions optionsImageAvatar = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.img_avatar_default)
            .showImageForEmptyUri(R.drawable.img_avatar_default)
            .showImageOnFail(R.drawable.img_avatar_default)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.EXACTLY)
            .build();

    public static DisplayImageOptions optionsImageDefault = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.no_image_default)
            .showImageForEmptyUri(R.drawable.no_image_default)
            .showImageOnFail(R.drawable.no_image_default)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.EXACTLY)
            .build();

    public static ImageFileModel getImageFileFromUri(Context context, Uri selectedImage) {

        String mimeType;
        String picturePath;

        picturePath = getRealPathFromURI(context, selectedImage);
        if (TextUtils.isEmpty(picturePath))
            return null;

        File file = new File(picturePath);

        if (file == null)
            return null;

        // image info
        mimeType = getContentTypeFromFileString(file);
        if (TextUtils.isEmpty(mimeType))
            mimeType = "image/jpeg";

        return new ImageFileModel(file, mimeType);
    }

    public static String getContentTypeFromFileString(File file) {
        String type = null;
        if (file != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    MimeTypeMap.getFileExtensionFromUrl((Uri.fromFile(file)
                            .toString())));
        }
        return type;
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null,
                    null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
