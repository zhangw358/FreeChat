package com.example.freechat.audio;

import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.Toast;

public class FCAudioPlayer {
	private static final String LOG_TAG = "AudioPlayer";
	
	private MediaPlayer mPlayer;
	private Context mContext;
	
	public FCAudioPlayer(Context context) {
		mContext = context;
	}
	
	public void play (String fileName) {
		mPlayer = new MediaPlayer();
		try {
			mPlayer.setDataSource(fileName);
			mPlayer.prepare();
			mPlayer.start();
		} catch (IOException e) {
			Toast.makeText(mContext, "play failed!", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void release() {
		mPlayer.release();
		mPlayer = null;
	}
}
