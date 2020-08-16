package com.example.sticker.adapter;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.sticker.R;
import com.example.sticker.view.RatioRelativeLayout;

import java.util.List;



public class TextSelectAdapter extends BaseAdapter<Bitmap, BaseViewHolder> {

    public TextSelectAdapter(Context context, List<Bitmap> data) {
        super(context, R.layout.video_edit_text_choose_item, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, Bitmap item, int position) {
        RatioRelativeLayout ratioRelativeLayout = holder.getView(R.id.video_edit_text_choose_bg);
        ratioRelativeLayout.setRatio(1.5f);
        if (item != null) {
            holder.setImageBitmap(R.id.video_eidt_text_choose_img, item);
        }
    }
}
