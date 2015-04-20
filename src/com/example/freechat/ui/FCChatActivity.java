package com.example.freechat.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import com.example.freechat.R;
import com.example.freechat.network.*;

public class FCChatActivity extends FCActionBarActivity {

    private ListView m_chatListView;
    private Button m_sendTxtButton;
    private Button m_sendPictureButton;
    private EditText m_sendMessgeText;
    
    private FCMessageAdapter m_messageAdapter;
    private List<FCMessage> m_messageList;
    
    private FCUDPReceiver m_UdpReceiver;
    private FCUDPSender m_UdpSender;
    private String m_IPAddress;
    private int m_PORT = 6650;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initUI();
        initChatList();
        initNetwork();
    }

    private void initUI() {
        m_chatListView = (ListView) findViewById(R.id.lv_chat_list);
        m_sendPictureButton = (Button) findViewById(R.id.bt_send_picture);
        m_sendTxtButton = (Button) findViewById(R.id.bt_send_txt);
        m_sendMessgeText = (EditText) findViewById(R.id.et_chatinfo);
        
        m_sendPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        m_sendTxtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	String sendInfo = m_sendMessgeText.getText().toString();
                m_messageList.add(new FCMessage(sendInfo, FCMessage.SEND_MESSAGE));
                
                m_messageAdapter.notifyDataSetChanged();
                m_chatListView.setSelection(m_messageList.size()-1);
                
                m_UdpSender.send(sendInfo.getBytes());
            }
        });
    }

    private void initChatList() {
        m_messageList = new ArrayList<FCMessage>();
        m_messageAdapter = new FCMessageAdapter(this, m_messageList);
        m_chatListView.setAdapter(m_messageAdapter);

    }

    private void initNetwork() {
    	m_IPAddress = FCNetwork.getIPv4Address();
    	m_UdpSender = new FCUDPSender(m_IPAddress, m_PORT);
    	m_UdpReceiver = new FCUDPReceiver(m_PORT);
    	m_UdpReceiver.setCallback(new FCUDPReceiver.OnReceiveCallback() {
			
			@Override
			public void onReceive(String ip, byte[] data) {
				// TODO Auto-generated method stub
				String getInfo = new String(data);
				m_messageList.add(new FCMessage("get"+getInfo, FCMessage.RECEIVE_MESSAGE));
				m_messageAdapter.notifyDataSetChanged();
                m_chatListView.setSelection(m_messageList.size()-1);
			}
		});
    	m_UdpReceiver.start();
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
}
