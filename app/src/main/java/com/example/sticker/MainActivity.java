package com.example.sticker;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sticker.fragment.VideoEditTextFragment;
import com.example.sticker.fragment.VideoTextEditFragment;
import com.example.sticker.view.RuptChildClickRelativeLayout;
import com.example.sticker.view.TextStickerContent;

import java.util.ArrayList;


public class MainActivity extends FragmentActivity {

    // Panel for choosing sticker
    private VideoEditTextFragment mVideoEditTextFragment;
    // Panel for displaying sticker
    private VideoTextEditFragment mStickerDisplayFragment;
    private IEditTouchListener mTextTouchListener;

    private RuptChildClickRelativeLayout mParentLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_layout);
        mVideoEditTextFragment = new VideoEditTextFragment();
        mStickerDisplayFragment = new VideoTextEditFragment();
        initView();
    }

    private void initView(){
        mParentLayout = findViewById(R.id.activity_edit_parent_layout);
        mParentLayout.post(new Runnable() {
            @Override
            public void run() {

                //load sticker data
                final Context context = WowApplication.getAppContext();
                ArrayList<TextBean> beans;
                // This is for emoji sticker, will finish in the further
//                beans = new VideoTextFontFactory(context).getAllStickers();
//                if (mActivity != null && !mActivity.isDestroyed() &&
//                        getEditFragmentBottomManager().getEditStickerFragment() != null) {
//                    getEditFragmentBottomManager().getEditStickerFragment().setInnerStickerData(beans);
//                    mLoadStickerDataFinish = true;
//                    if (mHasClickSticker || mFromMaterialStore) {
//                        ThreadHelper.postRunOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mSticker.performClick();
//                            }
//                        });
//                    }
//                }

                beans = new VideoTextFontFactory(context).getAllFonts();
                ArrayList<Bitmap> cacheBitmapList = new ArrayList<>(beans.size());
                for (int i = 0; i < beans.size(); i++) {
                    TextStickerContent content = new TextStickerContent(context);
                    content.setTextBean(beans.get(i));
                    Bitmap bitmap = content.getBitmap();
                    cacheBitmapList.add(i, bitmap);
                }
                mVideoEditTextFragment.setInnerData(cacheBitmapList, beans);


                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


                mParentLayout = findViewById(R.id.activity_edit_parent_layout);

                Bundle bundle = new Bundle();
                mStickerDisplayFragment.setArguments(bundle);
                fragmentTransaction.add(R.id.activity_edit_center_fragment, mStickerDisplayFragment);
                fragmentTransaction.add(R.id.video_edit_text_fragment, mVideoEditTextFragment);
                fragmentTransaction.commitAllowingStateLoss();
            }
        });
    }




    public void setIEditTextTouchListener(IEditTouchListener textTouchListener) {
        mTextTouchListener = textTouchListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mTextTouchListener.touchEvent(event);
    }

    public interface IEditTouchListener {
        boolean touchEvent(MotionEvent event);
    }


}
