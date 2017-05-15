package com.logan.actme;

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
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.mobilecampus.R;
import com.util.title.TitleBar;

@ContentView(R.layout.me_dynamic)
public class DynamicActivity extends Activity {
	private Intent mIntent;
	// 列表
	private SimpleAdapter mAdapter;
	@ViewInject(R.id.list_dynamic)
	private ListView mListView;
	private List<HashMap<String, Object>> mHashmap;
	private HashMap<String, Object> mMap;
	@ViewInject(R.id.title_bar)
	private TitleBar titlebar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		x.view().inject(this);
		initView();
		mAdapter = new SimpleAdapter(this, getData(), R.layout.item_classdynamic,
				new String[] { "head", "title", "info", "upload","comment" }, new int[] {
						R.id.head, R.id.title, R.id.info, R.id.upload,R.id.comment});
		mListView.setAdapter(mAdapter);
	}

	private void initView() {
		titlebar.setTitle("我的动态");
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
		mMap.put("head", R.drawable.touxiang);
		mMap.put("title", "刘波");
		mMap.put("info", "不要老拿自己跟别人比，要多跟自己比，做好自己就行了。");
		mMap.put("upload", R.drawable.upload);
		mMap.put("comment", "");
		mHashmap.add(mMap);
		return mHashmap;
	}
}
