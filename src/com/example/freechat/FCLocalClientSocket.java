package com.example.freechat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.RemoteException;
import android.util.Log;

import com.example.freechat.aidl.AIDLChatActivity;

public class FCLocalClientSocket {
	private static final String LOG_TAG = FCLocalClientSocket.class.getName()
			.toString();
	private AIDLChatActivity mCallback;
	private Socket mClient;

	public FCLocalClientSocket() {
		initSocket();
	}

	public FCLocalClientSocket(AIDLChatActivity callback) {
		mCallback = callback;
		initSocket();
	}

	public void setCallBack(AIDLChatActivity callback) {
		mCallback = callback;
	}

	private void initSocket() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mClient = new Socket(FCConfigure.SERVER_ADDR,
							FCConfigure.SERVER_TCP_PORT);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (mClient != null) {
					startReceiveData();
				}
			}
		}).start();
	}

	public void startReceiveData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				doStartReceive();
			}
		}).start();
	}

	private void doStartReceive() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					mClient.getInputStream()));
			while (true) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Log.e("Halfish", "while true...");
				if (mCallback == null) {
					Log.e("Halfish", "mCallBack is null");
					// has not register yet
					continue;
				}
				Log.e("Halfish", "begin to read message..");
				
				String msg = "";
				
				msg = reader.readLine();
				//reader.r
				Log.e("halfish", "reader.readLine() " + msg);
				
				if (!msg.equals("")) {
					try {
						mCallback.onNewMessageReceived(msg);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}

		} catch (IOException e) {
			Log.e("halfish", "RemoteException" + e.getMessage().toString());
			e.printStackTrace();
		}
	}

	public boolean sendMessageToServer(String msg) throws IOException {
		if (msg.equals("")) {
			Log.e(LOG_TAG, "empty message, send failed");
			return false;
		}

		if (mClient == null) {
			Log.e(LOG_TAG, "client socket is null");
			return false;

		} else {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					mClient.getOutputStream()));
			writer.write(msg);
			writer.flush();
		}
		return true;
	}

	public void stop() {
		Log.e(LOG_TAG, "socket stop!");
		if (mClient != null) {
			try {
				mClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mClient = null;
		}
	}
}
