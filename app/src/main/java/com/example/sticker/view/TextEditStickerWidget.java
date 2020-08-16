package com.example.sticker.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.example.sticker.R;
import com.example.sticker.TextBean;
import com.example.sticker.WowApplication;
import com.example.sticker.utils.LogTagConstant;
import com.example.sticker.utils.Loger;



public class TextEditStickerWidget extends RelativeLayout {
    private static final String TAG = LogTagConstant.TEXT_STICKER_WIDGET;
    private static final int ROTATE_LINE_HEIGHT = 30;
    private final int mRotateWidth;
    private final int mRotateHeight;
    private float mWidgetScale = 1.0f;
    private float mHeightScale = 1.0f;
    private float mStartX;
    private float mStartY;
    private float mDownX;
    private float mDownY;
    private Context mContext;
    private ImageView mControlPoint;
    private ImageView mEditPoint;
    private ImageView mDeletePoint;
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
    private Paint mRotatePaint;
//    private Paint mTestPaint;

    private boolean mJustSelected = true;
    private ImageView mRotatePoint;
    private NotifyTextTouchListener mTouchingListener;
    private float mPx;
    private float mPy;
    private TextBean mTextBean;
    private TextEditStickerContent mContent;
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
    private float startRotation;
    private int mDeleteLeft;
    private int mDeleteTop;
    private int mEditLeft;
    private int mEditTop;
    private String mStatisticsLocation = "";
    private float mStatisticsTextSize;

    public TextEditStickerWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        setOrientation(LinearLayout.HORIZONTAL);
        setClipChildren(false);
        setClipToPadding(false);
        mContext = context.getApplicationContext();
        mRotateWidth = mContext.getResources().getDimensionPixelSize(R.dimen.text_rotate_point_width);
        mRotateHeight = mContext.getResources().getDimensionPixelSize(R.dimen.text_rotate_point_height);

        mRoundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        PathEffect effect = new DashPathEffect(new float[]{20, 20}, 1);
        mRoundPaint.setAntiAlias(true);
        mRoundPaint.setColor(getResources().getColor(R.color.white));
        mRoundPaint.setPathEffect(effect);
        mRoundPaint.setStyle(Paint.Style.STROKE);
        mRoundPaint.setStrokeWidth(3);

        mRotatePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRotatePaint.setAntiAlias(true);
        mRotatePaint.setColor(getResources().getColor(R.color.white));
        mRotatePaint.setStyle(Paint.Style.FILL);
        mRotatePaint.setStrokeWidth(3);

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
//        setBackgroundColor(Color.RED);
//        setGravity(Gravity.CENTER_VERTICAL);
//        adjustRotatePoint();
        addTextView();
        addScalePoint();
        addDeleteButton();
        addEditButton();
        addRotatePoint();
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
        if (mTextBean != null) {
            mTextBean = textBean;
            removeView(mContent);
            mContent = null;
            invalidate();
        } else {
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
            mRotatePoint.setVisibility(VISIBLE);
            postInvalidate();
        } else {
            mJustSelected = true;
            mControlPoint.setVisibility(INVISIBLE);
            mRotatePoint.setVisibility(INVISIBLE);
            mEditPoint.setVisibility(INVISIBLE);
            mDeletePoint.setVisibility(INVISIBLE);
            postInvalidate();

        }
    }

    public void setWidthAndHeight(int w, int h) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        layoutParams.width = w + (ROTATE_LINE_HEIGHT + mRotateWidth) * 2;
        layoutParams.height = h + (ROTATE_LINE_HEIGHT + mRotateHeight) * 2;
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

    public void refreshTextBean(TextBean textBean) {
        refreshTextBeanWithoutTranslate(textBean);
        LayoutParams params = (LayoutParams) mContent.getLayoutParams();
        params.width = mTextBean.getDefaultWidth();
        params.height = mTextBean.getDefaultHeight();
        params.addRule(CENTER_IN_PARENT);
        mContent.setLayoutParams(params);
        setWidthAndHeight(mTextBean.getDefaultWidth(), mTextBean.getDefaultHeight());
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
        setTextTypeface(textBean.getTypeface());
        setTextColor(mTextBean.getTextColor()[0]);
    }

    public void setTextColor(int color) {
        mContent.setColor(color);
        mContent.invalidate();
    }

    public void setTextTypeface(String typeface) {
        mContent.setTextTypeface(typeface);
    }

    public float getScale() {
        try {
            return mContent.getWidth() / mContent.getHeight();
        } catch (Exception e) {
            return 1;
        }
    }

    public void setScale(float scale) {
        mContent.setScale(scale);
    }

    public void setScaleWidth(float scale) {
        mContent.setScaleWidth(scale);
    }

    public void setScaleHeight(float scale) {
        mContent.setScaleHeight(scale);
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
                String loaction = mStatisticsLocation.contains("12") || mStatisticsLocation.contains("21") ? "12" : mStatisticsLocation.substring(mStatisticsLocation.length() - 1, mStatisticsLocation.length());
            }
            mStatisticsLocation = "";
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

    public void statisticsTextSize() {
        float textSize = mContent.getTextSize();
        if (textSize != 0 && mStatisticsTextSize != textSize) {
            mStatisticsTextSize = textSize;
        }
    }

    public void setHadScale(boolean hadScale) {
        mHadScale = hadScale;
    }


    private void addTextView() {
        initTextView();
        mContent.post(new Runnable() {
            @Override
            public void run() {
                if (mContent == null) {
                    return;
                }
                mContent.setMaxLines(100);
                mContent.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mSurfaceViewHeight, getResources()
                        .getDisplayMetrics()));
                LayoutParams params = (LayoutParams) mContent.getLayoutParams();
                params.width = mTextBean.getDefaultWidth();
                params.height = mTextBean.getDefaultHeight();
                params.addRule(CENTER_IN_PARENT);
                mContent.setLayoutParams(params);
                setWidthAndHeight(mTextBean.getDefaultWidth(), mTextBean.getDefaultHeight());
                setX(mSurfaceViewWidth / 2 - getMeasuredWidth() / 2);
                setY(mSurfaceViewHeight / 2 - getMeasuredHeight() / 2);
            }
        });
    }

    private void initTextView() {
        mContent = new TextEditStickerContent(WowApplication.getAppContext());
        mContent.setText(mTextBean.getText());
        mContent.setTextBean(mTextBean);
        LayoutParams layoutParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(CENTER_IN_PARENT);
        mContent.setId(android.R.id.content);
        addView(mContent, layoutParams);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addScalePoint() {
        mControlPoint = new ImageView(WowApplication.getAppContext());
        mControlPoint.setVisibility(INVISIBLE);
        mControlPoint.setId(android.R.id.icon);
        mControlPoint.setImageResource(R.drawable.ic_sticker_rotate);
        mControlPoint.setScaleType(ImageView.ScaleType.CENTER);
        int width = WowApplication.getAppContext().getResources().getDimensionPixelSize(R.dimen.text_control_point_width2);
        int height = WowApplication.getAppContext().getResources().getDimensionPixelSize(R.dimen.text_control_point_height2);
        LayoutParams layoutParams = new LayoutParams(width, height);
        layoutParams.addRule(ALIGN_PARENT_RIGHT);
        layoutParams.setMargins(0,
                0,
                ROTATE_LINE_HEIGHT + mRotateWidth - width / 2,
                ROTATE_LINE_HEIGHT + mRotateHeight - height / 2);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(mControlPoint, layoutParams);

        mControlPoint.setOnTouchListener(new OnTouchListener() {
            float mLastX;
            float mLastY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mScaling = true;
                        setSelecting(true);
                        mLastX = event.getRawX();
                        mLastY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        float rawX = event.getRawX() - mLastX;
                        float rawY = event.getRawY() - mLastY;
                        if (Math.abs(rawX) > 1) {
                            zoomWidth(rawX);
                        }
                        if (Math.abs(rawY) > 1) {
                            zoomHeight(rawY);
                        }
                        mLastX = event.getRawX();
                        mLastY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        mScaling = false;
                        return true;

                }

                return false;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addRotatePoint() {
        mRotatePoint = new ImageView(WowApplication.getAppContext());
        mRotatePoint.setVisibility(INVISIBLE);
        mRotatePoint.setId(android.R.id.button1);
        mRotatePoint.setImageResource(R.drawable.ic_sticker_rotate2);
        mRotatePoint.setScaleType(ImageView.ScaleType.CENTER);
        LayoutParams layoutParams = new LayoutParams(mRotateWidth , mRotateHeight);
        layoutParams.addRule(CENTER_HORIZONTAL);
        layoutParams.addRule(ALIGN_PARENT_BOTTOM);
        addView(mRotatePoint, layoutParams);

        mRotatePoint.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mInitAngle = (float) getRotateAngle(TextEditStickerWidget.this.getPivotX(),
                                TextEditStickerWidget.this.getPivotY() - v.getHeight() / 2,
                                TextEditStickerWidget.this.getPivotX(), TextEditStickerWidget.this.getPivotY());
                        mStartRotating = false;
                        setSelecting(true);
                        int[] loc = new int[2];
                        TextEditStickerWidget.this.getLocationOnScreen(loc);
                        mPx = TextEditStickerWidget.this.getX() + TextEditStickerWidget.this.getPivotX() + (mScreenWidth -
                                mSurfaceViewWidth) / 2.0f;
                        mPy = TextEditStickerWidget.this.getY() + TextEditStickerWidget.this.getPivotY() + (mScreenHeight -
                                mSurfaceViewHeight) / 2.0f;
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        float finalAngle = mInitAngle + (float) getRotateAngle(event.getRawX(), event.getRawY(), mPx, mPy);
                        if (mStartRotating) {
                            mHadRotate = true;
                            float angle = getAngleByRule(finalAngle);
                            TextEditStickerWidget.this.setRotation(angle);
                            TextEditStickerWidget.this.postInvalidate();
                            mStatisticsLocation = mStatisticsLocation.contains("1") ? mStatisticsLocation : mStatisticsLocation + "1";
                        }
                        mStartRotating = true;
                        return true;
                    case MotionEvent.ACTION_UP:
                        mStartRotating = false;
                        return true;

                }

                return false;
            }
        });
    }

    private float getAngleByRule(float finalAngle) {
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
        return angle;
    }

    private void addEditButton() {
        mEditPoint = new ImageView(WowApplication.getAppContext());
        mEditPoint.setVisibility(INVISIBLE);
        mEditPoint.setId(android.R.id.button2);
        mEditPoint.setImageResource(R.drawable.ic_sticker_edit);
        mEditPoint.setScaleType(ImageView.ScaleType.CENTER);
        int width = WowApplication.getAppContext().getResources().getDimensionPixelSize(R.dimen.text_control_point_width2);
        int height = WowApplication.getAppContext().getResources().getDimensionPixelSize(R.dimen.text_control_point_height2);
        LayoutParams layoutParams = new LayoutParams(width, height);
        layoutParams.addRule(ALIGN_PARENT_RIGHT);
        layoutParams.setMargins(0,
                ROTATE_LINE_HEIGHT + mRotateHeight - height / 2,
                ROTATE_LINE_HEIGHT + mRotateWidth - width / 2,
                0);
        addView(mEditPoint, layoutParams);
        mEditPoint.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditing = true;
                mTouchingListener.touch(TextEditStickerWidget.this, MotionEvent.ACTION_UP);
                mEditing = false;
            }
        });

    }


    private void addDeleteButton() {
        mDeletePoint = new ImageView(WowApplication.getAppContext());
        mDeletePoint.setVisibility(INVISIBLE);
        mDeletePoint.setId(android.R.id.button2);
        mDeletePoint.setImageResource(R.drawable.ic_sticker_close);
        mDeletePoint.setScaleType(ImageView.ScaleType.CENTER);
        int width = WowApplication.getAppContext().getResources().getDimensionPixelSize(R.dimen.text_control_point_width2);
        int height = WowApplication.getAppContext().getResources().getDimensionPixelSize(R.dimen.text_control_point_height2);
        LayoutParams layoutParams = new LayoutParams(width, height);
        layoutParams.setMargins(ROTATE_LINE_HEIGHT + mRotateWidth - width / 2,
                ROTATE_LINE_HEIGHT + mRotateHeight - height / 2,
                0,
                0);
        addView(mDeletePoint, layoutParams);
        mDeletePoint.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mClosing = true;
                mTouchingListener.touch(TextEditStickerWidget.this, MotionEvent.ACTION_DOWN);
                mClosing = false;
            }
        });
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

    private void zoomWidth(float f) {
        setScaleWidth(f);
        mHadScale = true;
    }

    private void zoomHeight(float f) {
        setScaleHeight(f);
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

//        canvas.drawPoint(mX2, mY2, mTestPaint);
//        canvas.drawPoint(mCx, mCy, mTestPaint);

        if (isSelecting()) {
            int rectLeft = (getMeasuredWidth() - mContent.getMeasuredWidth()) / 2;
            int rectTop = (getMeasuredHeight() - mContent.getMeasuredHeight()) / 2;
            int rectRight = rectLeft + mContent.getMeasuredWidth();
            int rectBottom = rectTop + mContent.getMeasuredHeight();
            canvas.drawRect(
                    rectLeft,
                    rectTop,
                    rectRight,
                    rectBottom,
                    mRoundPaint);

//            canvas.drawPoint(getMeasuredWidth() / 2, 30, mTestPaint);
            canvas.drawLine(
                    getMeasuredWidth() / 2,
                    rectBottom,
                    getMeasuredWidth() / 2,
                    rectBottom + ROTATE_LINE_HEIGHT,
                    mRotatePaint);

            drawLineDash();

            if (mIsShowEditButton) {
                mEditPoint.setVisibility(VISIBLE);
            } else {
                mEditPoint.setVisibility(INVISIBLE);
            }
            if (mIsShowDeleteButton) {
                mDeletePoint.setVisibility(VISIBLE);
            } else {
                mDeletePoint.setVisibility(INVISIBLE);
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
                mTouchingListener.touch(TextEditStickerWidget.this, MotionEvent.ACTION_DOWN);
                mTouchDownTime = System.currentTimeMillis();
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                mBaginScale = true;
                mStartRotating = true;
                if (event.getActionIndex() >= event.getPointerCount()) {
                    break;
                }
                startRotation = getRotation(event);

                return true;
            case MotionEvent.ACTION_MOVE:
                if (mStartRotating) {
                    //处理旋转
                    mHadRotate = true;
                    float endRotate = getRotation(event);
                    float rotate = endRotate - startRotation;
                    TextEditStickerWidget.this.setRotation(getAngleByRule(getRotation() + rotate));
                    TextEditStickerWidget.this.postInvalidate();
                    mStatisticsLocation = mStatisticsLocation.contains("2") ? mStatisticsLocation : mStatisticsLocation + "2";
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
                    mTouchingListener.touch(TextEditStickerWidget.this, MotionEvent.ACTION_MOVE);
                }
                return true;
            case MotionEvent.ACTION_UP:
                Loger.i(TAG, "mMoving --> " + mMoving);
                if (System.currentTimeMillis() - mTouchDownTime < 100 && !mJustSelected && !mClosing & !mEditing && !mScaling
                        && mTextBean
                        .isEditable()) {
                    mTouchingListener.touch(TextEditStickerWidget.this, MotionEvent.ACTION_UP);
                }
                mMoveX = 0;
                mMoveY = 0;
                mMoving = false;
                mStartRotating = false;
                mBaginScale = false;
                mClosing = false;
                mEditing = false;
                drawLineDash();

                return true;
            case MotionEvent.ACTION_POINTER_UP:
                mStartRotating = false;
                return true;

        }
        return super.onTouchEvent(event);
    }

    private float getRotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }


    double getRotateAngle(float x2, float y2, float cx, float cy) {
        double radian = Math.atan2(y2 - cy, x2 - cx);
        return Math.toDegrees(radian);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        if (!mDeletePoint.isRecycled()) {
//            mDeletePoint.recycle();
//        }
        mContext = null;
        Loger.i("TextStickerWidget", "---onDetachedFromWindow--");
    }

    public interface NotifyTextTouchListener {
        void touch(TextEditStickerWidget sf, int event);
    }

    public interface LineDashVisibleListener {
        void onHorizontalLineVisible(boolean i);

        void onVerticalLineVisible(boolean i);

        void onTextStickerSelected(boolean i);
    }
}
