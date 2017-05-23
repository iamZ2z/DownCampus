package com.logan.acthome.studentteacher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.bean.ClassActivityBean;
import com.logan.constant.InterfaceTest;
import com.util.title.TitleBar;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ContentView(R.layout.activity_classactivity)
public class ClassActivityActivity extends Activity {
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;
    @ViewInject(R.id.sp_subject)
    private Spinner sp_subject;
    String[] str_subject = {"全部", "课内", "课外"};
    @ViewInject(R.id.sp_begindata)
    private Spinner sp_begindata;
    String[] str_begindata = {"2017-02-15", "2017-02-01", "2017-03-01"};
    @ViewInject(R.id.sp_enddata)
    private Spinner sp_enddata;
    String[] str_enddata = {"2017-02-15", "2017-02-01", "2017-03-01"};
    @ViewInject(R.id.list)
    private ListView list;
    private SimpleAdapter mAdapter;
    private List<HashMap<String, Object>> mHashmap;
    private HashMap<String, Object> mMap;
    private List<? extends Map<String, ?>> data;

    @ViewInject(R.id.swiperefresh)
    private SwipeRefreshLayout swiperefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        initView();
        spinner_subject();
        spinner_begindata();
        spinner_enddata();

        dourl();
        swipe();
    }

    private void initView() {
        titlebar.setTitle("班级活动");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAdapter = new SimpleAdapter(this, getData(),
                R.layout.home_classactactivity_list, new String[]{"leavetime",
                "content", "subject", "author"}, new int[]{
                R.id.leavetime, R.id.content, R.id.subject, R.id.author});
        list.setAdapter(mAdapter);
    }

    private void spinner_subject() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_bluebord, str_subject);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 绑定 Adapter到控件
        sp_subject.setAdapter(adapter);
        sp_subject.setOnItemSelectedListener(new OnItemSelectedListener() {
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

    private void spinner_begindata() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_bluebord, str_begindata);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 绑定 Adapter到控件
        sp_begindata.setAdapter(adapter);
        sp_begindata.setOnItemSelectedListener(new OnItemSelectedListener() {
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

    private void spinner_enddata() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_bluebord, str_enddata);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 绑定 Adapter到控件
        sp_enddata.setAdapter(adapter);
        sp_enddata.setOnItemSelectedListener(new OnItemSelectedListener() {
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

    private List<? extends Map<String, ?>> getData() {
        mHashmap = new ArrayList<HashMap<String, Object>>();
        mMap = new HashMap<String, Object>();
        mMap.put("subject", "活动类型");
        mMap.put("author", "刘波");
        mMap.put("content", "生活就像海洋，只有意志坚强的人才能到达彼岸。");
        mMap.put("leavetime", "2017-02-01 16:00");
        mHashmap.add(mMap);
        return mHashmap;
    }

    private void dourl() {
        InterfaceTest interfaceTest = new InterfaceTest();
        String url = interfaceTest.getServerurl() + interfaceTest.getClassactivity();
        String token = interfaceTest.getToken();
        String studentId = interfaceTest.getStudentId();
        Log.e("班级活动的studentId", "请求数据:" + studentId);

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
                        Log.e("班级活动的result", "请求数据:" + str);
                        Gson gson = new Gson();
                        ClassActivityBean accountListBean = gson.fromJson(str,
                                ClassActivityBean.class);
                        data = getData2(accountListBean);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter = new SimpleAdapter(ClassActivityActivity.this, data,
                                        R.layout.home_classactactivity_list, new
                                        String[]{"leavetime",
                                        "content", "subject", "author"}, new int[]{R.id
                                        .leavetime, R.id.content, R.id.subject,
                                        R.id.author});
                                list.setAdapter(mAdapter);

                                mAdapter.notifyDataSetChanged();
                                swiperefresh.setRefreshing(false);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private List<? extends Map<String, ?>> getData2(ClassActivityBean accountListBean) {
                mHashmap = new ArrayList<>();
                for (int j = 0; j < accountListBean.getData().size(); j++) {
                    mMap = new HashMap<>();
                    mMap.put("subject", accountListBean.getData().get(j).getName());
                    switch (accountListBean.getData().get(j).getActivityType()) {
                        case "1":
                            mMap.put("author", "日常班会");
                            break;
                        case "2":
                            mMap.put("author", "主题班会");
                            break;
                        case "3":
                            mMap.put("author", "文艺活动");
                            break;
                        case "4":
                            mMap.put("author", "体育活动");
                            break;
                        case "5":
                            mMap.put("author", "小组活动");
                            break;
                        case "6":
                            mMap.put("author", "劳动活动");
                            break;
                        case "7":
                            mMap.put("author", "公益活动");
                            break;
                        case "8":
                            mMap.put("author", "节日活动");
                            break;
                        case "9":
                            mMap.put("author", "旅游活动");
                            break;
                        default:
                            break;
                    }
                    mMap.put("content", accountListBean.getData().get(j).getSummary());
                    mMap.put("leavetime", accountListBean.getData().get(j).getStartTime() + "-" +
                            accountListBean.getData().get(j).getEndTime());
                    mHashmap.add(mMap);
                }
                return mHashmap;
            }
        }).start();

    }


    private void swipe() {
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dourl();
            }
        });
    }


}
