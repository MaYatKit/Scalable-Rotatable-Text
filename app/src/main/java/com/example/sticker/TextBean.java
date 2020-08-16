package com.example.sticker;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;


public class TextBean implements Parcelable, Cloneable {

    public static final int GRAVITY_DEFAULT = -1;

    public static final int GRAVITY_CENTER = 0;

    public static final int GRAVITY_VERTICAL_CENTER = 1;

    public static final int GRAVITY_HORIZONTAL_CENTER = 2;

    public static final int COLOR_GRADIENT_HORIZONTAL = 1;

    public static final int COLOR_GRADIENT_VERTICAL = 2;

    public static final Creator<TextBean> CREATOR = new Creator<TextBean>() {
        @Override
        public TextBean createFromParcel(Parcel in) {
            return new TextBean(in);
        }

        @Override
        public TextBean[] newArray(int size) {
            return new TextBean[size];
        }
    };
    private int mTextSize;
    private String mTypeface;
    private int[] mTextColor = new int[1];
    private Rect mTextRect = new Rect();
    private int mTextGravity = GRAVITY_DEFAULT;
    private int mBackgroundRes;
    private Drawable mBgDrawable;

    private String mImagePath;

    private int mImageRes;
    private int mImageX;
    private int mImageY;
    private int mImageWidth;
    private int mImageHeight;
    private int mImageGravity = GRAVITY_DEFAULT;
    private int mDefaultWidth;
    private int mDefaultHeight;
    private String mText;
    private String mOriginalText;
    private int mMinTextSize;
    private int mTextRotateAngle;
    private float mStrokeWidth;
    private int[] mStrokeColor = new int[1];
    private float mLetterSpace;
    private float mTextStartXOffset;
    private float mTextStartYOffset;
    private int mTextColorGradient;
    private int mTextStrokeGradient;

    private float mShadowRadius;
    private float mShadowDisX;
    private float mShadowDisY;
    private int mShadowColor;

    private float mLineSpace;

    private boolean mEditable = true;

    private long mTabId;
    private String mName;
    private String mTabName;
    private long mTextBeanId;

    public TextBean() {

    }

    protected TextBean(Parcel in) {
        mTextSize = in.readInt();
        mTypeface = in.readString();
        mTextColor = new int[in.readInt()];
        in.readIntArray(mTextColor);
        mTextRect = in.readParcelable(Rect.class.getClassLoader());
        mTextGravity = in.readInt();
        mBackgroundRes = in.readInt();
        mImagePath = in.readString();
        mImageRes = in.readInt();
        mImageX = in.readInt();
        mImageY = in.readInt();
        mImageGravity = in.readInt();
        mDefaultWidth = in.readInt();
        mDefaultHeight = in.readInt();
        mText = in.readString();
        mOriginalText = in.readString();
        mMinTextSize = in.readInt();
        mTextRotateAngle = in.readInt();
        mStrokeWidth = in.readFloat();
        mStrokeColor = new int[in.readInt()];
        in.readIntArray(mStrokeColor);
        mLetterSpace = in.readFloat();
        mTextStartXOffset = in.readFloat();
        mTextStartYOffset = in.readFloat();
        mImageWidth = in.readInt();
        mImageHeight = in.readInt();
        mTextColorGradient = in.readInt();
        mShadowRadius = in.readFloat();
        mShadowDisX = in.readFloat();
        mShadowDisY = in.readFloat();
        mShadowColor = in.readInt();
        mLineSpace = in.readFloat();
        mTextStrokeGradient = in.readInt();
        mName = in.readString();
        mTabName = in.readString();
        mTabId = in.readLong();
        mTextBeanId = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mTextSize);
        dest.writeString(mTypeface);
        dest.writeInt(mTextColor.length);
        dest.writeIntArray(mTextColor);
        dest.writeParcelable(mTextRect, flags);
        dest.writeInt(mTextGravity);
        dest.writeInt(mBackgroundRes);
        dest.writeString(mImagePath);
        dest.writeInt(mImageRes);
        dest.writeInt(mImageX);
        dest.writeInt(mImageY);
        dest.writeInt(mImageGravity);
        dest.writeInt(mDefaultWidth);
        dest.writeInt(mDefaultHeight);
        dest.writeString(mText);
        dest.writeString(mOriginalText);
        dest.writeInt(mMinTextSize);
        dest.writeInt(mTextRotateAngle);
        dest.writeFloat(mStrokeWidth);
        dest.writeInt(mStrokeColor.length);
        dest.writeIntArray(mStrokeColor);
        dest.writeFloat(mLetterSpace);
        dest.writeFloat(mTextStartXOffset);
        dest.writeFloat(mTextStartYOffset);
        dest.writeInt(mImageWidth);
        dest.writeInt(mImageHeight);
        dest.writeInt(mTextColorGradient);
        dest.writeFloat(mShadowRadius);
        dest.writeFloat(mShadowDisX);
        dest.writeFloat(mShadowDisY);
        dest.writeInt(mShadowColor);
        dest.writeFloat(mLineSpace);
        dest.writeInt(mTextStrokeGradient);
        dest.writeString(mName);
        dest.writeString(mTabName);
        dest.writeLong(mTabId);
        dest.writeLong(mTextBeanId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getDefaultWidth() {
        return mDefaultWidth;
    }

    public void setDefaultWidth(int defaultWidth) {
        mDefaultWidth = defaultWidth;
    }

    public int getDefaultHeight() {
        return mDefaultHeight;
    }

    public void setDefaultHeight(int defaultHeight) {
        mDefaultHeight = defaultHeight;
    }

    public int[] getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int[] color) {
        this.mTextColor = color;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int textSize) {
        this.mTextSize = textSize;
    }

    public String getTypeface() {
        return mTypeface;
    }

    public void setTypeface(String typeface) {
        this.mTypeface = typeface;
    }

    public int getBackgroundRes() {
        return mBackgroundRes;
    }

    public void setBackgroundRes(int backgroundRes) {
        mBackgroundRes = backgroundRes;
    }

    public void setBackgroundDrawable(Drawable drawable) {
        mBgDrawable = drawable;
    }


    public int getImageRes() {
        return mImageRes;
    }

    public void setImageRes(int imageRes) {
        mImageRes = imageRes;
    }

    public Rect getTextRect() {
        return mTextRect;
    }

    public void setTextRect(Rect textRect) {
        mTextRect = textRect;
    }

    public int getImageX() {
        return mImageX;
    }

    public void setImageX(int imageX) {
        mImageX = imageX;
    }

    public int getImageY() {
        return mImageY;
    }

    public void setImageY(int imageY) {
        mImageY = imageY;
    }

    public int getTextGravity() {
        return mTextGravity;
    }

    public void setTextGravity(int textGravity) {
        mTextGravity = textGravity;
    }

    public int getImageGravity() {
        return mImageGravity;
    }

    public void setImageGravity(int imageGravity) {
        mImageGravity = imageGravity;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public int getMinTextSize() {
        return mMinTextSize;
    }

    public void setMinTextSize(int minTextSize) {
        mMinTextSize = minTextSize;
    }

    public int getTextRotateAngle() {
        return mTextRotateAngle;
    }

    public void setTextRotateAngle(int textRotateAngle) {
        mTextRotateAngle = textRotateAngle;
    }

    public float getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        mStrokeWidth = strokeWidth;
    }

    public int[] getStrokeColor() {
        return mStrokeColor;
    }

    public void setStrokeColor(int[] strokeColor) {
        mStrokeColor = strokeColor;
    }

    public float getLetterSpace() {
        return mLetterSpace;
    }

    public String getTabId() {
        return mTabId + "";
    }

    public void setTabId(long mTabId) {
        this.mTabId = mTabId;
    }

    public void setLetterSpace(float letterSpace) {
        mLetterSpace = letterSpace;
    }

    public float getTextStartXOffset() {
        return mTextStartXOffset;
    }

    public void setTextStartXOffset(float textStartXOffset) {
        mTextStartXOffset = textStartXOffset;
    }

    public float getTextStartYOffset() {
        return mTextStartYOffset;
    }

    public void setTextStartYOffset(float textStartYOffset) {
        mTextStartYOffset = textStartYOffset;
    }

    public int getImageWidth() {
        return mImageWidth;
    }

    public void setImageWidth(int imageWidth) {
        mImageWidth = imageWidth;
    }

    public int getImageHeight() {
        return mImageHeight;
    }

    public void setImageHeight(int imageHeight) {
        mImageHeight = imageHeight;
    }

    public int getTextColorGradient() {
        return mTextColorGradient;
    }

    public void setTextColorGradient(int textColorGradient) {
        mTextColorGradient = textColorGradient;
    }

    public Drawable getBgDrawable() {
        return mBgDrawable;
    }

    public float getShadowRadius() {
        return mShadowRadius;
    }

    public String getOriginalText() {
        return mOriginalText;
    }

    public void setOriginalText(String mOriginalText) {
        this.mOriginalText = mOriginalText;
    }

    public void setShadowLayer(float shadowDisX, float shadowDisY, float shadowRadius, int shadowColor) {
        mShadowRadius = shadowRadius;
        mShadowDisX = shadowDisX;
        mShadowDisY = shadowDisY;
        mShadowColor = shadowColor;
    }

    public float getShadowDisX() {
        return mShadowDisX;
    }


    public float getShadowDisY() {
        return mShadowDisY;
    }


    public int getShadowColor() {
        return mShadowColor;
    }

    public float getLineSpace() {
        return mLineSpace;
    }

    public void setLineSpace(float lineSpace) {
        mLineSpace = lineSpace;
    }

    public int getTextStrokeGradient() {
        return mTextStrokeGradient;
    }

    public void setTextStrokeGradient(int textStrokeGradient) {
        mTextStrokeGradient = textStrokeGradient;
    }

    public boolean isEditable() {
        return mEditable;
    }

    public void setEditable(boolean editable) {
        mEditable = editable;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getTabName() {
        return mTabName;
    }

    public void setTabName(String mTabName) {
        this.mTabName = mTabName;
    }

    public String getImagePath() {
        return mImagePath;
    }

    public void setImagePath(String imagePath) {
        mImagePath = imagePath;
    }

    public long getTextBeanId() {
        return mTextBeanId;
    }

    public void setTextBeanId(long mTextBeanId) {
        this.mTextBeanId = mTextBeanId;
    }

    public TextBean clone() {
        TextBean o = null;
        try {
            o = (TextBean) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }

    @Override
    public String toString() {
        return "TextBean{" +
                "mTextSize=" + mTextSize +
                ", mTypeface='" + mTypeface + '\'' +
                ", mTextColor=" + Arrays.toString(mTextColor) +
                ", mTextRect=" + mTextRect +
                ", mTextGravity=" + mTextGravity +
                ", mBackgroundRes=" + mBackgroundRes +
                ", mBgDrawable=" + mBgDrawable +
                ", mImagePath='" + mImagePath + '\'' +
                ", mImageRes=" + mImageRes +
                ", mImageX=" + mImageX +
                ", mImageY=" + mImageY +
                ", mImageWidth=" + mImageWidth +
                ", mImageHeight=" + mImageHeight +
                ", mImageGravity=" + mImageGravity +
                ", mDefaultWidth=" + mDefaultWidth +
                ", mDefaultHeight=" + mDefaultHeight +
                ", mText='" + mText + '\'' +
                ", mMinTextSize=" + mMinTextSize +
                ", mTextRotateAngle=" + mTextRotateAngle +
                ", mStrokeWidth=" + mStrokeWidth +
                ", mStrokeColor=" + Arrays.toString(mStrokeColor) +
                ", mLetterSpace=" + mLetterSpace +
                ", mTextStartXOffset=" + mTextStartXOffset +
                ", mTextStartYOffset=" + mTextStartYOffset +
                ", mTextColorGradient=" + mTextColorGradient +
                ", mTextStrokeGradient=" + mTextStrokeGradient +
                ", mShadowRadius=" + mShadowRadius +
                ", mShadowDisX=" + mShadowDisX +
                ", mShadowDisY=" + mShadowDisY +
                ", mShadowColor=" + mShadowColor +
                ", mLineSpace=" + mLineSpace +
                ", mEditable=" + mEditable +
                ", mTabId=" + mTabId +
                ", mName='" + mName + '\'' +
                ", mTabName='" + mTabName + '\'' +
                ", mTextBeanId=" + mTextBeanId +
                '}';
    }
}
