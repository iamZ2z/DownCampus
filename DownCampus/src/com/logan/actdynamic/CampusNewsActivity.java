package com.logan.actdynamic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.mobilecampus.R;
import com.util.TitleBar;

@ContentView(R.layout.find_campusnews)
public class CampusNewsActivity extends Activity implements OnItemClickListener {
	// 列表
	@ViewInject(R.id.list)
	private ListView mListView;
	private SimpleAdapter mAdapter;
	private List<HashMap<String, Object>> mHashmap;
	private HashMap<String, Object> mMap;

	@ViewInject(R.id.title_bar)
	private TitleBar titlebar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		x.view().inject(this);
		initView();

	}

	private void initView() {
		titlebar.setTitle("校园新闻");
		titlebar.setLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		mAdapter = new SimpleAdapter(this, getData(),
				R.layout.item_campusnews_big,
				new String[] { "img", "title", "type", "data", "img2",
						"title2", "type2", "data2", "img3", "title3", "type3",
						"data3", "img4", "title4", "type4", "data4", "img5",
						"title5", "type5", "data5", }, new int[] { R.id.img,
						R.id.title, R.id.type, R.id.data, R.id.img2,
						R.id.title2, R.id.type2, R.id.data2, R.id.img3,
						R.id.title3, R.id.type3, R.id.data3, R.id.img4,
						R.id.title4, R.id.type4, R.id.data4, R.id.img5,
						R.id.title5, R.id.type5, R.id.data5,
				});
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}

	private List<? extends Map<String, ?>> getData() {
		mHashmap=new ArrayList<HashMap<String,Object>>();
		addContent(R.drawable.upload,"俊县骏洲北关小学：走进自然寻找春天","综合时间","12-17 12:00",
				R.drawable.upload,"“智能+”时代，软件和数据如何驱动营销？","广东广州","12-17 12:00",
				R.drawable.upload,"“智能+”时代，软件和数据如何驱动营销？","广东广州","12-17 12:00",
				R.drawable.upload,"“智能+”时代，软件和数据如何驱动营销？","广东广州","12-17 12:00",
				R.drawable.upload,"“智能+”时代，软件和数据如何驱动营销？","广东广州","12-17 12:00"
				);
		
		addContent(R.drawable.upload,"俊县骏洲北关小学：走进自然寻找春天","综合时间","12-17 12:00",
				R.drawable.upload,"“智能+”时代，软件和数据如何驱动营销？","广东广州","12-17 12:00",
				R.drawable.upload,"“智能+”时代，软件和数据如何驱动营销？","广东广州","12-17 12:00",
				R.drawable.upload,"“智能+”时代，软件和数据如何驱动营销？","广东广州","12-17 12:00",
				R.drawable.upload,"“智能+”时代，软件和数据如何驱动营销？","广东广州","12-17 12:00"
				);		
		return mHashmap;
	}



	private void addContent(int upload, String string, String string2,
			String string3, int upload2, String string4, String string5,
			String string6, int upload3, String string7, String string8,
			String string9, int upload4, String string10, String string11,
			String string12, int upload5, String string13, String string14,
			String string15) {
		mMap=new HashMap<String, Object>();
		mMap.put("img", upload);
		mMap.put("title", string);
		mMap.put("type", string2);
		mMap.put("data", string3);
		
		mMap.put("img2", upload2);
		mMap.put("title2", string4);
		mMap.put("type2", string5);
		mMap.put("data2", string6);
		
		mMap.put("img3", upload3);
		mMap.put("title3", string7);
		mMap.put("type3", string8);
		mMap.put("data3", string9);
		
		mMap.put("img4", upload4);
		mMap.put("title4", string10);
		mMap.put("type4", string11);
		mMap.put("data4", string12);
		
		mMap.put("img5", upload5);
		mMap.put("title5", string13);
		mMap.put("type5", string14);
		mMap.put("data5", string15);
		mHashmap.add(mMap);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

}
