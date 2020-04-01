package com.example.advanced.app;

import com.example.advanced.utils.FileUtil;

import java.io.File;

/**
 * User: milan
 * Time: 2020/4/1 14:07
 * Des:
 */
public class Constants {

    public static final String M_SDROOT_CACHE_PATH = FileUtil.getCacheDir(App.getApp().getApplicationContext()) + File.separator;

    public static final String H_NAME = "H_NAME";

    public final static String API_LIBRARY_BASE = "http://47.104.142.138:8081/";
}
