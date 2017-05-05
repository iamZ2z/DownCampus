package com.logan.acthome.more;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.adapter.RateContentAdapter;
import com.logan.bean.RateContentBean;
import com.logan.constant.InterfaceTest;
import com.util.TitleBar;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ContentView(R.layout.home_ratecontent)
public class RateContentActivity extends Activity {
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;
    @ViewInject(R.id.sp)
    private Spinner sp;
    String[] str = {"李刚", "郭霖", "鸿洋"};
    @ViewInject(R.id.list)
    private ListView mListView;
    private ArrayList<RateActivityUtil> mArrayList;

    @ViewInject(R.id.btn)
    private Button btn;
    private RateContentAdapter adapter_arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);

        titlebar.setTitle("教师评价详情");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        spinner_teacher();
        list_Listener();
        dourl();
    }

    private void list_Listener() {
        mArrayList = new ArrayList<>();
        RateActivityUtil util1 = new RateActivityUtil("老师修养",
                "爱岗敬业，工作认真。沟通时态度和气、无体罚和变相体罚学生现象。", 10, 0);
        mArrayList.add(util1);
        RateActivityUtil util2 = new RateActivityUtil("家访情况", "主动家访，交流", 20, 0);
        mArrayList.add(util2);
        RateActivityUtil util3 = new RateActivityUtil("家访情况", "主动家访，交流", 50, 0);
        mArrayList.add(util3);
        final RateContentAdapter adapter = new RateContentAdapter(this, mArrayList);
        mListView.setAdapter(adapter);
    }

    private void spinner_teacher() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, str);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                /*
                 * Toast.makeText(LeaveActivity.this,"你点击的是" +
				 * leave_type[position], Toast.LENGTH_SHORT).show();
				 */
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Event(value = R.id.btn)
    private void onBtnClick(View v) {
        int pos = mArrayList.get(0).getSetscore();
        int pos2 = mArrayList.get(1).getSetscore();
        int pos3 = mArrayList.get(2).getSetscore();
        Toast.makeText(this, "测试提交：" + pos + pos2 + pos3, Toast.LENGTH_SHORT).show();
    }

    private void dourl() {
        InterfaceTest interfaceTest = new InterfaceTest();
        String url = interfaceTest.getServerurl() + interfaceTest.getStudentquery();
        String token = interfaceTest.getToken();
        String studentId = interfaceTest.getUser_id();

        FormBody formBody = new FormBody.Builder().add("token", token).add("studentId",
                studentId).build();
        final Request request = new Request.Builder().url(url).post(formBody).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = new OkHttpClient().newCall(request).execute();
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        Log.e("RateContent的result", "请求数据:" + str);
                        final RateContentBean bean = new Gson().fromJson(str, RateContentBean
                                .class);
                        mArrayList = new ArrayList<>();
                        for (int j = 0; j < bean.getData().size(); j++) {
                            String str1 = bean.getData().get(j).getProject();
                            String str2 = bean.getData().get(j).getStandard();
                            int str3 = Integer.parseInt(bean.getData().get(j).getScore());
                            int str4 = Integer.parseInt(bean.getData().get(j).getCode());
                            RateActivityUtil util = new RateActivityUtil(str1, str2, str3, str4);
                            mArrayList.add(util);
                        }
                        final RateContentAdapter adapter = new RateContentAdapter
                                (RateContentActivity.this, mArrayList);
                        mListView.setAdapter(adapter);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

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
