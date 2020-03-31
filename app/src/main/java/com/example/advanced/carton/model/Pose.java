package com.example.advanced.carton.model;

/**
 * User: milan
 * Time: 2020/3/13 10:54
 * Des:
 */
public class Pose {
    public double x;
    public double y;
    public double theta;

    @Override
    public String toString() {
        return "Pose{" +
                "x=" + x +
                ", y=" + y +
                ", theta=" + theta +
                '}';
    }
}
