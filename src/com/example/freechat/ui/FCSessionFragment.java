package com.example.freechat.ui;

import java.util.ArrayList;
import java.util.List;

import com.example.freechat.R;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class FCSessionFragment extends Fragment {
	
	private FCSessionAdapter m_adapter;
	private ListView m_sessionListView;
	private List<FCSession> m_sessionList;
	
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
    
    void initUI() {
    	m_sessionList = new ArrayList<FCSession>();
        m_sessionListView = (ListView) rootView.findViewById(R.id.lv_session_list);
        m_sessionList.add(new FCSession("testSession", "123"));
        m_adapter = new FCSessionAdapter(getActivity().getApplicationContext(), m_sessionList);
        m_sessionListView.setAdapter(m_adapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
