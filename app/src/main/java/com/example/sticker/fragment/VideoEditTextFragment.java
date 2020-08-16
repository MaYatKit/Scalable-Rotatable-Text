package com.example.sticker.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sticker.MainActivity;
import com.example.sticker.R;
import com.example.sticker.TextBean;
import com.example.sticker.VideoTextFontFactory;
import com.example.sticker.adapter.BaseAdapter;
import com.example.sticker.adapter.TextSelectAdapter;
import com.example.sticker.view.TextEditStickerWidget;
import com.example.sticker.view.TextStickerContent;

import java.util.ArrayList;
import java.util.List;




public class VideoEditTextFragment extends Fragment implements View.OnClickListener {

    private List<TextBean> mTextFontList;
    private List<TextBean> mTypefaceList;
    private List<Bitmap> mCacheBitmapList;
    private View mRootView;
    private RecyclerView mRecyclerView;
    private TextSelectAdapter mAdapter;
    private MainActivity mActivity;
    private TextEditStickerWidget mCurrentTextStickerWidget;
    private int mLastTextPosition;
    private int mTextTypeFacePosition = 0;
    private VideoTextEditFragment mVideoTextEditFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_video_edit_text, container, false);
        mRecyclerView = mRootView.findViewById(R.id.edit_text_recyclerview);

        mVideoTextEditFragment = (VideoTextEditFragment) getFragmentManager().findFragmentById(R.id.activity_edit_center_fragment);
        initData(savedInstanceState);
        setListener();

        mVideoTextEditFragment.getView().post(new Runnable() {
            @Override
            public void run() {
                defaultAddText();
            }
        });
        return mRootView;
    }


    private void initData(Bundle savedInstanceState) {

        mActivity = (MainActivity) getActivity();
        mTextFontList = new VideoTextFontFactory(getContext()).getAllFonts();



        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);


        mAdapter = new TextSelectAdapter(getContext(), mCacheBitmapList);
        mRecyclerView.setAdapter(mAdapter);
        replaceBitmapByPosition(mTextTypeFacePosition, R.color.video_edit_tool_text_select_color);
    }

    private void setListener() {
        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mAdapter.setItemClickListner(new BaseAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                textTypeItemClick(position);
                addNewText(position);
            }
        });

    }

    private void textTypeItemClick(int position) {
        mTextTypeFacePosition = position;
        if (mLastTextPosition != position) {
            replaceBitmapByPosition(position, R.color.video_edit_tool_text_select_color);
            int defaultColor = R.color.white;
            if (mLastTextPosition == 27) {
                defaultColor = R.color.mybea_font_color;
            } else if (mLastTextPosition == 28) {
                defaultColor = R.color.subtitle1_font_color;
            } else if (mLastTextPosition == 31) {
                defaultColor = R.color.title2_font_color;
            }
            replaceBitmapByPosition(mLastTextPosition, defaultColor);
            mLastTextPosition = position;
            mAdapter.notifyDataSetChanged();
        }
        initTextStickerWidget(false);
    }

    private void defaultAddText() {
        putTextOrEmoji(mTextTypeFacePosition);
        mCurrentTextStickerWidget = mVideoTextEditFragment.getNewestTextEditStickerWidget();
        initTextStickerWidget(true);
        mVideoTextEditFragment.setTextStickerDeleteButtonVisibility(true);
        refreshListState(mCurrentTextStickerWidget);
    }


    private void initTextStickerWidget(boolean translate) {
        TextBean textBean = mTextFontList.get(mTextTypeFacePosition);
        String text = mCurrentTextStickerWidget.getText();
        textBean.setText(text);

        if (translate) {
            mCurrentTextStickerWidget.refreshTextBean(textBean);
        } else {
            mCurrentTextStickerWidget.refreshTextBeanWithoutTranslate(textBean);
        }
        if (mCurrentTextStickerWidget != null) {
            mVideoTextEditFragment.setTextStickerText(
                    mCurrentTextStickerWidget.getText(), 0, mCurrentTextStickerWidget);
        }
    }


    public void refreshListState(TextEditStickerWidget textStickerWidget) {
        mVideoTextEditFragment.setAllTextVisibility(false);
        mCurrentTextStickerWidget = textStickerWidget;
        if (mCurrentTextStickerWidget != null) {
            mCurrentTextStickerWidget.setVisible(true);
            TextBean textBean = mCurrentTextStickerWidget.getTextBean();

            String typeFace = textBean.getTypeface();
            if (typeFace == null) {
                textTypeItemClick(28); // 因为之前的特殊处理，所以这里跟住特殊处理
                return;
            }
            for (int i = 0; i < mTextFontList.size(); i++) {
                if (typeFace.equals(mTextFontList.get(i).getTypeface())) {
                    mTextTypeFacePosition = i;
                    textTypeItemClick(i);
                    break;
                }
            }
        }
    }


    public void addNewText(int position) {
        mTextTypeFacePosition = position;
        if (mLastTextPosition != position) {
            replaceBitmapByPosition(position, R.color.video_edit_tool_text_select_color);
            int defaultColor = R.color.white;
            if (mLastTextPosition == 27) {
                defaultColor = R.color.mybea_font_color;
            } else if (mLastTextPosition == 28) {
                defaultColor = R.color.subtitle1_font_color;
            } else if (mLastTextPosition == 31) {
                defaultColor = R.color.title2_font_color;
            }
            replaceBitmapByPosition(mLastTextPosition, defaultColor);
            mLastTextPosition = position;
            mAdapter.notifyDataSetChanged();
        }
        putTextOrEmoji(mTextTypeFacePosition);
        mCurrentTextStickerWidget = mVideoTextEditFragment.getNewestTextEditStickerWidget();
        initTextStickerWidget(true);
    }

    private void replaceBitmapByPosition(int position, int textColor) {
        TextBean textBean = mTypefaceList.get(position);
        textBean.setTextColor(new int[]{getContext().getResources().getColor(textColor), getContext().getResources().getColor
                (textColor)});
        TextStickerContent content = new TextStickerContent(getContext());
        content.setTextBean(textBean);
        Bitmap bitmap = content.getBitmap();
        mCacheBitmapList.set(position, bitmap);
    }

    private void putTextOrEmoji(int position) {
        if (mActivity == null || mActivity.isDestroyed()) {
            return;
        }
        TextBean textBean = mTextFontList.get(position).clone();
        mVideoTextEditFragment.addText(textBean);
    }

    public void setInnerData(List<Bitmap> cacheBitmapList, List<TextBean> typefaceList) {
        mCacheBitmapList = new ArrayList<>();
        mCacheBitmapList.addAll(cacheBitmapList);
        mTypefaceList = new ArrayList<>();
        mTypefaceList.addAll(typefaceList);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            defaultAddText();
            mVideoTextEditFragment.setTextStickerDeleteButtonVisibility(true);
        }
    }

    @Override
    public void onClick(View v) {
        if (mActivity == null || mActivity.isDestroyed()) {
            return;
        }
    }




}
