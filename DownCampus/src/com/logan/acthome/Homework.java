package com.logan.acthome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xutils.view.annotation.Event;
import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilecampus.R;
import com.util.TitleBar;

import cn.aigestudio.datepicker.cons.DPMode;
import cn.aigestudio.datepicker.views.DatePicker;

@ContentView(R.layout.home_homework)
public class Homework extends Activity {
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;

    @ViewInject(R.id.sp_subject)
    private Spinner sp_subject;
    String[] str_subject = {"科目", "语文", "数学", "英语"};

    @ViewInject(R.id.date_begin)
    private DatePicker date_begin;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        initView();
        spinner_subject();
        datebegin();
    }

    @Event(value = R.id.tv_date_begin)
    private void onTv_beginClick(View v) {
        i = 1;
        date_begin.setVisibility(View.VISIBLE);
    }

    @Event(value = R.id.tv_date_end)
    private void onTv_endClick(View v) {
        i = 2;
        date_begin.setVisibility(View.VISIBLE);
    }

    private void datebegin() {
        Toast.makeText(Homework.this, "左右滑动月份，上下滑动年份", Toast.LENGTH_SHORT).show();
        date_begin.setDate(2017, 3);
        date_begin.setMode(DPMode.SINGLE);
        date_begin.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
            @Override
            public void onDatePicked(String date) {
                if (i == 1) tv_date_begin.setText(date);
                else tv_date_end.setText(date);
                date_begin.setVisibility(View.GONE);
            }
        });
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
                String[]{"title", "time", "content", "subject", "author"}, new int[]{R.id.title,
                R.id.time, R.id.content, R.id.subject, R.id.author});
        list.setAdapter(mAdapter);
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
        mMap.put("time", "2017-02-01 16:00");
        mHashmap.add(mMap);
        return mHashmap;
    }

}
