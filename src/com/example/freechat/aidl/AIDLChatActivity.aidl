package com.example.freechat.aidl;
interface AIDLChatActivity {
	void onMessageSendFinished(String message);
	void onNewMessageReceived(String message);
}