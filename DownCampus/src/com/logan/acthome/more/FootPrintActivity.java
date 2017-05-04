package com.logan.acthome.more;

import java.util.ArrayList;
import java.util.Calendar;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.mobilecampus.R;
import com.util.TitleBar;

@ContentView(R.layout.home_footprint)
public class FootPrintActivity extends Activity {
	// 列表
	private SimpleAdapter mAdapter;
	@ViewInject(R.id.footprint_list)
	private ListView mListView;
	private List<HashMap<String, Object>> mHashmap;
	private HashMap<String, Object> mMap;
	@ViewInject(R.id.title_bar)
	private TitleBar titlebar;

	@ViewInject(R.id.footprint_search_left)
	private ImageView footprint_search_left;
	@ViewInject(R.id.footprint_search_center)
	private TextView footprint_search_center;
	@ViewInject(R.id.footprint_search_right)
	private ImageView footprint_search_right;
	private Calendar mCalendar=Calendar.getInstance();
	private String st=mCalendar.get(Calendar.YEAR)+"-"+mCalendar.get(Calendar.MONTH);


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		x.view().inject(this);
		initView();

		mAdapter = new SimpleAdapter(this, getData(),
				R.layout.home_footprint_list, new String[] { "data", "time",
						"sign" }, new int[] { R.id.footprint_listdate,
						R.id.footprint_listtime,
						R.id.footprint_listsign });
		mListView.setAdapter(mAdapter);


		footprint_search_center.setText(st);
	}

	private List<? extends Map<String, ?>> getData() {
		mHashmap = new ArrayList<HashMap<String, Object>>();
		mMap = new HashMap<String, Object>();
		mMap.put("data", "3月30日");
		mMap.put("time", "9:00签到");
		mMap.put("sign", "正常");
		mHashmap.add(mMap);
		return mHashmap;
	}

	@Event(value = R.id.footprint_search_left)
	private void onImg_leftClick(View v){
		String str[]=st.split("-");
		int str_year=Integer.parseInt(str[0]);
		int str_month= Integer.parseInt(str[1])-1;
		if(str_month==0){
			str_year--;
			str_month=12;
		}
		st=str_year+"-"+str_month;
		footprint_search_center.setText(st);
	}

	@Event(value = R.id.footprint_search_right)
	private void onImg_rightClick(View v){
		String str[]=st.split("-");
		int str_year=Integer.parseInt(str[0]);
		int str_month= Integer.parseInt(str[1])+1;
		if(str_month==13){
			str_year++;
			str_month=1;
		}
		st=str_year+"-"+str_month;
		footprint_search_center.setText(st);
	}

	private void initView() {
		titlebar.setTitle("足迹");
		titlebar.setLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}


}
