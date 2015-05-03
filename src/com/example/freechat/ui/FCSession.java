package com.example.freechat.ui;

public class FCSession {
	private String m_title;
    private String m_desIp;

    public FCSession(String name, String desIp) {
        m_title = "chat with " + name;
        m_desIp = desIp;
    }
    
    public FCSession(String name) {
    	m_title = "chat with " + name;
    }

    public String getTitle() {
        return m_title;
    }

    public String getIp() {
        return m_desIp;
    }
}
