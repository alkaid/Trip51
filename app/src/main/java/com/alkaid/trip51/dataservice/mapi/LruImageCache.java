package com.alkaid.trip51.dataservice.mapi;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by alkaid on 2015/12/6.
 */
public class LruImageCache implements ImageLoader.ImageCache {

    private static LruCache<String, Bitmap> mMemoryCache;

    private static LruImageCache lruImageCache;

    private LruImageCache(){
        // Get the Max available memory
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap){
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
    }

    public static LruImageCache instance(){
        if(lruImageCache == null){
            lruImageCache = new LruImageCache();
        }
        return lruImageCache;
    }

    @Override
    public Bitmap getBitmap(String cacheKey) {
        return mMemoryCache.get(cacheKey);
    }

    @Override
    public void putBitmap(String cacheKey, Bitmap bmp) {
        if(getBitmap(cacheKey) == null){
            mMemoryCache.put(cacheKey, bmp);
        }
    }

}