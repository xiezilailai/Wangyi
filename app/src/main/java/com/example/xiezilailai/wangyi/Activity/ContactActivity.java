package com.example.xiezilailai.wangyi;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by 蝎子莱莱123 on 2016/1/8.
 */
public class ContactActivity extends AppCompatActivity implements com.example.xiezilailai.wangyi.AudioFragment.onRedioCallBack {

    private Button send, emotion, record, pic;
    private EditText editText;
    private ListView listView;
    private ContactListAdapter contactListAdapter;//listViewAdapter
    private List<ContactListInfo> data;//data集合，ContactInfo记录了每一条消息的具体内容，然后传给adapter在listView上进行展示
    private String id;
    private static final int TaKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;
    private MediaRecorder mediaRecorder;
    private static int OPEN = 1;
    private static int CLOSE = 2;
    private int audio_state = CLOSE;
    private int add_state = CLOSE;
    private ViewGroup.LayoutParams lay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_view);

        //初始化各部件，拉取记录
        init();




//点击录音按钮，弹出audiofragment
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.main_fragment);
                if (frameLayout.getVisibility() == View.GONE) {
                    frameLayout.setVisibility(View.VISIBLE);
                }

                if (audio_state == OPEN) {
                    frameLayout.setVisibility(View.GONE);
                    audio_state = CLOSE;
                } else {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    AudioFragment audioFragment=new AudioFragment();
                    audioFragment.setOnRedioCallBack(ContactActivity.this);
                    fragmentTransaction.replace(R.id.main_fragment, audioFragment);
                    fragmentTransaction.commit();
                    audio_state = OPEN;
                    add_state = CLOSE;
                }

            }
        });/*
        当editText数目发生改变时，决定发送按钮是“发送”还是“添加”


        */

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editText.length() > 0) {
                    send.setText("发送");
                } else {
                    send.setText("添加");
                }
            }
        });

        //发送按钮点击事件
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (send.getText() == "添加") {
                    /*
                    * 如果是“添加”功能，则打开addFragment
                    * */
                    FrameLayout frameLayout = (FrameLayout) findViewById(R.id.main_fragment);
                    if (frameLayout.getVisibility() == View.GONE) {
                        frameLayout.setVisibility(View.VISIBLE);
                    }


                    if (add_state == OPEN) {
                        frameLayout.setVisibility(View.GONE);
                        add_state = CLOSE;
                    } else {
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_fragment, new AddFragment());
                        fragmentTransaction.commit();
                        add_state = OPEN;
                        audio_state = CLOSE;
                    }


                } else {
                    /*
                    * 如果是“发送”
                    * 则将消息发送出去，发送成功之后添加到listView上
                    *
                    *
                    * */
                    final String content = editText.getText().toString();
                    IMMessage message = MessageBuilder.createTextMessage(id, SessionTypeEnum.P2P, content);
                    NIMClient.getService(MsgService.class).sendMessage(message, true).setCallback(new RequestCallback<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            addSelfText(content);
                            editText.setText("");
                        }

                        @Override
                        public void onFailed(int i) {
                            Toast.makeText(ContactActivity.this, "send failed!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onException(Throwable throwable) {
                            Toast.makeText(ContactActivity.this, "send error!", Toast.LENGTH_SHORT).show();

                        }
                    });
                }

            }
        });

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.main_fragment);
                if (frameLayout.getVisibility() == View.VISIBLE) {
                    frameLayout.setVisibility(View.GONE);
                    add_state = CLOSE;
                    audio_state = CLOSE;
                }
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.main_fragment);
                if (frameLayout.getVisibility() == View.VISIBLE) {
                    frameLayout.setVisibility(View.GONE);
                    add_state = CLOSE;
                    audio_state = CLOSE;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

        /*
        * 消息接受监听，
        * 先判断消息类型，然后添加到listView 中
        *
        *
        *
        * */
        com.netease.nimlib.sdk.Observer<List<IMMessage>> incoming =
                new com.netease.nimlib.sdk.Observer<List<IMMessage>>() {
                    @Override
                    public void onEvent(List<IMMessage> imMessages) {


                        for (final IMMessage a : imMessages) {

                            if (a.getMsgType() == MsgTypeEnum.text) {
                                ContactListInfo contactListInfo = new ContactListInfo();
                                contactListInfo.setType(ContactListInfo.OPPO_TEXT);
                                contactListInfo.setText(a.getContent());
                                data.add(contactListInfo);
                                contactListAdapter = new ContactListAdapter(getLayoutInflater(), data);
                                listView.setAdapter(contactListAdapter);
                            } else if (a.getMsgType() == MsgTypeEnum.image) {
                                NIMClient.getService(MsgService.class).downloadAttachment(a, false).setCallback(new RequestCallback() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        ContactListInfo contactListInfo = new ContactListInfo();
                                        contactListInfo.setType(ContactListInfo.OPPO_PIC);

                                        ImageAttachment b = (ImageAttachment) a.getAttachment();
                                        File tmp = new File(b.getThumbPathForSave());
                                        contactListInfo.setTag(b.getPathForSave());
                                        Uri uri = Uri.fromFile(tmp);
                                        Bitmap bitmap = null;
                                        try {
                                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        BitmapFactory.Options opts=new BitmapFactory.Options();
                                        opts.inJustDecodeBounds=true;
                                        BitmapFactory.decodeFile(b.getThumbPathForSave(), opts);
                                        int h=opts.outHeight;
                                        int w=opts.outWidth;

                                        lay.height=h*lay.width/w;
                                        contactListInfo.setSize(lay.width,lay.height);

                                        contactListInfo.setPic(bitmap);
                                        data.add(contactListInfo);
                                        contactListAdapter = new ContactListAdapter(getLayoutInflater(), data);
                                        listView.setAdapter(contactListAdapter);
                                    }

                                    @Override
                                    public void onFailed(int i) {

                                    }

                                    @Override
                                    public void onException(Throwable throwable) {

                                    }
                                });

                            }else if(a.getMsgType()==MsgTypeEnum.audio){
                                Toast.makeText(ContactActivity.this,"aa",Toast.LENGTH_SHORT).show();
                                NIMClient.getService(MsgService.class).downloadAttachment(a,true);
                                addOppoAudio(a.getAttachment());
                            }


                        }
                    }
                };
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incoming, true);


    }

/*
* 调取历史记录，建立一个EmptyMessage，进行查询（本地）
* 将查询到的结果，按照消息监听的方法展现在listView中
*
*
* 注意：历史消息有的是对方发出来的，有的是自己发出去的，要对方向进行判断
*
*
* */
    private void getHistory() {
        data = new ArrayList<>();
        long now = System.currentTimeMillis();
        IMMessage b = MessageBuilder.createEmptyMessage(id, SessionTypeEnum.P2P, now);
//        NIMClient.getService(MsgService.class).pullMessageHistory(b, 10000, true);
        NIMClient.getService(MsgService.class).queryMessageListEx(b, QueryDirectionEnum.QUERY_OLD, 10000, true)
                .setCallback(new RequestCallback<List<IMMessage>>() {
            @Override
            public void onSuccess(List<IMMessage> imMessages) {
                for (IMMessage a : imMessages) {
//                    AbortableFuture future2=NIMClient.getService(MsgService.class).downloadAttachment(a,false);

                    if (a.getDirect().equals(MsgDirectionEnum.Out) && a.getMsgType() == MsgTypeEnum.text) {
                        addSelfText(a.getContent());
                    } else if (a.getDirect().equals(MsgDirectionEnum.Out) && a.getMsgType() == MsgTypeEnum.image) {

                        NIMClient.getService(MsgService.class).downloadAttachment(a, false);
                        NIMClient.getService(MsgService.class).downloadAttachment(a, true);
                        addSelfPic(a.getAttachment());
                    } else if (a.getDirect().equals(MsgDirectionEnum.In) && a.getMsgType() == MsgTypeEnum.text) {
                        addOppoText(a.getContent());
                    } else if (a.getDirect().equals(MsgDirectionEnum.In) && a.getMsgType() == MsgTypeEnum.image) {

                        NIMClient.getService(MsgService.class).downloadAttachment(a, false);
                        NIMClient.getService(MsgService.class).downloadAttachment(a, true);
                        addOppoPic(a.getAttachment());
                    } else if (a.getDirect().equals(MsgDirectionEnum.Out) && a.getMsgType() == MsgTypeEnum.audio) {
                        NIMClient.getService(MsgService.class).downloadAttachment(a, true);
                        addSelfAudio(a.getAttachment());
                    } else if (a.getDirect().equals(MsgDirectionEnum.In) && a.getMsgType() == MsgTypeEnum.audio) {
                        NIMClient.getService(MsgService.class).downloadAttachment(a, true);
                        addOppoAudio(a.getAttachment());
                    }
                }

            }

            @Override
            public void onFailed(int i) {

            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }

    private void addSelfAudio(MsgAttachment attachment) {
        ContactListInfo contactListInfo=new ContactListInfo();
        contactListInfo.setType(ContactListInfo.SELF_SOUND);
        AudioAttachment audioAttachment=(AudioAttachment)attachment;
        contactListInfo.setTime(longTransString(audioAttachment.getDuration()));
        contactListInfo.setTag(audioAttachment.getPathForSave());
        data.add(contactListInfo);
        contactListAdapter=new ContactListAdapter(getLayoutInflater(),data);
        listView.setAdapter(contactListAdapter);
    }

    private void addOppoAudio(MsgAttachment attachment){
        ContactListInfo contactListInfo=new ContactListInfo();
        contactListInfo.setType(ContactListInfo.OPPO_SOUND);

        AudioAttachment audioAttachment=(AudioAttachment)attachment;
        contactListInfo.setTag(audioAttachment.getPathForSave());
        contactListInfo.setTime(longTransString(audioAttachment.getDuration()));
        data.add(contactListInfo);
        contactListAdapter=new ContactListAdapter(getLayoutInflater(),data);
        listView.setAdapter(contactListAdapter);
    }

    private String longTransString(long duration){
        String min,second;
        int time= (int) (duration/1000);
        if(time/60<10){
            min="0"+time/60;
        }
        else{
            min=time/60+"";
        }
        if(time%60<10){
            second="0"+time%60;
        }
        else{
            second=time%60+"";
        }
        return min+":"+second;
    }


    private void init() {
        LayoutInflater layoutInflater=LayoutInflater.from(this);
        View view1=layoutInflater.inflate(R.layout.model, null);
        ImageView a= (ImageView) view1.findViewById(R.id.imageView5);

        lay=a.getLayoutParams();

        listView = (ListView) findViewById(R.id.listView);
        send = (Button) findViewById(R.id.add_send);
        record = (Button) findViewById(R.id.audio);
        record.setTag("false");
        send.setTag("false");
        send.setText("添加");
        editText = (EditText) findViewById(R.id.text_send);
        emotion = (Button) findViewById(R.id.emotion);

        data = new ArrayList<>();
        Intent intent = getIntent();
        id = intent.getStringExtra("oppoId");
        getHistory();//拉取本队缓存记录
        listView.setSelection(listView.getCount()-1);//定位至最后一行

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TaKE_PHOTO) {
            File file = new File(Info.takePhotoPath);
            sendPic(Info.takePhotoPath, file, TaKE_PHOTO);
        } else {
            Uri uri = data.getData();

            String[] proj = {MediaStore.Images.Media.DATA};

            Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);

            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            actualimagecursor.moveToFirst();

            String img_path = actualimagecursor.getString(actual_image_column_index);

            File file = new File(img_path);
            if (!file.exists()) {
                Toast.makeText(ContactActivity.this, "no exits", Toast.LENGTH_SHORT).show();
                return;
            }
            sendPic(img_path, file, CHOOSE_PHOTO);
        }
    }

    private void sendPic(final String path, final File file, final int type) {
        IMMessage message = MessageBuilder.createImageMessage(
                id,
                SessionTypeEnum.P2P,
                file,
                null
        );
        NIMClient.getService(MsgService.class).sendMessage(message, true).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

//                Bitmap bitmap = getBitmapByWidth(path, 190, 20);

                Bitmap bitmap = null;
                ContactListInfo contactListinfo = new ContactListInfo();
                contactListinfo.setType(ContactListInfo.SELF_PIC);
                Uri uri = Uri.fromFile(file);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                BitmapFactory.Options opts=new BitmapFactory.Options();
                opts.inJustDecodeBounds=true;
                BitmapFactory.decodeFile(path, opts);
                int h=opts.outHeight;
                int w=opts.outWidth;
                lay.height=h*lay.width/w;

                contactListinfo.setSize(lay.width,lay.height);
                contactListinfo.setPic(bitmap);
                contactListinfo.setTag(path);
                data.add(contactListinfo);
                contactListAdapter = new ContactListAdapter(getLayoutInflater(), data);
                listView.setAdapter(contactListAdapter);
                if (type == TaKE_PHOTO) {
                    file.delete();
                }
                listView.setSelection(listView.getCount()-1);
            }

            @Override
            public void onFailed(int i) {
                Toast.makeText(ContactActivity.this, "send failed!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onException(Throwable throwable) {
                Toast.makeText(ContactActivity.this, "send error!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void addSelfText(String content) {
        ContactListInfo contactListInfo = new ContactListInfo();
        contactListInfo.setType(ContactListInfo.SELF_TEXT);
        contactListInfo.setText(content);
        data.add(contactListInfo);
        contactListAdapter = new ContactListAdapter(getLayoutInflater(), data);
        listView.setAdapter(contactListAdapter);
        listView.setSelection(listView.getCount() - 1);
    }

    private void addSelfPic(MsgAttachment msgAttachment) {
        ContactListInfo contactListInfo = new ContactListInfo();
        contactListInfo.setType(ContactListInfo.SELF_PIC);

        ImageAttachment b = (ImageAttachment) msgAttachment;
        contactListInfo.setTag(b.getPathForSave());
        String path = b.getThumbPathForSave();
        File tmp = new File(path);
        contactListInfo.setTag(b.getPathForSave());

        Uri uri = Uri.fromFile(tmp);
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BitmapFactory.Options opts=new BitmapFactory.Options();
        opts.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(path, opts);
        int h=opts.outHeight;
        int w=opts.outWidth;

        lay.height=h*lay.width/w;
        contactListInfo.setSize(lay.width,lay.height);
        contactListInfo.setPic(bitmap);
        data.add(contactListInfo);
        contactListAdapter = new ContactListAdapter(getLayoutInflater(), data);
        listView.setAdapter(contactListAdapter);
        listView.setSelection(listView.getCount() - 1);

    }



    private void addOppoText(String content) {
        ContactListInfo contactListInfo = new ContactListInfo();
        contactListInfo.setType(ContactListInfo.OPPO_TEXT);
        contactListInfo.setText(content);
        data.add(contactListInfo);
        contactListAdapter = new ContactListAdapter(getLayoutInflater(), data);
        listView.setAdapter(contactListAdapter);
        listView.setSelection(listView.getCount() - 1);
    }

    private void addOppoPic(MsgAttachment msgAttachment) {
        ContactListInfo contactListInfo = new ContactListInfo();
        contactListInfo.setType(ContactListInfo.OPPO_PIC);
        ImageAttachment b = (ImageAttachment) msgAttachment;
        File tmp = new File(b.getThumbPathForSave());
        contactListInfo.setTag(b.getPathForSave());

        Uri uri = Uri.fromFile(tmp);
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BitmapFactory.Options opts=new BitmapFactory.Options();
        opts.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(b.getThumbPathForSave(), opts);
        int h=opts.outHeight;
        int w=opts.outWidth;
//        if (h > lay.height) {
//            lay.width = (int) (w * 1.0000000 / h * lay.height);
//        } else {
        lay.height=h*lay.width/w;
//        }
        contactListInfo.setSize(lay.width,lay.height);
        contactListInfo.setPic(bitmap);
        data.add(contactListInfo);
        contactListAdapter = new ContactListAdapter(getLayoutInflater(), data);
        listView.setAdapter(contactListAdapter);
        listView.setSelection(listView.getCount() - 1);
    }

//




   

    @Override
    public void sendAudio(final String filePath,final String time) {
        IMMessage imMessage=MessageBuilder.createAudioMessage(
                id,SessionTypeEnum.P2P,new File(filePath),timeTrans(time)
        );
        NIMClient.getService(MsgService.class).sendMessage(imMessage,true).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ContactListInfo contactListInfo = new ContactListInfo();
                contactListInfo.setType(ContactListInfo.SELF_SOUND);
                contactListInfo.setTag(filePath);
                contactListInfo.setTime(time);
                data.add(contactListInfo);
                contactListAdapter=new ContactListAdapter(getLayoutInflater(),data);
                listView.setAdapter(contactListAdapter);
                listView.setSelection(listView.getCount()-1);
            }

            @Override
            public void onFailed(int i) {

            }

            @Override
            public void onException(Throwable throwable) {

            }
        });

    }

    private long timeTrans(String time) {
        int min=((int)time.charAt(0)-(int)('0'))*10+((int)time.charAt(1)-(int)('0'));
        int second=((int)time.charAt(3)-(int)('0'))*10+((int)time.charAt(4)-(int)('0'));
        return (min*60+second)*1000;
    }
}
