package com.example.freechat.ui;


/**
 * Created by zhangwei on 15-4-17.
 */
public class FCMessage {
    public static final int SEND_MESSAGE = 0;
    public static final int RECEIVE_MESSAGE = 1;

    private String m_content;
    private long m_timeStamp;
    private int  m_messageType;

    public FCMessage(String content, long timeStamp, int type) {
        m_content = content;
        m_timeStamp = timeStamp;
        m_messageType = type;
    }

    public FCMessage(String content, int type) {
        m_content = content;
        m_messageType = type;
        m_timeStamp = System.currentTimeMillis();
    }

    public String getContent() {
        return m_content;
    }

    public long getTimeStamp() {
        return m_timeStamp;
    }

    public int getMessageType() {
        return m_messageType;
    }
}
