package com.logan.acthome.parentleader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.SimpleAdapter;

import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.bean.MeetingManagerBean;
import com.logan.bean.MultiSpinnerBean;
import com.logan.bean.OrganizationBean;
import com.logan.constant.InterfaceTest;
import com.logan.widgets.MultiSpinner;
import com.util.title.TitleBar;

import org.xutils.view.annotation.ContentView;
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

/**
 * Created by Z2z on 2017/4/11.
 */

@ContentView(R.layout.home_sendmeetingmanage)
public class SendMeetingActivity extends Activity {
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;
    private Intent mIntent;
    @ViewInject(R.id.spmulti)
    private MultiSpinner mMultiSpinner;
    private ArrayList<String> mArrayList;

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
                mIntent = new Intent();
                mIntent.setClass(SendMeetingActivity.this, MeetingManage.class);
                startActivity(mIntent);
            }
        });

        //multiSpinnerListener();
        dourl();
    }

    private void multiSpinnerListener() {
        mMultiSpinner.setTitle("选择发送对象");
        ArrayList multispinnerlist = new ArrayList();
        for (int i = 0; i < mArrayList.size(); i++) {
            MultiSpinnerBean multiSpinnerBean = new MultiSpinnerBean();
            multiSpinnerBean.setName(mArrayList.get(i));
            multiSpinnerBean.setValue(i+1);
            multispinnerlist.add(multiSpinnerBean);
        }
        mMultiSpinner.setDataList(multispinnerlist);
    }

    private void dourl() {
        InterfaceTest interfaceTest = new InterfaceTest();
        String url = interfaceTest.getServerurl() + interfaceTest.getOrganizationquery();
        String token = interfaceTest.getToken();
        FormBody formBody = new FormBody.Builder().add("token", token).build();
        final Request request = new Request.Builder().url(url).post(formBody).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = new OkHttpClient().newCall(request).execute();
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        Log.e("urlmeeting的result", "请求数据:" + str);
                        final OrganizationBean bean = new Gson().fromJson(str, OrganizationBean
                                .class);
                        mArrayList = new ArrayList<>();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < bean.getData().size(); i++) {
                                    String str = bean.getData().get(i).getName();
                                    mArrayList.add(str);
                                }
                                multiSpinnerListener();
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
