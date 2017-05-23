package com.logan.acthome.studentteacher;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.adapter.WorkRestAdapter;
import com.logan.bean.WorkRestBean;
import com.logan.constant.InterfaceTest;
import com.logan.constant.UsuallyData;
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

@ContentView(R.layout.home_workrest)
public class WorkRestActivity extends Activity {
    // spinner
    @ViewInject(R.id.workrest_year)
    private Spinner mSpinner;
    private List<String> list = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    // spinner2
    @ViewInject(R.id.workrest_term)
    private Spinner mSpinner2;
    private List<String> list2 = new ArrayList<>();
    private ArrayAdapter<String> adapter2;
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;
    // 列表
    @ViewInject(R.id.list)
    private ListView mListView;
    private ArrayList<HashMap<String, Object>> mArrayList;
    private HashMap<String, Object> mHashMap;
    private WorkRestAdapter mWorkRestAdapter;

    private InterfaceTest interfaceTest = new InterfaceTest();
    private UsuallyData usuallyData = new UsuallyData();
    private List<? extends Map<String, ?>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        titlebar.setTitle("作息安排");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        spinner_year();
        spinner_term();

        // 禁止滑动
        mListView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        return true;
                    default:
                        break;
                }
                return true;
            }
        });
        workRestAdapter();

        urlworkrest();
    }

    private void spinner_year() {
        // 添加下拉list
        list.add("2016-2017");
        list.add("2017-2018");
        adapter = new ArrayAdapter<>(this, R.layout.spinner_workrest, list);
        adapter.setDropDownViewResource(R.layout.spinnerdropdownitem);
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

    private void spinner_term() {
        // 添加下拉list
        list2.add("春季学期");
        list2.add("冬季学期");
        adapter2 = new ArrayAdapter<>(this, R.layout.spinner_workrest, list2);
        adapter2.setDropDownViewResource(R.layout.spinnerdropdownitem);
        mSpinner2.setAdapter(adapter2);
        mSpinner2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void workRestAdapter() {
        getData();
        mWorkRestAdapter = new WorkRestAdapter(this, mArrayList);
        mListView.setAdapter(mWorkRestAdapter);
    }

    private List<? extends Map<String, ?>> getData() {
        mArrayList = new ArrayList<>();
        mHashMap = new HashMap<>();
        mHashMap.put("act", "师生活动");
        mHashMap.put("time", "时间");
        mHashMap.put("leng", "时长");
        mArrayList.add(mHashMap);

        mHashMap = new HashMap<>();
        mHashMap.put("act", "起床");
        mHashMap.put("time", "6:00-7:00");
        mHashMap.put("leng", "1hour");
        mArrayList.add(mHashMap);
        return mArrayList;
    }

    private void urlworkrest() {
        String url = interfaceTest.getServerurl() + interfaceTest.getSchedulequery();
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
                        Log.e("urlworkrest的result", "请求数据:" + str);
                        WorkRestBean accountListBean = new Gson().fromJson(str, WorkRestBean.class);
                        data = getData2(accountListBean);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mWorkRestAdapter = new WorkRestAdapter(WorkRestActivity.this,
                                        mArrayList);
                                mListView.setAdapter(mWorkRestAdapter);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private List<? extends Map<String, ?>> getData2(WorkRestBean accountListBean) {
                mArrayList = new ArrayList<>();
                mHashMap = new HashMap<>();
                mHashMap.put("act", "师生活动");
                mHashMap.put("time", "时间");
                mHashMap.put("leng", "时长");
                mArrayList.add(mHashMap);

                for (int j = 0; j < accountListBean.getData().size(); j++) {
                    mHashMap = new HashMap<>();
                    mHashMap.put("act", accountListBean.getData().get(j).getName());
                    mHashMap.put("time", accountListBean.getData().get(j).getStartstr() + "-" +
                            accountListBean.getData().get(j).getEndstr());
                    mHashMap.put("leng",0 );
                    mArrayList.add(mHashMap);
                }
                return mArrayList;
            }
        }).start();
    }

}
