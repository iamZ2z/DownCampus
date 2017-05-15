package com.logan.actdynamic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.bean.CampusActBean;
import com.logan.constant.InterfaceTest;
import com.util.title.TitleBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ContentView(R.layout.find_campusact)
public class CampusActActivity extends Activity implements OnItemClickListener {
    private Intent mIntent;
    // 列表
    @ViewInject(R.id.list)
    private ListView mListView;
    private SimpleAdapter mAdapter;
    private List<HashMap<String, Object>> mHashmap;
    private HashMap<String, Object> mMap;
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;

    private InterfaceTest interfaceTest = new InterfaceTest();
    private List<? extends Map<String, ?>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        initView();

        mAdapter = new SimpleAdapter(this, getData(), R.layout.item_campusact,
                new String[]{"img", "title", "type", "place", "data"},
                new int[]{R.id.img, R.id.title, R.id.type, R.id.place,
                        R.id.data});
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        urlcampusact();

    }

    private void initView() {
        titlebar.setTitle("校园活动");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private List<HashMap<String, Object>> getData() {
        mHashmap = new ArrayList<>();
        mMap = new HashMap<>();
        mMap.put("img", R.drawable.upload);
        mMap.put("title", "伶仃希望小学帝第七届校运会");
        mMap.put("type", "活动类型：学校运动场");
        mMap.put("place", "体育运动场");
        mMap.put("data", "2017-03-01 12:00");
        mHashmap.add(mMap);

        mMap = new HashMap<String, Object>();
        mMap.put("img", R.drawable.upload);
        mMap.put("title", "伶仃希望小学帝第七届校运会");
        mMap.put("type", "活动类型：学校运动场");
        mMap.put("place", "体育运动场");
        mMap.put("data", "2017-03-01 12:00");
        mHashmap.add(mMap);
        return mHashmap;
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // String titles=mHashmap.get(position).get("title");
        /*
         * Map<String, Object> map = (Map<String, Object>)
		 * CampusActActivity.this.mAdapter .getItem(position);
		 */

        //Bitmap img= mHashmap.get(position).get("img");
        String title = mHashmap.get(position).get("title").toString();
        String type = mHashmap.get(position).get("type").toString();
        String place = mHashmap.get(position).get("place").toString();
        String data = mHashmap.get(position).get("data").toString();
        String description = mHashmap.get(position).get("description").toString();
        String remark = mHashmap.get(position).get("remark").toString();

        mIntent = new Intent(this, CampusActDetailActivity.class);
        mIntent.putExtra("campusact", mHashmap.get(position));
        startActivity(mIntent);
    }

    private void urlcampusact() {
        String url = interfaceTest.getServerurl() + interfaceTest.getCampusactivityquery();
        String token = interfaceTest.getToken();
        final OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder().add("token", token).build();
        final Request request = new Request.Builder().url(url).post(formBody).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        Log.e("urlCampusAct的result", "请求数据:" + str);
                        Gson gson = new Gson();
                        CampusActBean accountListBean = gson.fromJson(str,
                                CampusActBean.class);
                        data = getData2(accountListBean);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //放入list
                                mAdapter = new SimpleAdapter(CampusActActivity.this, data, R
                                        .layout.item_campusact, new String[]{"img", "title",
                                        "type", "place", "data"}, new int[]{R.id.img, R.id.title,
                                        R.id.type, R.id.place, R.id.data});
                                mListView.setAdapter(mAdapter);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private List<? extends Map<String, ?>> getData2(CampusActBean accountListBean) {
                mHashmap = new ArrayList<>();
                for (int j = 0; j < accountListBean.getData().size(); j++) {
                    mMap = new HashMap<>();
                    mMap.put("img", "https://timgsa.baidu" +
                            ".com/timg?image&quality=80&size=b9999_10000&sec=1493126948527&di" +
                            "=6cd3cc4f72b57af4d44a88354f0edfaa&imgtype=0&src=http%3A%2F%2Fs.news" +
                            ".bandao.cn%2Fnews_upload%2F201704%2F20170424094443_13AX1MEV.jpg");
                    mMap.put("title", accountListBean.getData().get(j).getName());
                    mMap.put("type", "活动类型：学校运动场");
                    mMap.put("place", accountListBean.getData().get(j).getAddress());
                    mMap.put("data", accountListBean.getData().get(j).getCreateTime());

                    mMap.put("description", accountListBean.getData().get(j).getDescription());
                    mMap.put("remark", accountListBean.getData().get(j).getRemark());
                    mHashmap.add(mMap);
                }
                return mHashmap;
            }
        }).start();
    }
}
