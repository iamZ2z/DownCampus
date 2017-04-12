package com.logan.actdynamic;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.mobilecampus.R;
import com.util.TitleBar;

@ContentView(R.layout.find_campusact)
public class CampusActActivity extends Activity implements OnItemClickListener {
	private Intent mIntent;

	// 列表
	@ViewInject(R.id.list)
	private ListView mListView;
	private SimpleAdapter mAdapter;
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

		mAdapter = new SimpleAdapter(this, getData(), R.layout.item_campusact,
				new String[] { "img", "title", "type", "place", "data" },
				new int[] { R.id.img, R.id.title, R.id.type, R.id.place,
						R.id.data });
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}

	private void initView() {
		titlebar.setTitle("校园活动");
		titlebar.setLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private List<HashMap<String, Object>> getData() {
		mHashmap = new ArrayList<HashMap<String, Object>>();
		mMap = new HashMap<String, Object>();
		mMap.put("img", R.drawable.upload);
		mMap.put("title", "伶仃希望小学帝第七届校运会");
		mMap.put("type", "活动类型：学校运动场");
		mMap.put("place", "体育运动场");
		mMap.put("data", "2017-03-01 12:00");
		mHashmap.add(mMap);
		
		mMap = new HashMap<String, Object>();
		mMap.put("img", R.drawable.upload);
		mMap.put("title", "伶仃希望小学帝第七届校运会");
		mMap.put("type", "活动类型：学校运动场");
		mMap.put("place", "体育运动场");
		mMap.put("data", "2017-03-01 12:00");
		mHashmap.add(mMap);
		return mHashmap;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// String titles=mHashmap.get(position).get("title");
		/*
		 * Map<String, Object> map = (Map<String, Object>)
		 * CampusActActivity.this.mAdapter .getItem(position);
		 */
		mIntent = new Intent(this, CampusActDetailActivity.class);
		startActivity(mIntent);
	}
}
