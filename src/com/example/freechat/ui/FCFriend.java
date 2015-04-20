package com.example.freechat.ui;

/**
 * Created by zhangwei on 15-4-17.
 */
public class FCFriend {
    private String m_name;
    private String m_desIp;

    public FCFriend(String name, String desIp) {
        m_name = name;
        m_desIp = desIp;
    }

    public String getName() {
        return m_name;
    }

    public String getIp() {
        return m_desIp;
    }
}
