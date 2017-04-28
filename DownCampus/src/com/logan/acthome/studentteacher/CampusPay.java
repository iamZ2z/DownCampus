package com.logan.acthome.studentteacher;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.example.mobilecampus.R;
import com.util.TitleBar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

@ContentView(R.layout.home_campuspay)
public class CampusPay extends Activity {
	@ViewInject(R.id.title_bar)
	private TitleBar titlebar;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		x.view().inject(this);
		initView();
	}


	private void initView() {
		titlebar.setTitle("校园缴费");
		titlebar.setLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
