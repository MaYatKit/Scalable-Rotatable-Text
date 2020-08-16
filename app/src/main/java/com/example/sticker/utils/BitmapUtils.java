
package com.example.sticker.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Bitmap Utils
 *
 */
public class BitmapUtils {

    private static final String TAG = "BitmapUtils";


    public static Bitmap getBitmap(String path) {
        if (path == null) {
            return null;
        }
        File file = new File(path);
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(file));
            return BitmapFactory.decodeStream(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
