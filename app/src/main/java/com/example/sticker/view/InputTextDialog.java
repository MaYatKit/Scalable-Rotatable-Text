package com.example.sticker.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sticker.R;
import com.example.sticker.utils.KeyboardUtils;


public class InputTextDialog extends BaseDialog {

    private EditText mEditText;
    private View mConfirmButton;
    private View mCloseButton;
    private View mBottomLayout;
    private TextView mEnterText;
    private Context mContext;
    private ICloseListener mListener;
    private String mExistText;
    private String mTextBeforeChanged;
    private int mLastTextPos;
    private int mConfirmMarginBottom;
    private boolean mIsLimitText = false;
    private int mLimitLine = -1;

    public InputTextDialog(Activity context, int theme, ICloseListener listener, String existText) {
        super(context, theme);
        mContext = context;
        mListener = listener;
        mExistText = existText;
        mIsLimitText = false;
    }

    public InputTextDialog(Activity context, int theme, ICloseListener listener, String existText, int limitLine) {
        this(context, theme, listener, existText);
        if (limitLine <= 0) {
            throw new IllegalArgumentException("InputTextDialog limitLine must not be less than 0");
        }
        mLimitLine = limitLine;
        mIsLimitText = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setWindowAnimations(R.style.animation_baseDialog);
        setContentView(R.layout.video_edit_text_input);

        mEditText = (EditText) findViewById(android.R.id.edit);
        mConfirmButton = findViewById(R.id.text_edit_confirm_img);
        mCloseButton = findViewById(R.id.text_edit_close_img);
        mEnterText = (TextView) findViewById(R.id.text_edit_enter_text);
        mBottomLayout = findViewById(R.id.text_edit_bootom_layout);

        mEditText.setText(mExistText);
        mEditText.setSelection(mExistText.length());
        mEditText.selectAll();
        if (mIsLimitText) {
            mEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    mTextBeforeChanged = s.toString();
                    mLastTextPos = mEditText.getSelectionStart();
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    mEditText.removeTextChangedListener(this);
                    if (mEditText.getLineCount() > mLimitLine) {
                        mEditText.setText(mTextBeforeChanged);
                        mEditText.setSelection(mLastTextPos);
                    }
                    mEditText.addTextChangedListener(this);
                }
            });
        }
        mEditText.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        KeyboardUtils.registerSoftInputChangedListener(mActivity , new KeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(final int height) {
                if (mBottomLayout.getTranslationY() == 0) {
                    ObjectAnimator animator = ObjectAnimator.ofFloat(mBottomLayout, "translationY", 0, -height);
                    animator.setDuration(100);
                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mBottomLayout.setTranslationY(0);
                            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mBottomLayout.getLayoutParams();
                            layoutParams.setMargins(0, 0, 0, height);
                            mBottomLayout.setLayoutParams(layoutParams);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    animator.start();
                } else {
                    ObjectAnimator animator = ObjectAnimator.ofFloat(mBottomLayout, "translationY", -height, 0);
                    animator.setDuration(100);
                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator.start();
                }
            }
        });

        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mListener.cancel();
            }
        });
        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mListener.close(mEditText.getText().toString().trim(), mEditText.getLineCount());
            }
        });
        mEditText.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager manager = (InputMethodManager) mEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.showSoftInput(mEditText, 0);
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    /*mConfirmButton.setBackground(mContext.getResources().getDrawable(R.drawable.edit_input_null_bg));
                    mConfirmButton.setClickable(false);
                } else {
                    mConfirmButton.setBackground(mContext.getResources().getDrawable(R.drawable.main_take_photo_confirm_bg));
                    mConfirmButton.setClickable(true);*/
                }
            }
        });


    }

    @Override
    public void dismiss() {
        super.dismiss();
        KeyboardUtils.unregisterSoftInputChangedListener(mActivity);
    }

    public interface ICloseListener {
        void close(String text, int lineCount);

        void cancel();
    }
}
