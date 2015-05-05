package com.example.freechat;

import java.io.IOException;

import com.example.freechat.aidl.AIDLChatActivity;
import com.example.freechat.aidl.AIDLPushService;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class FCPushService extends Service {

	private static String Log_Tag = FCPushService.class.getName().toString();
	private AIDLChatActivity mCallback;
	private FCLocalClientSocket mClientSocket;

	public AIDLPushService.Stub mStub = new AIDLPushService.Stub() {
		@Override
		public boolean sendMessage(String message) throws RemoteException {
			boolean flag = false;
			if (mClientSocket == null) {
				return false;
			}
			try {
				flag = mClientSocket.sendMessageToServer(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
			mCallback.onMessageSendFinished(message);

			return flag;
		}

		@Override
		public void registerToPushService(AIDLChatActivity callback)
				throws RemoteException {
			mCallback = callback;
			mClientSocket.setCallBack(mCallback);
		}
	};

	public void onCreate() {
		mClientSocket = new FCLocalClientSocket();
	};

	@Override
	public void onDestroy() {
		mClientSocket.stop();
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.e(Log_Tag, "onBind called");
		return mStub;
	}

}
