package com.example.android.miwok;

import android.content.Context;

public class Word {

    private static final int NO_IMAGE_PROVIDED = -1;
    /**  */
    private String mDefaultTranslation;
    /**  */
    private String mMiwokTranslation;
    private int mImageResID = NO_IMAGE_PROVIDED;
    private int mAudioID;

    public Word(String defaultTranslation, String miwokTranslation, int audioId) {
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mAudioID = audioId;

    }

    public Word(String defaultTranslation, String miwokTranslation, int imageID, int audioId) {
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mImageResID = imageID;
        mAudioID = audioId;
    }

    public String getDefaultTranslation() {
        return mDefaultTranslation;
    }

    public String getmMiwokTranslation() {
        return mMiwokTranslation;
    }

    public int getImageResourceID() {
        return mImageResID;
    }

    public int getAudioID() {
        return mAudioID;
    }

    /**
     * @return
     */
    public boolean hasImage() {
        return mImageResID != NO_IMAGE_PROVIDED;
    }
}
