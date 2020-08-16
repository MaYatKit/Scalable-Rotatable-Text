package com.example.sticker.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sticker.R;


public abstract class ConfirmCommonDialog extends BaseDialog implements
        OnDismissListener, OnClickListener {
    protected boolean mIsConfirm = false;

    protected OnConfirmListener mOnConfirmListener;
    protected OnConfirmDetailListener mConfirmDetailListener;

    protected TextView mTitle;
    protected View mDivider;
    protected TextView mOk;
    protected TextView mCancel;
    protected RelativeLayout mContentLayout;
    protected RelativeLayout mContentView;

    protected boolean mFlagDismissAuto = true;
    private View mWindow;
    private boolean mIsCancelOutside = false;


    public ConfirmCommonDialog(Activity act) {
        this(act, true);
    }

    public ConfirmCommonDialog(Activity act, boolean cancelOutside) {
        super(act, R.style.dialog_theme_without_animation, cancelOutside);
        mIsCancelOutside = cancelOutside;
        init(act);
    }

    protected void init(Activity act) {
        setContentView(R.layout.dialog_common_confirm_layout);

        mWindow = findViewById(R.id.confirm_common_dialog_window);
        mTitle = (TextView) findViewById(R.id.confirm_common_dialog_title);
        mDivider = findViewById(R.id.confirm_common_dialog_divider);
        mOk = (TextView) findViewById(R.id.confirm_common_dialog_confirm);
        mCancel = (TextView) findViewById(R.id.confirm_common_dialog_cancel);
        mContentLayout = (RelativeLayout) findViewById(R.id.confirm_common_dialog_content_view);
        mContentView = (RelativeLayout) findViewById(R.id.confirm_common_dialog_layout);

        initCustomLayout(mContentLayout);
        setOnDismissListener(this);
        mOk.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        if (mIsCancelOutside) {
            mWindow.setOnClickListener(this);
        }
        mContentView.setOnClickListener(this);

        setCancelText(R.string.cancel);
        setSize(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    RelativeLayout getContentLayout() {
        return mContentLayout;
    }

    protected abstract void initCustomLayout(RelativeLayout contentLayout);


    public void setOkText(int resId) {
        if (mOk != null) {
            mOk.setText(getString(resId));
        }
    }


    public void setCancelText(int resId) {
        if (mCancel != null) {
            mCancel.setText(getString(resId));
        }
    }


    public void setOnConfirmDetailListener(OnConfirmDetailListener listener) {
        mConfirmDetailListener = listener;
    }

    @Override
    public void show() {
        if (mActivity != null && !mActivity.isDestroyed()) {
            super.show();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mOnConfirmListener != null) {
            mOnConfirmListener.onConfirm(mIsConfirm);
        }
        mIsConfirm = false;
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mOk)) {
            mIsConfirm = true;
            if (mConfirmDetailListener != null) {
                mConfirmDetailListener.onConfirm();
            }
            if (mFlagDismissAuto) {
                dismiss();
            }
        } else if (v.equals(mCancel)) {
            mIsConfirm = false;
            if (mConfirmDetailListener != null) {
                mConfirmDetailListener.onCancel();
            }
            if (mFlagDismissAuto) {
                dismiss();
            }
        } else if (v.equals(mWindow)) {
            if (mIsCancelOutside) {
                mIsConfirm = false;
                dismiss();
            }
        }
    }

    public interface OnConfirmListener {
        void onConfirm(boolean isConfirm);
    }

    public interface OnConfirmDetailListener {
        void onConfirm();

        void onCancel();

        void onBackPress();
    }

    public void setCancelGone() {
        mCancel.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mConfirmDetailListener != null) {
            mConfirmDetailListener.onBackPress();
        }
    }

}
