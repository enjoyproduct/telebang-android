package com.neo2.telebang.modules.upload_video;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by Billy on 1/12/17.
 */

public class MVideoFile {
    private File file;
    private String mimeType;

    public MVideoFile(File file, String mimeType) {
        this.file = file;
        this.mimeType = mimeType;
    }

    public byte[] getByVideo() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            InputStream input = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                stream.write(buffer, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] b = stream.toByteArray();
        return b;
    }

    public String getMimeType() {
        return mimeType;
    }

    public File getFile() {
        return file;
    }
}
