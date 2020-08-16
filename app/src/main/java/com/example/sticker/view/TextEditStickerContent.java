package com.example.sticker.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.NonNull;

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.method.TransformationMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.example.sticker.TextBean;
import com.example.sticker.utils.BitmapUtils;

import java.util.StringTokenizer;


public class TextEditStickerContent extends androidx.appcompat.widget.AppCompatTextView {
    private static final int SCALE_MIN_WIDTH = 160;
    private static final int SCALE_MIN_HEIGHT = 80;
    private final Context mContext;
    private int mDefaultWidth = 400;
    private int mDefaultHeight = 400;
    private TextBean mTextBean;
    private float mScale = 1f;
    private Paint mBitmapPaint = new Paint();
    private Bitmap mImageBitmap;
    private String mText = "Wonder Video";
    private String mTypeface;
    private String mImagePath;
    private int[] mTextColor;
    private int mBackgroundRes;
    private Drawable mBgDrawable;
    private static final int NO_LINE_LIMIT = -1;
    private final RectF mAvailableSpaceRect = new RectF();
    private SizeTester mSizeTester;
    private float mMaxTextSize, mSpacingMult = 1.0f, mSpacingAdd = 0.0f, mMinTextSize;
    private int mWidthlimit, mMaxLines;
    private boolean mInitialized = false;
    private TextPaint mPaint;
    private int mImageRes;
    private int mImageWidth;
    private int mImageHeight;


    public TextEditStickerContent(Context context) {
        super(context);
        mContext = context;
        setWillNotDraw(false);
        init();
    }

    private interface SizeTester {
        /**
         * @param suggestedSize  Size of text to be tested
         * @param availableSpace available space in which text must fit
         * @return an integer < 0 if after applying {@code suggestedSize} to
         * text, it takes less space than {@code availableSpace}, > 0
         * otherwise
         */
        int onTestSize(int suggestedSize, RectF availableSpace);
    }

    private void init() {
        mBitmapPaint.setAntiAlias(true);
        setGravity(Gravity.CENTER);

        mMinTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 2, getResources().getDisplayMetrics());
        mMaxTextSize = getTextSize();
        mPaint = new TextPaint(getPaint());
        if (mMaxLines == 0)
            // no value was assigned during construction
            mMaxLines = NO_LINE_LIMIT;
        // prepare size tester:
        mSizeTester = new SizeTester() {
            final RectF textRect = new RectF();

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public int onTestSize(final int suggestedSize, final RectF availableSpace) {
                mPaint.setTextSize(suggestedSize);
                final TransformationMethod transformationMethod = getTransformationMethod();
                final String text;
                if (transformationMethod != null)
                    text = transformationMethod.getTransformation(getText(), TextEditStickerContent.this).toString();
                else
                    text = getText().toString();
                final boolean singleLine = getMaxLines() == 1;
                if (singleLine) {
                    textRect.bottom = mPaint.getFontSpacing();
                    textRect.right = mPaint.measureText(text);
                } else {
                    final StaticLayout layout = new StaticLayout(text, mPaint, mWidthlimit, Layout.Alignment.ALIGN_NORMAL,
                            mSpacingMult, mSpacingAdd, true);
                    // return early if we have more lines
                    if (getMaxLines() != NO_LINE_LIMIT && layout.getLineCount() > getMaxLines())
                        return 1;
                    textRect.bottom = layout.getHeight();
                    int maxWidth = -1;
                    String word = getLongestWord(text) + text.charAt(0); //huck
                    float measureWidth = mPaint.measureText(word);
                    float avWidth = availableSpace.width();
                    if (measureWidth >= avWidth) {
                        // too big
                        return 1;
                    }
                    int lineCount = layout.getLineCount();
                    for (int i = 0; i < lineCount; i++) {
                        if (maxWidth < layout.getLineRight(i) - layout.getLineLeft(i))
                            maxWidth = (int) layout.getLineRight(i) - (int) layout.getLineLeft(i);
                    }
                    textRect.right = maxWidth;
                }
                textRect.offsetTo(0, 0);
                if (availableSpace.contains(textRect))
                    // may be too small, don't worry we will find the best match
                    return -1;
                // else, too big
                return 1;
            }
        };
        mInitialized = true;
    }

    public boolean isValidWordWrap(char before, char after) {
        return before == ' ' || before == '-';
    }

    @Override
    public void setAllCaps(boolean allCaps) {
        super.setAllCaps(allCaps);
        adjustTextSize();
    }

    @Override
    public void setTypeface(final Typeface tf) {
        super.setTypeface(tf);
        adjustTextSize();
    }

    @Override
    public void setTextSize(final float size) {
        mMaxTextSize = size;
        adjustTextSize();
    }

    @Override
    public void setMaxLines(final int maxLines) {
        super.setMaxLines(maxLines);
        mMaxLines = maxLines;
        adjustTextSize();
    }

    @Override
    public int getMaxLines() {
        return mMaxLines;
    }

    @Override
    public void setSingleLine() {
        super.setSingleLine();
        mMaxLines = 1;
        adjustTextSize();
    }

    @Override
    public void setSingleLine(final boolean singleLine) {
        super.setSingleLine(singleLine);
        if (singleLine)
            mMaxLines = 1;
        else mMaxLines = NO_LINE_LIMIT;
        adjustTextSize();
    }

    @Override
    public void setLines(final int lines) {
        super.setLines(lines);
        mMaxLines = lines;
        adjustTextSize();
    }

    @Override
    public void setTextSize(final int unit, final float size) {
        final Context c = getContext();
        Resources r;
        if (c == null)
            r = Resources.getSystem();
        else r = c.getResources();
        mMaxTextSize = TypedValue.applyDimension(unit, size, r.getDisplayMetrics());
        adjustTextSize();
    }

    @Override
    public void setLineSpacing(final float add, final float mult) {
        super.setLineSpacing(add, mult);
        mSpacingMult = mult;
        mSpacingAdd = add;
    }

    /**
     * Set the lower text size limit and invalidate the view
     *
     * @param minTextSize
     */
    public void setMinTextSize(final float minTextSize) {
        mMinTextSize = minTextSize;
        adjustTextSize();
    }


    private void adjustTextSize() {
        // This is a workaround for truncated text issue on ListView, as shown here: https://github
        // .com/AndroidDeveloperLB/AutoFitTextView/pull/14
        // TODO think of a nicer, elegant solution.
//    post(new Runnable()
//    {
//    @Override
//    public void run()
//      {
        if (!mInitialized)
            return;
        final int startSize = (int) mMinTextSize;
        int heightLimit;
        if (mBackgroundRes == 0) {
            setPadding(0, 0, 0, 0);
        } else {

        }
        heightLimit = getMeasuredHeight() - getCompoundPaddingBottom() - getCompoundPaddingTop();
        mWidthlimit = getMeasuredWidth() - getCompoundPaddingLeft() - getCompoundPaddingRight();
        if (mWidthlimit <= 0)
            return;
        mPaint = new TextPaint(getPaint());
        mAvailableSpaceRect.right = mWidthlimit;
        mAvailableSpaceRect.bottom = heightLimit;
//        Loger.i("textedit", "getMeasuredWidth() " + getMeasuredWidth() + "   getPaddingLeft() " + getPaddingLeft() + "   getPaddingRight() " + getPaddingRight());
        superSetTextSize(startSize);
//      }
//    });
    }

    private String getLongestWord(String text) {
        StringTokenizer st = new StringTokenizer(text);
        String result = "";
        while (st.hasMoreTokens()) {
            String s = st.nextToken();
            if (s.length() > result.length()) {
                result = s;
            }
        }
        return result;
    }


    private void superSetTextSize(int startSize) {
        int textSize = binarySearch(startSize, (int) mMaxTextSize, mSizeTester, mAvailableSpaceRect);
        super.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    private int binarySearch(final int start, final int end, final SizeTester sizeTester, final RectF availableSpace) {
        int lastBest = start, lo = start, hi = end - 1, mid;
        while (lo <= hi) {
            mid = lo + hi >>> 1;
            final int midValCmp = sizeTester.onTestSize(mid, availableSpace);
            if (midValCmp < 0) {
                lastBest = lo;
                lo = mid + 1;
            } else if (midValCmp > 0) {
                hi = mid - 1;
                lastBest = hi;
            } else return mid;
        }
        // make sure to return last best
        // this is what should always be returned
        return lastBest;
    }

    @Override
    protected void onTextChanged(final CharSequence text, final int start, final int before, final int after) {
        super.onTextChanged(text, start, before, after);
        adjustTextSize();
    }

    @Override
    protected void onSizeChanged(final int width, final int height, final int oldwidth, final int oldheight) {
        super.onSizeChanged(width, height, oldwidth, oldheight);
        if (width != oldwidth || height != oldheight)
            adjustTextSize();
    }


    public void setTextBean(@NonNull TextBean bean) {
        mTextBean = bean;
        mTypeface = mTextBean.getTypeface();
        mTextColor = mTextBean.getTextColor();
        mBackgroundRes = mTextBean.getBackgroundRes();
        mBgDrawable = mTextBean.getBgDrawable();
        mImageRes = mTextBean.getImageRes();
        mDefaultWidth = mTextBean.getDefaultWidth();
        mDefaultHeight = mTextBean.getDefaultHeight();
        mText = mTextBean.getText();
        mImageWidth = mTextBean.getImageWidth();
        mImageHeight = mTextBean.getImageHeight();
        mImagePath = mTextBean.getImagePath();

        addBg();
        addImage();
        if (mText != null || mTypeface != null) {
            addText();
        }
    }

    private void addBg() {
        //画服务器下发贴图
        if (mImagePath != null) {
            setBackground(new BitmapDrawable(mContext.getResources(), BitmapUtils.getBitmap(mImagePath)));
            return;
        }
        //内置贴图
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
    }

    private void addText() {
        setTextTypeface(mTypeface);
        setText(mText);
        initColor();

    }

    public void setTextTypeface(String typeface) {
        Typeface type = null;
        if (typeface != null) {
            type = Typeface.createFromAsset(mContext.getAssets(), typeface);
        }
        setTypeface(type);
    }

    private void initColor() {
        setTextColor(mTextColor[0]);
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
            params.width = (int) (mDefaultWidth * scale);
            params.height = (int) (mDefaultHeight * scale);
            view.setLayoutParams(params);
            ((TextEditStickerWidget) getParent()).setWidthAndHeight(params.width, params.height);
        }
    }

    public void setScaleHeight(float scale) {
        scaleViewHeight(this, scale);
    }

    private void scaleViewHeight(View view, float scale) {
        if (view != null && view.getParent() != null) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            int height = (int) (params.height + scale);
            if (height >= SCALE_MIN_HEIGHT) {
                params.height = height;
                view.setLayoutParams(params);
                ((TextEditStickerWidget) getParent()).setWidthAndHeight(params.width, height);
                postInvalidate();
            }
        }
    }

    public void setScaleWidth(float scale) {
        scaleViewtWidth(this, scale);
    }

    private void scaleViewtWidth(View view, float scale) {
        if (view != null && view.getParent() != null) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            int width = (int) (params.width + scale);
            if (width >= SCALE_MIN_WIDTH) {
                params.width = width;
                view.setLayoutParams(params);
                ((TextEditStickerWidget) getParent()).setWidthAndHeight((int) width, params.height);
                postInvalidate();
            }
        }
    }


    private void drawImage(Canvas canvas) {
        if (mImageBitmap == null) {
            return;
        }
        Bitmap newBitmap = Bitmap.createScaledBitmap(mImageBitmap, mImageWidth, mImageHeight, true);
        canvas.drawBitmap(newBitmap, mTextBean.getImageX(), mTextBean.getImageY(), mBitmapPaint);
    }

    public void setText(String text, int lineCount) {
        mText = text;
        setText(mText);
        addBg();
        adjustTextSize();
    }


    public void setColor(int textColor) {
        setTextColor(textColor);
        mTextColor = new int[]{textColor, textColor};
    }

    public String getText() {
        return mText;
    }


}
