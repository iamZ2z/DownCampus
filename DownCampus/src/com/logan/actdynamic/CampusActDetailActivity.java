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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobilecampus.R;
import com.util.TitleBar;

@ContentView(R.layout.find_campusactdetail)
public class CampusActDetailActivity extends Activity implements OnClickListener {
	private Intent mIntent;
	
	@ViewInject(R.id.img)
	private ImageView img;
	@ViewInject(R.id.title)
	private TextView title;
	@ViewInject(R.id.type)
	private TextView type;
	@ViewInject(R.id.place)
	private TextView place;
	@ViewInject(R.id.data)
	private TextView data;
	@ViewInject(R.id.introduce)
	private TextView introduce;
	@ViewInject(R.id.remark)
	private TextView remark;
	@ViewInject(R.id.appendix)
	private TextView appendix;
	
	@ViewInject(R.id.download)
	private Button download;	
	@ViewInject(R.id.sign)
	private Button sign;
	
	@ViewInject(R.id.title_bar)
	private TitleBar titlebar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		x.view().inject(this);
		initView();
		
		img.setBackgroundResource(R.drawable.upload);
		title.setText("铃站希望小学运动会开幕式");
		type.setText("活动类型："+"运动会");
		place.setText("体育运动场");
		data.setText("2017-03-01 12:00");
		introduce.setText("根据2017年体育计划，为丰富学生生活，增强体质，认真组织");
		remark.setText("体育项目竞争激烈，请注意健康");
		appendix.setText("活动章程附件");
		
		download.setOnClickListener(this);
		sign.setOnClickListener(this);
	}

	private void initView() {
		titlebar.setTitle("");
		titlebar.setLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.download:
			
			break;
			
		case R.id.sign:
			//mIntent = new Intent(this,EnrollActivity.class);
			//startActivity(mIntent);	
			finish();
			break;
		default:
			break;
		}
	}
}
