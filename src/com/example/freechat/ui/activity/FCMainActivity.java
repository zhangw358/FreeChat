package com.example.freechat.ui.activity;

import org.json.JSONArray;
import org.json.JSONException;

import com.example.freechat.FCConfigure;
import com.example.freechat.FCPushService;
import com.example.freechat.R;
import com.example.freechat.aidl.AIDLChatActivity;
import com.example.freechat.aidl.AIDLPushService;
import com.example.freechat.ui.FCActionBarActivity;
import com.example.freechat.ui.FCFriend;
import com.example.freechat.ui.FCFriendFragment;
import com.example.freechat.ui.FCSessionFragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;

public class FCMainActivity extends FCActionBarActivity {

	protected static final int NEW_MESSAGE = 1000;
	private Fragment[] m_fragments;
	private FragmentManager m_fragmentManager;
	private FragmentTransaction m_fragmentTransaction;
	private RadioGroup m_bottomGroup;
	private Handler m_HeartBeathandler;
	private Handler m_msgHandler;
	private String m_message = "";

	private static String LOG_TAG = FCMainActivity.class.getName();

	// private RadioButton m_left_RadioButton;
	// private RadioButton m_right_RadioButton;

	private AIDLChatActivity.Stub mCallback = new AIDLChatActivity.Stub() {

		@Override
		public void onMessageSendFinished(boolean flag) throws RemoteException {
			Log.v(LOG_TAG, "message finished!");
		}

		@Override
		public void onNewMessageReceived(char type, byte[] message)
				throws RemoteException {
			switch (type) {
			case 'a':
				String msgString = new String(message);
				if (!m_message.equals(msgString)) {
					m_message = msgString;
					m_msgHandler.sendEmptyMessage(NEW_MESSAGE);
				}
				Log.v(LOG_TAG, "new message: " + msgString);
				break;

			case 'b':
				break;

			case 'c':
				break;

			default:
				break;
			}
		}
	};

	private AIDLPushService mPushService;

	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mPushService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {

			mPushService = AIDLPushService.Stub.asInterface(service);

			Log.v(LOG_TAG, "get mPushService");

			try {
				mPushService.registerToPushService(mCallback);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_fcmain);
		initFragment();
		setFragIndicator();
		initMsgHandler();
		bindMyPushService();
		startHeartBeat();
		
		
		// test TODO
		FCFriend friend = new FCFriend("Halfish", "0");
		((FCFriendFragment) m_fragments[0])
				.addNewOnlineFriend(friend);

		Log.v(LOG_TAG, "onCreate");
	}

	private void initFragment() {
		m_fragments = new Fragment[2];
		m_fragmentManager = getFragmentManager();
		m_fragments[0] = m_fragmentManager
				.findFragmentById(R.id.fragment_friend);
		m_fragments[1] = m_fragmentManager
				.findFragmentById(R.id.fragment_session);
		m_fragmentTransaction = m_fragmentManager.beginTransaction()
				.hide(m_fragments[0]).hide(m_fragments[1]);
		m_fragmentTransaction.show(m_fragments[0]).commit();
		setActionBarCenterTitle("Friends");

	}

	private void setFragIndicator() {
		m_bottomGroup = (RadioGroup) findViewById(R.id.rg_bottomGroup);
		// m_left_RadioButton = (RadioButton) findViewById(R.id.rb_left);
		// m_right_RadioButton = (RadioButton) findViewById(R.id.rb_right);

		m_bottomGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						m_fragmentTransaction = m_fragmentManager
								.beginTransaction().hide(m_fragments[0])
								.hide(m_fragments[1]);
						switch (checkedId) {
						case R.id.rb_left:
							m_fragmentTransaction.show(m_fragments[0]).commit();
							setActionBarCenterTitle("Friends");
							break;

						case R.id.rb_right:
							m_fragmentTransaction.show(m_fragments[1]).commit();
							setActionBarCenterTitle("Session");
							break;
						}
					}
				});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.fcmain, menu);
		return true;
	};

	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case android.R.id.home:
			AlertDialog isExit = new AlertDialog.Builder(this).create();
			isExit.setTitle("系统提示");
			isExit.setMessage("确定要注销吗？");
			isExit.setButton2("确认", listener);
			isExit.setButton("取消", listener);
			isExit.show();
			break;

		case R.id.action_refresh:

			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_NEGATIVE:// "确认"按钮退出程序
				Intent intent = new Intent(FCMainActivity.this,
						FCLoginActivity.class);
				startActivity(intent);
				finish();
				break;
			case AlertDialog.BUTTON_POSITIVE:// "取消"第二个按钮取消对话框
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onResume() {
		Log.v(LOG_TAG, "onResume");

		((FCSessionFragment) m_fragments[1]).reloadData();
		startHeartBeat();
		super.onResume();
	}

	@Override
	protected void onStop() {
		Log.v(LOG_TAG, "onStop");

		// unBindMyPushService();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		sendStateMessage("offline_request");

		super.onDestroy();
	}

	private void initMsgHandler() {
		m_msgHandler = new Handler(getMainLooper()) {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case NEW_MESSAGE:
					parseMessageFromServer();
					break;

				default:
					break;
				}

			}
		};
	}

	private void parseMessageFromServer() {

		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(m_message);

			String stateString = jsonArray.getString(0);
			if (stateString.equals("online_names")
					|| stateString.equals("online_notification")) {
				// online friends or new friend
				for (int i = 1; i < jsonArray.length(); i++) {

					FCFriend friend = new FCFriend(jsonArray.getString(i), "0");
					((FCFriendFragment) m_fragments[0])
							.addNewOnlineFriend(friend);
				}

			} else if (stateString.equals("offline_notification")) {
				// online friends or new friend
				for (int i = 1; i < jsonArray.length(); i++) {

					FCFriend friend = new FCFriend(jsonArray.getString(i), "0");
					((FCFriendFragment) m_fragments[0])
							.removeOnlineFriend(friend);
				}

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void startHeartBeat() {
		m_HeartBeathandler = new Handler();
		m_HeartBeathandler.postDelayed(new Runnable() {
			public void run() {
				sendStateMessage("upload_name");
				// m_HeartBeathandler.postDelayed(this, 5000);
			}
		}, 1000);
	}

	private void bindMyPushService() {
		Log.v(LOG_TAG, "bindMyPushService");

		Intent intent = new Intent(FCMainActivity.this, FCPushService.class);
		bindService(intent, mConnection, BIND_AUTO_CREATE);
	}

	// just tell server I am online or offLine
	private void sendStateMessage(String action) {
		
		JSONArray jsonArray = new JSONArray();
		try {
			jsonArray.put(0, action);
			jsonArray.put(1, FCConfigure.myName);
			byte[] b = jsonArray.toString().getBytes();
			mPushService.sendMessage('a', b);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
