package com.logan.acthome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.example.mobilecampus.R;
import com.util.TitleBar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

@ContentView(R.layout.activity_classactivity)
public class ClassActivity extends Activity {
	@ViewInject(R.id.title_bar)
	private TitleBar titlebar;
	
	@ViewInject(R.id.sp_subject)
	private Spinner sp_subject;
	String[] str_subject = { "全部", "课内", "课外" };

	@ViewInject(R.id.sp_begindata)
	private Spinner sp_begindata;
	String[] str_begindata = { "2017-02-15", "2017-02-01", "2017-03-01" };

	@ViewInject(R.id.sp_enddata)
	private Spinner sp_enddata;
	String[] str_enddata = { "2017-02-15", "2017-02-01", "2017-03-01" };

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
		spinner_begindata();
		spinner_enddata();
	}

	private void initView() {
		titlebar.setTitle("班级活动");
		titlebar.setLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mAdapter = new SimpleAdapter(this, getData(),
				R.layout.home_homework_list, new String[] { "title", "time",
						"content", "subject", "author" }, new int[] {
						R.id.title, R.id.time, R.id.content, R.id.subject,
						R.id.author });
		list.setAdapter(mAdapter);
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

	private void spinner_begindata() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.spinner_bluebord, str_begindata);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 绑定 Adapter到控件
		sp_begindata.setAdapter(adapter);
		sp_begindata.setOnItemSelectedListener(new OnItemSelectedListener() {
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

	private void spinner_enddata() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.spinner_bluebord, str_enddata);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 绑定 Adapter到控件
		sp_enddata.setAdapter(adapter);
		sp_enddata.setOnItemSelectedListener(new OnItemSelectedListener() {
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

	private List<? extends Map<String, ?>> getData() {
		mHashmap = new ArrayList<HashMap<String, Object>>();
		mMap = new HashMap<String, Object>();
		mMap.put("title", "主题班会");
		mMap.put("time", "2017-02-01 16:00");
		mMap.put("content", "生活就像海洋，只有意志坚强的人才能到达彼岸。");
		mMap.put("subject", "活动类型");
		mMap.put("author", "刘波");
		mHashmap.add(mMap);
		return mHashmap;
	}

}
