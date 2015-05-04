package com.example.freechat.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.style.LineHeightSpan.WithDensity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import com.example.freechat.FCPushService;
import com.example.freechat.R;
import com.example.freechat.aidl.AIDLChatActivity;
import com.example.freechat.aidl.AIDLPushService;
import com.example.freechat.storage.DatabaseHandler;
import com.example.freechat.ui.FCActionBarActivity;
import com.example.freechat.ui.FCMessage;
import com.example.freechat.ui.FCMessageAdapter;

public class FCChatActivity extends FCActionBarActivity {

    private ListView m_chatListView;
    private Button m_sendTxtButton;
    private Button m_sendPictureButton;
    private Button m_sendAudioButton;
    private EditText m_sendMessgeText;
    
    private FCMessageAdapter m_messageAdapter;
    private List<FCMessage> m_messageList;    

    private String m_userid;
    
    private DatabaseHandler m_dbhandler;
    
    private AIDLChatActivity.Stub mCallback = new AIDLChatActivity.Stub() {

		@Override
		public void onNewMessageReceived(String from, String to, int timeStamp,
				String content) throws RemoteException {
			
		}

		@Override
		public void onMessageSendFinished(String from, String to,
				int timeStamp, String content) throws RemoteException {
			
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
        initChatListFromDB();
        bindMyPushService();
    }

    private void initUI() {
    	
    	m_userid = getIntent().getExtras().getString("userid");
    	setActionBarCenterTitle("chat with " + m_userid);
    	
        m_chatListView = (ListView) findViewById(R.id.lv_chat_list);
        m_sendPictureButton = (Button) findViewById(R.id.bt_send_picture);
        m_sendTxtButton = (Button) findViewById(R.id.bt_send_txt);
        m_sendAudioButton = (Button) findViewById(R.id.bt_send_audio);
        m_sendMessgeText = (EditText) findViewById(R.id.et_chatinfo);
        
        m_sendPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        
        m_sendAudioButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});

        m_sendTxtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	String sendInfo = m_sendMessgeText.getText().toString();
            	m_sendMessgeText.setText("");
            	FCMessage message = new FCMessage(sendInfo, FCMessage.SEND_MESSAGE);
            	
                m_messageList.add(message);
                m_messageAdapter.notifyDataSetChanged();
                m_chatListView.setSelection(m_messageList.size()-1);
                
                m_dbhandler.insertMessage(m_userid, message);
            }
        });
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
}
