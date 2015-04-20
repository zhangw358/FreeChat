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
public class FCFriendAdapter extends BaseAdapter {

        private Context m_context;
        private List<FCFriend> m_friendList;

        public FCFriendAdapter(Context context, List<FCFriend> messageList) {
            m_context = context;
            m_friendList = messageList;
        }

        @Override
        public int getCount() {
            return m_friendList.size();
        }

        @Override
        public Object getItem(int position) {
            return m_friendList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = LayoutInflater.from(m_context).inflate(R.layout.friend_item, null);

            TextView textView = (TextView) convertView.findViewById(R.id.tv_friend_name);
            textView.setText(m_friendList.get(position).getName());

            return convertView;
        }
}
