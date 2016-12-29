package com.inspius.yo365.model;

import android.text.TextUtils;

import com.inspius.yo365.helper.TimeUtil;

/**
 * Created by Billy on 12/29/16.
 */

public class CommentModel {
    private CommentJSON commentJSON;
    public String createAt;

    public CommentModel(CommentJSON commentJSON) {
        this.commentJSON = commentJSON;

        this.createAt = TimeUtil.getDateTimeFormat(commentJSON.createAt);
    }

    public String getCreateAt() {
        return createAt;
    }

    public String getUserAvatar() {
        return commentJSON.customerJSON.avatar;
    }

    public String getCommentContent() {
        return commentJSON.commentText;
    }

    public String getFullName() {
        String fullName = "";
        fullName = String.format("%s %s", commentJSON.customerJSON.firstName, commentJSON.customerJSON.lastName).trim();

        if (TextUtils.isEmpty(fullName))
            fullName = commentJSON.customerJSON.username.trim();

        if (TextUtils.isEmpty(fullName))
            fullName = commentJSON.customerJSON.email.trim();

        if (TextUtils.isEmpty(fullName))
            fullName = "Guest";

        return fullName;
    }
}
