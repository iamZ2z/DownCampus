package com.logan.actnews;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.mobilecampus.R;
import com.util.title.TitleBar;

@ContentView(R.layout.news_other)
public class OtherActivity extends Activity implements OnClickListener {
	private TextView other_name;

	@ViewInject(R.id.other_send)
	private Button other_send;

	private Intent mIntent;
	String address_name;
	// 存储路径
	private static String path = "/sdcard/MobileCampus/";	

	@ViewInject(R.id.title_bar)
	private TitleBar titlebar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		x.view().inject(this);
		address_name = getIntent().getStringExtra("userId");
		other_name.setText(address_name);
		initView();
		other_send.setOnClickListener(this);
	}

	private void initView() {
		titlebar.setTitle(address_name);
		titlebar.setLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		other_name = (TextView) findViewById(R.id.other_name);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.other_send:
			saveNameChat();
			/*mIntent = new Intent(this, NewsChatActivity.class);
			Bundle mBundle = new Bundle();
			mBundle.putString("other_name", address_name);
			mIntent.putExtras(mBundle);
			startActivity(mIntent);*/
			startActivity(new Intent(this, com.easemob.NewsChatActivity.class).putExtra("userId",address_name));
			break;
		default:
			break;
		}
	}

	//打开聊天框时保存
	private void saveNameChat() {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
			return;
		}
		FileOutputStream b = null;
		File file = new File(path);
		if (!file.exists())
			file.mkdirs();// 创建文件夹
		String fileName = path + "other.txt";
		try {
			Calendar c=Calendar.getInstance();
			String address=address_name+"#"+c.get(Calendar.HOUR_OF_DAY)+"#"+c.get(Calendar.MINUTE);
			b = new FileOutputStream(fileName);
			b.write(address.getBytes());
			b.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
