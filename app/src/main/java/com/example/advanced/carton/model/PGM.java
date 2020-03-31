package com.example.advanced.carton.model;

import android.util.SparseArray;

/**
 * User: milan
 * Time: 2020/3/13 9:44
 * Des:
 */
public class PGM {

    public char ch0, ch1;
    public int width, height;
    public int maxpix;
    public SparseArray<Integer> map = new SparseArray<>();

    @Override
    public String toString() {
        return "PGM{" +
                "ch0=" + ch0 +
                ", ch1=" + ch1 +
                ", width=" + width +
                ", height=" + height +
                ", maxpix=" + maxpix +
                '}';
    }
}
