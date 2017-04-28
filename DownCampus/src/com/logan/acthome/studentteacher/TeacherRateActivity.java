package com.logan.acthome.studentteacher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.mobilecampus.R;
import com.logan.acthome.more.RateContentActivity;
import com.util.TitleBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Z2z on 2017/3/31.
 */

@ContentView(R.layout.home_teacherrate)
public class TeacherRateActivity extends Activity{
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;

    @ViewInject(R.id.sp)
    private Spinner sp;
    String[] str = {"2017-02-15", "2017-02-01", "2017-03-01"};

    @ViewInject(R.id.btn)
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        titlebar.setTitle("教师评价");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sp_Year();
    }


    private void sp_Year() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_bluebord_icon, str);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 绑定 Adapter到控件
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				/*
				 * Toast.makeText(LeaveActivity.this,"你点击的是" +
				 * leave_type[position], Toast.LENGTH_SHORT).show();
				 */
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Event(value = R.id.btn)
    private void onBtn_Click(View v){
        Intent mIntent=new Intent(this, RateContentActivity.class);
        startActivity(mIntent);
    }
}
