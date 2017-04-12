package com.logan.actme.option;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.mobilecampus.R;
import com.logan.actme.OptionActivity;
import com.util.TitleBar;

@ContentView(R.layout.option_message)
public class MessageActivity extends Activity implements
		OnCheckedChangeListener {
	private Intent mIntent;

	@ViewInject(R.id.message_vibrate)
	private ToggleButton message_vibrate;
	private Vibrator vibrator;

	@ViewInject(R.id.title_bar)
	private TitleBar titlebar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		x.view().inject(this);
		initView();
		message_vibrate.setOnCheckedChangeListener(this);
	}

	private void initView() {
		titlebar.setTitle("新消息提醒");
		titlebar.setLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mIntent = new Intent();
				mIntent.setClass(MessageActivity.this, OptionActivity.class);
				startActivity(mIntent);
				finish();
			}
		});
	}


	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			Toast.makeText(this, "已开启", Toast.LENGTH_SHORT).show();
			vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			long[] pattern = { 100, 400, 100, 400 }; // 停止 开启 停止 开启
			vibrator.vibrate(pattern, -1);
		} else{
			Toast.makeText(this, "已关闭", Toast.LENGTH_SHORT).show();
			vibrator.cancel();
		}
	}
}
