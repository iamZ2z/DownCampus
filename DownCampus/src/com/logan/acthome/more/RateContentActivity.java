package com.logan.acthome.more;

import java.util.ArrayList;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mobilecampus.R;
import com.logan.acthome.more.RateActivity_Adapter;
import com.logan.acthome.more.RateActivity_Util;
import com.util.TitleBar;

@ContentView(R.layout.home_ratecontent)
public class RateContentActivity extends Activity {

	@ViewInject(R.id.title_bar)
	private TitleBar titlebar;

	@ViewInject(R.id.sp)
	private Spinner sp;
	String[] str = { "李刚", "郭霖", "鸿洋" };

	@ViewInject(R.id.list)
	private ListView mListView;
	private ArrayList<RateActivity_Util> dataList;
	
	@ViewInject(R.id.btn)
	private Button btn;
	RateActivity_Adapter adapter_arr;
	int[][] act_arr=new int[10][10];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		x.view().inject(this);

		titlebar.setTitle("教师评价");
		titlebar.setLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		spinner_teacher();
		list_Listener();
	}

	private void list_Listener() {
		dataList = new ArrayList<RateActivity_Util>();
		RateActivity_Util util1 = new RateActivity_Util(
				"老师修养",
				"爱岗敬业，工作认真。尊重家长，沟通时态度和气、语言亲切礼貌。无体罚和变相体罚学生现象。不擅自向学生家长征订、推销学习资料或其他用品；不向学生及其家长索要钱物、不收礼。",
				2, "2#4");
		dataList.add(util1);
		RateActivity_Util util2 = new RateActivity_Util("家访情况", "主动家访，交流", 4,
				"3#7#10#15");
		dataList.add(util2);
		RateActivity_Util util3 = new RateActivity_Util("家访情况", "主动家访，交流", 6,
				"3#7#10#15#20#50");
		dataList.add(util3);
		final RateActivity_Adapter adapter = new RateActivity_Adapter(this, dataList);
		mListView.setAdapter(adapter);
	}

	private void spinner_teacher() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, str);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(adapter);
		sp.setOnItemSelectedListener(new OnItemSelectedListener() {
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
	
	@Event(value=R.id.btn)
	public void onBtnClick(View v){
		act_arr=adapter_arr.getScore_arr();
		Toast.makeText(this,"测试"+act_arr[0][9]+act_arr[1][9]+act_arr[2][9], Toast.LENGTH_SHORT).show();
	}
}
