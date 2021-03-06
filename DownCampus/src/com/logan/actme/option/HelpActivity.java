package com.logan.actme.option;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.example.mobilecampus.R;
import com.logan.actme.OptionActivity;
import com.util.title.TitleBar;

@ContentView(R.layout.option_help)
public class HelpActivity extends Activity {

	private Intent mIntent;
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
		titlebar.setTitle("使用帮助");
		titlebar.setLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mIntent = new Intent();
				mIntent.setClass(HelpActivity.this, OptionActivity.class);
				startActivity(mIntent);
				finish();
				finish();
			}
		});
	}

}
