package com.example.freechat.ui;

public class FCSession {
	private String m_title;
    private String m_desIp;

    public FCSession(String name, String desIp) {
        m_title = name;
        m_desIp = desIp;
    }
    
    public FCSession(String name) {
    	m_title =  name;
    }

    public String getTitle() {
        return "chat with " + m_title;
    }
    
    public String getName() {
    	return m_title;
    }

    public String getIp() {
        return m_desIp;
    }
}
