package com.example.freechat.audio;

import java.io.IOException;

import android.media.MediaRecorder;
import android.util.Log;

public class FCAudioRecorder {

	private static final String LOG_TAG = "AudioRecorder";

	private MediaRecorder mRecorder;

	public FCAudioRecorder() {
		
	}

	public void startRecord(String FileName) {
		// TODO Auto-generated method stub
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setOutputFile(FileName);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		try {
			mRecorder.prepare();
			
		} catch (IOException e) {
			Log.e(LOG_TAG, "prepare() failed");
		}
		mRecorder.start();
	}

	public void stopRecord() {
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
	}

}
