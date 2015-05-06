package com.example.freechat.ui.activity;

import com.example.freechat.R;
import com.example.freechat.ui.FCActionBarActivity;
import com.example.freechat.ui.FCSessionFragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;

public class FCMainActivity extends FCActionBarActivity {

	private Fragment[] m_fragments;
	private FragmentManager m_fragmentManager;
	private FragmentTransaction m_fragmentTransaction;
	private RadioGroup m_bottomGroup;

	// private RadioButton m_left_RadioButton;
	// private RadioButton m_right_RadioButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fcmain);
		initFragment();
		setFragIndicator();
	}

	private void initFragment() {
		m_fragments = new Fragment[2];
		m_fragmentManager = getFragmentManager();
		m_fragments[0] = m_fragmentManager
				.findFragmentById(R.id.fragment_friend);
		m_fragments[1] = m_fragmentManager
				.findFragmentById(R.id.fragment_session);
		m_fragmentTransaction = m_fragmentManager.beginTransaction()
				.hide(m_fragments[0]).hide(m_fragments[1]);
		m_fragmentTransaction.show(m_fragments[0]).commit();
		setActionBarCenterTitle("Friends");

	}

	private void setFragIndicator() {
		m_bottomGroup = (RadioGroup) findViewById(R.id.rg_bottomGroup);
		// m_left_RadioButton = (RadioButton) findViewById(R.id.rb_left);
		// m_right_RadioButton = (RadioButton) findViewById(R.id.rb_right);

		m_bottomGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						m_fragmentTransaction = m_fragmentManager
								.beginTransaction().hide(m_fragments[0])
								.hide(m_fragments[1]);
						switch (checkedId) {
						case R.id.rb_left:
							m_fragmentTransaction.show(m_fragments[0]).commit();
							setActionBarCenterTitle("Friends");
							break;

						case R.id.rb_right:
							m_fragmentTransaction.show(m_fragments[1]).commit();
							setActionBarCenterTitle("Session");
							break;
						}
					}
				});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.fcmain, menu);
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
			// finish();
			break;

		case R.id.action_refresh:

			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		Log.v("mainActivity", "onResume");
		super.onResume();
		((FCSessionFragment) m_fragments[1]).reloadData();
	}
}
