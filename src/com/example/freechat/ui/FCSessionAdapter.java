package com.example.freechat.ui;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.freechat.R;

/**
 * Created by zhangwei on 15-4-17.
 */
public class FCSessionAdapter extends BaseAdapter {

	private Context m_context;
	private List<FCSession> m_sessionList;

	public FCSessionAdapter(Context context, List<FCSession> sessionList) {
		m_context = context;
		m_sessionList = sessionList;
	}

	@Override
	public int getCount() {
		return m_sessionList.size();
	}

	@Override
	public Object getItem(int position) {
		return m_sessionList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		convertView = LayoutInflater.from(m_context).inflate(R.layout.session_item, null);

		TextView textView = (TextView) convertView
				.findViewById(R.id.tv_session_name);
		textView.setText(m_sessionList.get(position).getTitle());

		return convertView;
	}
}
