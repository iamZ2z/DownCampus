package com.logan.acthome.parentleader;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.constant.InterfaceTest;
import com.logan.bean.MeetingManagerBean;
import com.logan.widgets.MultiSpinner;
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

@ContentView(R.layout.home_meetingmanage)
public class MeetingManage extends Activity {
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
    private ListView list;
    private SimpleAdapter mAdapter;
    private List<HashMap<String, Object>> mHashmap;
    private HashMap<String, Object> mMap;
    private Intent mIntent;
    @ViewInject(R.id.loadingimg)
    private ImageView loadingimg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        initView();
        spinner_subject();

        urlmeeting();
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
                    String strdate=getDate(date);
                    tv_date_begin.setText(strdate.replaceAll("/","-"));
                } else if (i == 2) {
                    String strdate=getDate(date);
                    tv_date_end.setText(strdate.replaceAll("/","-"));
                }
            }
        }).setType(TimePickerView.Type.YEAR_MONTH_DAY).setLabel("年", "月", "日", "", "",
                "")
                //设置空字符串以隐藏单位提示   hide label
                .setDividerColor(Color.BLUE).setContentSize(20).setDate(selectedDate).build();

    }

    private void initView() {
        titlebar.setTitle("会议管理");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titlebar.setActionTextColor(Color.WHITE);
        titlebar.addAction(new TitleBar.TextAction("发布会议") {
            @Override
            public void performAction(View view) {
                mIntent = new Intent();
                mIntent.setClass(MeetingManage.this, SendMeetingActivity.class);
                startActivity(mIntent);
            }
        });
        mAdapter = new SimpleAdapter(this, getData(), R.layout.home_meetingmanage_list, new
                String[]{"title", "content", "time"}, new int[]{R.id.title,
                R.id.content, R.id.leavetime});
        list.setAdapter(mAdapter);
    }

    private void spinner_subject() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.sp_bluebordgrayword, str_subject);
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
        mMap.put("title", "学习习近平讲话精神");
        mMap.put("content", "今天请各位回家预习《论语》，明天老师进行抽查。今天请各位回家预习《论语》，明天老师进行抽查。");
        mMap.put("time", "2017-02-01 16:00");
        mHashmap.add(mMap);
        return mHashmap;
    }

    private void urlmeeting() {
        InterfaceTest interfaceTest=new InterfaceTest();
        String url = interfaceTest.getServerurl() + interfaceTest.getMeetingquery();
        String token = interfaceTest.getToken();
        FormBody formBody = new FormBody.Builder().add("token", token).build();
        final Request request = new Request.Builder().url(url).post(formBody).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = new OkHttpClient().newCall(request).execute();
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        Log.e("urlmeeting的result", "请求数据:" + str);
                        final MeetingManagerBean accountListBean = new Gson().fromJson(str,
                                MeetingManagerBean.class);
                        for (int i = 0; i < accountListBean.getList().size(); i++) {
                            Log.e("title:", accountListBean.getList().get(i).getName());
                            Log.e("content:", accountListBean.getList().get(i).getContent());
                            Log.e("time:", accountListBean.getList().get(i).getCreateTime());
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadingimg.setVisibility(View.GONE);
                                mAdapter = new SimpleAdapter(MeetingManage.this, getData2
                                        (accountListBean), R.layout.home_meetingmanage_list, new
                                        String[]{"title", "content", "time"}, new int[]{R.id.title,
                                        R.id.content, R.id.leavetime});
                                list.setAdapter(mAdapter);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private List<? extends Map<String, ?>> getData2(MeetingManagerBean accountListBean) {
                mHashmap = new ArrayList<>();
                for (int j = 0; j < accountListBean.getList().size(); j++) {
                    mMap = new HashMap<>();
                    mMap.put("title", accountListBean.getList().get(j).getName());
                    mMap.put("content", accountListBean.getList().get(j).getContent());
                    mMap.put("time", accountListBean.getList().get(j).getCreateTime());
                    mHashmap.add(mMap);
                }
                return mHashmap;
            }
        }).start();
    }

}
