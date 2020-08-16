package com.example.sticker.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.example.sticker.R;



public class ConfirmDialog1 extends ConfirmCommonDialog {

    private TextView mContentTextView;

    public ConfirmDialog1(Activity act, boolean cancelOutside) {
        super(act, cancelOutside);
    }

    @Override
    protected void initCustomLayout(RelativeLayout contentLayout) {
        LayoutInflater.from(mActivity).inflate(
                R.layout.dialog_confirm_layout_1, contentLayout, true);
        mContentTextView = (TextView) findViewById(R.id.confirm_dialog_1_text);
        mContentTextView.setTextColor(ActivityCompat.getColor(mActivity, R.color.white));
    }

    public void setContentGravity(int gravity) {
        mContentTextView.setGravity(gravity);
    }

    public void setContentText(String str) {
        mContentTextView.setText(str);
    }

    public void setContentText(int stringId) {
        mContentTextView.setText(stringId);
    }

    public void setContentHeight(int px) {
        mContentTextView.setHeight(px);
    }

    public void setContentVisibility(int visibility) {
        mContentTextView.setVisibility(visibility);
    }

}
