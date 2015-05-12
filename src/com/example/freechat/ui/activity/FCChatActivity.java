package com.example.freechat.ui.activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.example.freechat.FCMessageUtil;
import com.example.freechat.FCPushService;
import com.example.freechat.R;
import com.example.freechat.aidl.AIDLChatActivity;
import com.example.freechat.aidl.AIDLPushService;
import com.example.freechat.audio.FCAudioPlayer;
import com.example.freechat.audio.FCAudioRecorder;
import com.example.freechat.storage.DatabaseHandler;
import com.example.freechat.storage.FCFileHelper;
import com.example.freechat.ui.FCActionBarActivity;
import com.example.freechat.ui.FCMessage;
import com.example.freechat.ui.FCMessageAdapter;

public class FCChatActivity extends FCActionBarActivity {
	public static final int PIC_REQUEST = 1;
	public static final int AUD_REQUEST = 2;

	private ListView m_chatListView;
	private Button m_sendTxtButton;
	private Button m_sendPictureButton;
	private Button m_sendAudioButton;
	private EditText m_sendMessgeText;

	private Button m_RecorderButton;
	private View m_RecorderPopupView;
	private ImageView m_RecordVolumeView;
	private FCAudioPlayer m_player;
	private FCAudioRecorder m_FCAudioRecorder;
	private FCFileHelper m_FileHelper;
	private String filepath;
	private Handler m_RecorderHandler = new Handler();
	private static final int POLL_INTERVAL = 100;

	private static final String LOG_TAG = FCChatActivity.class.getName();

	private FCMessageAdapter m_messageAdapter;
	private List<FCMessage> m_messageList;

	private String m_userid;

	private DatabaseHandler m_dbhandler;

	private boolean inRecorderMode = false;

	private AIDLChatActivity.Stub mCallback = new AIDLChatActivity.Stub() {

		@Override
		public void onMessageSendFinished(boolean flag) throws RemoteException {
			if (flag) {
				Log.v(LOG_TAG, "message send successfully");
			}
		}

		@Override
		public void onNewMessageReceived(char type, byte[] message)
				throws RemoteException {
			switch (type) {
			case 'a':
				String msg = new String(message);
				m_newMessage = msg;
				m_handler.sendEmptyMessage(NEW_MESSAGE);
				Log.v(LOG_TAG, "new message: " + message);
				break;

			case 'b':
				m_bitmapBytes = new byte[message.length];
				m_bitmapBytes = message;

				m_handler.sendEmptyMessage(NEW_PIC_MESSAGE);
				Log.v(LOG_TAG, "new picture get!");
				break;

			case 'c':
				m_audioBytes = new byte[message.length];
				m_audioBytes = message;

				m_handler.sendEmptyMessage(NEW_AUDIO_MESSAGE);
				Log.v(LOG_TAG, "new audio get!");
				break;

			default:
				break;
			}
		}

	};

	private static final int NEW_MESSAGE = 1000;
	private static final int NEW_PIC_MESSAGE = 1001;
	private static final int NEW_AUDIO_MESSAGE = 1002;

	private String m_newMessage = "";
	private byte[] m_bitmapBytes = null;
	private byte[] m_audioBytes = null;

	private Handler m_handler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NEW_MESSAGE:
				updateNewTextMessage();
				break;

			case NEW_PIC_MESSAGE:
				updateNewPictureMessage();
				break;

			case NEW_AUDIO_MESSAGE:
				updateNewAudioMessage();
				break;

			default:
				break;
			}
		}
	};

	private void updateNewTextMessage() {
		if (m_newMessage.equals("")) {
			return;
		}
		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(m_newMessage);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		FCMessage message = FCMessageUtil.jsonArrayToMessage(jsonArray);
		if (message != null) {
			updateMessageList(message);
		}
	}

	private void updateNewPictureMessage() {
		if (m_bitmapBytes == null) {
			return;
		}

		FCFileHelper fileHelper = new FCFileHelper(getBaseContext());
		String filename = fileHelper.generateFileName();
		fileHelper.writeToFile(filename, m_bitmapBytes);

		FCMessage message = new FCMessage(filename, FCMessage.RECEIVE_MESSAGE,
				FCMessage.TYPE_PIC);
		updateMessageList(message);
	}

	private void updateNewAudioMessage() {
		if (m_audioBytes == null) {
			return;
		}

		FCFileHelper fileHelper = new FCFileHelper(getBaseContext());
		String filename = fileHelper.generateFileName();
		fileHelper.writeToFile(filename, m_audioBytes);

		FCMessage message = new FCMessage(filename, FCMessage.RECEIVE_MESSAGE,
				FCMessage.TYPE_AUD);
		updateMessageList(message);
	}

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
		setContentView(R.layout.activity_chat);
		initUI();
		initAudio();
		initChatListFromDB();
		bindMyPushService();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(mConnection);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == FCChatActivity.PIC_REQUEST
				&& resultCode == RESULT_OK) {
			String content = data.getStringExtra("content");
			if (sendFileMessage('b', content)) {
				FCMessage msg = new FCMessage(content, FCMessage.SEND_MESSAGE,
						FCMessage.TYPE_PIC);
				updateMessageList(msg);
			}
		}
	}

	private boolean sendFileMessage(char type, String path) {

		int length;
		byte[] buffer = null;
		try {
			FileInputStream fin = new FileInputStream(path);
			length = fin.available();
			buffer = new byte[length];
			fin.read(buffer);
			fin.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			mPushService.sendMessage(type, buffer);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return true;
	}

	private void initUI() {

		m_userid = getIntent().getExtras().getString("userid");
		setActionBarCenterTitle("chat with " + m_userid);

		m_chatListView = (ListView) findViewById(R.id.lv_chat_list);
		m_sendPictureButton = (Button) findViewById(R.id.bt_send_picture);
		m_sendTxtButton = (Button) findViewById(R.id.bt_send_txt);
		m_sendAudioButton = (Button) findViewById(R.id.bt_send_audio);
		m_sendMessgeText = (EditText) findViewById(R.id.et_chatinfo);

		m_RecorderButton = (Button) findViewById(R.id.recorder_button);
		m_RecorderPopupView = (View) findViewById(R.id.voice_record_popup);
		m_RecordVolumeView = (ImageView) findViewById(R.id.voice_record_volume);

		m_sendPictureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FCChatActivity.this,
						FCPictureActivity.class);
				startActivityForResult(intent, PIC_REQUEST);
			}
		});

		m_sendAudioButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!inRecorderMode) {
					InputMethodManager imm = (InputMethodManager) getSystemService(FCChatActivity.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(
							m_sendAudioButton.getWindowToken(), 0);

					m_sendTxtButton.setVisibility(View.GONE);
					m_sendMessgeText.setVisibility(View.GONE);
					m_RecorderButton.setVisibility(View.VISIBLE);
					m_sendAudioButton.setText(R.string.text);
					inRecorderMode = true;
				} else {
					m_RecorderButton.setVisibility(View.GONE);
					m_sendTxtButton.setVisibility(View.VISIBLE);
					m_sendMessgeText.setVisibility(View.VISIBLE);
					m_sendAudioButton.setText(R.string.audio);
					inRecorderMode = false;
				}
			}
		});

		m_RecorderButton.setOnTouchListener(new View.OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					m_RecorderButton.setBackgroundColor(getResources()
							.getColor(R.color.Steelblue));
					m_RecorderButton.setText(R.string.release_to_send);
					m_RecorderButton.setTextColor(getResources().getColor(
							R.color.Clearwhite));
					// show popup
					m_RecorderPopupView.setVisibility(View.VISIBLE);

					Log.e("FCChatActivity onTouch()", "set popup visible");
					// start recording
					filepath = m_FileHelper.generateFileName();
					m_FCAudioRecorder.startRecord(filepath);
					Log.e("FCChatActivity onTouch()", "start recording, path:"
							+ filepath);
					m_RecorderHandler.postDelayed(m_PollTask, POLL_INTERVAL);
					break;

				case MotionEvent.ACTION_UP:
					m_RecorderButton.setBackgroundColor(getResources()
							.getColor(R.color.Skyblue));
					m_RecorderButton.setText(R.string.hold_to_talk);
					m_RecorderButton.setTextColor(getResources().getColor(
							R.color.Clearwhite));
					// hide popup
					m_RecorderPopupView.setVisibility(View.GONE);
					// stop recording
					m_RecorderHandler.removeCallbacks(m_PollTask);
					m_FCAudioRecorder.stopRecord();
					m_RecordVolumeView.setImageResource(R.drawable.amp1);
					
					if (sendFileMessage('c', filepath)) {
						// make message
						FCMessage msg = new FCMessage(filepath,
								FCMessage.SEND_MESSAGE, FCMessage.TYPE_AUD);
						updateMessageList(msg);
					}
					
					break;

				case MotionEvent.ACTION_MOVE:
					// cancel sending
				default:
					break;
				}
				return false;
			}
		});

		m_chatListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int type = m_messageList.get(position).getMessageType();
				String content = m_messageList.get(position).getContent();
				switch (type) {
				case FCMessage.TYPE_TXT:
					Toast.makeText(getApplicationContext(), "txt",
							Toast.LENGTH_SHORT).show();
					break;
				case FCMessage.TYPE_PIC:
					//TODO: show picture alert
					Intent intent = new Intent(FCChatActivity.this, FCBrowseActivity.class);
					intent.putExtra("address", content);
					startActivity(intent);
					break;
				case FCMessage.TYPE_AUD:
					m_player.play(content);
					// show audio alert
					break;
				default:
					break;
				}

			}
		});

		m_sendTxtButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String sendInfo = m_sendMessgeText.getText().toString();
				m_sendMessgeText.setText("");
				FCMessage message = new FCMessage(sendInfo,
						FCMessage.SEND_MESSAGE);

				String msgString = FCMessageUtil.messageToJson(message,
						m_userid).toString();
				sendMessageToServer(msgString);

				updateMessageList(message);
			}
		});
	}

	private void sendMessageToServer(String msg) {
		try {
			mPushService.sendMessage('a', msg.getBytes());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void updateMessageList(FCMessage msg) {
		m_messageList.add(msg);
		m_messageAdapter.notifyDataSetChanged();
		m_chatListView.setSelection(m_messageList.size() - 1);
		m_dbhandler.insertMessage(m_userid, msg);
	}

	private void initChatListFromDB() {
		m_messageList = new ArrayList<FCMessage>();
		m_messageAdapter = new FCMessageAdapter(this, m_messageList);
		m_dbhandler = new DatabaseHandler(getApplicationContext());
		m_messageList.addAll(m_dbhandler.selectMessageByName(m_userid));
		m_chatListView.setAdapter(m_messageAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.fcchat, menu);
		return true;
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void bindMyPushService() {
		Intent intent = new Intent(FCChatActivity.this, FCPushService.class);
		bindService(intent, mConnection, BIND_AUTO_CREATE);
	}

	private Runnable m_PollTask = new Runnable() {
		public void run() {
			double amp = m_FCAudioRecorder.getAmplitude();
			updateDisplay(amp);
			m_RecorderHandler.postDelayed(m_PollTask, POLL_INTERVAL);
		}
	};

	private void initAudio() {
		m_player = new FCAudioPlayer(this);
		m_FCAudioRecorder = new FCAudioRecorder();
		m_FileHelper = new FCFileHelper(this);
	}

	private void updateDisplay(double signalEMA) {
		switch ((int) signalEMA) {
		case 0:
		case 1:
			m_RecordVolumeView.setImageResource(R.drawable.amp1);
			break;
		case 2:
		case 3:
			m_RecordVolumeView.setImageResource(R.drawable.amp2);
			break;
		case 4:
		case 5:
			m_RecordVolumeView.setImageResource(R.drawable.amp3);
			break;
		case 6:
		case 7:
			m_RecordVolumeView.setImageResource(R.drawable.amp4);
			break;
		case 8:
		case 9:
			m_RecordVolumeView.setImageResource(R.drawable.amp5);
			break;
		case 10:
		case 11:
			m_RecordVolumeView.setImageResource(R.drawable.amp6);
			break;
		default:
			m_RecordVolumeView.setImageResource(R.drawable.amp7);
			break;
		}
	}
}
