package com.example.freechat.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.text.style.LineHeightSpan.WithDensity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;

import com.example.freechat.FCPushService;
import com.example.freechat.R;
import com.example.freechat.R.string;
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
	
    private FCMessageAdapter m_messageAdapter;
    private List<FCMessage> m_messageList;    

    private String m_userid;
    
    private DatabaseHandler m_dbhandler;

    private boolean inRecorderMode = false;

    private AIDLChatActivity.Stub mCallback = new AIDLChatActivity.Stub() {

		@Override
		public void onMessageSendFinished(String message)
				throws RemoteException {
			
		}

		@Override
		public void onNewMessageReceived(String message) throws RemoteException {
			
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
        setContentView(R.layout.activity_chat);
        initUI();
        initAudio();
        initChatListFromDB();
        bindMyPushService();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
    	super.onActivityResult(requestCode, resultCode, data);
    	
    	if(requestCode == FCChatActivity.PIC_REQUEST && resultCode == RESULT_OK) {
    		String content = data.getStringExtra("content");
    		FCMessage msg = new FCMessage(content, FCMessage.SEND_MESSAGE, FCMessage.TYPE_PIC);
    		updateMessageList(msg);
    	}
    	
//    	if(requestCode == FCChatActivity.AUD_REQUEST && resultCode == RESULT_OK) {
//    		String content = data.getStringExtra("content");
//    		FCMessage msg = new FCMessage(content, FCMessage.SEND_MESSAGE, FCMessage.TYPE_AUD);
//    		updateMessageList(msg);
//    	}
    	
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
            	Intent intent = new Intent(FCChatActivity.this, FCPictureActivity.class);
            	startActivityForResult(intent, PIC_REQUEST);
            }
        });
        
        m_sendAudioButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!inRecorderMode) {
					InputMethodManager imm = (InputMethodManager)
							getSystemService(FCChatActivity.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(m_sendAudioButton.getWindowToken(), 0);

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
        	@Override
        	public boolean onTouch(View view, MotionEvent event) {
        		switch(event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					m_RecorderButton.setBackgroundColor(getResources().getColor(R.color.Steelblue));
					m_RecorderButton.setText(R.string.release_to_send);
					m_RecorderButton.setTextColor(getResources().getColor(R.color.Clearwhite));
					//show popup
					m_RecorderPopupView.setVisibility(View.VISIBLE);

					Log.e("FCChatActivity onTouch()", "set popup visible");
					//start recording
					filepath = m_FileHelper.generateFileName();
					m_FCAudioRecorder.startRecord(filepath);
					Log.e("FCChatActivity onTouch()", "start recording, path:" + filepath);
					m_RecorderHandler.postDelayed(m_PollTask, POLL_INTERVAL);
					break;

				case MotionEvent.ACTION_UP:
					m_RecorderButton.setBackgroundColor(getResources().getColor(R.color.Skyblue));
					m_RecorderButton.setText(R.string.hold_to_talk);
					m_RecorderButton.setTextColor(getResources().getColor(R.color.Clearwhite));
					//hide popup
					m_RecorderPopupView.setVisibility(View.GONE);
					//stop recording
					m_RecorderHandler.removeCallbacks(m_PollTask);
					m_FCAudioRecorder.stopRecord();
					m_RecordVolumeView.setImageResource(R.drawable.amp1);
					//make message
					FCMessage msg = new FCMessage(filepath, FCMessage.SEND_MESSAGE, FCMessage.TYPE_AUD);
		    		updateMessageList(msg);
					break;

				case MotionEvent.ACTION_MOVE:
					// TODO cancel sending
				default:
					break;
				}
				return false;
			}
		});

        m_chatListView.setOnItemClickListener(new OnItemClickListener() {
        	
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		int type = m_messageList.get(position).getMessageType();
        		String content = m_messageList.get(position).getContent();
        		switch (type) {
				case FCMessage.TYPE_TXT:
					Toast.makeText(getApplicationContext(), "txt", Toast.LENGTH_SHORT).show();
					break;
				case FCMessage.TYPE_PIC:
					//TODO: show picture alert
					Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT).show();
					break;
				case FCMessage.TYPE_AUD:
					m_player.play(content);
					//TODO: show audio alert
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
            	FCMessage message = new FCMessage(sendInfo, FCMessage.SEND_MESSAGE);           	
                updateMessageList(message);           
            }
        });
    }
    
    private void updateMessageList(FCMessage msg) {
    	m_messageList.add(msg);
        m_messageAdapter.notifyDataSetChanged();
        m_chatListView.setSelection(m_messageList.size()-1);
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
		startService(intent);
	}
	
	private Runnable m_PollTask = new Runnable(){
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
		case 0: case 1:
			m_RecordVolumeView.setImageResource(R.drawable.amp1);
			break;
		case 2: case 3:
			m_RecordVolumeView.setImageResource(R.drawable.amp2);
			break;
		case 4: case 5:
			m_RecordVolumeView.setImageResource(R.drawable.amp3);
			break;
		case 6: case 7:
			m_RecordVolumeView.setImageResource(R.drawable.amp4);
			break;
		case 8: case 9:
			m_RecordVolumeView.setImageResource(R.drawable.amp5);
			break;
		case 10: case 11:
			m_RecordVolumeView.setImageResource(R.drawable.amp6);
			break;
		default:
			m_RecordVolumeView.setImageResource(R.drawable.amp7);
			break;
		}
	}
}
