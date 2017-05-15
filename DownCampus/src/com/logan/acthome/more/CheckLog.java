package com.logan.acthome.more;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.example.mobilecampus.R;
import com.util.title.TitleBar;

@ContentView(R.layout.home_log_check)
public class CheckLog extends Activity {	
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
		titlebar.setTitle("查看汇报");
		titlebar.setLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
