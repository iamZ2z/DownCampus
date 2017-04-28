package com.logan.actnews;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mobilecampus.R;
import com.logan.adapter.AddContactAdapter;
import com.logan.widgets.ClearEditText;
import com.util.address.CharacterParser;
import com.util.address.GroupMemberBean;
import com.util.address.PinyinComparator;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Z2z on 2017-4-18.
 */

@ContentView(R.layout.news_addcontact)
public class AddContactActivity extends Activity {
    @ViewInject(R.id.filter_edit)
    private ClearEditText myClearEt;
    @ViewInject(R.id.search)
    private TextView mTextSearch;

    //汉字转换成拼音的类
    private CharacterParser characterParser;
    //根据拼音来排列ListView里面的数据类
    private PinyinComparator pinyinComparator;

    private ArrayList<String> listsearch = new ArrayList<>();
    private AddContactAdapter mAdaper;
    @ViewInject(R.id.list)
    private ListView mListView;
    private List<GroupMemberBean> groupBeanList=new ArrayList<>();
    @ViewInject(R.id.no_search_result_tv)
    private TextView noSearchResultTv;

    @ViewInject(R.id.back)
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);

        initData();
        initSearch();
    }

    private void initData() {
        //添加数据
        listsearch.add("8005");
        listsearch.add("8006");
        listsearch.add("8007");
        mAdaper = new AddContactAdapter(this, groupBeanList);
        mListView.setAdapter(mAdaper);

        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        groupBeanList = fillData(listsearch);
        Collections.sort(groupBeanList, pinyinComparator);
    }

    private void initSearch() {
        myClearEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (myClearEt.getText().equals("")) mTextSearch.setText("取消");
                else mTextSearch.setText("搜索");
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 为ListView填充数据
     */
    private List<GroupMemberBean> fillData(List<String> date) {
        List<GroupMemberBean> mSortList = new ArrayList<>();
        for (int i = 0; i < date.size(); i++) {
            GroupMemberBean sortModel = new GroupMemberBean();
            sortModel.setName(date.get(i));
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(date.get(i));
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) sortModel.setSortLetters(sortString.toUpperCase());
            else sortModel.setSortLetters("#");
            mSortList.add(sortModel);
        }
        return mSortList;
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     */
    private void filterData(String filterStr) {
        List<GroupMemberBean> groupFilterList = new ArrayList<>();

        if (TextUtils.isEmpty(filterStr)) {
            groupFilterList = groupBeanList;
            noSearchResultTv.setVisibility(View.GONE);
        } else {
            groupFilterList.clear();
            for (int i = 0; i < groupBeanList.size(); i++) {
                //标记departGroup是否加入元素
                GroupMemberBean sortModel = groupBeanList.get(i);
                String name = sortModel.getName();
                // depart有字符直接加入
                if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name)
                        .startsWith(filterStr.toString())) {
                    if (!groupFilterList.contains(sortModel)) groupFilterList.add(sortModel);
                }

                /*for (int j = 0; j < listsearch.size(); j++) {
                    GroupMemberBean sortChildModel = listsearch.get(j);
                    String childName = sortChildModel.getName();
                    // child有字符直接加入，其父也加入
                    if (childName.indexOf(filterStr.toString()) != -1
                            || characterParser.getSelling(childName)
                            .startsWith(filterStr.toString())) {
                        tempFilterList.add(sortChildModel);
                        if (!groupFilterList.contains(groupBeanList.get(i))) {
                            groupFilterList.add(groupBeanList.get(i));
                        }
                    }
                }*/
            }
            // 根据a-z进行排序
            Collections.sort(groupBeanList, pinyinComparator);
        }

        if (mAdaper != null) {
            mAdaper.updateListView(groupFilterList);
        }

        //如果查询的结果为0时，显示为搜索到结果的提示
        if (groupFilterList.size() == 0) noSearchResultTv.setVisibility(View.VISIBLE);
        else noSearchResultTv.setVisibility(View.GONE);
    }

    @Event(value = R.id.search)
    private void onSearchClick(View v) {
        if (myClearEt.getText().equals("")) finish();
        else ;
    }

    @Event(value = R.id.back)
    private void onBackClick(View v) {
        finish();
    }
}
