package com.example.xiezilailai.wangyi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by 蝎子莱莱123 on 2016/1/9.
 */
public class TakePicOrChoosePicDialog {
    private static int mark;

    public static void dialog(final Context context, final Activity activity) {
        final String items[] = {"拍照", "本地图片"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("选择");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mark = i;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mark == 0) {
                    String imagePath = Info.sdkPath + "/image";
                    File file = new File(imagePath);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    int time = (int) (System.currentTimeMillis());
                    java.sql.Timestamp ts = new java.sql.Timestamp(time);

                    Uri imageUri = Uri.fromFile(new File(imagePath, ts.toString() + ".png"));
                    Info.takePhotoPath = Info.sdkPath + "/image/" + ts.toString() + ".png";
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    activity.startActivityForResult(intent, 1);
                    dialogInterface.dismiss();
                } else {

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

                    intent.setType("image/*");

                    activity.startActivityForResult(intent, 2);
                    dialogInterface.dismiss();
                }

            }
        });

        builder.create().show();
    }
}
