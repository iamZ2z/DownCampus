package com.logan.acthome.studentteacher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.constant.InterfaceTest;
import com.logan.adapter.ClassScheduleAdapter;
import com.logan.server.ClassScheduleListBean;
import com.util.title.TitleBar;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ContentView(R.layout.home_classschedule)
public class ClassScheduleActivity extends Activity implements OnClickListener {
    // 列表
    @ViewInject(R.id.schedule_list)
    private ListView mListView;
    private ArrayList<ArrayList<String>> mArrayList = new ArrayList<>();
    private ArrayList<String> mArray;
    @ViewInject(R.id.schedule_sp)
    private Spinner mSpinner;
    private List<String> list = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;
    private ClassScheduleAdapter mClassScheduleAdapter;

    private String[] mArrayList_getData = new String[5];
    private ArrayList<String>[] mArraySchedule;
    private String[][] data;

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

        initURLClassSchedule();
    }

    private void initURLClassSchedule() {
        InterfaceTest interfaceTest = new InterfaceTest();
        String url = interfaceTest.getServerurl() + interfaceTest.getQueryschedule();
        String token = interfaceTest.getToken();

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
                    Response response = new OkHttpClient().newCall(request).execute();
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        Log.e("课表result", "请求数据:" + str);
                        final ClassScheduleListBean bean = new Gson().fromJson(str,
                                ClassScheduleListBean.class);
                        mArraySchedule = new ArrayList[bean.getList().size()];
                        data = new String[bean.getList().size()][8];
                        for (int i = 0; i < bean.getList().size(); i++) {
                            for (int j = 0; j < bean.getList().get(i).size(); j++) {
                                data[i][j] = bean.getList().get(i).get(j).getSubject();
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getData(bean);
                                mClassScheduleAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void classScheduleAdapter() {
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

    private List<? extends List<String>> getData(ClassScheduleListBean bean) {
        mArray = new ArrayList<>();
        mArray.add("");
        mArray.add("一");
        mArray.add("二");
        mArray.add("三");
        mArray.add("四");
        mArray.add("五");
        mArray.add("六");
        mArray.add("日");
        mArrayList.add(mArray);

        for (int j = 0; j < bean.getList().size(); j++) {
            mArray = new ArrayList<>();
            mArray.add("第" + (j + 1) + "节");
            for (int i = 0; i < 7; i++) {
                if (data[0][i] != null) mArray.add(data[j][i]);
                else mArray.add("");
            }
            mArrayList.add(mArray);
        }

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
