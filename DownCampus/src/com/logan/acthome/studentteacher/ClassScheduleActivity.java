package com.logan.acthome.studentteacher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.net.InterfaceTest;
import com.logan.adapter.ClassScheduleAdapter;
import com.logan.net.UsuallyData;
import com.logan.server.ClassScheduleListBean;
import com.util.title.TitleBar;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ContentView(R.layout.home_classschedule)
public class ClassScheduleActivity extends Activity implements OnClickListener {
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
    private String[][] data;
    private UsuallyData usuallyData = new UsuallyData();
    @ViewInject(R.id.nulldata)
    private ImageView nulldata;

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

    }

    private void dourlClassSchedule(String semester_id, String semester_yearId, String gradeid,
                                    String clazzid) {
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .content(R.string.loading)
                .progress(true, 0)
                .show();
        InterfaceTest interfaceTest = new InterfaceTest();
        String url = interfaceTest.getServerurl() + interfaceTest.getQueryschedule();
        String token = interfaceTest.getToken();

        //写死Params
        FormBody formBody = new FormBody.Builder().add("token", token).add("year",
                semester_yearId).add("semester", semester_id).add("grade", gradeid).add("clazz",
                clazzid).build();
        final Request request = new Request.Builder().url(url).post(formBody).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = new OkHttpClient().newCall(request).execute();
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        Log.e("课表result", "请求数据:" + str);
                        if(!str.equals("{\"code\":\"1\",\"message\":\"查询数据为空!\",\"data\":0}")){
                        final ClassScheduleListBean bean = new Gson().fromJson(str,
                                ClassScheduleListBean.class);
                            data = new String[bean.getData().size()][8];
                            for (int i = 0; i < bean.getData().size(); i++) {
                                for (int j = 0; j < bean.getData().get(i).size(); j++) {
                                    data[i][j] = bean.getData().get(i).get(j).getSubject();
                                }
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                    getData(bean);
                                    mClassScheduleAdapter.notifyDataSetChanged();
                                }
                            });
                        }else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                    nulldata.setVisibility(View.VISIBLE);
                                }
                            });
                        }
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
        //list.add("一年一班");
        ArrayList<String> arrayList = usuallyData.getClazzname();
        for (int i = 0; i < arrayList.size(); i++) {
            list.add(arrayList.get(i));
        }
        adapter = new ArrayAdapter<>(this, R.layout.spinner_workrest, list);
        adapter.setDropDownViewResource(R.layout.spinnerdropdownitem);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String gradeid = usuallyData.getGradeid().get(position);
                String clazzid = usuallyData.getClazzid().get(position);
                String semester_id = usuallyData.getSemesterid().get(0);
                String semester_yearId = usuallyData.getSemesteryearid().get(0);
                dourlClassSchedule(semester_id, semester_yearId, gradeid, clazzid);
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

        for (int j = 0; j < bean.getData().size(); j++) {
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
