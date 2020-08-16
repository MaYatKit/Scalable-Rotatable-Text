package com.example.sticker.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sticker.MainActivity;
import com.example.sticker.R;
import com.example.sticker.TextBean;
import com.example.sticker.WowApplication;
import com.example.sticker.utils.Loger;
import com.example.sticker.view.ConfirmCommonDialog;
import com.example.sticker.view.ConfirmDialog1;
import com.example.sticker.view.InputTextDialog;
import com.example.sticker.view.TextEditStickerWidget;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;




public class VideoTextEditFragment extends Fragment implements MainActivity.IEditTouchListener,
        TextEditStickerWidget.NotifyTextTouchListener, InputTextDialog.ICloseListener, TextEditStickerWidget.LineDashVisibleListener {
    public static final String EXTRA_DATA = "EXTRA_TEXT_BEAN";

    public static final String FRAGMENT_TAG = "FRAGMENT_TEXT_EDIT";
    private static final int INVALID_POINTER_ID = -1;
    private static final String SHOW_TEXT = WowApplication.getAppContext().getString(R.string.tap_to_edit);
    float mSpinFontScale = 1.0f;
    private View mRootView;
    private TextBean mTextBean;
    private MainActivity mActivity;
    private int mPtrID1, mPtrID2;
    private boolean mScaleing;
    private float mOldDist = 0;
    private List<TextEditStickerWidget> mFontList = Collections.synchronizedList(new ArrayList<TextEditStickerWidget>());
    private Context mApplicantionContext;
    private int mVideoWidth, mVideoHeight, mSurfaceViewWidth, mSurfaceViewHeight, mScreenWidth, mScreenHeight;
    private boolean mInterceptorText = false;
    private List<TextEditStickerWidget> mTempFontList = new ArrayList<>();
    private ConfirmDialog1 mCloseDialog;

    private VideoEditTextFragment mVideoEditTextFragment;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mActivity = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_edit_text, container, false);

        mActivity = (MainActivity) getActivity();
        mApplicantionContext = WowApplication.getAppContext();

        mVideoEditTextFragment = (VideoEditTextFragment) getFragmentManager().findFragmentById(R.id.video_edit_text_fragment);

        mRootView.setClickable(false);


        mRootView.post(new Runnable() {
            @Override
            public void run() {
                mVideoWidth = mRootView.getWidth();
                mVideoHeight = mRootView.getHeight();
                mSurfaceViewWidth = mRootView.getWidth();
                mSurfaceViewHeight = mRootView.getHeight();
                mScreenWidth = mRootView.getWidth();
                mScreenHeight = mRootView.getHeight();

            }
        });
        return mRootView;
    }




    public void setLineHorizontalVisible() {

    }

    public void setLineVerticalVisible() {

    }

    public void setLineHorizontalGone() {

    }

    public void setLineVerticalGone() {

    }

    public void setLineGone() {
        setLineHorizontalGone();
        setLineVerticalGone();
    }

    public void setLineVisible() {
        setLineHorizontalVisible();
        setLineVerticalVisible();
    }

    public void updateDrawingSize(int surfaceViewWidth, int surfaceViewHeight) {
        if (isAdded()) {
            /*mSurfaceViewWidth = surfaceViewWidth;
            mSurfaceViewHeight = surfaceViewHeight;
            mScreenWidth = screenWidth;
            mScreenHeight = screenHeight;
            ViewGroup.LayoutParams layoutParams = mRootView.getLayoutParams();
            layoutParams.width = mSurfaceViewWidth;
            layoutParams.height = mSurfaceViewHeight;
            mRootView.setLayoutParams(layoutParams);*/
            ViewGroup.LayoutParams layoutParams = mRootView.getLayoutParams();
            layoutParams.width = surfaceViewWidth;
            layoutParams.height = surfaceViewHeight;
            mRootView.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).setIEditTextTouchListener(this);


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        if (args == null) {
            return;
        }
        mTextBean = args.getParcelable(EXTRA_DATA);
        addText(mTextBean);
    }



    public void addText(TextBean textBean) {
        if (textBean == null) {
            return;
        }
        final TextEditStickerWidget widget;
        mActivity = (MainActivity) getActivity();
        if (mActivity != null) {
            widget = new TextEditStickerWidget(mApplicantionContext, null);
            widget.setScreenAndSurface(mSurfaceViewWidth, mSurfaceViewHeight,
                    mScreenWidth, mScreenHeight);
        } else {
            return;
        }
        if (textBean.getTextBeanId() <= 10) {
            textBean.setText(SHOW_TEXT);
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                textBean.getDefaultWidth(),
                textBean.getDefaultHeight(),
                Gravity.START);
        widget.setLayoutParams(layoutParams);
        widget.setTextBean(textBean);
        widget.setX(mSurfaceViewWidth / 2 - textBean.getDefaultWidth() / 2 - textBean.getDefaultWidth() / 5);
        widget.setY(mSurfaceViewHeight / 2 - textBean.getDefaultHeight() / 2 - textBean.getDefaultHeight() / 5);
        ((ViewGroup) mRootView).addView(widget, layoutParams);

        notifyAllUnSelect();
        widget.setTouchingListener(VideoTextEditFragment.this);
        widget.setVisibleListener(VideoTextEditFragment.this);
        widget.setSelecting(true);
        mFontList.add(widget);

    }


    public void isInterceptorText(boolean isInterceptorText) {
        this.mInterceptorText = isInterceptorText;
        setAllTextVisibility(false);
    }

    public void setAllTextVisibility(boolean visible) {
        if (mFontList != null && mFontList.size() > 0) {
            for (TextEditStickerWidget textStickerWidget : mFontList) {
                textStickerWidget.setVisible(visible);
            }
        }
    }

    public void setTextStickerDeleteButtonVisibility(boolean visibility) {
        if (mFontList != null && mFontList.size() > 0) {
            for (TextEditStickerWidget textStickerWidget : mFontList) {
                textStickerWidget.setDeleteButtonVisible(visibility);
                textStickerWidget.setEditeButtonVisible(visibility);
                textStickerWidget.invalidate();
            }
        }
    }


    public void deleteInvaildFonts() {
        /*for (int i = mFontList.size() - 1; i >= 0; i--) {
            TextTimeManager.Time time = TextTimeManager.getInstance().getTextTime(i);
            if (time.getStartTime() == -1 && time.getEndTime() == -1) {
                TextTimeManager.getInstance().deleteTime(i);
                mFontList.remove(i);
                ((ViewGroup) mRootView).removeViewAt(i);
            }
        }*/
    }

    public void refreshLocationByRatio(int w, int h, int screenWidth, int screenHeight) {
        for (TextEditStickerWidget textStickerWidget : mFontList) {
            int[] loc = new int[2];
            textStickerWidget.getLocationOnScreen(loc);
            int newX, newY, surfaceX, surfaceY;

            surfaceX = (mScreenWidth - mSurfaceViewWidth) / 2;
            surfaceY = (mScreenHeight - mSurfaceViewHeight) / 2;

            //获取文字左上角坐标
            int startX = loc[0] - surfaceX;
            int startY = loc[1] - surfaceY;
            //获取文字不旋转的时候中心点坐标
            int tempX = startX + textStickerWidget.getWidth() / 2;
            int tempY = startY + textStickerWidget.getHeight() / 2;
            //获取文字旋转之后的中心点坐标（角度要用弧度！不要用textStickerWidget.getRotation()！）
            int centerX = (int) ((tempX - startX) * Math.cos(textStickerWidget.getRotation() / 180 * Math.PI) - (tempY -
                    startY) * Math.sin(textStickerWidget.getRotation() / 180 * Math.PI) + startX);
            int centerY = (int) ((tempX - startX) * Math.sin(textStickerWidget.getRotation() / 180 * Math.PI) + (tempY -
                    startY) * Math.cos(textStickerWidget.getRotation() / 180 * Math.PI) + startY);

            //获取文字的最后坐标
            newX = (centerX - textStickerWidget.getWidth() / 2) * w / mSurfaceViewWidth;
            newY = (centerY - textStickerWidget.getHeight() / 2) * h / mSurfaceViewHeight;

            textStickerWidget.setX(newX);
            textStickerWidget.setY(newY);

            textStickerWidget.setScreenAndSurface(w, h, mScreenWidth, mScreenHeight);
        }
        mSurfaceViewWidth = w;
        mSurfaceViewHeight = h;
        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;
    }


    public void refreshParams(int surfaceViewWidth, int surfaceViewHeight, int videoWidth, int videoHeight) {
        this.mSurfaceViewWidth = surfaceViewWidth;
        this.mSurfaceViewHeight = surfaceViewHeight;
        this.mVideoWidth = videoWidth;
        this.mVideoHeight = videoHeight;
    }

    public String getFontName(int index) {
        if (index < 0 || mFontList == null || mFontList.size() == 0 || index >= mFontList.size()) {
            return "";
        }
        return mFontList.get(index).getTextBean().getName();
    }

    //过滤名字相同的字体
    public String getAddedFontsName() {
        StringBuilder stringBuilder = new StringBuilder();
        int fontSize = mFontList.size();
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < fontSize; i++) {
            String typeface = mFontList.get(i).getTextBean().getTypeface();
            Integer integer = map.get(typeface);
            map.put(typeface, integer == null ? 1 : integer + 1);
        }
        Set<Map.Entry<String, Integer>> set = map.entrySet();
        for (Map.Entry<String, Integer> entry : set) {
            stringBuilder.append(entry.getKey()).append("*").append(entry.getValue()).append("_");
        }
        if (fontSize > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }


    public String getAddedColors() {
        StringBuilder colors = new StringBuilder();
        int fontSize = mFontList.size();
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < fontSize; i++) {
            String color = Integer.toHexString(mFontList.get(i).getTextBean().getTextColor()[0]);
            Integer integer = map.get(color);
            map.put(color, integer == null ? 1 : integer + 1);
        }
        Set<Map.Entry<String, Integer>> set = map.entrySet();
        for (Map.Entry<String, Integer> entry : set) {
            colors.append(entry.getKey()).append("*").append(entry.getValue()).append("_");
        }
        if (fontSize > 0) {
            colors.deleteCharAt(colors.length() - 1);
        }
        return colors.toString();
    }

    public List<TextEditStickerWidget> getFonts() {
        return mFontList;
    }

    public String getFontText() {
        if (mFontList == null || mFontList.size() == 0) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        int index = 0;
        for (TextEditStickerWidget text : mFontList) {
            stringBuilder.append(text);
            index++;
            if (index != mFontList.size()) {
                stringBuilder.append("|");
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean touchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:

                mPtrID1 = event.getPointerId(event.getActionIndex());
                Loger.i(FRAGMENT_TAG, "getActionIndex-->" + event.getActionIndex());

                break;
            case MotionEvent.ACTION_POINTER_DOWN:
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
                mScaleing = true;

                return true;

            case MotionEvent.ACTION_MOVE:
                if (mScaleing) {
                    //处理缩放
                    if (event.getPointerCount() == 1 || mPtrID2 == INVALID_POINTER_ID) {
                        break;
                    }
                    float newDist = spacing(event, mPtrID1, mPtrID2);
                    mSpinFontScale = newDist / mOldDist;
                    float scaled = 1.0f;
                    for (TextEditStickerWidget font : mFontList) {
                        if (font.isSelecting()) {
                            scaled = font.getScale();
                        }
                    }
                    if (mSpinFontScale * scaled > 3 || mSpinFontScale * scaled < 0.5) {
                        break; //最大不超过3倍，最小不超过0.5倍
                    }
//                    if (newDist > mOldDist + 1) {
//                        zoom(mSpinFontScale);
//                        mOldDist = newDist;
//                    }
//                    if (newDist < mOldDist - 1) {
//                        zoom(mSpinFontScale);
//                        mOldDist = newDist;
//                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                if (mScaleing) {
                    mScaleing = false;
                } else {
                    notifyAllUnSelect();
                }
                mPtrID1 = INVALID_POINTER_ID;
                break;
            case MotionEvent.ACTION_POINTER_UP:
//                mScaleing = false;
                mPtrID2 = INVALID_POINTER_ID;
                break;
        }

        return false;
    }


    public void notifyAllUnSelect() {
        for (TextEditStickerWidget font : mFontList) {
            font.setSelecting(false);
            font.staticRotateAndScale();
            font.statisticsTextSize();
        }
    }

    public void notifyAllEditEnd(boolean isEnd) {
        for (TextEditStickerWidget font : mFontList) {
            font.setEditEnd(isEnd);
        }
    }

    //缩放实现
    private void zoom(float f) {
        TextEditStickerWidget widget = null;
        for (TextEditStickerWidget font : mFontList) {
            if (font.isSelecting()) {
                widget = font;
                break;
            }
        }
        if (widget != null) {
            widget.setScale(f);
            widget.setHadScale(true);
        }
    }

    /**
     * 计算两点之间的距离
     *
     * @param event 触摸事件
     * @return 两点之间的距离
     */
    private float spacing(MotionEvent event, int ID1, int ID2) {
        Loger.i(FRAGMENT_TAG, "ID1-->" + ID1);
        Loger.i(FRAGMENT_TAG, "ID2-->" + ID2);
        Loger.i(FRAGMENT_TAG, "event.getPointerCount-->" + event.getPointerCount());
        float x = event.getX(ID1) - event.getX(ID2);
        float y = event.getY(ID1) - event.getY(ID2);
        return (float) Math.sqrt(x * x + y * y);
    }


    public TextEditStickerWidget getNewestTextEditStickerWidget() {
        if (mFontList.size() > 0) {
            return mFontList.get(mFontList.size() - 1);
        }
        return null;
    }

    private void addListLog(String tag) {
        for (TextEditStickerWidget textStickerWidget : mFontList) {
            Loger.v("textedit", tag + textStickerWidget.getTextBean().toString());
        }
    }

    private void deleteTextByIndex(TextEditStickerWidget sf, int index) {
        //删除文字
        ((ViewGroup) mRootView).removeView(sf);
        //mActivity.getVideoEditMainFragmentManager().getVideoEditTextChooseFragment().refreshChooseBg();
        mFontList.remove(sf);
        if (mTempFontList.contains(sf)) {
            mTempFontList.remove(sf);
        }
    }

    @Override
    public void touch(TextEditStickerWidget sf, int event) {
        if (sf.isSelecting() && mActivity != null && event == MotionEvent.ACTION_UP) {
            //打开修改页面
            Loger.i(FRAGMENT_TAG, "open");
            String existText = sf.getText().trim();
            /*if (existText.equals(sf.getTextBean().getOriginalText())) {
                existText = mApplicantionContext.getString(R.string.tap_to_edit);
            }*/
            InputTextDialog dialog = new InputTextDialog(mActivity, R.style.Theme_AppCompat_Translucent_Fullscreen, this,
                    existText);
            dialog.show();
        } else if (sf.isSelecting() && mActivity != null
                && event == MotionEvent.ACTION_DOWN && sf.isClosing()) {
            showCloseDialog(sf);
        } else if (sf.isSelecting() && mActivity != null
                && event == MotionEvent.ACTION_DOWN && sf.isEditing()) {
            mVideoEditTextFragment.refreshListState(sf);
        } else if (mActivity != null && event == MotionEvent.ACTION_DOWN) {
            for (TextEditStickerWidget font : mFontList) {
                if (!font.equals(sf) && (font.isMoving() || font.isScaling() || font.isStartRotating())) {
                    //如果当前有其他字体在操作，那这个字体就不能选中
                    return;
                }
            }
            sf.setSelecting(true);
            sf.bringToFront();
            for (TextEditStickerWidget font : mFontList) {
                if (!font.equals(sf)) {
                    font.setSelecting(false);
                }
            }

            int index = mFontList.indexOf(sf);
            //getEditProgressFragment().setProgress((int) TextTimeManager.getInstance().getCurrentTime().getStartTime());
            /*mActivity.getVideoEditMainFragmentManager().getVideoEditTextChooseFragment().slideDownRecyclerView();
            mActivity.getVideoEditMainFragmentManager().getVideoEditTextChooseFragment().showPlayBtn(false);
            mActivity.getVideoEditMainFragmentManager().getVideoEditTextChooseFragment().
                    setRangeSeekBar(TextTimeManager.getInstance().getCurrentTime().getStartTime(),
                            TextTimeManager.getInstance().getCurrentTime().getEndTime());
            mActivity.pauseVideo();*/

        } /*else if (mActivity != null && event == MotionEvent.ACTION_MOVE) {
            mActivity.getVideoEditMainFragmentManager().getVideoEditTextChooseFragment().slideDownRecyclerView();
        }*/
    }

    private void showCloseDialog(final TextEditStickerWidget sf) {
        if (mCloseDialog == null) {
            mCloseDialog = new ConfirmDialog1(getActivity(), true);
        }
        mCloseDialog.setContentText(R.string.video_text_delete_text_dialog_message);
        mCloseDialog.setContentGravity(Gravity.CENTER);
        mCloseDialog.setOkText(R.string.yes);
        mCloseDialog.setCancelText(R.string.cancel);
        mCloseDialog.setOnConfirmDetailListener(new ConfirmCommonDialog.OnConfirmDetailListener() {
            @Override
            public void onConfirm() {
                performClose(sf);
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onBackPress() {

            }
        });
        mCloseDialog.show();
    }

    private void performClose(TextEditStickerWidget sf) {
        deleteText(sf);
        String textStyle = sf.getTextBean().getTypeface();
        if (textStyle == null) {
            //目前只有"I'onHorizontalLineVisible subtitle1"这个字体没有用字体文件
            textStyle = "subtitle1";
        }
    }

    public void deleteText(TextEditStickerWidget sf) {
        int index = mFontList.indexOf(sf);
        deleteTextByIndex(sf, index);
        setLineGone();
    }

    @Override
    public void close(String text, int lineCount) {
        if (text == null) {
            return;
        }
        int index = 0;
        for (TextEditStickerWidget font : mFontList) {
            if (font.isSelecting()) {
                String curText = font.getText();
                if (curText.trim().equals(getString(R.string.app_name))
                        && text.equals("")) {
                    setTextStickerText(curText + text, lineCount, font);
                } else if (text.equals("")) {
                    //删除文字
                    deleteTextByIndex(font, index);
                    String textStyle = font.getTextBean().getTypeface();
                    if (textStyle == null) {
                        //目前只有"I'onHorizontalLineVisible subtitle1"这个字体没有用字体文件
                        textStyle = "subtitle1";
                    }
                    //font.setText(mApplicantionContext.getResources().getString(R.string.app_name), 1);
//                    font.setMaxLines(1);
                } else {
                    setTextStickerText(text, lineCount, font);
//                    font.setMaxLines(lineCount);
                }
            }
            index++;
        }
    }

    public void setTextStickerText(String text, int lineCount, TextEditStickerWidget font) {
        font.setText(text, lineCount);
    }

    @Override
    public void cancel() {
    }


    public int getTextNum() {
        if (mRootView == null) {
            return 0;
        } else {
            deleteInvaildFonts();
            return ((ViewGroup) mRootView).getChildCount();
        }
    }

    public void setAddOrDelete() {
        mTempFontList.clear();
        mTempFontList.addAll(mFontList);
    }

    public boolean shouldUACDot() {
//        if (mTempFontList.size() != mFontList.size()) {
//            return true;
//        } else {
//            int size = mTempFontList.size();
//            for (int i = 0; i < size; i++) {
//                if (!mTempFontList.get(i).equals(mFontList.get(i))) {
//                    return true;
//                }
//            }
//        }
        return mTempFontList.size() != mFontList.size();
    }

    public void selectFonts(int index) {
        if (index < 0 || index >= mFontList.size()) {
            return;
        }
        TextEditStickerWidget sf = mFontList.get(index);
        sf.setSelecting(true);
        for (TextEditStickerWidget font : mFontList) {
            if (!font.equals(sf)) {
                font.setSelecting(false);
            }
        }
    }

    @Override
    public void onHorizontalLineVisible(boolean visible) {
        if (visible) {
            setLineHorizontalVisible();
        } else {
            setLineHorizontalGone();
        }
    }

    public void onVerticalLineVisible(boolean visible) {
        if (visible) {
            setLineVerticalVisible();
        } else {
            setLineVerticalGone();
        }
    }

    public void onTextStickerSelected(boolean selected) {
        if (!selected) {
            setLineGone();
        }
    }
}
