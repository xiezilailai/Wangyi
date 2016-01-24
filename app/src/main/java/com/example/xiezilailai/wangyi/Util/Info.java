package com.example.xiezilailai.wangyi;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by 蝎子莱莱123 on 2016/1/9.
 */
public class Info {
    private static ImageLoader imageLoader;
    public static String sdkPath;
    public static String takePhotoPath;
    public static ImageLoader getImageLoader(){
        if(imageLoader==null){
            imageLoader=ImageLoader.getInstance();
        }
        return imageLoader;
    }
}
