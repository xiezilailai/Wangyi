package com.example.xiezilailai.wangyi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

/**
 * Created by 蝎子莱莱123 on 2016/1/11.
 */
public class PicShowActivity extends AppCompatActivity {

    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pic_show);
        imageView=(ImageView)findViewById(R.id.imageView2);

        Intent intent=getIntent();
        String path=intent.getStringExtra("path");

        File tmp = new File(path);
        Uri uri = Uri.fromFile(tmp);
        Bitmap bitmap=null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(bitmap);
    }
}
