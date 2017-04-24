package com.logan.actmobilecampus;

import com.example.mobilecampus.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class AdvertisementActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_advertisement);

		Handler handler = new Handler();
		// Integer time = 2000;
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(AdvertisementActivity.this,
						AccountActivity.class));
				AdvertisementActivity.this.finish();
			}
		}, 1);

	}
}
