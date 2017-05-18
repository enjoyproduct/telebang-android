package com.neo2.telebang.modules.upload_video;

import java.io.File;

/**
 * Created by Billy on 1/12/17.
 */

public class MVideoUploadModel {
    private MThumbnailFile thumbnailFile;
    private MVideoFile videoFile;

    private String videoTitle;
    private String videoDes;
    private String categoryId;
    private long videoLength;
    private byte[] thumbnailBytes;

    public MVideoUploadModel(String videoTitle, MThumbnailFile thumbnail, MVideoFile videoFile) {
        this.videoTitle = videoTitle;
        this.thumbnailFile = thumbnail;
        this.videoFile = videoFile;

        videoLength = MUploadHelper.getVideoLength(videoFile.getFile().getAbsolutePath());
        thumbnailBytes = MUploadHelper.getByteBitmap(thumbnailFile.getMimeType(), thumbnailFile.getBitmap());
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public String getVideoDes() {
        return videoDes;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public long getVideoLength() {
        return videoLength;
    }

    public File getVideoFile() {
        return videoFile.getFile();
    }

    public String getVideoFileType() {
        return videoFile.getMimeType();
    }

    public String getThumbnailName() {
        return thumbnailFile.getName();
    }

    public String getThumbnailMimeType() {
        return thumbnailFile.getMimeType();
    }

    public byte[] getThumbnailBytes() {
        return thumbnailBytes;
    }

    public MThumbnailFile getThumbnailFile() {
        return thumbnailFile;
    }

    public void setVideoDes(String videoDes) {
        this.videoDes = videoDes;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
