package com.logan.acthome.parentleader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.example.mobilecampus.R;
import com.util.TitleBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Z2z on 2017/4/11.
 */

@ContentView(R.layout.home_sendmeetingmanage)
public class SendMeetingActivity extends Activity{
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;

    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        titlebar.setTitle("");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titlebar.setActionTextColor(Color.WHITE);
        titlebar.addAction(new TitleBar.TextAction("请假记录") {
            @Override
            public void performAction(View view) {
                mIntent = new Intent();
                mIntent.setClass(SendMeetingActivity.this,MeetingManage.class);
                startActivity(mIntent);
            }
        });
    }
}
