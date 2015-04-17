package com.example.freechat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class FCChatActivity extends FCActionBarActivity {

    private ListView m_chatListView;
    private Button m_sendTxtButton;
    private Button m_sendPictureButton;

    private FCMessageAdapter m_messageAdapter;
    private List<FCMessage> m_messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initUI();
        initChatList();
    }

    private void initUI() {
        m_chatListView = (ListView) findViewById(R.id.lv_chat_list);
        m_sendPictureButton = (Button) findViewById(R.id.bt_send_picture);
        m_sendTxtButton = (Button) findViewById(R.id.bt_send_txt);

        m_sendPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_messageList.add(new FCMessage("I'm receiver", 1));
                m_messageAdapter.notifyDataSetChanged();
                m_chatListView.setSelection(m_messageList.size()-1);
            }
        });

        m_sendTxtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_messageList.add(new FCMessage("I'm sender", 0));
                m_messageAdapter.notifyDataSetChanged();
                m_chatListView.setSelection(m_messageList.size()-1);

            }
        });
    }

    private void initChatList() {
        m_messageList = new ArrayList<FCMessage>();
        m_messageAdapter = new FCMessageAdapter(this, m_messageList);
        m_chatListView.setAdapter(m_messageAdapter);

    }


}
