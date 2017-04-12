package com.logan.acthome;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.example.mobilecampus.R;
import com.util.TitleBar;

@ContentView(R.layout.home_myseat)
public class MySeat extends Activity {
	@ViewInject(R.id.title_bar)
	private TitleBar titlebar;

	/*@ViewInject(R.id.grid)
	private GridView mGridView;
	private List<HashMap<String, Object>> mHashmap;
	private HashMap<String, Object> mMap;
	private SimpleAdapter mAdapter;*/
	
	@ViewInject(R.id.iv)
	private ImageView iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		x.view().inject(this);
		initView();
		
		//自定义view
		//Seat_View seatView=new Seat_View(this);
	}

	private void initView() {
		titlebar.setTitle("我的座位");
		titlebar.setLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		/*mAdapter = new SimpleAdapter(this, getData(),
				R.adapter_photo_item.home_myseat_grid, new String[] { "iv" },
				new int[] { R.id.iv });
		mGridView.setAdapter(mAdapter);*/
		//mGridView.setOverScrollMode(View.OVER_SCROLL_NEVER);
		//mGridView.setAdapter(new SeatAdapter(this));
	}
}
