package com.logan.actmobilecampus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_advertisement);

		Handler handler = new Handler();
		// Integer time = 2000;
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(SplashActivity.this,
						AccountActivity.class));
				SplashActivity.this.finish();
			}
		}, 1);*/

        startActivity(new Intent(this, AccountActivity.class));
        finish();
    }
}
