package com.logan.acthome.more;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.bean.LeaveRecordBean;
import com.logan.bean.LeaveRecordStudentBean;
import com.logan.constant.InterfaceTest;
import com.util.TitleBar;

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

@ContentView(R.layout.home_leaverecord)
public class LeaveRecordActivity extends Activity {
    // 列表
    private SimpleAdapter mAdapter;
    @ViewInject(R.id.leaverecord_list)
    private ListView mListView;
    private List<HashMap<String, Object>> mHashmap;
    private HashMap<String, Object> mMap;
    private String benginend_time;

    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;
    InterfaceTest interfaceTest = new InterfaceTest();
    private List<? extends Map<String, ?>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        titlebar.setTitle("请假记录");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        receiveTime();

        mAdapter = new SimpleAdapter(this, getData(), R.layout.home_leaverecord_list,
                new String[]{"iv", "name", "submit", "leavetime", "or"}, new int[]
                {R.id.iv, R.id.name, R.id.submit, R.id.leavetime, R.id.or});
        mListView.setAdapter(mAdapter);

        if (interfaceTest.getRole().equals("学生")) urlstudentleave();
        else urlleave();
    }

    private void receiveTime() {
        Intent mIntent = getIntent();
        benginend_time = mIntent.getStringExtra("begin_time") + "——"
                + mIntent.getStringExtra("end_time");
    }

    private List<? extends Map<String, ?>> getData() {
        mHashmap = new ArrayList<>();
        mMap = new HashMap<>();
        mMap.put("iv", R.drawable.touxiang);
        mMap.put("name", "刘波");
        mMap.put("submit", "申请时间：");
        mMap.put("leavetime", benginend_time);
        mMap.put("or", "待审核");
        mHashmap.add(mMap);
        return mHashmap;
    }

    private void urlstudentleave() {
        String url = interfaceTest.getServerurl() + interfaceTest.getStudentleavequery();
        String token = interfaceTest.getToken();
        String studentId = interfaceTest.getStudentId();

        final OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder().add("token", token).add("studentId",
                studentId).build();
        final Request request = new Request.Builder().url(url).post(formBody).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        Log.e("urlstudentleave的result", "请求数据:" + str);
                        LeaveRecordStudentBean accountListBean = new Gson().fromJson(str,
                                LeaveRecordStudentBean.class);
                        data = getData2(accountListBean);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //放入list
                                mAdapter = new SimpleAdapter(LeaveRecordActivity.this, data, R
                                        .layout.home_leaverecord_list, new String[]{"iv", "name",
                                        "submit", "leavetime", "or"}, new int[]{R.id.iv, R.id
                                        .name, R.id.submit, R.id.leavetime, R.id.or});
                                mListView.setAdapter(mAdapter);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private List<? extends Map<String, ?>> getData2(LeaveRecordStudentBean
                                                                    accountListBean) {
                mHashmap = new ArrayList<>();
                for (int j = 0; j < accountListBean.getData().size(); j++) {
                    mMap = new HashMap<>();
                    mMap.put("iv", R.drawable.touxiang);
                    mMap.put("name", "刘波");
                    mMap.put("submit", accountListBean.getData().get(j).getApplyTime());
                    mMap.put("leavetime", accountListBean.getData().get(j).getStartTime() + "——" +
                            accountListBean.getData().get(j).getEndTime());
                    if (accountListBean.getData().get(j).getApprove().equals("0"))
                        mMap.put("or", "未审批");
                    else if (accountListBean.getData().get(j).getApprove().equals("1"))
                        mMap.put("or", "同意");
                    else if (accountListBean.getData().get(j).getApprove().equals("2"))
                        mMap.put("or", "驳回");
                    else if (accountListBean.getData().get(j).getApprove().equals("3"))
                        mMap.put("or", "不通过");
                    mHashmap.add(mMap);
                }
                return mHashmap;
            }
        }).start();
    }

    private void urlleave() {
        String url = interfaceTest.getServerurl() + interfaceTest.getLeavequery();
        String token = interfaceTest.getToken();
        final OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder().add("token", token).build();
        final Request request = new Request.Builder().url(url).post(formBody).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        Log.e("urlleave的result", "请求数据:" + str);
                        LeaveRecordBean accountListBean = new Gson().fromJson(str,
                                LeaveRecordBean.class);
                        data = getData2(accountListBean);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //放入list
                                mAdapter = new SimpleAdapter(LeaveRecordActivity.this, data, R
                                        .layout.home_leaverecord_list, new String[]{"iv", "name",
                                        "submit", "leavetime", "or"}, new int[]{R.id.iv, R.id
                                        .name, R.id.submit, R.id.leavetime, R.id.or});
                                mListView.setAdapter(mAdapter);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private List<? extends Map<String, ?>> getData2(LeaveRecordBean accountListBean) {
                mHashmap = new ArrayList<>();
                for (int j = 0; j < accountListBean.getData().size(); j++) {
                    mMap = new HashMap<>();
                    mMap.put("iv", R.drawable.touxiang);
                    mMap.put("name", accountListBean.getData().get(j).getUserName());
                    mMap.put("submit", "申请时间：" + accountListBean.getData().get(j).getCreateTime());
                    mMap.put("leavetime", "请假时间：" + accountListBean.getData().get(j).getStart() +
                            "——" + accountListBean.getData().get(j).getEnd());
                    mMap.put("or", accountListBean.getData().get(j).getAuditStr());
                    mHashmap.add(mMap);
                }
                return mHashmap;
            }
        }).start();
    }
}
