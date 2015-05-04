package com.example.freechat;

import com.example.freechat.aidl.AIDLChatActivity;
import com.example.freechat.aidl.AIDLPushService;
import com.example.freechat.network.FCNetwork;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FCTChatActivity extends Activity {

	private static final int NEW_MESSAGE_RECEIVED = 1000;
	private static final int MESSAGE_SEND_SUCCESSFULLY = 1100;
	private Button mButton;
	private TextView mTextView;
	private Handler mHandler;

	private AIDLChatActivity.Stub mCallback = new AIDLChatActivity.Stub() {

		@Override
		public void onNewMessageReceived(String message) throws RemoteException {
			Log.e("ChatActivity onNewMessageReceived()", "new Message!"
					+ message);

			Bundle bundle = new Bundle();
			bundle.putString("message", message);
			Message msg = new Message();
			msg.setData(bundle);
			msg.what = NEW_MESSAGE_RECEIVED;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onMessageSendFinished(String message)
				throws RemoteException {
			Log.e("ChatActivity onNewMessageReceived()", "message send!"
					+ message);
			Bundle bundle = new Bundle();
			bundle.putString("message", message);
			Message msg = new Message();
			msg.setData(bundle);
			msg.what = MESSAGE_SEND_SUCCESSFULLY;
			mHandler.sendMessage(msg);
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
		setContentView(R.layout.activity_chat_test);

		initWidgets();
		initHandler();
		bindMyPushService();
		
	}

	private void initWidgets() {
		mButton = (Button) findViewById(R.id.button1);
		mTextView = (TextView) findViewById(R.id.textView1);

		mButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					mPushService.sendMessage("send to Server: Hello World!");
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});

		mTextView.setText("local IP addr: "
				+ FCNetwork.getIPv4Address());
	}

	@SuppressLint("HandlerLeak")
	private void initHandler() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case NEW_MESSAGE_RECEIVED:
					mTextView.setText("He said: "
							+ msg.getData().getString("message"));
					break;

				case MESSAGE_SEND_SUCCESSFULLY:
					mTextView.setText("I said: "
							+ msg.getData().getString("message"));
					break;

				default:
					break;
				}
			}
		};
	}

	private void bindMyPushService() {
		Intent intent = new Intent(FCTChatActivity.this, FCPushService.class);
		bindService(intent, mConnection, BIND_AUTO_CREATE);
		startService(intent);
	}

}
