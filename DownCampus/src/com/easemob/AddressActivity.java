package com.easemob;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mobilecampus.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.logan.actnews.AddContactActivity;
import com.logan.actnews.GroupManagerActivity;
import com.logan.widgets.ClearEditText;
import com.util.address.AddressAdapter;
import com.util.address.CharacterParser;
import com.util.address.GroupMemberBean;
import com.util.address.PinyinComparator;
import com.util.title.TitleBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ContentView(R.layout.me_address)
public class AddressActivity extends Activity {
    @ViewInject(R.id.my_expand_lv)
    private ExpandableListView myExpandLv;
    @ViewInject(R.id.filter_edit)
    private ClearEditText myClearEt;
    @ViewInject(R.id.no_search_result_tv)
    private TextView noSearchResultTv;
    private AddressAdapter myAdapter;
    private List<GroupMemberBean> groupBeanList;
    private List<List<GroupMemberBean>> childBeanList = new ArrayList<>();
    //汉字转换成拼音的类
    private CharacterParser characterParser;
    //根据拼音来排列ListView里面的数据类
    private PinyinComparator pinyinComparator;
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;
    private ImageView mCollectView;
    private List<String> listString;
    private List<GroupMemberBean> tempOne;
    private ArrayList<String> mArrayListresult = new ArrayList<>();
    private MaterialDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        titlebar.setTitle("通讯录");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCollectView = (ImageView) titlebar.addAction(new TitleBar.ImageAction(R.mipmap
                .nav_btn_add) {
            @Override
            public void performAction(View view) {
                startActivity(new Intent(AddressActivity.this, AddContactActivity.class));
            }
        });

        mArrayListresult.add("联系人");
        initView();
        initEaseList();
        myExpandLv.setOnItemLongClickListener(new LongListener());
        dialog= new MaterialDialog.Builder(this)
                .content(R.string.loading)
                .progress(true, 0)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            mArrayListresult = data.getStringArrayListExtra("result");
            myAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initView();
        initEaseListRestart("");
        myExpandLv.setOnItemLongClickListener(new LongListener());
    }

    private void initView() {
        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        groupBeanList = fillData(mArrayListresult);

        // 用于生成列表信息
        myAdapter = new AddressAdapter(this, groupBeanList, childBeanList);
        myExpandLv.setAdapter(myAdapter);
        myExpandLv.setGroupIndicator(null);
        myClearEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initEaseList() {
        listString = new ArrayList<>();
        EMClient.getInstance().login("zzzjh", "123456", new EMCallBack() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
        EMClient.getInstance().contactManager().aysncGetAllContactsFromServer(new EMValueCallBack<List<String>>() {
            @Override
            public void onSuccess(List<String> strings) {
                for (int i = 0; i < strings.size(); i++) {
                    listString.add(strings.get(i));
                }
                tempOne = fillData(listString);
                // 根据a~z进行排序源数据
                Collections.sort(groupBeanList, pinyinComparator);
                Collections.sort(tempOne, pinyinComparator);
                childBeanList.add(tempOne);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myExpandLv.expandGroup(0);
                        myAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void onError(int i, String s) {

            }
        });
        //初始化时需要传入联系人list
        //mEaseContactlist.init(contactList);
    }

    public void initEaseListRestart(String movelist) {
        listString = new ArrayList<>();
        if (!movelist.equals("")) listString.add(movelist);
        tempOne = fillData(listString);
        // 根据a~z进行排序源数据
        Collections.sort(groupBeanList, pinyinComparator);
        Collections.sort(tempOne, pinyinComparator);
        childBeanList.add(tempOne);
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
        List<GroupMemberBean> tempFilterList;
        List<List<GroupMemberBean>> childFilterList = new ArrayList<>();

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
                tempFilterList = new ArrayList<>();
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
                    GroupMemberBean sortChildModel = childBeanList.get(i).get(j);
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
        if (groupFilterList.size() == 0) noSearchResultTv.setVisibility(View.VISIBLE);
        else noSearchResultTv.setVisibility(View.GONE);
    }

    private class LongListener implements android.widget.AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            String[] item = new String[]{"分组管理"};
            new AlertDialog.Builder(AddressActivity.this).setItems
                    (item, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(AddressActivity.this, GroupManagerActivity
                                    .class);
                            intent.putExtra("group", mArrayListresult);
                            startActivityForResult(intent, 0);
                        }
                    }).show();
            return true;
        }
    }
}
