package com.example.advanced.dex;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import androidx.core.graphics.drawable.DrawableCompat;

/**
 * User: milan
 * Time: 2020/4/2 10:52
 * Des:
 */
public class DrawableLess {
    /**
     * ========================================================
     * tint drawable with Color or ColorStateList
     * using the DrawableCompat in support v4 library
     * ========================================================
     */
    public static Drawable $tint(Drawable originDrawable, int color) {
        return $tint(originDrawable, ColorStateList.valueOf(color));
    }

    public static Drawable $tint(Drawable originDrawable, int color, PorterDuff.Mode tintMode) {
        return $tint(originDrawable, ColorStateList.valueOf(color), tintMode);
    }

    public static Drawable $tint(Drawable originDrawable, ColorStateList colorStateList) {
        return $tint(originDrawable, colorStateList, null);
    }

    public static Drawable $tint(Drawable originDrawable, ColorStateList colorStateList, PorterDuff.Mode tintMode) {
        Drawable tintDrawable = DrawableCompat.wrap(originDrawable);
        if (tintMode != null) {
            DrawableCompat.setTintMode(tintDrawable, tintMode);
        }
        DrawableCompat.setTintList(tintDrawable, colorStateList);
        return tintDrawable;
    }
}
