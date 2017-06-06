package com.logan.acthome.studentteacher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xutils.view.annotation.Event;
import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.bigkoo.pickerview.TimePickerView;
import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.adapter.ClassActivityAdapter;
import com.logan.bean.ClassActivityBean;
import com.logan.net.InterfaceTest;
import com.util.title.TitleBar;

import android.app.Activity;
import android.graphics.Color;
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
import android.widget.TextView;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static cn.finalteam.toolsfinal.DateUtils.getDate;

@ContentView(R.layout.activity_classactivity)
public class ClassActivityActivity extends Activity {
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;
    @ViewInject(R.id.sp_subject)
    private Spinner sp_subject;
    String[] str_subject = {"全部"};

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
    private List<? extends Map<String, ?>> data;
    @ViewInject(R.id.swiperefresh)
    private SwipeRefreshLayout swiperefresh;

    private ClassActivityAdapter classActivityAdapter;
    private InterfaceTest interfaceTest = new InterfaceTest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        initView();
        spinner_subject();
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

    private void dourl() {
        String url = interfaceTest.getServerurl() + interfaceTest.getClassactivity();
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
                        Log.e("班级活动的result", "请求数据:" + str);
                        Gson gson = new Gson();
                        final ClassActivityBean bean = gson.fromJson(str, ClassActivityBean.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                data = getData2(bean);
                                classActivityAdapter = new ClassActivityAdapter
                                        (ClassActivityActivity.this, mHashmap);

                                mAdapter = new SimpleAdapter(ClassActivityActivity.this, data, R
                                        .layout.home_classactactivity_list, new
                                        String[]{"leavetime", "content", "subject"}, new int[]{R
                                        .id.leavetime, R.id.content, R.id.subject});
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

            private List<? extends Map<String, ?>> getData2(ClassActivityBean bean) {
                mHashmap = new ArrayList<>();
                for (int j = 0; j < bean.getData().size(); j++) {
                    mMap = new HashMap<>();
                    mMap.put("img", interfaceTest.getImgurl() + bean.getData().get(j).getTitleImg
                            ());
                    switch (bean.getData().get(j).getActivityType()) {
                        case "1":
                            mMap.put("subject", "日常班会:" + bean.getData().get(j)
                                    .getName());
                            break;
                        case "2":
                            mMap.put("subject", "主题班会:" + bean.getData().get(j)
                                    .getName());
                            break;
                        case "3":
                            mMap.put("subject", "文艺活动:" + bean.getData().get(j)
                                    .getName());
                            break;
                        case "4":
                            mMap.put("subject", "体育活动:" + bean.getData().get(j)
                                    .getName());
                            break;
                        case "5":
                            mMap.put("subject", "小组活动:" + bean.getData().get(j)
                                    .getName());
                            break;
                        case "6":
                            mMap.put("subject", "劳动活动:" + bean.getData().get(j)
                                    .getName());
                            break;
                        case "7":
                            mMap.put("subject", "公益活动:" + bean.getData().get(j)
                                    .getName());
                            break;
                        case "8":
                            mMap.put("subject", "节日活动:" + bean.getData().get(j)
                                    .getName());
                            break;
                        case "9":
                            mMap.put("subject", "旅游活动:" + bean.getData().get(j)
                                    .getName());
                            break;
                        default:
                            break;
                    }
                    mMap.put("content", bean.getData().get(j).getSummary());
                    mMap.put("leavetime", bean.getData().get(j).getStartTime() + "-" +
                            bean.getData().get(j).getEndTime());
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
