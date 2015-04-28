package com.example.freechat;

import com.example.freechat.aidl.AIDLChatActivity;
import com.example.freechat.aidl.AIDLPushService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FCTChatActivity extends Activity {

	private Button mButton;
	private TextView mTextView;

	private AIDLChatActivity.Stub mCallback = new AIDLChatActivity.Stub() {

		@Override
		public void onNewMessageReceived(String from, String to, int timeStamp,
				String content) throws RemoteException {
			Toast.makeText(FCTChatActivity.this, "new Message !",
					Toast.LENGTH_SHORT).show();
			mTextView.setText("new Message");
		}

		@Override
		public void onMessageSendFinished(String from, String to,
				int timeStamp, String content) throws RemoteException {
			Toast.makeText(FCTChatActivity.this, "send successfully!",
					Toast.LENGTH_SHORT).show();
			mTextView.setText("send successfully");
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
		bindMyPushService();
	}

	private void initWidgets() {
		mButton = (Button) findViewById(R.id.button1);
		mTextView = (TextView) findViewById(R.id.textView1);

		mButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					mPushService.sendMessage("Halfish", "Zhang Wei", 10086,
							"Wow, we can chat now");
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void bindMyPushService() {
		Intent intent = new Intent(FCTChatActivity.this, FCPushService.class);
		bindService(intent, mConnection, BIND_AUTO_CREATE);
		startService(intent);
	}

}
