package com.example.freechat.audio;

import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.Toast;

public class FCAudioPlayer {
	
	private MediaPlayer m_Player;
	private Context m_Context;
	
	public FCAudioPlayer(Context context) {
		m_Context = context;
	}
	
	public void play (String fileName) {
		m_Player = new MediaPlayer();
		try {
			m_Player.setDataSource(fileName);
			m_Player.prepare();
			m_Player.start();
		}  catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(m_Context, "play failed!", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void release() {
		m_Player.stop();
		m_Player.release();
		m_Player = null;
	}
}
