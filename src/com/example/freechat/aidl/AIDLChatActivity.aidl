package com.example.freechat.aidl;
interface AIDLChatActivity {
	void onMessageSendFinished(String from, String to, int timeStamp, String content);
	void onNewMessageReceived(String from, String to, int timeStamp, String content);
}