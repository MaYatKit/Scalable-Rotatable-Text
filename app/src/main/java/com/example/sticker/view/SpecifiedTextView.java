package com.example.sticker.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.sticker.R;



public class SpecifiedTextView extends AppCompatTextView {

    private Context mContext;

    private String mTypeFace;


    public SpecifiedTextView(Context context) {
        this(context, null);
    }

    public SpecifiedTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpecifiedTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SpecifiedTextView);
            mTypeFace = array.getString(R.styleable.SpecifiedTextView_typeface);
            array.recycle();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mTypeFace == null) {
            Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), mContext.getResources().getString(R.string
                    .font_current_style));
            setTypeface(typeface);
        } else {
            Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), mTypeFace);
            setTypeface(typeface);
        }

    }

}
