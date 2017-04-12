package com.logan.acthome;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.adapter.ClassScheduleAdapter;
import com.logan.server.ClassScheduleListBean;
import com.util.TitleBar;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ContentView(R.layout.home_classschedule)
public class ClassScheduleActivity extends Activity implements OnClickListener {
    // 列表
    @ViewInject(R.id.schedule_list)
    private ListView mListView;
    private ArrayList<HashMap<String, Object>> mArrayList=new ArrayList<>();
    private HashMap<String, Object> mMap;

    @ViewInject(R.id.schedule_sp)
    private Spinner mSpinner;
    private List<String> list = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;
    private ClassScheduleAdapter mClassScheduleAdapter;
    private Intent mIntent;

    private String url = "http://192.168.89.173:8080/iccp/api/edu/querySyllabus.api";
    private String token;
    private String[] mArrayList_getData = new String[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        titlebar.setTitle("班级课表");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        spinner();
        classScheduleAdapter();

        mIntent = getIntent();
        if (mIntent.getStringExtra("token") != null) token = mIntent.getStringExtra("token");
        initURLClassSchedule();
    }

    private void initURLClassSchedule() {
        final OkHttpClient client = new OkHttpClient();
        //写死Params
        FormBody formBody = new FormBody.Builder().add("token", token).add("year",
                "4028812b5a6a878a015a6a8881f20001").add("semester",
                "4028812b5a7f452c015a7f4c469e0002").add("grade",
                "4028882d5a6a9068015a6a9c681e0001").add("clazz",
                "4028882d5a6a9068015a6a9e9fe30004").build();
        final Request request = new Request.Builder().url(url).post(formBody).build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        Log.e("result", "请求数据:" + str);
                        Gson gson = new Gson();
                        ClassScheduleListBean accountListBean = gson.fromJson(str,
                                ClassScheduleListBean.class);
                        for (int i = 0; i < accountListBean.getList().size(); i++) {
                            for (int j = 0; j < 5; j++) {
                                Log.e("id:", accountListBean.getList().get(i).get(j).getWeek());
                                Log.e("name:", accountListBean.getList().get(i).get(j).getSection
                                        ());
                                Log.e("name:", accountListBean.getList().get(i).get(j).getSubject
                                        ());
                                Log.e("name:", accountListBean.getList().get(i).get(j).getUser());

                                //数据填入列表
                                mArrayList_getData[j] = accountListBean.getList().get(0).get(j)
                                        .getSubject();
                            }
                        }
                        ClassScheduleActivity.this.runOnUiThread(updateThread);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    Runnable updateThread = new Runnable() {
        @Override
        public void run() {
            getData();
            mClassScheduleAdapter.notifyDataSetChanged();
        }
    };

    private void classScheduleAdapter() {
        //getData();
        mClassScheduleAdapter = new ClassScheduleAdapter(this, mArrayList);
        mListView.setAdapter(mClassScheduleAdapter);
    }

    private void spinner() {
        // 添加下拉list
        list.add("一年一班");
        list.add("一年二班");
        adapter = new ArrayAdapter<>(this, R.layout.spinner_workrest, list);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private List<? extends Map<String, ?>> getData() {
        mMap = new HashMap<>();
        mMap.put("item", "");
        mMap.put("mon", "一");
        mMap.put("tue", "二");
        mMap.put("wed", "三");
        mMap.put("thr", "四");
        mMap.put("fri", "五");
        mMap.put("sat", "六");
        mMap.put("sun", "日");
        mArrayList.add(mMap);

        mMap = new HashMap<>();
        mMap.put("item", "第一节");
        mMap.put("mon", mArrayList_getData[0]);
        mMap.put("tue", mArrayList_getData[1]);
        mMap.put("wed", mArrayList_getData[2]);
        mMap.put("thr", mArrayList_getData[3]);
        mMap.put("fri", mArrayList_getData[4]);
        mMap.put("sat", "化学");
        mMap.put("sun", "地理");
        mArrayList.add(mMap);

        return mArrayList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_backaccount:
                finish();
                break;
        }
    }

}
