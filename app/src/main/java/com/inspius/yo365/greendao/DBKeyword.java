package com.inspius.yo365.greendao;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.

/**
 * Entity mapped to table "DBKEYWORD".
 */
@Entity
public class DBKeyword {

    @Id
    private Long id;

    @NotNull
    private String keyword;

    @Generated
    public DBKeyword() {
    }

    public DBKeyword(Long id) {
        this.id = id;
    }

    @Generated
    public DBKeyword(Long id, String keyword) {
        this.id = id;
        this.keyword = keyword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public String getKeyword() {
        return keyword;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setKeyword(@NotNull String keyword) {
        this.keyword = keyword;
    }

}
