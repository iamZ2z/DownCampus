package com.logan.acthome.parentleader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xutils.view.annotation.Event;
import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.bean.MyApproveBean;
import com.logan.constant.InterfaceTest;
import com.util.title.TitleBar;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ContentView(R.layout.home_myapprove)
public class MyApproveActivity extends Activity {
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;
    @ViewInject(R.id.list)
    private ListView mListView;
    private SimpleAdapter mAdapter;
    private List<HashMap<String, Object>> mHashmap;
    private HashMap<String, Object> mMap;

    private Intent mIntent;
    @ViewInject(R.id.layout_type)
    private LinearLayout layout_type;
    @ViewInject(R.id.list_type)
    private ListView list_type;
    private String str_type[] = new String[]{"事假", "病假", "婚假"};

    private List<? extends Map<String, ?>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        titlebar.setTitle("我的审批");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView mCollectView = (ImageView) titlebar.addAction(new TitleBar.ImageAction(R.mipmap
                .nav_btn_search) {
            @Override
            public void performAction(View view) {
                mIntent = new Intent(MyApproveActivity.this, ApproveSearch.class);
                startActivity(mIntent);
            }
        });
        list_Listener();

        dourl();
    }

    private void list_Listener() {
        mAdapter = new SimpleAdapter(this, getData(),
                R.layout.home_approve_list, new String[]{"head", "name",
                "type", "wait", "time"}, new int[]{R.id.head,
                R.id.name, R.id.type, R.id.wait, R.id.leavetime});
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent mIntent = new Intent(MyApproveActivity.this, ApproveDetail.class);
                mIntent.putExtra("approvedetail", position);
                startActivity(mIntent);
            }
        });
    }

    private List<? extends Map<String, ?>> getData() {
        mHashmap = new ArrayList<>();
        mMap = new HashMap<>();
        mMap.put("head", R.drawable.touxiang);
        mMap.put("name", "姓名");
        mMap.put("type", "审批类型");
        mMap.put("wait", "待审批");
        mMap.put("time", "2017-03-15");
        mHashmap.add(mMap);
        return mHashmap;
    }

    @Event(value = R.id.layout_type)
    private void onlayoutClick(View v) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_approve,
                str_type);
        list_type.setAdapter(arrayAdapter);
        list_type.setVisibility(View.VISIBLE);
        list_type.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MyApproveActivity.this, str_type[position], Toast.LENGTH_SHORT)
                        .show();
                list_type.setVisibility(View.GONE);
            }
        });
    }

    private void dourl() {
        InterfaceTest interfaceTest = new InterfaceTest();
        String url = interfaceTest.getServerurl() + interfaceTest.getLeavequery();
        String token = interfaceTest.getToken();

        final OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder().add("token", token).add("pageSize", "10")
                .build();
        final Request request = new Request.Builder().url(url).post(formBody).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        Log.e("Myapprove的result", "请求数据:" + str);
                        final MyApproveBean bean = new Gson().fromJson(str, MyApproveBean.class);
                        data = getData2(bean);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter = new SimpleAdapter(MyApproveActivity.this, data,
                                        R.layout.home_approve_list, new String[]{"head", "name",
                                        "type", "wait", "time"}, new int[]{R.id.head,
                                        R.id.name, R.id.type, R.id.wait, R.id.leavetime});
                                mListView.setAdapter(mAdapter);
                                mListView.setOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view,
                                                            int position, long id) {
                                        Intent mIntent = new Intent(MyApproveActivity.this, ApproveDetail.class);
                                        mIntent.putExtra("position",position);
                                        mIntent.putExtra("approvedetail", bean);
                                        startActivity(mIntent);
                                    }
                                });

                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private List<? extends Map<String, ?>> getData2(MyApproveBean accountListBean) {
                mHashmap = new ArrayList<>();
                for (int j = 0; j < accountListBean.getData().size(); j++) {
                    mMap = new HashMap<>();
                    mMap.put("head", R.drawable.touxiang);
                    mMap.put("name", "姓名：" + accountListBean.getData().get(j).getUserName());
                    mMap.put("type", "请假类型：" + accountListBean.getData().get(j).getLeavetypeStr());
                    mMap.put("wait", accountListBean.getData().get(j).getAuditStr());
                    mMap.put("time", accountListBean.getData().get(j).getCreateTime());
                    mHashmap.add(mMap);
                }
                return mHashmap;
            }
        }).start();
    }
}
