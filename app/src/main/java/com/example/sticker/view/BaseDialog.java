package com.example.sticker.view;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.view.WindowManager;

import com.example.sticker.R;



public abstract class BaseDialog extends Dialog {

    protected Activity mActivity;

    public BaseDialog(Activity act) {
        this(act, false);
    }

    public BaseDialog(Activity act, int style) {
        this(act, style, false);
    }

    public BaseDialog(Activity act, boolean cancelOutside) {
        this(act, R.style.base_dialog_theme, cancelOutside);
    }

    public BaseDialog(Activity act, int style, boolean cancelOutside) {
        super(act, style);
        mActivity = act;
        setCanceledOnTouchOutside(cancelOutside);
    }

    public void setSize(int width, int height) {
        getWindow().setLayout(width, height);
    }


    public void setXY(int x, int y, int gravity) {
        final Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.x = x;
        lp.y = y;
        window.setGravity(gravity);
    }


    protected String getString(int resId) {
        return mActivity.getString(resId);
    }

}