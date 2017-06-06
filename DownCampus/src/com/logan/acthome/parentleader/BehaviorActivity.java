package com.logan.acthome.parentleader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.bean.BehaviorBean;
import com.logan.net.InterfaceTest;
import com.logan.net.UsuallyData;
import com.util.title.TitleBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ContentView(R.layout.home_behavior)
public class BehaviorActivity extends Activity {
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;
    @ViewInject(R.id.sp_year)
    private Spinner sp_year;
    String[] str_year = {"2016-2017学年"};
    @ViewInject(R.id.sp_term)
    private Spinner sp_term;
    String[] str_term = {"春季(上学期)"};
    @ViewInject(R.id.list)
    private ListView list;
    private SimpleAdapter mAdapter;
    private List<HashMap<String, Object>> mHashmap;
    private HashMap<String, Object> mMap;
    private List<? extends Map<String, ?>> data;
    @ViewInject(R.id.head)
    private CircleImageView head;
    @ViewInject(R.id.name)
    private TextView name;
    private InterfaceTest interfaceTest = new InterfaceTest();
    private UsuallyData usuallyData = new UsuallyData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        titlebar.setTitle("学生表现");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initView();
        initData();

        spinneryear();
        spinnerterm();
        dourl();
        list.setEmptyView(findViewById(R.id.nulldata));
    }

    private void initData() {
        String imgurl = interfaceTest.getPicture();
        DrawableRequestBuilder<Integer> thumbnailRequest = Glide.with(this).load(R.drawable
                .touxiang);
        Glide.with(this).load(imgurl).thumbnail(thumbnailRequest).diskCacheStrategy
                (DiskCacheStrategy.ALL).into(head);

        name.setText(usuallyData.getFullname());
    }

    private void spinneryear() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_bluebord_icon,
                str_year);
        adapter.setDropDownViewResource(R.layout.spinnerdropdownitem_behavior);
        // 绑定 Adapter到控件
        sp_year.setAdapter(adapter);
        sp_year.setOnItemSelectedListener(new OnItemSelectedListener() {
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

    private void spinnerterm() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_bluebord_icon,
                str_term);
        adapter.setDropDownViewResource(R.layout.spinnerdropdownitem);
        // 绑定 Adapter到控件
        sp_term.setAdapter(adapter);
        sp_term.setOnItemSelectedListener(new OnItemSelectedListener() {
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

    private void initView() {
        mAdapter = new SimpleAdapter(this, getData(), R.layout.home_behavior_list, new
                String[]{"title", "rank", "content", "time"}, new int[]{R.id.title, R
                .id.rank, R.id.content, R.id.time});
        list.setAdapter(mAdapter);
    }

    private List<? extends Map<String, ?>> getData() {
        mHashmap = new ArrayList<>();
        mMap = new HashMap<>();
        mMap.put("title", "评语老师");
        mMap.put("rank", "评价");
        mMap.put("content", "——");
        mMap.put("time", "2017-04");
        mHashmap.add(mMap);
        return mHashmap;
    }

    private List<? extends Map<String, ?>> getData2(BehaviorBean accountListBean) {
        mHashmap = new ArrayList<>();
        for (int j = 0; j < accountListBean.getData().size(); j++) {
            mMap = new HashMap<>();
            mMap.put("title", "评价老师：" + accountListBean.getData().get(j).getAppraiserName());
            switch (accountListBean.getData().get(j).getJudge()) {
                case "1":
                    mMap.put("rank", "评价：优秀");
                    break;
                case "2":
                    mMap.put("rank", "评价：良好");
                    break;
                case "3":
                    mMap.put("rank", "评价：中等");
                    break;
                case "4":
                    mMap.put("rank", "评价：及格");
                    break;
                case "5":
                    mMap.put("rank", "评价：不及格");
                    break;
                default:
                    break;
            }
            mMap.put("content", accountListBean.getData().get(j).getRemark());
            mMap.put("time", accountListBean.getData().get(j).getJudgeDate());
            mHashmap.add(mMap);
        }
        return mHashmap;
    }

    private void dourl() {
        String url = interfaceTest.getServerurl() + interfaceTest.getParentsperformance();
        String token = interfaceTest.getToken();
        String studentId = interfaceTest.getStudentId();

        FormBody formBody = new FormBody.Builder().add("token", token).add("studentId",
                studentId).add("pageNo", "1").build();
        final Request request = new Request.Builder().url(url).post(formBody).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = new OkHttpClient().newCall(request).execute();
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        Log.e("BehaviorBean的result", "请求数据:" + str);
                        final BehaviorBean bean = new Gson().fromJson(str,
                                BehaviorBean.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                data = getData2(bean);
                                mAdapter = new SimpleAdapter(BehaviorActivity.this, data, R
                                        .layout.home_behavior_list, new String[]{"title", "rank",
                                        "content", "time"}, new int[]{R.id.title, R.id.rank, R.id
                                        .content, R.id.time});
                                list.setAdapter(mAdapter);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
