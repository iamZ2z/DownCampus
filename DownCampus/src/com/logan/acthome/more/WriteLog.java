package com.logan.acthome.more;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilecampus.R;
import com.logan.bean.MultiSpinnerBean;
import com.logan.widgets.MultiSpinner;
import com.util.TitleBar;
import com.util.Utils;

import java.util.ArrayList;

@ContentView(R.layout.home_log_write)
public class WriteLog extends Activity {

	@ViewInject(R.id.title_bar)
	private TitleBar titlebar;

	@ViewInject(R.id.sp)
	private Spinner mSpinner;

	@ViewInject(R.id.spmulti)
	private MultiSpinner mMultiSpinner;
	
	@ViewInject(R.id.logdate)
	private TextView logdate;
	Utils util=new Utils();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		x.view().inject(this);
		initView();
		spinnerListener();
		multiSpinnerListener();
	}

	private void multiSpinnerListener() {
		mMultiSpinner.setTitle("选择汇报对象");
		ArrayList multispinnerlist=new ArrayList();
		MultiSpinnerBean multiSpinnerBean=new MultiSpinnerBean();
		multiSpinnerBean.setName("刘波");
		multiSpinnerBean.setValue(1);
		multispinnerlist.add(multiSpinnerBean);

		multiSpinnerBean=new MultiSpinnerBean();
		multiSpinnerBean.setName("刘波");
		multiSpinnerBean.setValue(2);
		multispinnerlist.add(multiSpinnerBean);

		multiSpinnerBean=new MultiSpinnerBean();
		multiSpinnerBean.setName("刘波");
		multiSpinnerBean.setValue(3);
		multispinnerlist.add(multiSpinnerBean);
		mMultiSpinner.setDataList(multispinnerlist);
	}

	private void initView() {
		titlebar.setTitle("写日志");
		titlebar.setLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		titlebar.addAction(new TitleBar.TextAction("发送") {
			@Override
			public void performAction(View view) {
				// TODO Auto-generated method stub
			}
		});
		
		logdate.setText(util.getmCalendar());
	}

	private void spinnerListener() {
		// 建立数据源
		String[] mItems = getResources().getStringArray(R.array.log_type);
		// 建立Adapter并且绑定数据源
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_bluebord_icon, mItems);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		// 绑定 Adapter到控件
		mSpinner.setAdapter(adapter);
		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String[] leave_type = getResources().getStringArray(
						R.array.log_type);
				Toast.makeText(WriteLog.this, "你点击的是" + leave_type[position],
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});
	}
}
