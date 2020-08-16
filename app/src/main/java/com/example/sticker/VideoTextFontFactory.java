package com.example.sticker;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.TypedValue;

import com.example.sticker.utils.DensityUtil;

import java.util.ArrayList;

import static com.example.sticker.utils.DensityUtil.dip2px;

/**
 * Text sticker factory
 */
public class VideoTextFontFactory {

    private Context mContext;
    private int mDefaultMinTextSize = 14;
    private int mMaxViewSize;

    public VideoTextFontFactory(Context context) {
        mContext = context.getApplicationContext();
        mMaxViewSize = (DensityUtil.getScreenWidth(context) - dip2px(context, 20 + 20 + 16)) / 2 - 20;
    }

    public ArrayList<TextBean> getAllFonts() {
        ArrayList<TextBean> result = new ArrayList<>();
        result.add(getFont("Roboto", 20, "Roboto.ttf", "Roboto", R.color.white));
        result.add(getFont("Lobster", 22, "Lobster.otf", "Lobster", R.color.white));
        result.add(getFont("PERMANENT", 14, "PermanentMarker-Regular.ttf", "PERMANENT", R.color.white));
        result.add(getFont("BOLDFONT", 17, "boldfont.ttf", "BOLDFONT", R.color.white));

        result.add(getFontSubTitle1());
        result.add(getFontSubTitle2());
        result.add(getFontTitle1());
        result.add(getFontTitle2());

        for (int i = 0; i < result.size(); i++) {
            TextBean textBean = result.get(i);
            textBean.setTextBeanId(i);
        }
        return result;
    }


    private TextBean getFont(String name, int textSize, String typeFace, String showText, int textColor) {
        TextBean textBean = new TextBean();
        textBean.setTextSize(textSize);
        textBean.setTextSize((int) (textBean.getTextSize() * 1.3));
        textBean.setTypeface(typeFace);
        textBean.setDefaultWidth(dip2px(mContext, 160));
        textBean.setDefaultHeight(dip2px(mContext, 80));
        textBean.setImageX(0);
        textBean.setImageY(0);
        textBean.setText(showText);
        textBean.setOriginalText(showText);
        textBean.setTextColor(new int[]{mContext.getResources().getColor(textColor), mContext.getResources().getColor
                (textColor)});
        int textRectTop = (dip2px(mContext, 113) - getMaxTextRectHeight(typeFace, 2)) / 2 + dip2px(mContext, 3);
        textBean.setTextRect(new Rect(0, textRectTop, dip2px(mContext, 150), dip2px(mContext, 113) - textRectTop));
        textBean.setTextGravity(TextBean.GRAVITY_CENTER);
        textBean.setMinTextSize(mDefaultMinTextSize);
        textBean.setTextRotateAngle(0);
        textBean.setStrokeWidth(dip2px(mContext, 3f));
        //textBean.setStrokeColor(new int[]{mContext.getResources().getColor(android.R.color.black)});
        textBean.setTextStrokeGradient(TextBean.COLOR_GRADIENT_VERTICAL);
        textBean.setLetterSpace(dip2px(mContext, 0));
        textBean.setTextStartXOffset(dip2px(mContext, 0));
        textBean.setTextStartYOffset(dip2px(mContext, -13));
        textBean.setName(name);
        return textBean;
    }

    private TextBean getFontTitle1() {
        TextBean textBean = new TextBean();
        textBean.setTextSize(20);
        textBean.setTypeface("title1.ttf");
        textBean.setName("title1");
        textBean.setDefaultWidth(dip2px(mContext, 141));
        textBean.setDefaultHeight(dip2px(mContext, 52));
        textBean.setImageX(0);
        textBean.setImageY(0);
        textBean.setText("I'M TITLE");
        textBean.setOriginalText(textBean.getText());
        textBean.setTextColor(new int[]{mContext.getResources().getColor(R.color.title1_font_color)});
        textBean.setBackgroundRes(R.drawable.edit_font_bg_title);
        int textRectTop = (dip2px(mContext, 52) - getMaxTextRectHeight("title1.ttf", 2)) / 2 + dip2px(mContext, 6);
        textBean.setTextRect(new Rect(dip2px(mContext, 16), textRectTop, dip2px(mContext, 141 - 16), dip2px(mContext, 52) -
                textRectTop));
        textBean.setTextGravity(TextBean.GRAVITY_CENTER);
        textBean.setMinTextSize(mDefaultMinTextSize);
        textBean.setTextRotateAngle(0);
        textBean.setStrokeWidth(dip2px(mContext, 0f));
        textBean.setStrokeColor(new int[]{mContext.getResources().getColor(android.R.color.white)});
        textBean.setLetterSpace(dip2px(mContext, 0));
        textBean.setTextStartXOffset(dip2px(mContext, 0));
        textBean.setTextStartYOffset(dip2px(mContext, 1));
        return textBean;
    }

    private TextBean getFontTitle2() {
        TextBean textBean = new TextBean();
        textBean.setTextSize(18);
        textBean.setTypeface("title2.ttf");
        textBean.setName("title2");
        textBean.setDefaultWidth(dip2px(mContext, 103));
        textBean.setDefaultHeight(dip2px(mContext, 103));
        textBean.setImageX(0);
        textBean.setImageY(0);
        textBean.setText("I'm\ntitle");
        textBean.setOriginalText(textBean.getText());
        textBean.setTextColor(new int[]{mContext.getResources().getColor(R.color.title2_font_color)});
        textBean.setBackgroundRes(R.drawable.edit_font_bg_titile2);
        textBean.setTextRect(new Rect(dip2px(mContext, 20), dip2px(mContext, 25), dip2px(mContext, 102 - 20), dip2px(mContext,
                102 - 25)));
        textBean.setTextGravity(TextBean.GRAVITY_CENTER);
        textBean.setMinTextSize(12);
        textBean.setTextRotateAngle(0);
//        textBean.setStrokeWidth(dip2px(mContext, 0f));
        textBean.setStrokeColor(new int[]{mContext.getResources().getColor(R.color.empty_font_stroke_color)});
        textBean.setLetterSpace(dip2px(mContext, 0));
        textBean.setLineSpace(dip2px(mContext, 6));
        textBean.setTextStartXOffset(dip2px(mContext, 0));
        textBean.setTextStartYOffset(dip2px(mContext, 0));
        return textBean;
    }

    private TextBean getFontSubTitle1() {
        TextBean textBean = new TextBean();
        textBean.setTextSize(18);
        textBean.setDefaultWidth(dip2px(mContext, 152));
        textBean.setDefaultHeight(dip2px(mContext, 36));
        textBean.setImageX(0);
        textBean.setImageY(0);
        textBean.setText("I'm subtitles 1");
        textBean.setOriginalText(textBean.getText());
        textBean.setName("subtitle1");
        textBean.setTextColor(new int[]{mContext.getResources().getColor(R.color.subtitle1_font_color)});
        textBean.setBackgroundRes(R.drawable.edit_font_bg_subtitle1);
        int textRextTop = (dip2px(mContext, 36) - getMaxTextRectHeight(null, 2)) / 2 + dip2px(mContext, 9);
        textBean.setTextRect(new Rect(dip2px(mContext, 2), textRextTop, dip2px(mContext, 152 - 2), dip2px(mContext, 36 - 0) -
                textRextTop));
        textBean.setTextGravity(TextBean.GRAVITY_CENTER);
        textBean.setTextRotateAngle(0);
        textBean.setMinTextSize(mDefaultMinTextSize);
//        textBean.setStrokeWidth(dip2px(mContext, 0f));
        textBean.setStrokeColor(new int[]{mContext.getResources().getColor(R.color.empty_font_stroke_color)});
        textBean.setLetterSpace(dip2px(mContext, 0f));
        if (DensityUtil.isHighDpi(mContext)) {
            textBean.setLineSpace(10);
        } else {
            textBean.setLineSpace(-10);
        }
        textBean.setTextStartXOffset(dip2px(mContext, 0));
        textBean.setTextStartYOffset(dip2px(mContext, 2));
        return textBean;
    }

    private TextBean getFontSubTitle2() {
        TextBean textBean = new TextBean();
        textBean.setTextSize(18);
        textBean.setTypeface("subtitle2.ttf");
        textBean.setName("subtitle2");
        textBean.setDefaultWidth(dip2px(mContext, 152));
        textBean.setDefaultHeight(dip2px(mContext, 36));
        textBean.setImageX(0);
        textBean.setImageY(0);
        textBean.setText("I'm subtitles 2");
        textBean.setOriginalText(textBean.getText());
        textBean.setTextColor(new int[]{mContext.getResources().getColor(R.color.subtitle2_font_color)});
        textBean.setBackgroundRes(R.drawable.edit_font_bg_subtitle2);
        int textRextTop = (dip2px(mContext, 36) - getMaxTextRectHeight("subtitle2.ttf", 2)) / 2 + dip2px(mContext, 9);
        textBean.setTextRect(new Rect(dip2px(mContext, 2), textRextTop, dip2px(mContext, 152 - 2), dip2px(mContext, 36 - 0) -
                textRextTop));
        textBean.setTextGravity(TextBean.GRAVITY_CENTER);
        textBean.setMinTextSize(mDefaultMinTextSize);
        textBean.setTextRotateAngle(0);
//        textBean.setStrokeWidth(dip2px(mContext, 0.3f));
        textBean.setStrokeColor(new int[]{mContext.getResources().getColor(R.color.empty_font_stroke_color)});
        textBean.setLetterSpace(dip2px(mContext, 0));
        if (DensityUtil.isHighDpi(mContext)) {
            textBean.setLineSpace(10);
        } else {
            textBean.setLineSpace(-10);
        }
        textBean.setTextStartXOffset(dip2px(mContext, 0));
        textBean.setTextStartYOffset(dip2px(mContext, 2));
        return textBean;
    }

    private TextBean getFontMyBea() {
        TextBean textBean = new TextBean();
        textBean.setTextSize(26);
        textBean.setTypeface("mybea.ttf");
        textBean.setName("mybea");
        textBean.setDefaultWidth(mMaxViewSize);
        textBean.setDefaultHeight(dip2px(mContext, 50));
        textBean.setImageX(0);
        textBean.setImageY(0);
        textBean.setText("My Bae");
        textBean.setOriginalText(textBean.getText());
        textBean.setTextColor(new int[]{mContext.getResources().getColor(R.color.mybea_font_color)});
        textBean.setBackgroundRes(R.drawable.edit_font_bg_mybea);
        int textRextTop = (dip2px(mContext, 50) - getMaxTextRectHeight("mybea.ttf", 2)) / 2;
        textBean.setTextRect(new Rect(dip2px(mContext, 23), textRextTop, mMaxViewSize - dip2px(mContext, 23), dip2px(mContext,
                50) - textRextTop));
        textBean.setTextGravity(TextBean.GRAVITY_CENTER);
        textBean.setMinTextSize(mDefaultMinTextSize);
        textBean.setTextRotateAngle(0);
        textBean.setStrokeWidth(dip2px(mContext, 2f));
        textBean.setStrokeColor(new int[]{mContext.getResources().getColor(R.color.mybea_font_stroke_color)});
        textBean.setLetterSpace(dip2px(mContext, 0f));
        textBean.setLineSpace(20);
        textBean.setTextStartXOffset(dip2px(mContext, 0));
        textBean.setTextStartYOffset(dip2px(mContext, 5));
        return textBean;
    }

    /**
     * 针对有些有行数限制的文本，通过计算最大的文本高度实现行数控制
     *
     * @param typeface
     * @param maxLineCount
     * @return
     */
    private int getMaxTextRectHeight(String typeface, int maxLineCount) {
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mDefaultMinTextSize, mContext.getResources
                ().getDisplayMetrics()));
        if (typeface != null) {
            textPaint.setTypeface(Typeface.createFromAsset(mContext.getAssets(), typeface));
        }
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        int textHeight = (int) Math.ceil(fontMetrics.bottom - fontMetrics.top);
        return textHeight * maxLineCount + (int) (textHeight * 0.9f) * (maxLineCount - 1);
    }

//    public List<TextBean> getEmoji(String startPath, int size) {
//        List<TextBean> tempList = new ArrayList<>();
//        for (int i = 1; i <= size; i++) {
//            TextBean textBean = new TextBean();
//            textBean.setDefaultWidth(dip2px(mContext, 90));
//            textBean.setDefaultHeight(dip2px(mContext, 90));
//            if (i < 10) {
//                textBean.setBackgroundRes(mContext.getResources().getIdentifier(startPath + "0" + i, "drawable", mContext
//                        .getPackageName()));
//            } else {
//                textBean.setBackgroundRes(mContext.getResources().getIdentifier(startPath + i, "drawable", mContext
//                        .getPackageName()));
//            }
//            textBean.setName(startPath + "_" + i);
//            textBean.setTabName("emoji");
//            textBean.setTabId(VideoEditStickerFragment.TAB_ID_EMOJI);
//            tempList.add(textBean);
//        }
//        return tempList;
//    }

//    private List<TextBean> getAllThugLife(String startPath, int size) {
//        List<TextBean> tempList = new ArrayList<>();
//        for (int i = 1; i <= size; i++) {
//            TextBean textBean = new TextBean();
//            textBean.setDefaultWidth(dip2px(mContext, 90));
//            textBean.setDefaultHeight(dip2px(mContext, 90));
//            if (i < 10) {
//                textBean.setBackgroundRes(mContext.getResources().getIdentifier(startPath + "0" + i, "drawable", mContext
//                        .getPackageName()));
//            } else {
//                textBean.setBackgroundRes(mContext.getResources().getIdentifier(startPath + i, "drawable", mContext
//                        .getPackageName()));
//            }
//            textBean.setName(startPath + "_" + i);
//            textBean.setTabName("thug_life");
//            textBean.setTabId(VideoEditStickerFragment.TAB_ID_THUGLIFE);
//            tempList.add(textBean);
//        }
//        return tempList;
//    }

//    private List<TextBean> getSticker(String startPath, int size, String tabName, long tabId) {
//        List<TextBean> tempList = new ArrayList<>();
//        for (int i = 1; i <= size; i++) {
//            TextBean textBean = new TextBean();
//            textBean.setDefaultWidth(dip2px(mContext, 90));
//            textBean.setDefaultHeight(dip2px(mContext, 90));
//            if (i < 10) {
//                textBean.setBackgroundRes(mContext.getResources().getIdentifier(startPath + "0" + i, "drawable", mContext
//                        .getPackageName()));
//            } else {
//                textBean.setBackgroundRes(mContext.getResources().getIdentifier(startPath + i, "drawable", mContext
//                        .getPackageName()));
//            }
//            textBean.setName(startPath + i);
//            textBean.setTabName(tabName);
//            textBean.setTabId(tabId);
//            tempList.add(textBean);
//        }
//        return tempList;
//    }

}
