package com.neo2.telebang.model;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Admin on 15/4/2016.
 */

public class ImageFileModel implements Serializable {
    private String mimeType;
    private File file;
    private String name;

    public ImageFileModel(File file, String mimeType) {
        this.mimeType = mimeType;
        this.file = file;
        this.name = file.getName();
    }

    public File getFile() {
        return file;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getName() {
        return name;
    }
}