package com.logan.acthome.studentteacher;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
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

import com.bigkoo.pickerview.TimePickerView;
import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.bean.HomeworkBean;
import com.logan.bean.HomeworkSubjectBean;
import com.logan.constant.InterfaceTest;
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
    String[] str_subject = {"科目", "语文", "数学", "英语"};
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        initView();
        spinner_subject();

        urlcheck();
        urlsubject();
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
                } else if (i == 2) {
                    String strdate = getDate(date);
                    tv_date_end.setText(strdate.replaceAll("/", "-"));
                }
            }
        }).setType(TimePickerView.Type.YEAR_MONTH_DAY).setLabel("年", "月", "日", "", "",
                "")
                //设置空字符串以隐藏单位提示   hide label
                .setDividerColor(Color.BLUE).setContentSize(20).setDate(selectedDate).build();
    }

    private void initView() {
        titlebar.setTitle("我的作业");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAdapter = new SimpleAdapter(this, getData(), R.layout.home_homework_list, new
                String[]{"subject", "author", "content", "leavetime"}, new int[]{R.id.subject,
                R.id.author, R.id.content, R.id.leavetime});
        mListView.setAdapter(mAdapter);
    }

    private void spinner_subject() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout
                .spinner_bluebord, str_subject);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 绑定 Adapter到控件
        sp_subject.setAdapter(adapter);
        sp_subject.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*
                 * Toast.makeText(LeaveActivity.this,"你点击的是" +
				 * leave_type[position], Toast.LENGTH_SHORT).show();
				 */
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private List<? extends Map<String, ?>> getData() {
        mHashmap = new ArrayList<>();
        mMap = new HashMap<>();
        mMap.put("subject", "科目：语文");
        mMap.put("author", "刘波");
        mMap.put("content", "今天请各位回家预习《论语》，明天老师进行抽查。今天请各位回家预习《论语》，明天老师进行抽查。");
        mMap.put("leavetime", "2017-02-01 16:00");
        mHashmap.add(mMap);
        return mHashmap;
    }

    private void urlcheck() {
        InterfaceTest interfaceTest = new InterfaceTest();
        String url = interfaceTest.getServerurl() + interfaceTest.getStudenthomework();
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
                        Log.e("url作业的result", "请求数据:" + str);
                        Gson gson = new Gson();
                        HomeworkBean accountListBean = gson.fromJson(str, HomeworkBean.class);
                        data = getData2(accountListBean);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter = new SimpleAdapter(HomeworkActivity.this, data, R
                                        .layout.home_homework_list, new
                                        String[]{"subject", "author", "content", "leavetime"},
                                        new int[]{R.id.subject,
                                                R.id.author, R.id.content, R.id.leavetime});
                                mListView.setAdapter(mAdapter);
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
                    mMap.put("subject", accountListBean.getData().get(j).getSubjectName());
                    mMap.put("author", accountListBean.getData().get(j).getAssigner());
                    mMap.put("content", accountListBean.getData().get(j).getContent());
                    mMap.put("leavetime", accountListBean.getData().get(j).getAssignTime());
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
                        Log.e("url作业的result", "请求数据:" + str);
                        Gson gson = new Gson();
                        final HomeworkSubjectBean accountListBean = gson.fromJson(str,
                                HomeworkSubjectBean.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                /*mAdapter = new SimpleAdapter(HomeworkActivity.this, data, R
                                .layout.home_homework_list, new
                                        String[]{"subject",  "author","content","leavetime"}, new
                                         int[]{R.id.subject,
                                        R.id.author, R.id.content, R.id.leavetime});
                                mListView.setAdapter(mAdapter);*/

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
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // 绑定 Adapter到控件
                sp_subject.setAdapter(adapter);
                sp_subject.setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position,
                                               long id) {
                /*
                 * Toast.makeText(LeaveActivity.this,"你点击的是" +
				 * leave_type[position], Toast.LENGTH_SHORT).show();
				 */
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        }).start();
    }
}
