package com.example.advanced.hotfix;

/**
 * User: milan
 * Time: 2020/4/5 12:48
 * Des:
 */
public class HuGe implements Star {

    public void sing(String song) {
        System.out.println("胡歌演唱： " + song);
    }

    public String act(String teleplay) {
        System.out.println("胡歌决定出演电视剧： " + teleplay);
        return "胡歌答应出演电视剧： " + teleplay;
    }
}
