package com.qf.myvolleytest;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * create by Luo Danan
 * on 16-1-18 10:10
 */
public class MyApplication extends Application{
    public static RequestQueue requestQueue;
    public static LruCache<String,Bitmap> lruCache;

    @Override
    public void onCreate() {
        super.onCreate();

        requestQueue = Volley.newRequestQueue(this);

        //获取app能使用的最大内存
        //last recently use
        int iMaxSize = (int) Runtime.getRuntime().maxMemory();

        lruCache = new LruCache<String,Bitmap>(iMaxSize/8){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };

    }
}
