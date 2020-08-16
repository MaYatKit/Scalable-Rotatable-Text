package com.example.sticker.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.sticker.R;
import com.example.sticker.TextBean;
import com.example.sticker.WowApplication;
import com.example.sticker.utils.LogTagConstant;
import com.example.sticker.utils.Loger;



public class TextStickerWidget extends RelativeLayout {
    private static final String TAG = LogTagConstant.TEXT_STICKER_WIDGET;
    private float mWidgetScale = 1.0f;
    private float mStartX;
    private float mStartY;
    private float mDownX;
    private float mDownY;
    private Context mContext;
    private ImageView mControlPoint;
    private float mInitAngle;
    private boolean mMoving;
    private boolean mStartRotating;
    private boolean mScaling;
    private float mMoveX;
    private float mMoveY;
    private long mStartTime;
    private long mEndTime;
    private boolean mSelecting;
    private boolean mClosing;
    private boolean mEditing;

    private Paint mRoundPaint;
    private Paint mDeletePaint;

    private boolean mJustSelected = true;
    private Bitmap mDeletePoint;
    private Bitmap mEditPoint;
    private NotifyTextTouchListener mTouchingListener;
    private int mPaddingSpace;
    private int mMarginSpace;
    private float mOldDist = 0, mNewDist = 0;
    private float mPx;
    private float mPy;
    private int mPtrID1;
    private int mPtrID2;
    private TextBean mTextBean;
    private TextStickerContent mContent;
    private int mControlPointWidth;
    private int mControlPointHeight;
    private int mSurfaceViewWidth, mSurfaceViewHeight, mScreenWidth, mScreenHeight;
    private long mTouchDownTime;

    private boolean mHadRotate;
    private boolean mHadScale;

    private boolean mIsEditEnd;
    public int mTouchSlope;

    private boolean mBaginScale;
    private boolean mIsShowDeleteButton = true;
    private boolean mIsShowEditButton;

    private static final int HORIZONTAL_OFFSET = 5;
    private static final int ANGLE_OFFSET = 5;
    private int[] mHorizontalOffset = new int[2];
    private int[] mVerticalOffset = new int[2];
    private int[] mAngleOffset = new int[]{0, 90, 180};
    private LineDashVisibleListener mVisibleListener;

    public TextStickerWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        setOrientation(LinearLayout.HORIZONTAL);
        setClipChildren(false);
        setClipToPadding(false);
        mContext = context.getApplicationContext();
        mPaddingSpace = context.getResources().getDimensionPixelSize(R.dimen.edit_text_space);
        mMarginSpace = context.getResources().getDimensionPixelSize(R.dimen.text_margin);

        mRoundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        PathEffect effect = new DashPathEffect(new float[]{20, 20}, 1);
        mRoundPaint.setAntiAlias(true);
        mRoundPaint.setColor(getResources().getColor(R.color.white));
        mRoundPaint.setPathEffect(effect);
        mRoundPaint.setStyle(Paint.Style.STROKE);
        mRoundPaint.setStrokeWidth(3);

        mDeletePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDeletePaint.setAntiAlias(true);
        mDeletePaint.setFilterBitmap(true);
        mDeletePaint.setDither(true);

        mStartTime = 0;
        mEndTime = Long.MAX_VALUE;
        setWillNotDraw(false);
        mTouchSlope = ViewConfiguration.get(mContext).getScaledTouchSlop();
    }

    public void setScreenAndSurface(int surfaceViewWidth, int surfaceViewHeight, int screenWidth, int screenHeight) {
        mSurfaceViewWidth = surfaceViewWidth;
        mSurfaceViewHeight = surfaceViewHeight;
        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;
        mHorizontalOffset[0] = mSurfaceViewHeight / 2 - HORIZONTAL_OFFSET;
        mHorizontalOffset[1] = mSurfaceViewHeight / 2 + HORIZONTAL_OFFSET;
        mVerticalOffset[0] = mSurfaceViewWidth / 2 - HORIZONTAL_OFFSET;
        mVerticalOffset[1] = mSurfaceViewWidth / 2 + HORIZONTAL_OFFSET;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setGravity(Gravity.CENTER_VERTICAL);
        addTextView();
        addRotateControlPoint();
        addDeleteButton();
        addEditButton();

    }


    public void setTouchingListener(NotifyTextTouchListener mTouchingListener) {
        this.mTouchingListener = mTouchingListener;
    }

    public void setVisibleListener(LineDashVisibleListener visibleListener) {
        this.mVisibleListener = visibleListener;
    }

    public void setVisible(boolean visible) {
        if (visible) {
            setVisibility(VISIBLE);
        } else {
            setVisibility(GONE);
        }
    }

    public TextBean getTextBean() {
        return mTextBean;
    }

    public void setTextBean(TextBean textBean) {
        if (mTextBean != null && !mTextBean.getText().equals(textBean.getText())) {
            mTextBean = textBean;
            removeView(mContent);
            mContent = null;
            invalidate();
        } else if (mTextBean == null) {
            mTextBean = textBean;
        }
    }

    public void setRange(int startTime, int endTime) {
        this.mStartTime = startTime;
        this.mEndTime = endTime;
    }

    public long getStartTime() {
        return mStartTime;
    }

    public long getEndTime() {
        return mEndTime;
    }

    public boolean isSelecting() {
        return mSelecting;
    }

    public boolean getSelecting() {
        return mSelecting;
    }

    public void setSelecting(boolean mSelecting) {
        this.mSelecting = mSelecting;
        if (mSelecting) {
            if (mJustSelected && mControlPoint.getVisibility() == VISIBLE) {
                mJustSelected = false;
            }
            mControlPoint.setVisibility(VISIBLE);
//            mDeletePoint.setVisibility(VISIBLE);
            postInvalidate();
        } else {
            mJustSelected = true;
            mControlPoint.setVisibility(INVISIBLE);
            postInvalidate();

        }
    }

    public void setWidthAndHeight(int w, int h) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        layoutParams.width = w + mMarginSpace;
        layoutParams.height = h + mMarginSpace;
        setLayoutParams(layoutParams);
    }

    public boolean isClosing() {
        return mClosing;
    }

    public boolean isEditing() {
        return mEditing;
    }

    public View getContentView() {
        return mContent;
    }

    public String getText() {
        return mContent.getText();
    }

    public void setText(String text, int lineCount) {
        mContent.setText(text, lineCount);
        mContent.invalidate();
    }

    public void setMaxLines(int i) {
//        mContent.getTextView().setMaxLines(i);
    }

    public void refreshTextBean(TextBean textBean) {
        mTextBean = textBean;
        mContent.setTextBean(mTextBean);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mContent.getLayoutParams();
        params.width = mTextBean.getDefaultWidth();
        params.height = mTextBean.getDefaultHeight();
        params.addRule(CENTER_IN_PARENT);
        params.setMargins(0, mMarginSpace / 4, 0, mMarginSpace / 2);
        mContent.setLayoutParams(params);
        setWidthAndHeight(mTextBean.getDefaultWidth(), mTextBean.getDefaultHeight());
        mContent.invalidate();
        invalidate();
        post(new Runnable() {
            @Override
            public void run() {
                if (mContent == null) {
                    return;
                }
                setX(mSurfaceViewWidth / 2 - getMeasuredWidth() / 2);
                setY(mSurfaceViewHeight / 2 - getMeasuredHeight() / 2);
            }
        });
    }

    public void refreshTextBeanWithoutTranslate(TextBean textBean) {
        mTextBean = textBean;
        mContent.setTextBean(mTextBean);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mContent.getLayoutParams();
        params.width = mTextBean.getDefaultWidth();
        params.height = mTextBean.getDefaultHeight();
        params.addRule(CENTER_IN_PARENT);
        params.setMargins(0, mMarginSpace / 4, 0, mMarginSpace / 2);
        mContent.setLayoutParams(params);
        setWidthAndHeight(mTextBean.getDefaultWidth(), mTextBean.getDefaultHeight());
        mContent.invalidate();
        invalidate();
    }

    public void setTextColor(int color) {
        mContent.setTextColor(color);
        mContent.invalidate();
    }

    public void setTextTypeface(String typeface) {
        mContent.setTextTypeface(typeface);
    }

    public float getScale() {
        return mContent.getScale();
    }

    public void setScale(float scale) {
        mContent.setScale(scale);
    }

    public boolean isMoving() {
        return mMoving;
    }

    public boolean isScaling() {
        return mScaling;
    }

    public boolean isStartRotating() {
        return mStartRotating;
    }


    public void staticRotateAndScale() {
        String textStyle = getTextBean().getTypeface();
        if (textStyle == null) {
            textStyle = "subtitle1";
        }

        if (mHadRotate) {
            if (mTextBean.getText() == null && mTextBean.getTypeface() == null) {
                String emojiName = mTextBean.getName();
            } else {
            }
            mHadRotate = false;
        }
        if (mHadScale) {
            if (mTextBean.getText() == null && mTextBean.getTypeface() == null) {
                String emojiName = mTextBean.getName();
            } else {
            }

            mHadScale = false;
        }
    }

    public void setHadScale(boolean hadScale) {
        mHadScale = hadScale;
    }

    private void addTextView() {
        mContent = new TextStickerContent(WowApplication.getAppContext());
        mContent.setTextBean(mTextBean);
        RelativeLayout.LayoutParams layoutParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(CENTER_IN_PARENT);
        mContent.setId(android.R.id.content);
        addView(mContent, layoutParams);
//        mContent.setParentWidthAndHeight();
        mContent.post(new Runnable() {
            @Override
            public void run() {
                if (mContent == null) {
                    return;
                }
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mContent.getLayoutParams();
                params.width = mTextBean.getDefaultWidth();
                params.height = mTextBean.getDefaultHeight();
                params.addRule(CENTER_IN_PARENT);
                params.setMargins(0, mMarginSpace / 4, 0, mMarginSpace / 2);
                mContent.setLayoutParams(params);
                setWidthAndHeight(mTextBean.getDefaultWidth(), mTextBean.getDefaultHeight());
                setX(mSurfaceViewWidth / 2 - getMeasuredWidth() / 2);
                setY(mSurfaceViewHeight / 2 - getMeasuredHeight() / 2);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addRotateControlPoint() {
//        mControlPoint = new ImageView(mContext);
        mControlPoint = new ImageView(WowApplication.getAppContext());
        mControlPoint.setVisibility(INVISIBLE);
        mControlPoint.setId(android.R.id.icon);
        mControlPoint.setImageResource(R.drawable.ic_sticker_rotate);
        mControlPoint.setScaleType(ImageView.ScaleType.CENTER);
        int width = WowApplication.getAppContext().getResources().getDimensionPixelSize(R.dimen.text_control_point_width);
        int height = WowApplication.getAppContext().getResources().getDimensionPixelSize(R.dimen.text_control_point_height);
        int padding = WowApplication.getAppContext().getResources().getDimensionPixelSize(R.dimen.text_control_point_margin);
        RelativeLayout.LayoutParams layoutParams = new LayoutParams(width, height);
        layoutParams.addRule(ALIGN_PARENT_RIGHT);
        layoutParams.setMargins(0, 0, 0, -mPaddingSpace);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mControlPoint.setPadding(width / 2, (padding - height) / 2, 0, padding / 2);
        addView(mControlPoint, layoutParams);

        mControlPoint.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mInitAngle = (float) getRotateAngle(TextStickerWidget.this.getMeasuredWidth() - v.getWidth() / 2,
                                TextStickerWidget.this.getMeasuredHeight() - v.getHeight() / 2,
                                TextStickerWidget.this.getPivotX(), TextStickerWidget.this.getPivotY());
                        mStartRotating = false;
                        mScaling = true;
                        setSelecting(true);
                        int[] loc = new int[2];
                        TextStickerWidget.this.getLocationOnScreen(loc);
                        mPx = TextStickerWidget.this.getX() + TextStickerWidget.this.getPivotX() + (mScreenWidth -
                                mSurfaceViewWidth) / 2.0f;
                        mPy = TextStickerWidget.this.getY() + TextStickerWidget.this.getPivotY() + (mScreenHeight -
                                mSurfaceViewHeight) / 2.0f;
                        mOldDist = spacing(mPx, event.getRawX(), mPy, event.getRawY());
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        float finalAngle = -mInitAngle + (float) getRotateAngle(event.getRawX(), event.getRawY(), mPx, mPy);
                        if (mStartRotating) {
                            mHadRotate = true;
                            float absAngle = Math.abs(finalAngle);
                            if (absAngle >= mAngleOffset[0] && absAngle <= mAngleOffset[0] + ANGLE_OFFSET) {
                                absAngle = mAngleOffset[0];
                            } else if (absAngle >= mAngleOffset[1] - ANGLE_OFFSET && absAngle <= mAngleOffset[1] + ANGLE_OFFSET) {
                                absAngle = mAngleOffset[1];
                            } else if (absAngle >= mAngleOffset[2] - ANGLE_OFFSET && absAngle <= mAngleOffset[2] + ANGLE_OFFSET) {
                                absAngle = mAngleOffset[2];
                            }
                            float angle;
                            if (finalAngle > 0) {
                                angle = absAngle;
                            } else {
                                angle = -absAngle;
                            }
                            TextStickerWidget.this.setRotation(angle);
                            TextStickerWidget.this.postInvalidate();
                        }
                        mStartRotating = true;
                        int[] loc2 = new int[2];
                        TextStickerWidget.this.getLocationOnScreen(loc2);

                        float distance = spacing(mPx, event.getRawX(), mPy, event.getRawY());
                        if (distance / mOldDist * mContent.getScale() > 3 || distance / mOldDist * mContent.getScale() < 0.5) {
                            return true;
                        }

                        mNewDist = distance;
                        mWidgetScale = mNewDist / mOldDist;
                        if (mNewDist > mOldDist + 1) {
                            zoom(mWidgetScale);
                            mOldDist = mNewDist;
                        }
                        if (mNewDist < mOldDist - 1) {
                            zoom(mWidgetScale);
                            mOldDist = mNewDist;
                        }

                        return true;
                    case MotionEvent.ACTION_UP:
                        mStartRotating = false;
                        mScaling = false;
                        return true;

                }

                return false;
            }
        });
    }

    private void addEditButton() {
        mEditPoint = BitmapFactory.decodeResource(WowApplication.getAppContext().getResources(), R.drawable.ic_sticker_edit);

    }

    private void addDeleteButton() {
        mDeletePoint = BitmapFactory.decodeResource(WowApplication.getAppContext().getResources(), R.drawable.ic_sticker_close);
    }

    public void setDeleteButtonVisible(boolean visible) {
        mIsShowDeleteButton = visible;
    }

    public void setEditeButtonVisible(boolean visible) {
        mIsShowEditButton = visible;
    }

    private float spacing(float x1, float x2, float y1, float y2) {
        float x = x1 - x2;
        float y = y1 - y2;
        return (float) Math.sqrt(x * x + y * y);
    }

    private float spacing(MotionEvent event, int ID1, int ID2) {
        float x = event.getX(ID1) - event.getX(ID2);
        float y = event.getY(ID1) - event.getY(ID2);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void zoom(float f) {
        setScale(f);
        mHadScale = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mControlPointWidth == 0 || mControlPointHeight == 0) {
            mControlPointWidth = mControlPoint.getMeasuredWidth();
            mControlPointHeight = mControlPoint.getMeasuredHeight();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mContent == null) {
            addTextView();
        }
        if (isSelecting()) {
            int rectLeft = mDeletePoint.getWidth() / 2;
            int rectTop = mDeletePoint.getHeight() / 2;
            int rectRight = getMeasuredWidth() - mDeletePoint.getWidth() / 2;
            int rectBottom = getMeasuredHeight() - mDeletePoint.getWidth();
            canvas.drawRect(
                    rectLeft,
                    rectTop,
                    rectRight,
                    rectBottom,
                    mRoundPaint);

            drawLineDash();

            if (mIsShowDeleteButton) {
                canvas.drawBitmap(mDeletePoint,
                        0,
                        0,
                        mDeletePaint);
            }

            if (mIsShowEditButton) {
                canvas.drawBitmap(mEditPoint,
                        getMeasuredWidth() - mEditPoint.getWidth(),
                        0,
                        mDeletePaint);
            }

        } else {
            if (mVisibleListener != null) {
                mVisibleListener.onTextStickerSelected(false);
            }
        }
    }

    private void drawLineDash() {
        int top = (int) getY();
        int left = (int) getX();
        int rectCentralPointY = top + (getMeasuredHeight() / 2);
        int rectCentralPointX = left + (getMeasuredWidth()) / 2;
        if (rectCentralPointY >= mHorizontalOffset[0] && rectCentralPointY <= mHorizontalOffset[1] && mMoving) {
            setY(mSurfaceViewHeight / 2 - getMeasuredHeight() / 2);
            if (mVisibleListener != null) {
                mVisibleListener.onHorizontalLineVisible(true);
            }
        } else {
            if (mVisibleListener != null) {
                mVisibleListener.onHorizontalLineVisible(false);
            }
        }

        if (rectCentralPointX >= mVerticalOffset[0] && rectCentralPointX <= mVerticalOffset[1] && mMoving) {
            setX(mSurfaceViewWidth / 2 - getMeasuredWidth() / 2);
            if (mVisibleListener != null) {
                mVisibleListener.onVerticalLineVisible(true);
            }
        } else {
            if (mVisibleListener != null) {
                mVisibleListener.onVerticalLineVisible(false);
            }
        }

    }

    public boolean isEditEnd() {
        return mIsEditEnd;
    }

    public void setEditEnd(boolean editEnd) {
        mIsEditEnd = editEnd;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev) && !mIsEditEnd;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsEditEnd) {
            return false;
        }
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = event.getRawX();
                mStartY = event.getRawY();
                mDownX = mStartX;
                mDownY = mStartY;
                if (event.getX() < mControlPointWidth / 2
                        && event.getY() < mControlPointHeight / 2 && mIsShowDeleteButton) {
                    mClosing = true;
                }
                if ((event.getX() < getMeasuredWidth() && event.getX() > getMeasuredWidth() - mEditPoint.getWidth())
                        && event.getY() < mControlPointHeight / 2 && mIsShowEditButton) {
                    mEditing = true;
                }
                mTouchingListener.touch(TextStickerWidget.this, MotionEvent.ACTION_DOWN);
//                setSelecting(true);
                mPtrID1 = event.getPointerId(event.getActionIndex());
                mTouchDownTime = System.currentTimeMillis();
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                mBaginScale = true;
                mScaling = true;
                if (event.getActionIndex() >= event.getPointerCount()) {
                    break;
                }
                mPtrID2 = event.getPointerId(event.getActionIndex());
                if (mPtrID1 < 0 || mPtrID1 > event.getPointerCount() - 1) {
                    mPtrID1 = 0;
                }
                if (mPtrID2 < 0 || mPtrID2 > event.getPointerCount() - 1) {
                    mPtrID2 = event.getPointerCount() - 1;
                }
                mOldDist = spacing(event, mPtrID1, mPtrID2);

                return true;
            case MotionEvent.ACTION_MOVE:
                if (mScaling) {
                    if (mPtrID1 < 0 || mPtrID2 < 0) {
                        break;
                    }
                    float newDist = spacing(event, mPtrID1, mPtrID2);
                    if (newDist / mOldDist * mContent.getScale() > 3 || newDist / mOldDist * mContent.getScale() < 0.5) {
                        return true;
                    }
                    mWidgetScale = newDist / mOldDist;
                    if (newDist > mOldDist + 1) {
                        zoom(mWidgetScale);
                        mOldDist = newDist;
                    }
                    if (newDist < mOldDist - 1) {
                        zoom(mWidgetScale);
                        mOldDist = newDist;
                    }
                } else if (mSelecting && !mBaginScale) {
                    mMoveX = mMoveX + (event.getRawX() - mStartX);
                    mMoveY = mMoveY + (event.getRawY() - mStartY);
                    if (mMoveX > 2 || mMoveY > 2 || mMoveX < -2 || mMoveY < -2) {
                        setX(getX() + mMoveX);
                        setY(getY() + mMoveY);
                        invalidate();
                        mMoveX = 0;
                        mMoveY = 0;
                        mStartX = event.getRawX();
                        mStartY = event.getRawY();
                        mMoving = true;
                    }
                }
                if (Math.abs(event.getRawX() - mDownX) > mTouchSlope || Math.abs(event.getRawY() - mDownY) > mTouchSlope) {
                    mTouchingListener.touch(TextStickerWidget.this, MotionEvent.ACTION_MOVE);
                }
                return true;
            case MotionEvent.ACTION_UP:
                Loger.i(TAG, "mMoving --> " + mMoving);
                if (System.currentTimeMillis() - mTouchDownTime < 100 && !mJustSelected && !mClosing & !mEditing && !mScaling
                        && mTextBean
                        .isEditable()) {
                    mTouchingListener.touch(TextStickerWidget.this, MotionEvent.ACTION_UP);
                }
                mMoveX = 0;
                mMoveY = 0;
                mMoving = false;
                mScaling = false;
                mPtrID1 = -1;
                mBaginScale = false;
                mClosing = false;
                mEditing = false;
                drawLineDash();

                return true;
            case MotionEvent.ACTION_POINTER_UP:
                mScaling = false;
                mPtrID2 = -1;
                return true;

        }
        return super.onTouchEvent(event);
    }


    double getRotateAngle(float x2, float y2, float cx, float cy) {
        double radian = Math.atan2(y2 - cy, x2 - cx);
        return Math.toDegrees(radian);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (!mDeletePoint.isRecycled()) {
            mDeletePoint.recycle();
        }
        mContext = null;
        Loger.i("TextStickerWidget", "---onDetachedFromWindow--");
    }

    public interface NotifyTextTouchListener {
        void touch(TextStickerWidget sf, int event);
    }

    public interface LineDashVisibleListener {
        void onHorizontalLineVisible(boolean i);

        void onVerticalLineVisible(boolean i);

        void onTextStickerSelected(boolean i);
    }
}
