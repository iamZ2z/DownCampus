package com.logan.acthome.more;

import java.util.Calendar;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.mobilecampus.R;
import com.util.TitleBar;

@ContentView(R.layout.home_sign)
public class SignActivity extends Activity{
	@ViewInject(R.id.sign_time)
	private TextView sign_time;
	
	@ViewInject(R.id.sign_location)
	private TextView sign_location;
	
	@ViewInject(R.id.title_bar)
	private TitleBar titlebar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		x.view().inject(this);

		initView();
		
		Calendar cal=Calendar.getInstance();
		sign_time.setText("签到时间："+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE));
		
		Intent mIntent=getIntent();
		if(mIntent.getStringExtra("signlocation")!=null&&mIntent.getStringExtra("signlocation")!=" ")
			sign_location.setText("签到地点："+mIntent.getStringExtra("signlocation"));
		else
			sign_location.setText("签到地点：");
	}

	private void initView() {
		titlebar.setTitle("签到");
		titlebar.setLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

}
