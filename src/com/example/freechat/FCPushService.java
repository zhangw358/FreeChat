package com.example.freechat;

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

	public AIDLPushService.Stub mStub = new AIDLPushService.Stub() {
		
		@Override
		public boolean sendMessage(String from, String to, int timeStamp,
				String content) throws RemoteException {
			
			Log.e(Log_Tag, "~~ sendMessage called ~~");
			
			Log.e(Log_Tag, "from: " + from);
			Log.e(Log_Tag, "to: " + to);
			Log.e(Log_Tag, "timeStamp: " + timeStamp);
			Log.e(Log_Tag, "content: " + content);
			
			mCallback.onMessageSendFinished(from, to, timeStamp, content);
			
			return true;
		}

		@Override
		public void registerToPushService(AIDLChatActivity callback)
				throws RemoteException {
			mCallback = callback;
		}
	};
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.e(Log_Tag, "onBind called");
		return mStub;
	}

}
