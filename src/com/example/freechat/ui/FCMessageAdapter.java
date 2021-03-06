package com.example.freechat.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import com.example.freechat.R;

/**
 * Created by zhangwei on 15-4-17.
 */
public class FCMessageAdapter extends BaseAdapter{

    private Context m_context;
    private List<FCMessage> m_messageList;
    private int [] m_layout = {R.layout.chat_item_send, R.layout.chat_item_receive};

    public FCMessageAdapter(Context context, List<FCMessage> messageList) {
        m_context = context;
        m_messageList = messageList;
    }

    @Override
    public int getCount() {
        return m_messageList.size();
    }

    @Override
    public Object getItem(int position) {
        return m_messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int messageAttr = m_messageList.get(position).getMessageAttr();
        convertView = LayoutInflater.from(m_context).inflate(m_layout[messageAttr], null);

        TextView textView = null;

        switch (messageAttr) {
            case FCMessage.RECEIVE_MESSAGE :
                textView = (TextView) convertView.findViewById(R.id.tv_message_receive);
                break;
            case FCMessage.SEND_MESSAGE :
                textView = (TextView) convertView.findViewById(R.id.tv_message_send);
                break;
        }

        textView.setText(m_messageList.get(position).setToListView());

        return convertView;
    }
}
