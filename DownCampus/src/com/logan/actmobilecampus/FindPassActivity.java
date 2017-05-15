package com.logan.actmobilecampus;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.example.mobilecampus.R;
import com.util.title.TitleBar;

@ContentView(R.layout.activity_findpass)
public class FindPassActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.check_pass)
	private CheckBox check_pass;
	
	@ViewInject(R.id.edit_newpass)
	private EditText edit_newpass;
	
	@ViewInject(R.id.btn_enterpass)
	private Button btn_enterpass;
	
	@ViewInject(R.id.title_bar)
	private TitleBar titlebar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		x.view().inject(this);
		titlebar.setTitle("找回密码");
		titlebar.setLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		
		btn_enterpass.setOnClickListener(this);
		check_pass.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					// 如果选中，显示密码
					edit_newpass
							.setTransformationMethod(HideReturnsTransformationMethod
									.getInstance());
				} else {
					// 否则隐藏密码
					edit_newpass
							.setTransformationMethod(PasswordTransformationMethod
									.getInstance());
				}

			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_enterpass:
			enterpass();
			break;
		default:
			break;
		}
	}

	private void enterpass() {
		Intent mIntent = new Intent(this, AccountActivity.class);
		startActivity(mIntent);
	}
}
