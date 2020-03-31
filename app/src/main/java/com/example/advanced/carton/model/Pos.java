package com.example.advanced.carton.model;

import java.util.Objects;

/**
 * User: milan
 * Time: 2020/3/14 13:19
 * Des:
 */
public class Pos {
    public int x;
    public int y;

    @Override
    public String toString() {
        return "Pos{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pos pos = (Pos) o;
        return x == pos.x &&
                y == pos.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
