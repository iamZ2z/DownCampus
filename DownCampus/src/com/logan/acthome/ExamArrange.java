package com.logan.acthome;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import com.example.mobilecampus.R;
import com.logan.acthome.more.ExamArrangeDetail;
import com.util.TitleBar;

@ContentView(R.layout.home_examarrange)
public class ExamArrange extends Activity {
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;

    @ViewInject(R.id.sp_year)
    private Spinner sp_year;

    @ViewInject(R.id.sp_term)
    private Spinner sp_term;

    @ViewInject(R.id.sp_type)
    private Spinner sp_type;

    @ViewInject(R.id.sp_name)
    private Spinner sp_name;

    @ViewInject(R.id.btn_sure)
    private Button btn_sure;

    String[] str_year = {"2015-2016", "2016-2017"};
    String[] str_term = {"春季学期", "秋季学期"};
    String[] str_type = {"月考", "周考"};
    String[] str_name = {"第一周周考", "第二周周考"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);

        titlebar.setTitle("考试安排");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sp_year_Listener();
        sp_term_Listener();
        sp_type_Listener();
        sp_name_Listener();
    }

    private void sp_year_Listener() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_bluebord_icon, str_year);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 绑定 Adapter到控件
        sp_year.setAdapter(adapter);
        sp_year.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				/*Toast.makeText(ExamArrange.this, "你点击的是" + str_year[position],
						Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void sp_term_Listener() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout
				.spinner_bluebord_icon, str_term);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 绑定 Adapter到控件
        sp_term.setAdapter(adapter);
        sp_term.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				/*Toast.makeText(ExamArrange.this, "你点击的是" + str_term[position],
						Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void sp_type_Listener() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout
				.spinner_bluebord_icon, str_type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 绑定 Adapter到控件
        sp_type.setAdapter(adapter);
        sp_type.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				/*Toast.makeText(ExamArrange.this, "你点击的是" + str_type[position],
						Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void sp_name_Listener() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_bluebord_icon,
				str_name);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 绑定 Adapter到控件
        sp_name.setAdapter(adapter);
        sp_name.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				/*Toast.makeText(ExamArrange.this, "你点击的是" + str_name[position],
						Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Event(value = R.id.btn_sure)
    private void onSureClick(View v) {
        Intent mIntent = new Intent(this, ExamArrangeDetail.class);
        startActivity(mIntent);
    }
}
