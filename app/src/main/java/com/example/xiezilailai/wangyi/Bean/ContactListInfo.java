package com.example.xiezilailai.wangyi;

import android.graphics.Bitmap;

/**
 * Created by 蝎子莱莱123 on 2016/1/8.
 */
public class ContactListInfo {
    public static int SELF_TEXT=0;
    public static int SELF_PIC=1;
    public static int SELF_SOUND=2;
    public static int OPPO_TEXT=3;
    public static int OPPO_PIC=4;
    public static int OPPO_SOUND=5;
    private int type=SELF_TEXT;
    private String text;
    private Bitmap bitmap;
    private String tag;
    private String time;
    private int pic_height,pic_width;

    public void setSize(int pic_width,int pic_height){
        this.pic_height=pic_height;
        this.pic_width=pic_width;
    }

    public int getPic_height(){
        return pic_height;
    }
    public int getPic_width(){
        return pic_width;
    }

    public String getTag() {
        return tag;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Bitmap getPic() {
        return bitmap;
    }

    public void setPic(Bitmap pic) {
        this.bitmap = pic;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
