package com.example.freechat.audio;

import java.io.IOException;

import android.media.MediaRecorder;
import android.util.Log;

public class FCAudioRecorder {

	private static final String LOG_TAG = "AudioRecorder";

	private MediaRecorder m_Recorder;

	public void startRecord(String FilePath) {
		// TODO Auto-generated method stub
		if (m_Recorder == null) {
			m_Recorder = new MediaRecorder();
			m_Recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			m_Recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			m_Recorder.setOutputFile(FilePath);
			m_Recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			try {
				m_Recorder.prepare();
				m_Recorder.start();
			} catch (IllegalStateException e) {
				// TODO
				Log.e(LOG_TAG, "prepare() failed");
			} catch (IOException e) {
				// TODO
				Log.e(LOG_TAG, "prepare() failed");
			}
		}
	}

	public void stopRecord() {
		if (m_Recorder != null) {
			m_Recorder.stop();
			m_Recorder.release();
			m_Recorder = null;
		}
	}

	public double getAmplitude() {
		if (m_Recorder != null)
			return (m_Recorder.getMaxAmplitude() / 2700.0);
		else
			return 0;
	}
}
