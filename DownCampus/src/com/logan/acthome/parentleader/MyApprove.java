package com.logan.acthome.parentleader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xutils.view.annotation.Event;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.mobilecampus.R;
import com.util.TitleBar;

@ContentView(R.layout.home_myapprove)
public class MyApprove extends Activity {
	@ViewInject(R.id.title_bar)
	private TitleBar titlebar;

	@ViewInject(R.id.list)
	private ListView list;
	private SimpleAdapter mAdapter;
	private List<HashMap<String, Object>> mHashmap;
	private HashMap<String, Object> mMap;

	private Intent mIntent;

	@ViewInject(R.id.layout_type)
	private LinearLayout layout_type;
	@ViewInject(R.id.list_type)
	private ListView list_type;
	private String str_type[]=new String[]{"事假","病假","婚假"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		x.view().inject(this);
		titlebar.setTitle("我的审批");
		titlebar.setLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		ImageView mCollectView=(ImageView)titlebar.addAction(new TitleBar.ImageAction(R.mipmap.nav_btn_search) {
			@Override
			public void performAction(View view) {
				mIntent = new Intent(MyApprove.this,ApproveSearch.class);
				startActivity(mIntent);
			}
		});
		list_Listener();
	}

	private void list_Listener() {
		mAdapter = new SimpleAdapter(this, getData(),
				R.layout.home_approve_list, new String[] { "head", "name",
						"type", "wait", "time" }, new int[] { R.id.head,
						R.id.name, R.id.type, R.id.wait, R.id.time });
		list.setAdapter(mAdapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent mIntent=new Intent(MyApprove.this,ApproveDetail.class);
				startActivity(mIntent);
			}
		});
	}

	private List<? extends Map<String, ?>> getData() {
		mHashmap = new ArrayList<HashMap<String, Object>>();
		mMap = new HashMap<String, Object>();
		mMap.put("head", R.drawable.touxiang);
		mMap.put("name", "姓名");
		mMap.put("type", "审批类型");
		mMap.put("wait", "待审批");
		mMap.put("time", "2017-03-15");
		mHashmap.add(mMap);
		return mHashmap;
	}

	@Event(value=R.id.layout_type)
	private void onlayoutClick(View v){
		ArrayAdapter<String> arrayAdapter= new ArrayAdapter<>(this,R.layout.spinner_approve, str_type);
		list_type.setAdapter(arrayAdapter);
		list_type.setVisibility(View.VISIBLE);
		list_type.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(MyApprove.this, str_type[position], Toast.LENGTH_SHORT).show();
				list_type.setVisibility(View.GONE);
			}
		});
	}
}
