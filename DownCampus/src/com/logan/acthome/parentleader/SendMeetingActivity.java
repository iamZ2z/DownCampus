package com.logan.acthome.parentleader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.bean.SaveMeetingBean;
import com.logan.net.InterfaceTest;
import com.util.title.TitleBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Z2z on 2017/4/11.
 */

@ContentView(R.layout.home_sendmeetingmanage)
public class SendMeetingActivity extends Activity {
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;
    private Intent mIntent;
    @ViewInject(R.id.tvname)
    private TextView tvname;
    private ArrayList<String> mArrayList;

    @ViewInject(R.id.ettitle)
    private EditText ettitle;
    @ViewInject(R.id.etcontent)
    private EditText etcontent;
    private String receiverid = "";
    private String receivername = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        titlebar.setTitle("发布会议");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titlebar.setActionTextColor(Color.WHITE);
        titlebar.addAction(new TitleBar.TextAction("发布") {
            @Override
            public void performAction(View view) {
                sendurl();
            }
        });
    }

    private void sendurl() {
        if (ettitle.getText().toString().equals(null) || etcontent.getText().toString().equals
                (null) || receivername.equals(""))
            Toast.makeText(SendMeetingActivity.this, "信息填写不完整", Toast
                    .LENGTH_SHORT).show();
        else {
            InterfaceTest interfaceTest = new InterfaceTest();
            String name = ettitle.getText().toString();
            String content = etcontent.getText().toString();
            String userid = interfaceTest.getUser_id();
            String url = interfaceTest.getServerurl() + interfaceTest.getMeetingsave();
            String token = interfaceTest.getToken();
            FormBody formBody = new FormBody.Builder().add("token", token).add("name", name).add
                    ("content", content).add("receiverIds", receiverid).add("receivers",
                    receivername).add("user.id", userid).build();
            Log.e("发送会议的url:","url:"+url+"\ntoken:"+token+"\nname:"+name+"\ncontent:"+content);
            Log.e("发送会议url:","receiverIds:"+receiverid+"\nreceivers:"+receivername+"\nuser.id:"+userid);

            final Request request = new Request.Builder().url(url).post(formBody).build();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Response response = new OkHttpClient().newCall(request).execute();
                        if (response.isSuccessful()) {
                            String str = response.body().string();
                            Log.e("发送会议的result", "请求数据:" + str);
                            final SaveMeetingBean bean = new Gson().fromJson(str, SaveMeetingBean
                                    .class);
                            mArrayList = new ArrayList<>();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (bean.getCode().equals("0")) {
                                        Toast.makeText(SendMeetingActivity.this, "发送成功", Toast
                                                .LENGTH_SHORT).show();
                                        finish();
                                    } else
                                        Toast.makeText(SendMeetingActivity.this, "发送失败", Toast
                                                .LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Event(value = R.id.btnadd)
    private void onbtnaddClick(View v) {
        Intent intent = new Intent(this, ChooseObjectActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //得到新Activity关闭后返回的数据
        receiverid = data.getStringExtra("strid");
        receivername = data.getStringExtra("strname");
        tvname.setText(receivername);
    }
}
