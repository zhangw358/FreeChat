package com.example.freechat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
	private InputStream mInputStream;
	private OutputStream mOutputStream;

	public FCLocalClientSocket() {
		initSocket();
	}

	public FCLocalClientSocket(AIDLChatActivity callback) {
		mCallback = callback;
		initSocket();
	}

	public void setCallBack(AIDLChatActivity callback) {
		Log.v(LOG_TAG, "set callback");
		mCallback = callback;
	}

	private void initSocket() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Log.v(LOG_TAG, "socket init");

					mClient = new Socket(FCConfigure.SERVER_TCP_ADDR,
							FCConfigure.SERVER_TCP_PORT);
					mInputStream = mClient.getInputStream();
					mOutputStream = mClient.getOutputStream();
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
				Log.v(LOG_TAG, "start receive data");

				doStartReceive();
			}
		}).start();
	}

	private void doStartReceive() {
		byte[] buffer = new byte[1024 * 4];
		int length = 0;
		try {
			while (!mClient.isClosed() && !mClient.isInputShutdown()) {

				// read 5 bytes first
				length = mInputStream.read(buffer, 0, 5);
				if (length == 5) {
					char type = (char) buffer[0];
					int dataLength = FCMessageUtil.byteToInt(buffer, 1);
					
					if (mInputStream.read(buffer, 0, dataLength) > 0) {

						if (mCallback != null) {
							mCallback.onNewMessageReceived(type, buffer);
						}
					}
				}

				Thread.sleep(200);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}	

	public boolean sendDataToServer(char type, byte[] msg) {

		if (mClient == null) {
			Log.e(LOG_TAG, "client socket is null");
			return false;
		}

		byte[] totalBuffer = new byte[msg.length + 5];

		totalBuffer[0] = 'a';
		byte[] lenBuffer = FCMessageUtil.intToByte(msg.length);

		// copy lenBuffer to totalBuffer
		System.arraycopy(lenBuffer, 0, totalBuffer, 1, lenBuffer.length);

		// copy lenBuffer to totalBuffer
		System.arraycopy(msg, 0, totalBuffer, 5, msg.length);

		Log.v(LOG_TAG, new String(totalBuffer));

		try {
			mOutputStream.write(totalBuffer);
			mOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
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
