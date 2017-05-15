package com.easemob;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.mobilecampus.R;
import com.util.title.TitleBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Z2z on 2017/4/12.
 */

@ContentView(R.layout.news_verification)
public class VerificationActivity extends Activity{
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;

    @ViewInject(R.id.sendverification)
    private Button sendverification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        titlebar.setTitle("验证申请");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Event(R.id.sendverification)
    private void onsendverificationClick(View v){
        //startActivity(new Intent(this, VerificationActivity.class));
        Toast.makeText(this,"发送成功",Toast.LENGTH_SHORT).show();
    }
}
