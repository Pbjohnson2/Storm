package com.swerve.storm.util;

import android.graphics.Bitmap;

public class ImageUtil {
    public static Bitmap cropBitmap(final Bitmap sourceBitmap) {
        if (sourceBitmap.getWidth() >= sourceBitmap.getHeight()){
            return Bitmap.createBitmap(
                    sourceBitmap,
                    sourceBitmap.getWidth()/2 - sourceBitmap.getHeight()/2,
                    0,
                    sourceBitmap.getHeight(),
                    sourceBitmap.getHeight()
            );
        }else{
            return Bitmap.createBitmap(
                    sourceBitmap,
                    0,
                    sourceBitmap.getHeight()/2 - sourceBitmap.getWidth()/2,
                    sourceBitmap.getWidth(),
                    sourceBitmap.getWidth()
            );
        }
    }
}
