package com.example.freechat;

import java.util.Date;

/**
 * Created by zhangwei on 15-4-17.
 */
public class FCMessage {
    public static final int SEND_MESSAGE = 0;
    public static final int RECEIVE_MESSAGE = 1;

    private String m_content;
    private Date m_timeStamp;
    private int  m_messageType;

    public FCMessage(String content, Date timeStamp, int type) {
        m_content = content;
        m_timeStamp = timeStamp;
        m_messageType = type;
    }

    public FCMessage(String content, int type) {
        m_content = content;
        m_messageType = type;
    }

    public String getContent() {
        return m_content;
    }

    public Date getTimeStamp() {
        return m_timeStamp;
    }

    public int getMessageType() {
        return m_messageType;
    }
}
