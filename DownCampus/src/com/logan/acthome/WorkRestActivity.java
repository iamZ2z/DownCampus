package com.logan.acthome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.example.mobilecampus.R;
import com.logan.adapter.WorkRestAdapter;
import com.util.TitleBar;

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
    }

    private void spinner_year() {
        // 添加下拉list
        list.add("2016-2017");
        list.add("2017-2018");
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

    private void spinner_term() {
        // 添加下拉list
        list2.add("春季学期");
        list2.add("冬季学期");
        adapter2 = new ArrayAdapter<String>(this, R.layout.spinner_workrest, list2);
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
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

}
