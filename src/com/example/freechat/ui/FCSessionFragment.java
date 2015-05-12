package com.example.freechat.ui;

import java.util.ArrayList;
import java.util.List;

import com.example.freechat.R;
import com.example.freechat.storage.DatabaseHandler;
import com.example.freechat.ui.activity.FCChatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


public class FCSessionFragment extends Fragment {
	
	private FCSessionAdapter m_adapter;
	private ListView m_sessionListView;
	private List<FCSession> m_sessionList;
	
	private DatabaseHandler m_dbHandler;
	private Context m_context;
	
	private View rootView;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_fcsession, container, false);  
        initUI();

        return rootView;
    }
    
    public void initUI() {
    	m_sessionList = new ArrayList<FCSession>();
    	m_dbHandler = new DatabaseHandler(m_context);
    	
    	m_sessionList.addAll(m_dbHandler.selectAllSession());
        m_sessionListView = (ListView) rootView.findViewById(R.id.lv_session_list);
        m_adapter = new FCSessionAdapter(m_context, m_sessionList);
        m_sessionListView.setAdapter(m_adapter);
        
        m_sessionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), FCChatActivity.class);              
                intent.putExtra("userid", m_sessionList.get(position).getName());
                startActivity(intent);
            }
        });
    }
    
    public void reloadData() {
    	if (m_dbHandler != null && m_sessionList !=null) {
    		m_sessionList.clear();
    		m_sessionList.addAll(m_dbHandler.selectAllSession());
    	}
	}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        m_context = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
