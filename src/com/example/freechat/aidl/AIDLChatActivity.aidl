package com.example.freechat.aidl;
interface AIDLChatActivity {
	void onMessageSendFinished(boolean flag);
	void onNewMessageReceived(char type, inout byte[] message);
}