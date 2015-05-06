package com.example.freechat.ui.activity;

import com.example.freechat.R;
import com.example.freechat.audio.FCAudioRecorder;
import com.example.freechat.storage.FCFileHelper;
import com.example.freechat.ui.FCActionBarActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class FCAudioActivity extends FCActionBarActivity {

	private TextView showPath;
	private Button sendAudioButton;
	private Button cancelButton;

	private FCAudioRecorder mRecorder;

	private FCFileHelper mFileHelper;
	private String mFileName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio);
		initUI();
		setActionBarCenterTitle("AudioRecord");
		mFileHelper = new FCFileHelper(this);
		mFileName = mFileHelper.generateFileName();
		showPath.setText(mFileName);

		// start record
		mRecorder.startRecord(mFileName);
		

	}

	private void initUI() {
		sendAudioButton = (Button) findViewById(R.id.bt_send_audio);
		cancelButton = (Button) findViewById(R.id.bt_cancel_audio);

		showPath = (TextView) findViewById(R.id.textView1);

		sendAudioButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mRecorder.stopRecord();
			     
				Intent intent = new Intent();
				intent.putExtra("content", mFileName);
				setResult(RESULT_OK, intent);
				finish();
			}
		});

		cancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

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
