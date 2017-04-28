package com.logan.acthome.studentteacher;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.acthome.more.LeaveRecordActivity;
import com.logan.acthome.parentleader.MeetingManage;
import com.logan.actme.OptionActivity;
import com.logan.bean.MeetingManagerBean;
import com.logan.constant.InterfaceTest;
import com.util.TitleBar;

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
import static cn.finalteam.toolsfinal.DateUtils.getTime;

@ContentView(R.layout.home_leave)
public class LeaveActivity extends Activity {
    private TextView time_difference;
    private Intent mIntent;
    // spinner
    @ViewInject(R.id.leave_type)
    private Spinner mSpinner;
    // 开始、结束
    private Calendar ca = Calendar.getInstance();
    private int month = ca.get(Calendar.MONTH), day = ca.get(Calendar.DAY_OF_MONTH), hour = ca
            .get(Calendar.HOUR_OF_DAY), hour_minute = ca.get(Calendar.MINUTE);
    private int month2 = ca.get(Calendar.MONTH), day2 = ca.get(Calendar.DAY_OF_MONTH), hour2 =
            (ca.get(Calendar.HOUR_OF_DAY) + 2), hour_minute2 = ca.get(Calendar.MINUTE);
    private String begin_time, end_time;

    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;

    /*@ViewInject(R.id.datepicker)
    private DatePicker mDatepicker;*/
    @ViewInject(R.id.tv_begindate)
    private TextView tv_begindate;
    private int i;
    @ViewInject(R.id.tv_enddate)
    private TextView tv_enddate;

    @ViewInject(R.id.tv_begintime)
    private TextView tv_begintime;
    @ViewInject(R.id.tv_endtime)
    private TextView tv_endtime;
    private int j;
    @ViewInject(R.id.timepicker)
    private TimePickerView mTimePicker;
    @ViewInject(R.id.leave_difference)
    private TextView leave_difference;

    @ViewInject(R.id.btn_send)
    private Button btn_send;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);

        initView();

        spinnerListener();
        //beginPickerListener();
        //endPickerListener();
    }

    private void time_calculate() {
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();

        if (i == 1 || i == 2) {
            mTimePicker = new TimePickerView.Builder(this, new TimePickerView
                    .OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {//选中事件回调
                    // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                    if (i == 1) tv_begindate.setText(getDate(date));
                    else if (i == 2) tv_enddate.setText(getDate(date));
                }
            }).setType(TimePickerView.Type.YEAR_MONTH_DAY).setLabel("", "", "", "", "", "")
                    //设置空字符串以隐藏单位提示   hide label
                    .setDividerColor(Color.BLUE).setContentSize(20).setDate(selectedDate).build();
        } else if (i == 3 || i == 4) {
            mTimePicker = new TimePickerView.Builder(this, new TimePickerView
                    .OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {//选中事件回调
                    // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                    if (i == 3) tv_begintime.setText(getTime(date));
                    else if (i == 4) tv_endtime.setText(getTime(date));
                }
            }).setType(TimePickerView.Type.HOURS_MINS).setLabel("", "", "", "", "", "")
                    //设置空字符串以隐藏单位提示   hide label
                    .setDividerColor(Color.BLUE).setContentSize(20).setDate(selectedDate).build();
        }
    }

    @Event(value = R.id.tv_begindate)
    private void onleave_textbegindate_Click(View v) {
        i = 1;
        time_calculate();
        mTimePicker.show();
    }

    @Event(value = R.id.tv_enddate)
    private void onleave_textenddate_Click(View v) {
        i = 2;
        time_calculate();
        mTimePicker.show();
    }

    @Event(value = R.id.tv_begintime)
    private void onleave_textbegintime_Click(View v) {
        i = 3;
        time_calculate();
        mTimePicker.show();
    }

    @Event(value = R.id.tv_endtime)
    private void onleave_textendtime_Click(View v) {
        i = 4;
        time_calculate();
        mTimePicker.show();
    }

   /*
    protected void timedifference() {
        if (month == month2)
            time_difference.setText("请假时长：" + (day2 - day) + "天" + (hour2 - hour) + "小时");
    }*/

    private void spinnerListener() {
        // 建立数据源
        String[] mItems = getResources().getStringArray(R.array.leave_type);
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_bluebord_icon,
                mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 绑定 Adapter到控件
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] leave_type = getResources().getStringArray(R.array.leave_type);
                Toast.makeText(LeaveActivity.this, "你点击的是" + leave_type[position], Toast
                        .LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initView() {
        titlebar.setTitle("我的请假");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titlebar.setActionTextColor(Color.WHITE);
        titlebar.addAction(new TitleBar.TextAction("请假记录") {
            @Override
            public void performAction(View view) {
                mIntent = new Intent();
                mIntent.setClass(LeaveActivity.this, LeaveRecordActivity.class);
                mIntent.putExtra("submit_time", "申请时间");
                mIntent.putExtra("begin_time", begin_time);
                mIntent.putExtra("end_time", end_time);
                startActivity(mIntent);
            }
        });

        /*mTimePicker = (TimePicker) findViewById(R.id.leave_begintime);
        mTimePicker2 = (TimePicker) findViewById(R.id.leave_endtime);
        time_difference = (TextView) findViewById(R.id.leave_difference);

        tv_begindate.setText("开始日期：" + (ca.get(Calendar.MONTH) + 1) + "-" + ca.get
                (Calendar.DAY_OF_MONTH) + "  " + ca.get(Calendar.HOUR_OF_DAY) + ":" + ca.get
                (Calendar.MINUTE));
        leave_textenddate.setText("结束日期：" + (ca.get(Calendar.MONTH) + 1) + "-" + ca.get(Calendar
                .DAY_OF_MONTH) + "  " + (ca.get(Calendar.HOUR_OF_DAY) + 2) + ":" + ca.get
                (Calendar.MINUTE));
        time_difference.setText("请假时长：0天2小时");
        // 隐藏年份，分钟
        *//*((ViewGroup) ((ViewGroup) mDatePicker.getChildAt(0)).getChildAt(0)).getChildAt(0)
         .setVisibility(View.GONE);
        ((ViewGroup) ((ViewGroup) mDatePicker2.getChildAt(0)).getChildAt(0)).getChildAt(0)
                .setVisibility(View.GONE);*//*
        ((ViewGroup) mTimePicker.getChildAt(0)).getChildAt(0).setVisibility(View.GONE);
        ((ViewGroup) mTimePicker2.getChildAt(0)).getChildAt(0).setVisibility(View.GONE);*/
    }

    @Event(value = R.id.btn_send)
    private void onleavebtn_sendClick(View v) {
        InterfaceTest interfaceTest = new InterfaceTest();
        String url = interfaceTest.getServerurl() + interfaceTest.getStudentleave();
        String token = interfaceTest.getToken();
        String studentId = interfaceTest.getStudentId();


        final OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder().add("token", token).add("proposer.id",
                studentId).build();
        final Request request = new Request.Builder().url(url).post(formBody).build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        Log.e("OptionsAcitivity的登出数据", "数据=" + str);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LeaveActivity.this, "发送成功", Toast.LENGTH_SHORT)
                                        .show();
                                mIntent = new Intent(LeaveActivity.this, LeaveRecordActivity.class);
                                mIntent.putExtra("submit_time", "申请时间");
                                mIntent.putExtra("begin_time", begin_time);
                                mIntent.putExtra("end_time", end_time);
                                startActivity(mIntent);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
