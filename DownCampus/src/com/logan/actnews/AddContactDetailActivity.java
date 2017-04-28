package com.logan.actnews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.easemob.VerificationActivity;
import com.example.mobilecampus.R;
import com.util.TitleBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Z2z on 2017-4-19.
 */

@ContentView(R.layout.news_addcontactdetail)
public class AddContactDetailActivity extends Activity {
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;

    @ViewInject(R.id.other_send)
    private Button other_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);

        String name=getIntent().getStringExtra("searchname");
        titlebar.setTitle(name);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Event(R.id.other_send)
    private void onaddClick(View v){
        //Intent intent=new Intent(this, VerificationActivity.class);
        startActivity(new Intent(this, VerificationActivity.class));
    }
}