package com.example.freechat;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;


public class FCActionBarActivity extends Activity {

    private ActionBar m_actionBar;
    private TextView m_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
    }

    private void initActionBar() {
        m_actionBar = getActionBar();

        m_title = (TextView) getLayoutInflater().inflate(R.layout.fc_customview_actionbar, null);

        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER);

        m_actionBar.setCustomView(m_title, lp);
        m_actionBar.setDisplayShowCustomEnabled(true);
        m_actionBar.setDisplayShowTitleEnabled(false);
        m_actionBar.setHomeButtonEnabled(true);
        m_actionBar.setIcon(R.drawable.fc_back);
    }

    public void setActionBarCenterTitle(String title) {
        m_title.setText(title);
        m_title.invalidate();
    }
}
