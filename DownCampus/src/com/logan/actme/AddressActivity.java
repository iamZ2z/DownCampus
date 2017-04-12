package com.logan.actme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilecampus.R;
import com.logan.actmobilecampus.MainActivity;
import com.util.TitleBar;
import com.util.address.CharacterParser;
import com.util.address.ClearEditText;
import com.util.address.GroupMemberBean;
import com.util.address.MyExpandableAdapter;
import com.util.address.PinyinComparator;

@ContentView(R.layout.me_address)
public class AddressActivity extends Activity {

	private Intent mIntent;

	private ImageView address_mate, address_parent, address_student;
	private ListView list_address_mate, list_address_parent,
			list_address_student;
	// 列表
	private SimpleAdapter mAdapter;
	private List<HashMap<String, Object>> mHashmap;
	private HashMap<String, Object> mMap;

	private SearchView mSearchView;
	private ArrayAdapter<String> autoAdaper;

	private ExpandableListView myExpandLv;
	private ClearEditText myClearEt;
	private TextView noSearchResultTv;
	private MyExpandableAdapter myAdapter;

	private List<GroupMemberBean> groupBeanList;
	private List<List<GroupMemberBean>> childBeanList = new ArrayList<List<GroupMemberBean>>();
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;
	
	@ViewInject(R.id.title_bar)
	private TitleBar titlebar;
	private ImageView mCollectView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		x.view().inject(this);
		initView();
		/*address_mate.setOnClickListener(this);
		address_parent.setOnClickListener(this);
		address_student.setOnClickListener(this);*/

		/*mAdapter = new SimpleAdapter(this, getData(), R.adapter_photo_item.item_address,
				new String[] { "img", "name" }, new int[] {
						R.id.exlist_itemimg, R.id.exlist_itemname });
		StringBuffer arr_autoo = null;
		
		 * for(HashMap<String, Object> eachmap:mHashmap){ for (Entry<String,
		 * Object> entry : eachmap.entrySet()) {
		 * 
		 * } }
		 
		Iterator<HashMap<String, Object>> it = mHashmap.iterator();
		HashMap<String, Object> hash;
		Object value = new Object();
		while (it.hasNext()) {
			hash = it.next();
			HashMap<String, Object> map = new HashMap<String, Object>();
			value = hash.get("name");
			arr_autoo.append(value.toString());
		}
		list_address_mate.setAdapter(mAdapter);

		
		 * String[] arr_auto = { "miss", "小智", "7", "de云色" }; autoAdaper = new
		 * ArrayAdapter<String>(this, R.adapter_photo_item.autocomplete_item, arr_auto);
		 * list_address_mate.setAdapter(autoAdaper);
		 

		//list_address_mate.setTextFilterEnabled(true);
	//	mSearchView.setIconified(false);
	//	mSearchView.setOnQueryTextListener(this);*/
	}

	private void initView() {
		titlebar.setTitle("通讯录");
		titlebar.setLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mCollectView = (ImageView) titlebar.addAction(new TitleBar.ImageAction(R.mipmap.nav_btn_add) {
            @Override
            public void performAction(View view) {
                Toast.makeText(AddressActivity.this, "点击了右上", Toast.LENGTH_SHORT).show();
                mIntent = new Intent();
				mIntent.putExtra("cam", 4);
				mIntent.setClass(AddressActivity.this, MainActivity.class);
				startActivity(mIntent);
				finish();
            }
        });

		/*
		 * address_mate = (ImageView) findViewById(R.id.address_mate);
		 * address_parent = (ImageView) findViewById(R.id.address_parent);
		 * address_student = (ImageView) findViewById(R.id.address_student);
		 * list_address_mate = (ListView) findViewById(R.id.list_address_mate);
		 * list_address_parent = (ListView)
		 * findViewById(R.id.list_address_parent); list_address_student =
		 * (ListView) findViewById(R.id.list_address_student);
		 * 
		 * mSearchView = (SearchView) findViewById(R.id.et_auto);
		 */
		noSearchResultTv = (TextView) this
				.findViewById(R.id.no_search_result_tv);
		myExpandLv = (ExpandableListView) this.findViewById(R.id.my_expand_lv);

		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		groupBeanList = filledData(this.getResources().getStringArray(
				R.array.depart));
		List<GroupMemberBean> tempOne = filledData(this.getResources()
				.getStringArray(R.array.child_one));
		List<GroupMemberBean> tempTwo = filledData(this.getResources()
				.getStringArray(R.array.child_two));
		// 根据a-z进行排序源数据
		Collections.sort(groupBeanList, pinyinComparator);
		Collections.sort(tempOne, pinyinComparator);
		Collections.sort(tempTwo, pinyinComparator);

		for (int i = 0; i < groupBeanList.size(); i++) {
			if (i % 2 == 0) {
				childBeanList.add(tempOne);
			} else {
				childBeanList.add(tempTwo);
			}
		}

		// 用于生成列表信息
		myAdapter = new MyExpandableAdapter(AddressActivity.this,
				groupBeanList, childBeanList);
		myExpandLv.setAdapter(myAdapter);
		myExpandLv.expandGroup(0);

		myClearEt = (ClearEditText) this.findViewById(R.id.filter_edit);

		myClearEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				filterData(s.toString());
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}


	/*private List<HashMap<String, Object>> getData() {
		mHashmap = new ArrayList<HashMap<String, Object>>();
		mMap = new HashMap<String, Object>();
		mMap.put("img", R.drawable.touxiang);
		mMap.put("name", "周老师");
		mHashmap.add(mMap);

		mMap = new HashMap<String, Object>();
		mMap.put("img", R.drawable.touxiang);
		mMap.put("name", "李老师");
		mHashmap.add(mMap);
		return mHashmap;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		if (!TextUtils.isEmpty(newText)) {
			Matrix mMatrix = new Matrix();
			address_mate.setScaleType(ScaleType.MATRIX);
			mMatrix.postRotate(90, address_mate.getWidth() / 2,
					address_mate.getHeight() / 2);
			address_mate.setImageMatrix(mMatrix);
			list_address_mate.setVisibility(View.VISIBLE);
			list_address_mate.setFilterText(newText);
		} else {
			Matrix mMatrix = new Matrix();
			address_mate.setScaleType(ScaleType.MATRIX);
			mMatrix.postRotate(0, address_mate.getWidth() / 2,
					address_mate.getHeight() / 2);
			address_mate.setImageMatrix(mMatrix);
			list_address_mate.setVisibility(View.GONE);
			list_address_mate.clearTextFilter();
		}
		return false;
	}*/
	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */
	private List<GroupMemberBean> filledData(String[] date) {
		List<GroupMemberBean> mSortList = new ArrayList<GroupMemberBean>();
		for (int i = 0; i < date.length; i++) {
			GroupMemberBean sortModel = new GroupMemberBean();
			sortModel.setName(date[i]);
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}
			mSortList.add(sortModel);
		}
		return mSortList;

	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<GroupMemberBean> groupFilterList = new ArrayList<GroupMemberBean>();
		List<GroupMemberBean> tempFilterList;
		List<List<GroupMemberBean>> childFilterList = new ArrayList<List<GroupMemberBean>>();

		if (TextUtils.isEmpty(filterStr)) {
			groupFilterList = groupBeanList;
			childFilterList = childBeanList;
			noSearchResultTv.setVisibility(View.GONE);
		} else {
			groupFilterList.clear();
			childFilterList.clear();
			for (int i = 0; i < groupBeanList.size(); i++) {
				//标记departGroup是否加入元素
				boolean isAddGroup = false;
				tempFilterList = new ArrayList<GroupMemberBean>();
				GroupMemberBean sortModel = groupBeanList.get(i);
				String name = sortModel.getName();
				// depart有字符直接加入
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					if (!groupFilterList.contains(sortModel)) {
						groupFilterList.add(sortModel);
						isAddGroup = true;
					}
				}

				for (int j = 0; j < childBeanList.get(i).size(); j++) {
					GroupMemberBean sortChildModel = childBeanList.get(i)
							.get(j);
					String childName = sortChildModel.getName();
					// child有字符直接加入，其父也加入
					if (childName.indexOf(filterStr.toString()) != -1
							|| characterParser.getSelling(childName)
									.startsWith(filterStr.toString())) {
						tempFilterList.add(sortChildModel);
						if (!groupFilterList.contains(groupBeanList.get(i))) {
							groupFilterList.add(groupBeanList.get(i));
							isAddGroup = true;
						}
					}

				}
				Collections.sort(tempFilterList, pinyinComparator);
				if (isAddGroup) {
					childFilterList.add(tempFilterList);
				}
			}

			// 根据a-z进行排序
			Collections.sort(groupBeanList, pinyinComparator);
		}

		if (myAdapter != null) {
			myAdapter.updateListView(groupFilterList, childFilterList);

			if (TextUtils.isEmpty(filterStr)) {
				for (int i = 0; i < groupFilterList.size(); i++) {
					if (i == 0) {
						myExpandLv.expandGroup(i);
						continue;
					}
					myExpandLv.collapseGroup(i);
				}
			} else {
				//搜索的结果全部展开
				for (int i = 0; i < groupFilterList.size(); i++) {
					myExpandLv.expandGroup(i);
				}
			}
		}
		
		//如果查询的结果为0时，显示为搜索到结果的提示
		if(groupFilterList.size() == 0){
			noSearchResultTv.setVisibility(View.VISIBLE);
		}else{
			noSearchResultTv.setVisibility(View.GONE);
		}
	}
}
