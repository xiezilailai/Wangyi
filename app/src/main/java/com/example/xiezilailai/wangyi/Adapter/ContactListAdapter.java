package com.example.xiezilailai.wangyi;

import android.content.Intent;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

/**
 * Created by 蝎子莱莱123 on 2016/1/8.
 */
public class ContactListAdapter extends BaseAdapter {

    private MediaPlayer mediaPlayer=new MediaPlayer();
    private LayoutInflater inflater;
    private List<ContactListInfo>list;
    public ContactListAdapter(LayoutInflater inflater,List<ContactListInfo>list){
        this.inflater=inflater;
        this.list=list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }


    @Override
    public int getViewTypeCount() {
        return 6;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        switch (getItemViewType(i)){
            case 0:
                view=inflater.inflate(R.layout.contact_self_text_view,null);
                viewHolder1 viewHolder0=new viewHolder1();
                viewHolder0.selfText=(TextView)view.findViewById(R.id.self_text);
                viewHolder0.selfText.setText(list.get(i).getText());
                break;
            case 1:
                view=inflater.inflate(R.layout.contact_self_pic,null);
                viewHolder2 viewHolder1=new viewHolder2();
                viewHolder1.selfPic=(ImageView)view.findViewById(R.id.self_pic);

                ViewGroup.LayoutParams layoutParams=viewHolder1.selfPic.getLayoutParams();
                layoutParams.width=list.get(i).getPic_width();
                layoutParams.height=list.get(i).getPic_height();
//                Toast.makeText(inflater.getContext(),list.get(i).getPic_height()+"||"+list.get(i).getPic_width(),Toast.LENGTH_SHORT).show();

                viewHolder1.selfPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent();
                        intent.setClass(view.getContext(), com.example.xiezilailai.wangyi.PicShowActivity.class);
                        intent.putExtra("path", list.get(i).getTag().toString());

                        view.getContext().startActivity(intent);
                    }
                });
                viewHolder1.selfPic.setImageBitmap(list.get(i).getPic());
                break;
            case 2:
                view=inflater.inflate(R.layout.contact_self_audio,null);
                viewHolder5 viewHolder5=new viewHolder5();
                viewHolder5.selfTime=(TextView)view.findViewById(R.id.self_audio_time);
                viewHolder5.self_click=(LinearLayout)view.findViewById(R.id.self_audio_click);
                viewHolder5.selfTime.setText(list.get(i).getTime());
                viewHolder5.self_click.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                        }
                        mediaPlayer.reset();
                        try {
                            mediaPlayer.setDataSource(list.get(i).getTag());
                            Toast.makeText(inflater.getContext(),list.get(i).getTag(),Toast.LENGTH_SHORT).show();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            mediaPlayer.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mediaPlayer.start();

                    }
                });
                break;
            case 3:
                view=inflater.inflate(R.layout.contact_oppo_text,null);
                viewHolder3 viewHolder3=new viewHolder3();
                viewHolder3.oppoText=(TextView)view.findViewById(R.id.oppo_text);
                viewHolder3.oppoText.setText(list.get(i).getText());
                break;
            case 4:
                view=inflater.inflate(R.layout.contact_oppo_pic,null);
                viewHolder4 viewHolder4=new viewHolder4();

                viewHolder4.oppoPic=(ImageView)view.findViewById(R.id.oppo_pic);
                ViewGroup.LayoutParams layoutParams2=viewHolder4.oppoPic.getLayoutParams();
                layoutParams2.width=list.get(i).getPic_width();
                layoutParams2.height=list.get(i).getPic_height();
//                viewHolder4.oppoPic.setLayoutParams(layoutParams2);
                viewHolder4.oppoPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setClass(view.getContext(), com.example.xiezilailai.wangyi.PicShowActivity.class);
                        intent.putExtra("path", list.get(i).getTag().toString());
                        view.getContext().startActivity(intent);
                    }
                });

                viewHolder4.oppoPic.setImageBitmap(list.get(i).getPic());
                break;
            case 5:
                view=inflater.inflate(R.layout.contact_oppo_audio,null);
                viewHolder6 viewHolder6=new viewHolder6();
                viewHolder6.oppo_click=(LinearLayout)view.findViewById(R.id.oppo_audio_click);
                viewHolder6.oppoTime=(TextView)view.findViewById(R.id.oppo_audio_time);
                viewHolder6.oppoTime.setText(list.get(i).getTime());
                viewHolder6.oppo_click.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                        }
                        mediaPlayer.reset();
                        try {
                            mediaPlayer.setDataSource(list.get(i).getTag());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            mediaPlayer.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mediaPlayer.start();
                    }
                });

                break;

        }
        return view;
    }

    public class viewHolder1{
        TextView selfText;
    }
    public class viewHolder2{
        ImageView selfPic;
    }
    public class viewHolder3{
        TextView oppoText;
    }
    public class viewHolder4{
        ImageView oppoPic;
    }
    public class viewHolder5{
        TextView selfTime;
        LinearLayout self_click;
    }
    public class viewHolder6{
        TextView oppoTime;
        LinearLayout oppo_click;
    }



}
