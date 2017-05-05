package com.logan.acthome.parentleader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.bean.MyApproveAuditBean;
import com.logan.bean.MyApproveBean;
import com.logan.constant.InterfaceTest;
import com.util.TitleBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ContentView(R.layout.home_approve_detail)
public class ApproveDetail extends Activity {
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;

    @ViewInject(R.id.name)
    private TextView name;
    @ViewInject(R.id.userid)
    private TextView userid;
    @ViewInject(R.id.type)
    private TextView type;
    @ViewInject(R.id.begintime)
    private TextView begintime;
    @ViewInject(R.id.endtime)
    private TextView endtime;
    @ViewInject(R.id.duration)
    private TextView duration;
    @ViewInject(R.id.reason)
    private TextView reason;
    @ViewInject(R.id.submittime)
    private TextView submittime;
    @ViewInject(R.id.submitwait)
    private TextView submitwait;

    private String id;
    @ViewInject(R.id.submityes)
    private Button submityes;
    @ViewInject(R.id.submitno)
    private Button submitno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        titlebar.setTitle("审批详情");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        doload();
    }

    private void doload() {
        int postion = getIntent().getIntExtra("position", 0);
        MyApproveBean myApproveBean = (MyApproveBean) getIntent().getSerializableExtra
                ("approvedetail");
        name.setText("姓名：" + myApproveBean.getData().get(postion).getUserName());
        userid.setText("帐号：" + myApproveBean.getData().get(postion).getUserId());
        type.setText(myApproveBean.getData().get(postion).getLeavetypeStr());
        begintime.setText(myApproveBean.getData().get(postion).getStart());
        endtime.setText(myApproveBean.getData().get(postion).getEnd());
        duration.setText(myApproveBean.getData().get(postion).getNum());
        reason.setText(myApproveBean.getData().get(postion).getContent());
        submittime.setText("申请时间：" + myApproveBean.getData().get(postion).getCreateTime());
        submitwait.setText(myApproveBean.getData().get(postion).getAuditStr());
        id = myApproveBean.getData().get(postion).getId();
    }

    @Event(value = {R.id.submityes, R.id.submitno})
    private void onsubmit_Click(View v) {
        switch (v.getId()) {
            case R.id.submityes:
                dourl("" + 1);
                break;
            case R.id.submitno:
                dourl("" + 2);
                break;
            default:
                break;
        }
    }


    private void dourl(String submitstate) {
        InterfaceTest interfaceTest = new InterfaceTest();
        String url = interfaceTest.getServerurl() + interfaceTest.getLeaveaudit();
        String token = interfaceTest.getToken();
        String userId = interfaceTest.getUser_id();
        Log.e("1", "123"+submitstate);

        final OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder().add("token", token).add("id", id).add("audit",
                submitstate).add("auditor.id", userId).build();
        final Request request = new Request.Builder().url(url).post(formBody).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("1", "1234");
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        Log.e("1", "12345");
                        String str = response.body().string();
                        Log.e("Myapprove的result", "请求数据:" + str);
                        final MyApproveAuditBean accbean = new Gson().fromJson(str,
                                MyApproveAuditBean.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("1", "是否通过");
                                if (accbean.getCode().equals("0"))
                                    Toast.makeText(ApproveDetail.this, "已通过", Toast
                                            .LENGTH_SHORT).show();
                                else if (accbean.getCode().equals("1"))
                                    Toast.makeText(ApproveDetail.this, "已拒绝", Toast
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
