package com.logan.acthome.studentteacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.mobilecampus.R;
import com.logan.bean.ExamArrangeBean;
import com.util.title.TitleBar;

@ContentView(R.layout.home_examarrange_detail)
public class ExamArrangeDetail extends Activity {
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;
    @ViewInject(R.id.list)
    private ListView mListView;
    private SimpleAdapter mAdapter;
    private List<HashMap<String, Object>> mHashmap;
    private HashMap<String, Object> mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        titlebar.setTitle("考试安排详情");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAdapter = new SimpleAdapter(this, doloadData(), R.layout.home_examarrange_detail_list, new
                String[]{"subject", "data", "time", "timelong"}, new int[]{R.id.subject, R.id
                .data, R.id.leavetime, R.id.timelong});
        mListView.setAdapter(mAdapter);
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


    }

    /*private List<HashMap<String, Object>> getData() {
        mHashmap = new ArrayList<>();
        mMap = new HashMap<>();
        mMap.put("subject", "语文");
        mMap.put("data", "2017-02-21");
        mMap.put("time", "9:00-11:00");
        mMap.put("timelong", "120m");
        mHashmap.add(mMap);
        return mHashmap;
    }*/

    private List<HashMap<String, Object>> doloadData() {
        ExamArrangeBean bean=(ExamArrangeBean)getIntent().getSerializableExtra("examarrange");
        mHashmap = new ArrayList<>();
        for (int i = 0; i <bean.getData().size() ; i++) {
            mMap = new HashMap<>();
            mMap.put("subject",bean.getData().get(i).getExam_Sub());
            mMap.put("data", bean.getData().get(i).getExam_time());
            mMap.put("time", bean.getData().get(i).getExam_Stime()+"-"+bean.getData().get(i).getExam_Etime());
            mMap.put("timelong", bean.getData().get(i).getExam_min()+"m");
            mHashmap.add(mMap);
        }
        return mHashmap;
    }
}
