package com.logan.actme.option;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;

import com.example.mobilecampus.R;
import com.logan.actme.OptionActivity;
import com.suke.widget.SwitchButton;
import com.util.title.TitleBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.option_message)
public class NewMessageActivity extends Activity {
    private Intent mIntent;

    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;

    @ViewInject(R.id.switchvibrate)
    private com.suke.widget.SwitchButton mVibrate;
    private Vibrator vibrator;

    @ViewInject(R.id.switchpush)
    private com.suke.widget.SwitchButton mPush;

    @ViewInject(R.id.switchmusic)
    private com.suke.widget.SwitchButton mMusic;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        initView();
        initState();
    }

    private void initState() {
        sp = getSharedPreferences("config", MODE_PRIVATE);
        Boolean bool = sp.getBoolean("switch_push", false);
        mPush.setChecked(bool);
        mMusic.setChecked(sp.getBoolean("switch_music", false));
        mVibrate.setChecked(sp.getBoolean("switch_vibrate", false));

        mPush.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("switch_push", isChecked);
                    editor.commit();
            }
        });

        mMusic.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("switch_music", isChecked);
                editor.commit();
            }
        });

        mVibrate.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("switch_vibrate", isChecked);
                editor.commit();
            }
        });
    }

    private void initView() {
        titlebar.setTitle("新消息提醒");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent();
                mIntent.setClass(NewMessageActivity.this, OptionActivity.class);
                startActivity(mIntent);
                finish();
            }
        });
    }
}
