package com.logan.acthome.studentteacher;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.TimePickerView;
import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.bean.HomeworkBean;
import com.logan.bean.HomeworkSubjectBean;
import com.logan.net.InterfaceTest;
import com.util.title.TitleBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static cn.finalteam.toolsfinal.DateUtils.getDate;

@ContentView(R.layout.home_homework)
public class HomeworkActivity extends Activity {
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;
    @ViewInject(R.id.sp_subject)
    private Spinner sp_subject;
    String[] str_subject = {"全部科目"};
    @ViewInject(R.id.timepicker)
    private TimePickerView mTimePicker;
    @ViewInject(R.id.tv_date_begin)
    private TextView tv_date_begin;

    @ViewInject(R.id.tv_date_end)
    private TextView tv_date_end;
    private int i;
    @ViewInject(R.id.list)
    private ListView mListView;
    private SimpleAdapter mAdapter;
    private List<HashMap<String, Object>> mHashmap;
    private HashMap<String, Object> mMap;
    private List<? extends Map<String, ?>> data;

    @ViewInject(R.id.swiperefresh)
    private SwipeRefreshLayout swiperefresh;
    private String startTime="2017-01-01";
    private String endTime="2017-12-30";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        titlebar.setTitle("我的作业");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        urlcheck(startTime,endTime);
        urlsubject();
        swipe();
    }

    @Event(value = R.id.tv_date_begin)
    private void onTv_beginClick(View v) {
        i = 1;
        chooseDate();
        mTimePicker.show();
    }

    @Event(value = R.id.tv_date_end)
    private void onTv_endClick(View v) {
        i = 2;
        chooseDate();
        mTimePicker.show();
    }

    private void chooseDate() {
        Calendar selectedDate = Calendar.getInstance();
        mTimePicker = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                if (i == 1) {
                    String strdate = getDate(date);
                    tv_date_begin.setText(strdate.replaceAll("/", "-"));
                    startTime=tv_date_begin.getText().toString();
                    urlcheck(startTime,endTime);
                } else if (i == 2) {
                    String strdate = getDate(date);
                    tv_date_end.setText(strdate.replaceAll("/", "-"));
                    endTime=tv_date_end.getText().toString();
                    urlcheck(startTime,endTime);
                }
            }
        }).setType(TimePickerView.Type.YEAR_MONTH_DAY).setLabel("年", "月", "日", "", "",
                "")
                //设置空字符串以隐藏单位提示   hide label
                .setDividerColor(Color.BLUE).setContentSize(20).setDate(selectedDate).build();
    }

    private void urlcheck(String startTime,String endTime) {
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .content(R.string.loading)
                .progress(true, 0)
                .show();
        InterfaceTest interfaceTest = new InterfaceTest();
        String url = interfaceTest.getServerurl() + interfaceTest.getStudenthomework();
        String token = interfaceTest.getToken();
        String studentId = interfaceTest.getStudentId();

        final OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder().add("token", token).add("studentId",
                studentId).add("startTime",startTime).add("endTime",endTime).build();
        final Request request = new Request.Builder().url(url).post(formBody).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        Log.e("url作业的result", "请求数据:" + str);
                        Gson gson = new Gson();
                        HomeworkBean accountListBean = gson.fromJson(str, HomeworkBean.class);
                        data = getData2(accountListBean);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter = new SimpleAdapter(HomeworkActivity.this, data, R
                                        .layout.home_homework_list, new String[]{"subject",
                                        "author", "content", "leavetime"}, new int[]{R.id.subject,
                                        R.id.author, R.id.content, R.id.leavetime});
                                mListView.setAdapter(mAdapter);

                                dialog.dismiss();
                                mAdapter.notifyDataSetChanged();
                                swiperefresh.setRefreshing(false);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private List<? extends Map<String, ?>> getData2(HomeworkBean accountListBean) {
                mHashmap = new ArrayList<>();
                for (int j = 0; j < accountListBean.getData().size(); j++) {
                    mMap = new HashMap<>();
                    mMap.put("subject", "科目:" + accountListBean.getData().get(j).getSubjectName());
                    mMap.put("author", "发布人:" + accountListBean.getData().get(j).getAssigner());
                    mMap.put("content", accountListBean.getData().get(j).getContent());
                    mMap.put("leavetime", "布置日期:" + accountListBean.getData().get(j)
                            .getAssignTime());
                    mHashmap.add(mMap);
                }
                return mHashmap;
            }
        }).start();
    }

    private void urlsubject() {
        InterfaceTest interfaceTest = new InterfaceTest();
        String url = interfaceTest.getServerurl() + interfaceTest.getStudentsubject();
        String token = interfaceTest.getToken();
        String studentId = interfaceTest.getStudentId();

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
                        Log.e("url作业的result", "请求数据:" + str);
                        final HomeworkSubjectBean accountListBean = new Gson().fromJson(str,
                                HomeworkSubjectBean.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sp_subject(accountListBean);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private void sp_subject(HomeworkSubjectBean accountBean) {
                String[] subject = new String[accountBean.getData().size()];
                for (int j = 0; j < accountBean.getData().size(); j++) {
                    subject[j] = accountBean.getData().get(j).getSubjectName();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(HomeworkActivity.this, R.layout
                        .spinner_bluebord, subject);
                adapter.setDropDownViewResource(R.layout.spinnerdropdownitem);
                // 绑定 Adapter到控件
                sp_subject.setAdapter(adapter);
                sp_subject.setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position,
                                               long id) {
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        }).start();
    }

    private void swipe() {
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                urlcheck(startTime,endTime);

            }
        });
    }


}
