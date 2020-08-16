package com.example.sticker.adapter;

import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sticker.TextBean;
import com.example.sticker.view.TextStickerWidget;


public class BaseViewHolder extends RecyclerView.ViewHolder {

    private final SparseArray<View> mViews;
    private View mConvertView;


    public BaseViewHolder(View itemView) {
        super(itemView);
        this.mViews = new SparseArray<>();
        mConvertView = itemView;
    }

    public View getConvertView() {
        return mConvertView;
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public BaseViewHolder setTextBean(int viewId, TextBean value) {
        TextStickerWidget view = getView(viewId);
        view.setTextBean(value);
        return this;
    }

    public BaseViewHolder setText(int viewId, CharSequence value) {
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }

    public BaseViewHolder setText(int viewId, @StringRes int strId) {
        TextView view = getView(viewId);
        view.setText(strId);
        return this;
    }

    public BaseViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView imageView = getView(viewId);
        imageView.setImageBitmap(bitmap);
        return this;
    }

    public BaseViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView imageView = getView(viewId);
        imageView.setImageDrawable(drawable);
        return this;
    }

    public BaseViewHolder setImageResource(int viewId, int resourceId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resourceId);
        return this;
    }

    public BaseViewHolder setChecked(int viewId, boolean checked) {
        View view = getView(viewId);
        // View unable cast to Checkable
        if (view instanceof CompoundButton) {
            ((CompoundButton) view).setChecked(checked);
        } else if (view instanceof CheckedTextView) {
            ((CheckedTextView) view).setChecked(checked);
        }
        return this;
    }

    public BaseViewHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public BaseViewHolder setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    public BaseViewHolder setColorFilter(int viewId, int color) {
        View view = getView(viewId);
        ((ImageView) view).setColorFilter(color);
        return this;
    }

    public BaseViewHolder setColorFilter(int viewId, ColorFilter colorFilter) {
        View view = getView(viewId);
        ((ImageView) view).setColorFilter(colorFilter);
        return this;
    }

    public BaseViewHolder setVisibility(int viewId, boolean visibility) {
        View view = getView(viewId);
        if (visibility) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
        return this;
    }

    public BaseViewHolder setChceked(int viewId, boolean checked) {
        View view = getView(viewId);
        ((CheckBox) view).setChecked(checked);
        return this;
    }

    public BaseViewHolder setVisibility(int viewId, int type) {
        View view = getView(viewId);
        if (type == View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        } else if (type == View.INVISIBLE) {
            view.setVisibility(View.INVISIBLE);
        } else if (type == View.GONE) {
            view.setVisibility(View.GONE);
        }
        return this;
    }

    public BaseViewHolder setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public BaseViewHolder setBackgroundRes(int viewId, int res) {
        View view = getView(viewId);
        view.setBackgroundResource(res);
        return this;
    }

    public BaseViewHolder setTextSize(int viewId, int size) {
        View view = getView(viewId);
        ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        return this;
    }

    public BaseViewHolder setTypeface(int viewId, Typeface typeface) {
        View view = getView(viewId);
        ((TextView) view).setTypeface(typeface);
        return this;
    }

    public BaseViewHolder setTextColor(int viewId, int[] color) {
        View view = getView(viewId);
        ((TextView) view).setTextColor(color[0]);
        return this;
    }

    public BaseViewHolder setAlpha(int viewId, float alpha) {
        View view = getView(viewId);
        view.setAlpha(alpha);
        return this;
    }
}
