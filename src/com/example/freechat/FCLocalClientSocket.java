package com.example.freechat;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import android.os.RemoteException;
import android.util.Log;

import com.example.freechat.aidl.AIDLChatActivity;

public class FCLocalClientSocket {
	private static final String LOG_TAG = FCLocalClientSocket.class.getName()
			.toString();
	private AIDLChatActivity mCallback;
	private Socket mClient;
	private BufferedWriter mWriter;
	private InputStream mIn;

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
					mClient = new Socket(FCConfigure.SERVER_TCP_ADDR,
							FCConfigure.SERVER_TCP_PORT);
					mIn = mClient.getInputStream();
					mWriter = new BufferedWriter(new OutputStreamWriter(
							mClient.getOutputStream()));
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

	private void startReceiveData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				doStartReceive();
			}
		}).start();
	}

	private void doStartReceive() {
		byte[] buffer = new byte[1024 * 4];
		int length = 0;
		try {
			while (!mClient.isClosed() && !mClient.isInputShutdown()
					&& ((length = mIn.read(buffer)) != -1)) {
				if (length > 0) {
					String message = new String(Arrays.copyOf(buffer, length))
							.trim();
					mCallback.onNewMessageReceived(message);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
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
			mWriter.write(msg);
			mWriter.flush();
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
