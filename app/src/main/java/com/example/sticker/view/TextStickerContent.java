package com.example.sticker.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.ScaleXSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;


import androidx.annotation.NonNull;

import com.example.sticker.R;
import com.example.sticker.TextBean;
import com.example.sticker.utils.BitmapUtils;
import com.example.sticker.utils.Loger;

import static android.widget.RelativeLayout.CENTER_IN_PARENT;
import static com.example.sticker.TextBean.GRAVITY_DEFAULT;


public class TextStickerContent extends FrameLayout {
    private final Context mContext;
    private int mDefaultWidth = 400;
    private int mDefaultHeight = 400;
    private TextBean mTextBean;
    private float mScale = 1f;
    private TextPaint mTextPaint = new TextPaint();
    private TextPaint mTextStrokePaint = new TextPaint();
    private Paint mBitmapPaint = new Paint();
    private Bitmap mImageBitmap;
    private String mText = "Wonder Video";
    private float mTextWidth;
    private float mTextHeight;

    private int mTextSize;
    private String mTypeface;
    private String mImagePath;
    private int[] mTextColor;
    private Rect mTextRect;
    private int mTextGravity = GRAVITY_DEFAULT;
    private int mBackgroundRes;
    private Drawable mBgDrawable;
    private int mMinTextSize;
    private float mTextStartXOffset;
    private float mTextStartYOffset;
    private StaticLayout mLayout;
    private StaticLayout mStrokeLayout;
    private int mTextRotateAngle;
    private StaticLayout.Alignment mAlignment;
    private float mStrokeWidth;
    private int[] mStrokeColor;
    private float mLetterSpace;
    private int mTextColorGradient;
    private int mTextStrokeGradient;
    private float mShadowRadius;
    private float mShadowDisX;
    private float mShadowDisY;
    private int mShadowColor;
    private float mLineSpace;

    private int mImageRes;
    private int mImageX;
    private int mImageY;
    private int mImageWidth;
    private int mImageHeight;
    private int mImageGravity = GRAVITY_DEFAULT;

    private int mParentPadding;

    public TextStickerContent(Context context) {
        super(context);
        mContext = context;
        setWillNotDraw(false);
        init();

    }

    public void setParentWidthAndHeight() {
        post(new Runnable() {
            @Override
            public void run() {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
                params.width = mDefaultWidth;
                params.height = mDefaultHeight;
                params.addRule(CENTER_IN_PARENT);
                setLayoutParams(params);
//                setText(mText, 1);
                ((TextStickerWidget) getParent()).setWidthAndHeight(mDefaultWidth, mDefaultHeight);
            }
        });
    }

    private void init() {
        mTextPaint.setAntiAlias(true);

        mBitmapPaint.setAntiAlias(true);
        mParentPadding = mContext.getResources().getDimensionPixelOffset(R.dimen.edit_text_space);
    }

    public void setTextBean(@NonNull TextBean bean) {
        mTextBean = bean;
        mTextSize = mTextBean.getTextSize();
        mTypeface = mTextBean.getTypeface();
        mTextColor = mTextBean.getTextColor();
        mBackgroundRes = mTextBean.getBackgroundRes();
        mBgDrawable = mTextBean.getBgDrawable();
        mImageRes = mTextBean.getImageRes();
        mImageX = mTextBean.getImageX();
        mImageY = mTextBean.getImageY();
        mImageGravity = mTextBean.getImageGravity();
        mDefaultWidth = mTextBean.getDefaultWidth();
        mDefaultHeight = mTextBean.getDefaultHeight();
        mText = mTextBean.getText();
        mTextGravity = mTextBean.getTextGravity();
        mTextRect = mTextBean.getTextRect();
        mMinTextSize = mTextBean.getMinTextSize();
        mTextRotateAngle = mTextBean.getTextRotateAngle();
        mStrokeWidth = mTextBean.getStrokeWidth();
        mStrokeColor = mTextBean.getStrokeColor();
        mLetterSpace = mTextBean.getLetterSpace();
        mTextStartXOffset = mTextBean.getTextStartXOffset();
        mTextStartYOffset = mTextBean.getTextStartYOffset();
        mImageWidth = mTextBean.getImageWidth();
        mImageHeight = mTextBean.getImageHeight();
        mTextColorGradient = mTextBean.getTextColorGradient();
        mTextStrokeGradient = mTextBean.getTextStrokeGradient();
        mShadowRadius = mTextBean.getShadowRadius();
        mShadowDisX = mTextBean.getShadowDisX();
        mShadowDisY = mTextBean.getShadowDisY();
        mShadowColor = mTextBean.getShadowColor();
        mLineSpace = mTextBean.getLineSpace();
        mImagePath = mTextBean.getImagePath();

        addBg();
        addImage();
        if (mText != null || mTypeface != null) {
            addText();
        }
    }

    private void addBg() {
        if (mImagePath != null) {
            setBackground(new BitmapDrawable(mContext.getResources(), BitmapUtils.getBitmap(mImagePath)));
            return;
        }
        if (mBackgroundRes == 0) {
            if (mBgDrawable != null) {
                setBackground(mBgDrawable);
            } else {
                setBackgroundResource(mBackgroundRes);
            }
            return;
        }
        setBackgroundResource(mBackgroundRes);
    }

    private void addImage() {
        if (mImageRes == 0) {
            return;
        }
        mImageBitmap = BitmapFactory.decodeResource(mContext.getResources(), mImageRes);
//        mDefaultWidth = mImageBitmap.getWidth();
//        mDefaultHeight = mImageBitmap.getHeight();
    }

    private void addText() {

        mTextPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSize, getResources()
                .getDisplayMetrics()));

        setTextTypeface(mTypeface);
        mTextPaint.setFakeBoldText(true);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setShadowLayer(mShadowRadius, mShadowDisX, mShadowDisY, mShadowColor);

        mTextStrokePaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSize, getResources()
                .getDisplayMetrics()));
        mTextStrokePaint.setStrokeWidth(mStrokeWidth);
        mTextStrokePaint.setStyle(Paint.Style.STROKE);
        mTextStrokePaint.setFakeBoldText(true);
        mTextStrokePaint.setAntiAlias(true);

        if (mTextGravity == TextBean.GRAVITY_CENTER || mTextGravity == TextBean.GRAVITY_HORIZONTAL_CENTER) {
            mAlignment = StaticLayout.Alignment.ALIGN_CENTER;
        } else {
            mAlignment = StaticLayout.Alignment.ALIGN_NORMAL;
        }


        mLayout = new StaticLayout(applyLetterSpacing(mText), mTextPaint, mTextRect.width(), mAlignment, 0.9f, mLineSpace, false);
        mStrokeLayout = new StaticLayout(applyLetterSpacing(mText), mTextStrokePaint, mTextRect.width(), mAlignment, 0.9f,
                mLineSpace, false);

//        mTextWidth = mTextPaint.measureText(mText);;
        mTextWidth = mLayout.getWidth();

        mTextHeight = mStrokeLayout.getHeight() >= mLayout.getHeight() ? mStrokeLayout.getHeight() : mLayout.getHeight();

        initColor();

    }

    public void setTextTypeface(String typeface) {
        Typeface type = null;
        if (typeface != null) {
            type = Typeface.createFromAsset(mContext.getAssets(), typeface);
        }
        mTextPaint.setTypeface(type);
        mTextStrokePaint.setTypeface(type);
    }

    private void initColor() {
        if (mStrokeColor != null) {
            if (mStrokeColor.length == 1) {
                mTextStrokePaint.setColor(mStrokeColor[0]);
            } else {
                switch (mTextStrokeGradient) {
                    case TextBean.COLOR_GRADIENT_HORIZONTAL:
                        mTextStrokePaint.setShader(new LinearGradient(0, 0, mTextRect.width(), 0, mStrokeColor[0],
                                mStrokeColor[1], Shader.TileMode.REPEAT));
                        break;
                    case TextBean.COLOR_GRADIENT_VERTICAL:
                        mTextStrokePaint.setShader(new LinearGradient(0, 0, 0, mTextHeight / (float) mLayout.getLineCount(),
                                mStrokeColor[0], mStrokeColor[1], Shader.TileMode.REPEAT));
                        break;
                    default:
                        mTextStrokePaint.setShader(new LinearGradient(0, 0, 0, mTextHeight / (float) mLayout.getLineCount(),
                                mStrokeColor[0], mStrokeColor[1], Shader.TileMode.REPEAT));
                        break;
                }
            }
        }

        if (mTextColor != null) {
            if (mTextColor.length == 1) {
                mTextPaint.setColor(mTextColor[0]);
            } else {
                switch (mTextColorGradient) {
                    case TextBean.COLOR_GRADIENT_HORIZONTAL:
                        mTextPaint.setShader(new LinearGradient(0, 0, mTextRect.width(), 0, mTextColor[0], mTextColor[1],
                                Shader.TileMode.REPEAT));
                        break;
                    case TextBean.COLOR_GRADIENT_VERTICAL:
                        mTextPaint.setShader(new LinearGradient(0, 0, 0, mTextHeight / (float) mLayout.getLineCount(),
                                mTextColor[0], mTextColor[1], Shader.TileMode.REPEAT));
                        break;
                    default:
                        mTextPaint.setShader(new LinearGradient(0, 0, 0, mTextHeight / (float) mLayout.getLineCount(),
                                mTextColor[0], mTextColor[1], Shader.TileMode.REPEAT));
                        break;
                }
            }
        }
    }

    private int getLines(String text) {
        int startWith = 0;
        int num = 1;
        while (text.indexOf("\n", startWith) != -1) {
            startWith = startWith + text.indexOf("\n", startWith) + 1;
            num++;
        }
        return num;
    }

    private float getWidth(String text) {
        float maxWidth = 0;
        for (String s : text.split("\\n")) {
            float temp = Math.max(mTextStrokePaint.measureText(s), mTextPaint.measureText(s));
            maxWidth = Math.max(maxWidth, temp);
        }
        return maxWidth;
    }

    public float getScale() {
        return mScale;
    }

    public void setScale(float scale) {
        mScale *= scale;
        scaleView(this, mScale);
        postInvalidate();
    }

    private void scaleView(View view, float scale) {
        if (view != null && view.getParent() != null) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            float t;
            params.width = (int) (t = mDefaultWidth * scale);
            params.height = (int) (mDefaultHeight * scale);
            view.setLayoutParams(params);
            ((TextStickerWidget) getParent()).setWidthAndHeight(params.width, params.height);
//            ((TextStickerWidget)view.getParent()).setScaleY(scale);
//            ((TextStickerWidget)view.getParent()).setScaleX(scale);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mTextBean == null) {
            return;
        }
        canvas.save();
        canvas.scale(mScale, mScale);
        drawImage(canvas);

        if (mText != null || mTypeface != null) {
            drawText(canvas);
        }
//        canvas.rotate(mTextRotateAngle, mTextRect.width()/2, mTextRect.height()/2);

        canvas.restore();
    }

    public Bitmap getBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(mTextBean.getDefaultWidth(), mTextBean.getDefaultHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        canvas.translate(0, 0);
//        canvas.drawColor(mContext.getResources().getColor(R.color.common_blue));
        canvas.drawColor(mContext.getResources().getColor(android.R.color.transparent));
        Bitmap bg = null;

        if (mBackgroundRes == 0) {
            if (mBgDrawable != null) {
                bg = ((BitmapDrawable) mBgDrawable).getBitmap();
                if (bg != null) {
                    canvas.drawBitmap(bg, 0, 0, mBitmapPaint);
                }
            }
        } else {
            Drawable drawable = mContext.getResources().getDrawable(mBackgroundRes);
            drawable.setBounds(0, 0, mTextBean.getDefaultWidth(), mTextBean.getDefaultHeight());
            drawable.draw(canvas);
        }


        drawImage(canvas);

        if (mText != null || mTypeface != null) {
            drawText(canvas);
        }

        return bitmap;
    }

    private void drawImage(Canvas canvas) {
        if (mImageBitmap == null) {
            return;
        }
        Bitmap newBitmap = Bitmap.createScaledBitmap(mImageBitmap, mImageWidth, mImageHeight, true);
        canvas.drawBitmap(newBitmap, mTextBean.getImageX(), mTextBean.getImageY(), mBitmapPaint);
    }

    private void drawText(Canvas canvas) {

        float dy = 0f, dx = 0f;
        switch (mTextGravity) {
            case GRAVITY_DEFAULT:
                dx = mTextRect.left;
                dy = mTextRect.top;
                break;
            case TextBean.GRAVITY_VERTICAL_CENTER:
                dx = mTextRect.left;
                dy = mTextRect.top + mTextRect.height() / 2 - mTextHeight / 2;
                break;
            case TextBean.GRAVITY_HORIZONTAL_CENTER:
                dx = mTextRect.left;
                dy = mTextRect.top;
                break;
            case TextBean.GRAVITY_CENTER:
                dx = mTextRect.left;
                dy = mTextRect.top + mTextRect.height() / 2 - mTextHeight / 2;
                break;
        }
        canvas.translate(mTextRect.left + mTextRect.width() / 2, mTextRect.top + mTextRect.height() / 2);
        canvas.rotate(mTextRotateAngle);
        canvas.translate(-(mTextRect.left + mTextRect.width() / 2), -(mTextRect.top + mTextRect.height() / 2));
        canvas.translate(dx + mTextStartXOffset, dy + mTextStartYOffset);

        mStrokeLayout.draw(canvas);
        mLayout.draw(canvas);
    }

    public void setText(String text, int lineCount) {
//        if (mText.trim().equals(text)) {
//            return;
//        }
        mText = text;
        mTextWidth = getWidth(text);

        float minDimension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mMinTextSize, getResources()
                .getDisplayMetrics());
        float maxDimension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 28, getResources().getDisplayMetrics());

        if (mTextWidth < mTextRect.width()) {
            while (mTextWidth < mTextRect.width()) {
                Loger.i("setText", "<<<<<<<<<<<<<<---mTextWidth-- " + mTextWidth + "---mTextRect.width()-- " + mTextRect.width());
                float moreDimension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSize + 1, getResources()
                        .getDisplayMetrics());
                if (moreDimension <= maxDimension) {
                    float scale = ((mTextSize + 1) / (float) mTextSize);
                    setStrokeWidth(scale);
                    setShadowRadius(scale);
                    setShadowDisX(scale);
                    setShadowDisY(scale);
                    setLineSpace(scale);
                    mTextSize = mTextSize + 1;
                    mTextPaint.setShadowLayer(mShadowRadius, mShadowDisX, mShadowDisY, mShadowColor);
                    mTextPaint.setTextSize(moreDimension);
                    mTextStrokePaint.setTextSize(moreDimension);
                    mTextStrokePaint.setStrokeWidth(mStrokeWidth);
                    mTextWidth = getWidth(text);
                } else {
                    break;
                }
            }
        }
        if (mTextWidth > mTextRect.width()) {
            while (mTextWidth > mTextRect.width()) {
                Loger.i("setText", ">>>>>>>>>>>>>>---mTextWidth-- " + mTextWidth + "---mTextRect.width()-- " + mTextRect.width());
                float lessDimension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSize - 1, getResources()
                        .getDisplayMetrics());

                if (lessDimension >= minDimension) {
                    float scale = ((mTextSize - 1) / (float) mTextSize);
                    setStrokeWidth(scale);
                    setShadowRadius(scale);
                    setShadowDisX(scale);
                    setShadowDisY(scale);
                    setLineSpace(scale);
                    mTextSize = mTextSize - 1;
                    mTextPaint.setShadowLayer(mShadowRadius, mShadowDisX, mShadowDisY, mShadowColor);
                    mTextPaint.setTextSize(lessDimension);
                    mTextStrokePaint.setTextSize(lessDimension);
                    mTextStrokePaint.setStrokeWidth(mStrokeWidth);
                    mTextWidth = getWidth(text);
                } else {
                    break;
                }
            }
        }

        mLayout = new StaticLayout(applyLetterSpacing(mText), mTextPaint, mTextRect.width(), mAlignment, 0.9f, mLineSpace, false);
        mStrokeLayout = new StaticLayout(applyLetterSpacing(mText), mTextStrokePaint, mTextRect.width(), mAlignment, 0.9f,
                mLineSpace, false);
        setTextHeight(mStrokeLayout.getHeight() >= mLayout.getHeight() ? mStrokeLayout.getHeight() : mLayout.getHeight());
        setColor(mTextWidth, mTextHeight);
        Loger.i("setText", "  mStrokeLayout.getHeight()--" + mStrokeLayout.getHeight());
        while (mTextHeight >= mTextRect.height()) {
            Loger.i("setText", ">>>>>>>>>>>>>>---mTextHeight-- " + mTextHeight + "---mTextRect.height()-- " + mTextRect.height());
            float lessDimension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSize - 1, getResources()
                    .getDisplayMetrics());
            if (lessDimension >= minDimension) {
                float scale = ((mTextSize - 1) / (float) mTextSize);
                setStrokeWidth(scale);
                setShadowRadius(scale);
                setShadowDisX(scale);
                setShadowDisY(scale);
                setLineSpace(scale);
                mTextSize = mTextSize - 1;
                mTextPaint.setTextSize(lessDimension);
                mTextPaint.setShadowLayer(mShadowRadius, mShadowDisX, mShadowDisY, mShadowColor);
                mTextStrokePaint.setStrokeWidth(mStrokeWidth);
                mTextStrokePaint.setTextSize(lessDimension);
                mLayout = new StaticLayout(applyLetterSpacing(mText), mTextPaint, mTextRect.width(), mAlignment, 0.9f,
                        mLineSpace, false);
                mStrokeLayout = new StaticLayout(applyLetterSpacing(mText), mTextStrokePaint, mTextRect.width(), mAlignment,
                        0.9f, mLineSpace, false);
                setTextHeight(mStrokeLayout.getHeight() >= mLayout.getHeight() ? mStrokeLayout.getHeight() : mLayout.getHeight());
//                setColor(mTextWidth, mTextHeight);
            } else {
                int sumHeight = 0;
                Rect rect = new Rect();
                int i = 0;
                for (; i < mLayout.getLineCount(); i++) {
                    mLayout.getLineBounds(i, rect);
                    sumHeight = sumHeight + rect.height();
                    if (sumHeight > mTextRect.height()) {
                        break;
                    }
                }
                float lastIndex = mLayout.getLineVisibleEnd(i - 1) / (float) mLayout.getLineVisibleEnd(mLayout.getLineCount() -
                        1);
                Loger.i("setText", "mLayout.getLineVisibleEnd(i - 1)--" + lastIndex);
                Loger.i("setText", "mText-->" + mText);
                Loger.i("setText", "mText.length-->" + mText.length());
                mText = mText.substring(0, (int) (mText.length() * lastIndex));
                mLayout = new StaticLayout(applyLetterSpacing(mText), mTextPaint, mTextRect.width(), mAlignment, 0.9f,
                        mLineSpace, false);
                mStrokeLayout = new StaticLayout(applyLetterSpacing(mText), mTextStrokePaint, mTextRect.width(), mAlignment,
                        0.9f, mLineSpace, false);
//                setColor(mTextWidth, mTextHeight);
                setTextHeight(mStrokeLayout.getHeight() >= mLayout.getHeight() ? mStrokeLayout.getHeight() : mLayout.getHeight());
                break;
            }
        }
        setTextHeight(mStrokeLayout.getHeight() >= mLayout.getHeight() ? mStrokeLayout.getHeight() : mLayout.getHeight());
        addBg();
        Loger.i("setText", "  mTextWidth--" + mTextWidth + "  mTextHeight--" + mTextHeight + "  mTextRect.width()--" +
                mTextRect.width()
                + "  mStrokeLayout.getHeight()--" + mStrokeLayout.getHeight());
//        mTextHeight = mStrokeLayout.getHeight();
    }


    private SpannableString applyLetterSpacing(String text) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            builder.append(text.charAt(i));
            if (i + 1 < text.length() && !isEmojiCharacter(text.charAt(i))) {
                builder.append("\u00A0");
            }
        }
        SpannableString finalText = new SpannableString(builder.toString());
        if (builder.toString().length() > 1) {
            for (int i = 1; i < builder.toString().length(); i += 2) {
                if (builder.toString().toCharArray()[i] == 160) {
                    finalText.setSpan(new ScaleXSpan((mLetterSpace + 1) / 10), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return finalText;
    }

    private boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }

    public void setTextColor(int textColor) {
        mTextPaint.setColor(textColor);
        mTextColor = new int[]{textColor, textColor};
    }

    public String getText() {
        return mText;
    }


    private void setShadowRadius(float scale) {
        mShadowRadius *= scale;
    }

    private void setShadowDisX(float scale) {
        mShadowDisX *= scale;
    }

    private void setShadowDisY(float scale) {
        mShadowDisY *= scale;
    }

    private void setLineSpace(float scale) {
        mLineSpace *= scale;
    }

    private void setStrokeWidth(float scale) {
        mStrokeWidth *= scale;
    }

    private void setColor(float gradientWidth, float textHeight) {
        if (mTextColor != null) {
            if (mTextColor.length == 1) {
                mTextPaint.setColor(mTextColor[0]);
            } else {
                switch (mTextColorGradient) {
                    case TextBean.COLOR_GRADIENT_HORIZONTAL:
                        mTextPaint.setShader(new LinearGradient(0, 0, gradientWidth, 0, mTextColor[0], mTextColor[1], Shader
                                .TileMode.REPEAT));
                        break;
                    case TextBean.COLOR_GRADIENT_VERTICAL:
                        mTextPaint.setShader(new LinearGradient(0, 0, 0, textHeight / (float) mLayout.getLineCount(),
                                mTextColor[0], mTextColor[1], Shader.TileMode.REPEAT));
                        break;
                    default:
                        mTextPaint.setShader(new LinearGradient(0, 0, 0, textHeight / (float) mLayout.getLineCount(),
                                mTextColor[0], mTextColor[1], Shader.TileMode.REPEAT));
                        break;
                }
            }
        }

        if (mStrokeColor != null) {
            if (mStrokeColor.length == 1) {
                mTextStrokePaint.setColor(mStrokeColor[0]);
            } else {
                switch (mTextStrokeGradient) {
                    case TextBean.COLOR_GRADIENT_HORIZONTAL:
                        mTextStrokePaint.setShader(new LinearGradient(0, 0, gradientWidth, 0, mStrokeColor[0], mStrokeColor[1],
                                Shader.TileMode.REPEAT));
                        break;
                    case TextBean.COLOR_GRADIENT_VERTICAL:
                        mTextStrokePaint.setShader(new LinearGradient(0, 0, 0, textHeight / (float) mLayout.getLineCount(),
                                mStrokeColor[0], mStrokeColor[1], Shader.TileMode.REPEAT));
                        break;
                    default:
                        mTextStrokePaint.setShader(new LinearGradient(0, 0, 0, textHeight / (float) mLayout.getLineCount(),
                                mStrokeColor[0], mStrokeColor[1], Shader.TileMode.REPEAT));
                        break;
                }
            }
        }
    }

    private void setTextHeight(float textHeight) {
        mTextHeight = textHeight;
    }
}
