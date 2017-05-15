package com.logan.actme.option;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.example.mobilecampus.R;
import com.util.title.TitleBar;

@ContentView(R.layout.option_rate)
public class CommentActivity extends Activity{
	private Intent mIntent;

	@ViewInject(R.id.title_bar)
	private TitleBar titlebar;

	private ImageView mCollectView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		x.view().inject(this);
		initView();

	}

	private void initView() {		
		titlebar.setTitle("吐槽产品经理");
		titlebar.setLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		titlebar.setActionTextColor(Color.WHITE);
		titlebar.addAction(new TitleBar.TextAction("发送") {
			@Override
			public void performAction(View view) {
				//Toast.makeText(MainActivity.this, "点击了发布", Toast.LENGTH_SHORT).show();
			}
		});
	}

}
