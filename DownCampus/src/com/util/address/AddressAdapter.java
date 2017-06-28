package com.util.address;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.NewsChatActivity;
import com.example.mobilecampus.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class AddressAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<GroupMemberBean> mGroup;
    private List<List<GroupMemberBean>> mChild;
    private LayoutInflater mInflater;
    private SharedPreferences sp;

    public AddressAdapter(Context mContext, List<GroupMemberBean> mGroup, List<List<GroupMemberBean>> mChild) {
        this.mContext = mContext;
        this.mGroup = mGroup;
        this.mChild = mChild;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     */
    public void updateListView(List<GroupMemberBean> mGroup,
                               List<List<GroupMemberBean>> mChild) {
        this.mGroup = mGroup;
        this.mChild = mChild;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return mGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChild.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChild.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        GroupHolderView groupHolderView;
        if (convertView == null) {
            groupHolderView = new GroupHolderView();
            convertView = mInflater.inflate(R.layout.item_group_address, null);
            groupHolderView.groupTv = (TextView) convertView
                    .findViewById(R.id.exlist_groupname);
            groupHolderView.groupIcon = (ImageView) convertView.findViewById(R.id.exlist_groupicon);
            convertView.setTag(groupHolderView);
        } else {
            groupHolderView = (GroupHolderView) convertView.getTag();
        }
        if (isExpanded) groupHolderView.groupIcon.setBackgroundResource(R.mipmap.search_icon_down);
        else groupHolderView.groupIcon.setBackgroundResource(R.mipmap.search_icon_right);
        groupHolderView.groupTv.setText(mGroup.get(groupPosition).getName());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildHolderView childHolderView;
        if (convertView == null) {
            childHolderView = new ChildHolderView();
            convertView = mInflater.inflate(R.layout.item_address, null);
            childHolderView.childIv = (ImageView) convertView
                    .findViewById(R.id.exlist_itemimg);
            childHolderView.childTv = (TextView) convertView
                    .findViewById(R.id.exlist_itemname);
            childHolderView.childLl = (RelativeLayout) convertView
                    .findViewById(R.id.exlist_itemlayout);
            childHolderView.remark = (TextView) convertView.findViewById(R.id.remark);
            convertView.setTag(childHolderView);
        } else {
            childHolderView = (ChildHolderView) convertView.getTag();
        }

        sp = mContext.getSharedPreferences("config", MODE_PRIVATE);
        String str = sp.getString(mChild.get(groupPosition)
                .get(childPosition).getName(), mChild.get(groupPosition)
                .get(childPosition).getName());
        childHolderView.childTv.setText(str);

        childHolderView.remark.setText(mChild.get(groupPosition).get(childPosition).getName());

        childHolderView.childLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewsChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID, childHolderView.remark.getText()
                        .toString()
                );
                mContext.startActivity(intent);
            }
        });

        childHolderView.childLl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String[] item = new String[]{"分组", "备注", "删除"};
                new AlertDialog.Builder(mContext).setItems(item, new DialogInterface
                        .OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                final String[] singlelist = new String[mGroup.size()];
                                for (int i = 0; i < mGroup.size(); i++) {
                                    singlelist[i] = mGroup.get(i).getName();
                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setTitle("移动到分组").setSingleChoiceItems(singlelist, 0, new
                                        DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                mChild.get(which).add(mChild.get(groupPosition)
                                                        .get(childPosition));
                                                mChild.get(groupPosition).remove(childPosition);
                                                notifyDataSetChanged();
                                                dialog.dismiss();
                                            }
                                        });
                                AlertDialog dialogg = builder.create();
                                dialogg.show();
                                break;
                            case 1:
                                dialogremark();
                                //Toast.makeText(mContext, "备注", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                String str = mChild.get(groupPosition).get(childPosition)
                                        .getName();
                                try {
                                    EMClient.getInstance().contactManager().deleteContact
                                            (str);
                                    mChild.get(groupPosition).remove(childPosition);
                                } catch (HyphenateException e) {
                                    e.printStackTrace();
                                }
                                notifyDataSetChanged();
                                //Toast.makeText(mContext, "删除", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }

                    private void dialogremark() {
                        final EditText et = new EditText(mContext);
                        sp = mContext.getSharedPreferences("config", MODE_PRIVATE);
                        final String str = mChild.get(groupPosition).get(childPosition)
                                .getName();
                        new AlertDialog.Builder(mContext)
                                .setTitle(str).setView(et)
                                .setPositiveButton("确定", new DialogInterface
                                        .OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String input = et.getText().toString();
                                        if (input.equals("")) {
                                            Toast.makeText(mContext, "备注不能为空", Toast
                                                    .LENGTH_SHORT).show();
                                        } else {
                                            SharedPreferences.Editor editor = sp.edit();
                                            editor.putString(str, input);
                                            editor.commit();
                                            notifyDataSetChanged();
                                        }
                                    }
                                })
                                .setNegativeButton("取消", null).show();
                    }

                }).show();
                return true;
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class GroupHolderView {
        TextView groupTv;
        ImageView groupIcon;
    }

    class ChildHolderView {
        ImageView childIv;
        TextView childTv;
        RelativeLayout childLl;
        TextView remark;
    }

}
