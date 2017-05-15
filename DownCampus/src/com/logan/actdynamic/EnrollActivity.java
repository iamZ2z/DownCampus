package com.logan.actdynamic;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.example.mobilecampus.R;
import com.util.title.TitleBar;

@ContentView(R.layout.find_enroll)
public class EnrollActivity extends Activity implements OnClickListener {
	//private TextView btn_backaccount,text_title;
	private Intent mIntent;
	
	@ViewInject(R.id.title_bar)
	private TitleBar titlebar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setContentView(R.adapter_photo_item.find_enroll);
		x.view().inject(this);
		initView();

		//btn_backaccount.setOnClickListener(this);
	}

	private void initView() {
		//text_title = (TextView) findViewById(R.id.text_title);
		//btn_backaccount = (TextView) findViewById(R.id.btn_backaccount);
		//text_title.setText("我要报名");		
		titlebar.setTitle("我要报名");
		titlebar.setLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mIntent = new Intent(EnrollActivity.this,CampusActDetailActivity.class);
				startActivity(mIntent);
				finish();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		default:
			break;
		}
	}
}
