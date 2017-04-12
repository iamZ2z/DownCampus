package com.logan.acthome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.mobilecampus.R;
import com.logan.acthome.more.WriteLog;
import com.util.TitleBar;

@ContentView(R.layout.home_logmanage)
public class LogManage extends Activity {
	// 列表
	private SimpleAdapter mAdapter;
	
	@ViewInject(R.id.list)
	private ListView mListView;
	
	private List<HashMap<String, Object>> mHashmap;
	private HashMap<String, Object> mMap;
	
	@ViewInject(R.id.title_bar)
	private TitleBar titlebar;
	private ImageView mCollectView;
	
	private Intent mIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		x.view().inject(this);
		initView();
		mAdapter = new SimpleAdapter(this, getData(), R.layout.home_log_item,
				new String[] { "img", "report", "type","readornot","time" }, new int[] {
						R.id.img, R.id.report, R.id.type,R.id.readornot,R.id.time });
		mListView.setAdapter(mAdapter);
	}

	private void initView() {
		titlebar.setTitle("日志管理");
		titlebar.setLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mCollectView=(ImageView)titlebar.addAction(new TitleBar.ImageAction(R.mipmap.nav_btn_add) {
			@Override
			public void performAction(View view) {
				mIntent = new Intent(LogManage.this,WriteLog.class);
				startActivity(mIntent);
			}
		});
	}

	private List<HashMap<String, Object>> getData() {
		mHashmap = new ArrayList<>();
		mMap = new HashMap<>();
		mMap.put("img", R.drawable.touxiang);
		mMap.put("report", "汇报人：刘波");
		mMap.put("type", "类型：日报");
		mMap.put("readornot", "未阅");
		mMap.put("time", "2017-02-17");
		mHashmap.add(mMap);
		return mHashmap;
	}

}
