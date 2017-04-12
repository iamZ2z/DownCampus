package com.logan.actnews;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.example.mobilecampus.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.EaseChatInputMenu;
import com.hyphenate.easeui.widget.EaseChatMessageList;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.util.TitleBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.news_newschat)
public class NewsChatActivity extends FragmentActivity implements EMMessageListener {
    /*private TextView btn_backaccount,text_title;
	// 列表
	private MyChatAdapter mAdapter;
	private ListView mListView;
	private ArrayList<HashMap<String, Object>> mHashmap;
	private HashMap<String, Object> mMap;

	String[] from = { "head", "content" };
	int[] to = { R.id.chat_head, R.id.chat_content, R.id.chat_other_head,
			R.id.chat_other_content };
	int[] layout = { R.layout.item_newschat, R.layout.item_newschat2 };

	private final static int ME = 0;
	private final static int OTHER = 1;

	private ImageView chat_send;
	private EditText send_content;

	// 存储路径
	private static String path = "/sdcard/MobileCampus/other.txt";	*/

    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;

    @ViewInject(R.id.input_menu)
    private EaseChatInputMenu inputMenu;
    @ViewInject(R.id.message_list)
    private EaseChatMessageList easeChatMessageList;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);

        titlebar.setTitle("　");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

		/*
		mHashmap = new ArrayList<HashMap<String, Object>>();
		addTexttoList("你是？", ME);
		mAdapter = new MyChatAdapter(getApplicationContext(), mHashmap, layout,
				from, to);
		mListView.setAdapter(mAdapter);*/

        inputMenu.init();
        inputMenu.setChatInputMenuListener(new EaseChatInputMenu.ChatInputMenuListener() {
            @Override
            public void onSendMessage(String content) {
                sendTextMessage(content);
            }

            @Override
            public void onBigExpressionClicked(EaseEmojicon emojicon) {
            }

            @Override
            public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
                return false;
            }
        });

        //EaseChatMessageList
        easeChatMessageList.init("8003", 0, new CustomChatRowProvider());
        easeChatMessageList.setItemClickListener(new EaseChatMessageList
				.MessageListItemClickListener() {
            @Override
            public void onResendClick(EMMessage message) {
                //重发消息按钮点击事件
            }

            @Override
            public boolean onBubbleClick(EMMessage message) {
                //气泡框点击事件，EaseUI有默认实现这个事件，如果需要覆盖，return值要返回true
                return false;
            }

            @Override
            public void onBubbleLongClick(EMMessage message) {
                //气泡框长按事件
            }

            @Override
            public void onUserAvatarClick(String username) {
                //头像点击事件
            }

            @Override
            public void onUserAvatarLongClick(String username) {

            }
        });
        swipeRefreshLayout = easeChatMessageList.getSwipeRefreshLayout();

        EaseUI.getInstance().init(this,null);
        EaseChatFragment mEaseChatFragment=new EaseChatFragment();
        mEaseChatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.layout_container,mEaseChatFragment).commit();
    }


    @Override
    public void onResume() {
        super.onResume();
        //回调接收数据
        EMClient.getInstance().chatManager().addMessageListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EMClient.getInstance().chatManager().removeMessageListener(this);
    }

    private void sendTextMessage(String content) {
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(content, "8003");
        //发送消息
        EMClient.getInstance().chatManager().sendMessage(message);
        //mTextView.setText(mTextView.getText()+"\n"+content);
        easeChatMessageList.refresh();
        easeChatMessageList.refreshSelectLast();

        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                Toast.makeText(NewsChatActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int i, String s) {
            }

            @Override
            public void onProgress(int i, String s) {
            }
        });
    }

    //以下是监听方法
    public void onMessageReceived(final List<EMMessage> list) {
        for (final EMMessage msg : list) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
					/*mTextView.setText(mTextView.getText()+"\n"
							+((EMTextMessageBody) msg.getBody()).getMessage());*/
                    String username=msg.getFrom();
                    // 如果是当前会话的消息，刷新聊天页面
                    if(username.equals("8003")){
                        /*msgList.add(msg);
                        adapter.notifyDataSetChanged();
                        if (msgList.size() > 0) {
                            et_content.setSelection(listView.getCount() - 1);

                        }*/
                        easeChatMessageList.refresh();
                        easeChatMessageList.refreshSelectLast();
                    }

                }
            });
        }
    }

    public void onCmdMessageReceived(List<EMMessage> list) {

    }

    public void onMessageRead(List<EMMessage> list) {

    }

    public void onMessageDelivered(List<EMMessage> list) {

    }

    public void onMessageChanged(EMMessage emMessage, Object o) {

    }



    //内部类
    private final class CustomChatRowProvider implements EaseCustomChatRowProvider {
        @Override
        public int getCustomChatRowTypeCount() {
            //音、视频通话发送、接收共4种
            return 4;
        }

        @Override
        public int getCustomChatRowType(EMMessage message) {
            if (message.getType() == EMMessage.Type.TXT) {
                //语音通话类型
				/*if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)){
					return message.direct == EMMessage.Direct.RECEIVE ?
					MESSAGE_TYPE_RECV_VOICE_CALL : MESSAGE_TYPE_SENT_VOICE_CALL;
				}else if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)){
					//视频通话
					return message.direct == EMMessage.Direct.RECEIVE ?
					MESSAGE_TYPE_RECV_VIDEO_CALL : MESSAGE_TYPE_SENT_VIDEO_CALL;
				}*/
            }
            return 0;
        }

        @Override
        public EaseChatRow getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
            if (message.getType() == EMMessage.Type.TXT) {
                // 语音通话、视频通话
				/*if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false) ||
						message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)){
					//ChatRowVoiceCall为一个继承自EaseChatRow的类
					return new ChatRowVoiceCall(getActivity(), message, position, adapter);
				}*/
            }
            return null;
        }
    }
	/*private void initView() {
		text_title = (TextView) findViewById(R.id.text_title);
		btn_backaccount = (TextView) findViewById(R.id.btn_backaccount);

		Bundle mBundle = this.getIntent().getExtras();
		String other_name = mBundle.getString("other_name");
		if (other_name != null)
			text_title.setText(other_name);
		else
			text_title.setText("");
		chat_send = (ImageView) findViewById(R.id.send);
		send_content = (EditText) findViewById(send_content);
		mListView = (ListView) findViewById(R.id.list_chat);
		mListView.setBackgroundColor(Color.parseColor("#ffffff"));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_backaccount:
			break;
		case R.id.send:
			String myword = (send_content.getText() + "").toString();
			if (myword.length() == 0)
				return;
			send_content.setText("");
			addTexttoList(myword, ME);
			Calendar c=Calendar.getInstance();
			myword+="#"+c.get(Calendar.HOUR_OF_DAY)+"#"+c.get(Calendar.MINUTE);
			// 更新数据列表
			mAdapter.notifyDataSetChanged();
			mListView.setSelection(mHashmap.size() - 1);
			break;
		default:
			break;
		}
	}

	private void addTexttoList(String text, int who) {
		mMap = new HashMap<String, Object>();
		mMap.put("person", who);
		mMap.put("head", who == ME ? R.drawable.btn_my_pre
				: R.drawable.touxiang);
		mMap.put("content", text);
		mHashmap.add(mMap);
	}

	class MyChatAdapter extends BaseAdapter {
		private Context context = null;
		private ArrayList<HashMap<String, Object>> mHashMap = null;
		private int[] layout;
		private String[] from;
		private int[] to;

		private Intent mIntent;

		public MyChatAdapter(Context context,
				ArrayList<HashMap<String, Object>> mHashMap, int[] layout,
				String[] from, int[] to) {
			super();
			this.context = context;
			this.mHashMap = mHashMap;
			this.layout = layout;
			this.from = from;
			this.to = to;
		}

		class ViewHolder {
			public ImageView imageView = null;
			public TextView textView = null;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			final int who = (Integer) mHashMap.get(position).get("person");
			convertView = LayoutInflater.from(context).inflate(
					layout[who == ME ? 0 : 1], null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView
					.findViewById(to[who * 2 + 0]);
			holder.textView = (TextView) convertView
					.findViewById(to[who * 2 + 1]);
			holder.imageView.setBackgroundResource((Integer) mHashMap.get(
					position).get(from[0]));
			holder.textView.setText(mHashMap.get(position).get(from[1])
					.toString());

			holder.imageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					showInfo(who);
				}
			});
			return convertView;
		}
		
		// write SDCard
	    private void writeFileSdcardFile(String fileName, String writeStr) throws IOException {
	        try {
	            
	            FileOutputStream fout = new FileOutputStream(fileName);
	            byte[] bytes = writeStr.getBytes();
	            
	            fout.write(bytes);
	            fout.close();
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}*/
}
