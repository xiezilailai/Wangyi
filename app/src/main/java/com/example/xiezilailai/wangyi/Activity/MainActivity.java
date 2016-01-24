package com.example.xiezilailai.wangyi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

public class MainActivity extends AppCompatActivity {

    private EditText myid, mypw, opid;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });


    }

    private void register() {
        LoginInfo info = new LoginInfo(myid.getText().toString(), MD5.getStringMD5(mypw.getText().toString())); // config...
        RequestCallback<LoginInfo> callback =
                new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo loginInfo) {
                        Intent intent = new Intent();
                        intent.putExtra("oppoId", opid.getText().toString());
                        intent.setClass(MainActivity.this, ContactActivity.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onFailed(int i) {
                        Toast.makeText(MainActivity.this, "no", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onException(Throwable throwable) {
                        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();

                    }
                };
        NIMClient.getService(AuthService.class).login(info)
                .setCallback(callback);
    }

    private void init() {
        myid = (EditText) findViewById(R.id.myid);
        myid.setText("jskjskjsk");
        mypw = (EditText) findViewById(R.id.mypassword);
        mypw.setText("jskjsk");

        opid = (EditText) findViewById(R.id.oppoid);
        opid.setText("jskjskjsk2");

        button=(Button)findViewById(R.id.register);
    }


}
