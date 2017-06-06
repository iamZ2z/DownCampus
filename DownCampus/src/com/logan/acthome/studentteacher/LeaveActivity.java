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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.acthome.more.LeaveRecordActivity;
import com.logan.bean.LeaveBean;
import com.logan.net.InterfaceTest;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.util.title.TitleBars;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

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
    private TitleBars titlebar;
    @ViewInject(R.id.tv_begindate)
    private TextView tv_begindate;
    private int i;
    @ViewInject(R.id.tv_enddate)
    private TextView tv_enddate;
    private int j;
    @ViewInject(R.id.timepicker)
    private TimePickerView mTimePicker;
    @ViewInject(R.id.leave_time)
    private TextView leave_time;
    @ViewInject(R.id.reason)
    private EditText reason;
    @ViewInject(R.id.btn_send)
    private Button btn_send;
    private long time1, time2=0;
    private String leavetype="1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);

        initView();
        spinnerListener();
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
        titlebar.addAction(new TitleBars.TextAction("请假记录") {
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

        //输入法弹出
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.blue_55x176x233);
    }

    private void fillleavetime() {
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        mTimePicker = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                if (i == 1) {
                    String strdate=getDate(date) + " " + getTime(date);
                    tv_begindate.setText(strdate.replaceAll("/","-"));
                    time1 = date.getTime();
                    timedifference();
                } else if (i == 2) {
                    String strdate=getDate(date) + " " + getTime(date);
                    tv_enddate.setText(strdate.replaceAll("/","-"));
                    time2 = date.getTime();
                    timedifference();
                }
            }
        }).setType(TimePickerView.Type.YEAR_MONTH_DAY_HOUR_MIN).setLabel("年", "月", "日", "时", "分",
                "")
                //设置空字符串以隐藏单位提示   hide label
                .setDividerColor(Color.BLUE).setContentSize(20).setDate(selectedDate).build();
    }

    @Event(value = R.id.tv_begindate)
    private void onleave_textbegindate_Click(View v) {
        i = 1;
        fillleavetime();
        mTimePicker.show();
    }

    @Event(value = R.id.tv_enddate)
    private void onleave_textenddate_Click(View v) {
        i = 2;
        fillleavetime();
        mTimePicker.show();
    }

    private void timedifference() {
        if (time1 != 0 && time2 != 0) {
            long diff = time2 - time1;
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) /
                    (1000 * 60);
            leave_time.setText("" + days + "天" + hours + "小时" + minutes + "分");
        }
    }

    private void spinnerListener() {
        // 建立数据源
        String[] mItems = getResources().getStringArray(R.array.leave_type);
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_bluebord_icon,
                mItems);
        adapter.setDropDownViewResource(R.layout.spinnerdropdownitem);
        // 绑定 Adapter到控件
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*String[] leave_type = getResources().getStringArray(R.array.leave_type);
                Toast.makeText(LeaveActivity.this, "你点击的是" + leave_type[position], Toast
                        .LENGTH_SHORT).show();*/
                leavetype=(position+1)+"";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Event(value = R.id.btn_send)
    private void onleavebtn_sendClick(View v) {
        InterfaceTest interfaceTest = new InterfaceTest();
        String url = interfaceTest.getServerurl() + interfaceTest.getStudentleave();
        String token = interfaceTest.getToken();
        String role = interfaceTest.getRole();

        FormBody formBody;
        String content = reason.getText().toString();
        String start = tv_begindate.getText().toString();
        String end = tv_enddate.getText().toString();
        if (role.equals("老师")) {
            url=interfaceTest.getServerurl()+interfaceTest.getLeavesave();
            String name = "name";
            String toid = "leaderid";
            String userid = interfaceTest.getUser_id();
            formBody = new FormBody.Builder().add("token", token).add("name", name).add
                    ("content", content).add("start", start).add("end", end).add("to.id", toid)
                    .add("leavetype", leavetype).add("user.id", userid).build();
        } else {
            String studentId = interfaceTest.getStudentId();
            formBody = new FormBody.Builder().add("token", token).add("proposer.id", studentId)
                    .add("leaveType", leavetype).add("startTime", start).add("endTime", end).add
                            ("leaveLen", leave_time.getText().toString()).add("reason", content)
                    .build();
        }
        final Request request = new Request.Builder().url(url).post(formBody).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = new OkHttpClient().newCall(request).execute();
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        Log.e("LeaveAcitivity的数据", str);
                        final LeaveBean bean = new Gson().fromJson(str, LeaveBean.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (bean.getCode().equals("0")) {
                                    Toast.makeText(LeaveActivity.this, "发送成功", Toast.LENGTH_SHORT)
                                            .show();
                                    mIntent = new Intent(LeaveActivity.this, LeaveRecordActivity
                                            .class);
                                    mIntent.putExtra("submit_time", "申请时间");
                                    mIntent.putExtra("begin_time", begin_time);
                                    mIntent.putExtra("end_time", end_time);
                                    startActivity(mIntent);
                                }
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
