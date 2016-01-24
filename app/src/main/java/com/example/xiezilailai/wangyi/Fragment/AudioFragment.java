package com.example.xiezilailai.wangyi;

import android.app.Fragment;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

/**
 * Created by 蝎子莱莱123 on 2016/1/18.
 */
public class AudioFragment extends Fragment {

    private View rootView;
    private RelativeLayout re;
    private TextView tip, time;
    private int minute = 0, second = 0;
    private int mark = 0;
    private MediaRecorder mediaRecorder;
    private onRedioCallBack onRedioCallBack;
    private String filePath;
    private String currentTime;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        rootView = layoutInflater.inflate(R.layout.audio_fragment_view, null);
        re = (RelativeLayout) rootView.findViewById(R.id.re);
        time = (TextView) rootView.findViewById(R.id.audio_time);
        re.setOnTouchListener(new LiTouchListener());
        tip = (TextView) rootView.findViewById(R.id.audio_text);
        tip.setText("按下录音");
        time.setText("00:00");
    }
    public interface onRedioCallBack{
        void sendAudio(String filePath,String time);
    }

    public void setOnRedioCallBack(onRedioCallBack onRedioCallBack){
        this.onRedioCallBack=onRedioCallBack;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return rootView;
    }

    class LiTouchListener implements View.OnTouchListener {


        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    tip.setText("松手发送");
                    mark = 1;
                    Message start = handler.obtainMessage(1);
                    handler.sendMessage(start);


                    startRecording();
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    mark = 0;
                    tip.setText("按下录音");
                    stopRecording();
                    onRedioCallBack.sendAudio(filePath,currentTime);
            }

            return true;
        }
    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1 && mark == 1) {

                time_show();
                Message con = handler.obtainMessage(1);
                handler.sendMessageDelayed(con, 1000);
                second++;

            }


            super.handleMessage(msg);
        }
    };

    private void time_show() {
        String m = "", s = "";
        if (second == 60) {
            minute++;
            second = 0;
        }
        if (second < 10) {
            s = "0" + second;
        } else {
            s = second + "";
        }
        if (minute < 10) {
            m = "0" + minute;
        } else {
            m = minute + "";
        }
        time.setText(m + ":" + s);
        currentTime=m+":"+s;
    }

    private void startRecording() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        String path = Info.sdkPath + "/audio";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        int time = (int) (System.currentTimeMillis());
        java.sql.Timestamp ts = new java.sql.Timestamp(time);
        filePath = path + "/" + MD5.getStringMD5(ts+"")+ ".arm";
        mediaRecorder.setOutputFile(filePath);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.release();
    }


}
