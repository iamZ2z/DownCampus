package com.logan.actnews;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.example.mobilecampus.R;
import com.logan.actmobilecampus.MainActivity;
import com.util.title.TitleBar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;

@ContentView(R.layout.news_newsdetails)
public class NewsDetailsActivity extends Activity {
	// 列表
	private SimpleAdapter mAdapter;
	@ViewInject(R.id.list)
	private ListView mListView;
	private List<HashMap<String, Object>> mHashmap;
	private HashMap<String, Object> mMap;
	@ViewInject(R.id.title_bar)
	private TitleBar titlebar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		x.view().inject(this);
		initView();
		mAdapter = new SimpleAdapter(this, getData(),
				R.layout.item_newsdetails, new String[] { "title", "time",
						"upload", "content" }, new int[] { R.id.title,
						R.id.leavetime, R.id.upload, R.id.content });
		mListView.setAdapter(mAdapter);
	}

	private void initView() {
		titlebar.setTitle("小秘书");
		titlebar.setLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent();
				mIntent.putExtra("cam", 3);
				mIntent.setClass(NewsDetailsActivity.this, MainActivity.class);
				startActivity(mIntent);
				finish();
			}
		});
	}

	private List<HashMap<String, Object>> getData() {
		mHashmap = new ArrayList<HashMap<String, Object>>();
		mMap = new HashMap<String, Object>();
		mMap.put("title", "1.1新版管理功能大升级");
		mMap.put("time", "12月7日 16:00");
		mMap.put("upload", R.drawable.upload);
		mMap.put("content",
				"增加话单查询功能，话费支出一目了然；还可以设置员工每月拨打额度，限制拨打范围，费用控制一步到位，企业不乱花钱，老板放心充值，员工方便使用");
		mHashmap.add(mMap);
		return mHashmap;
	}
}
