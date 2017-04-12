package com.logan.acthome.more;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.mobilecampus.R;
import com.util.TitleBar;

@ContentView(R.layout.home_leaverecord)
public class LeaveRecordActivity extends Activity {
	// 列表
	private SimpleAdapter mAdapter;
	@ViewInject(R.id.leaverecord_list)
	private ListView mListView;
	private List<HashMap<String, Object>> mHashmap;
	private HashMap<String, Object> mMap;
	private String benginend_time;
	
	@ViewInject(R.id.title_bar)
	private TitleBar titlebar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		initView();
		receiveTime();

		mAdapter = new SimpleAdapter(this, getData(),
				R.layout.home_leaverecord_list, new String[] { "iv", "name",
						"submit", "time", "or" }, new int[] { R.id.iv,
						R.id.name, R.id.submit, R.id.time, R.id.or });
		mListView.setAdapter(mAdapter);
	}

	private void receiveTime() {
		Intent mIntent = getIntent();
		benginend_time = mIntent.getStringExtra("begin_time") + "——"
				+ mIntent.getStringExtra("end_time");
	}

	private List<? extends Map<String, ?>> getData() {
		mHashmap = new ArrayList<HashMap<String, Object>>();
		mMap = new HashMap<String, Object>();
		mMap.put("iv", R.drawable.touxiang);
		mMap.put("name", "刘波");
		mMap.put("submit", "申请时间：");
		mMap.put("time", benginend_time);
		mMap.put("or", "待审核");
		mHashmap.add(mMap);
		return mHashmap;
	}

	private void initView() {
		titlebar.setTitle("请假记录");
		titlebar.setLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
