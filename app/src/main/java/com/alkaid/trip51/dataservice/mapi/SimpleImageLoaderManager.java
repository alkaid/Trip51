package com.alkaid.trip51.dataservice.mapi;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by df on 2015/12/24.
 */
public class SimpleImageLoaderManager implements ImageLoaderManager{
    protected ImageLoader imgloader;
    protected RequestQueue requestQueue;
    public SimpleImageLoaderManager(Context context){
        this.requestQueue= Volley.newRequestQueue(context);
        this.imgloader=new ImageLoader(requestQueue, LruImageCache.instance());
    }
    @Override
    public void pauseImageLoad(){
        requestQueue.stop();
    }
    @Override
    public void resumeImageLoad(){
        requestQueue.start();
    }

    public ImageLoader getImgloader() {
        return imgloader;
    }
}
