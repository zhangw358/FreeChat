package com.example.freechat.ui;

import java.util.ArrayList;
import java.util.List;

import com.example.freechat.R;
import com.example.freechat.ui.activity.FCChatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class FCFriendFragment extends Fragment {

    private ListView m_friendListView;
    private FCFriendAdapter m_adapter;
    private List<FCFriend> m_friendList;

    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        
       
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_friend, container, false);

        initUI();
        return rootView;
    }

    void initUI() {
    	m_friendListView = (ListView) rootView.findViewById(R.id.lv_friend_list);
        m_friendList = new ArrayList<FCFriend>();

        m_adapter = new FCFriendAdapter(getActivity().getApplicationContext(), m_friendList);
        m_friendListView.setAdapter(m_adapter);

        m_friendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), FCChatActivity.class);
                startActivity(intent);
            }
        });
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