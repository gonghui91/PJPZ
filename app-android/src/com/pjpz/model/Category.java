package com.pjpz.model;

/**
 * Created by storm on 14-3-25.
 */
public enum Category {
    text("文字版"), image("欣赏版");
    private String mDisplayName;

    Category(String displayName) {
        mDisplayName = displayName;
    }

    public String getDisplayName() {
        return mDisplayName;
    }
}
