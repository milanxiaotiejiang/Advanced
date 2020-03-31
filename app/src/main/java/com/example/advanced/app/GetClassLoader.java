package com.example.advanced.app;

import dalvik.system.PathClassLoader;

/**
 * User: milan
 * Time: 2020/3/31 19:25
 * Des:
 */

class GetClassLoader extends PathClassLoader {
    public GetClassLoader(String dexPath, ClassLoader parent) {
        super(dexPath, parent);
    }

    public Class<?> findClass(String name) throws ClassNotFoundException {
        // 将 name 记录到文件
//        writeToFile(name, "coldstart_classes.txt");
        return super.findClass(name);
    }
}
