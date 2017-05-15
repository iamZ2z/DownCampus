package com.logan.acthome.studentteacher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.acthome.more.WriteLog;
import com.logan.bean.LogManageBean;
import com.logan.constant.InterfaceTest;
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

@ContentView(R.layout.home_logmanage)
public class LogManageActivity extends Activity {
    // 列表
    private SimpleAdapter mAdapter;
    @ViewInject(R.id.list)
    private ListView mListView;
    private List<HashMap<String, Object>> mHashmap;
    private HashMap<String, Object> mMap;
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;
    private ImageView mCollectView;
    private Intent mIntent;
    private InterfaceTest interfaceTest = new InterfaceTest();

    private List<? extends Map<String, ?>> data2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        initView();

        urllogmanage();
    }

    private void initView() {
        titlebar.setTitle("日志管理");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCollectView = (ImageView) titlebar.addAction(new TitleBar.ImageAction(R.mipmap
                .nav_btn_add) {
            @Override
            public void performAction(View view) {
                mIntent = new Intent(LogManageActivity.this, WriteLog.class);
                startActivity(mIntent);
            }
        });
    }

    private List<HashMap<String, Object>> getData() {
        mHashmap = new ArrayList<>();
        mMap = new HashMap<>();
        mMap.put("img", R.drawable.touxiang);
        mMap.put("name", "汇报人：刘波");
        mMap.put("logtypeStr", "类型：日报");
        mMap.put("readornot", "未阅");
        mMap.put("createTime", "2017-02-17");
        mHashmap.add(mMap);
        return mHashmap;
    }

    private void urllogmanage() {
        String url = interfaceTest.getServerurl() + interfaceTest.getLogmanagequery();
        String token = interfaceTest.getToken();
        int i = 0;

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
                        Log.e("urllogmanage的result", "请求数据:" + str);
                        Gson gson = new Gson();
                        LogManageBean accountListBean = gson.fromJson(str,
                                LogManageBean.class);
                        //放入list
                        data2 = getData2(accountListBean);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter = new SimpleAdapter(LogManageActivity.this, data2, R.layout
                                        .home_log_item, new String[]{"img", "name", "logtypeStr",
                                        "createTime",
                                        "readornot"}, new int[]{R.id.img, R.id.name, R.id
                                        .logtypeStr, R.id.createTime, R
                                        .id.readornot});
                                mListView.setAdapter(mAdapter);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private List<? extends Map<String, ?>> getData2(LogManageBean accountListBean) {
                mHashmap = new ArrayList<>();
                for (int j = 0; j < accountListBean.getList().size(); j++) {
                    mMap = new HashMap<>();
                    mMap.put("img", R.drawable.touxiang);
                    mMap.put("name", accountListBean.getList().get(j).getUserName());
                    mMap.put("logtypeStr", accountListBean.getList().get(j).getLogtypeStr());
                    mMap.put("createTime", accountListBean.getList().get(j).getCreateTime());
                    mMap.put("readornot", "未阅");
                    mHashmap.add(mMap);
                }
                return mHashmap;
            }
        }).start();

    }
}
