package com.example.freechat.ui.activity;

import com.example.freechat.ChatActivity;
import com.example.freechat.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class FCLoginActivity extends Activity {


    private Button regButton;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setTitle("FreeChat");
        regButton = (Button) findViewById(R.id.bt_goto_reg);
        loginButton = (Button) findViewById(R.id.bt_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FCLoginActivity.this, FCMainActivity.class);
                startActivity(intent);
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FCLoginActivity.this, FCRegisterActivity.class);
                startActivity(intent);
            }
        });

        test();
    }
    
    private void test() {
    	Intent intent = new Intent(FCLoginActivity.this, ChatActivity.class);
    	startActivity(intent);
    	finish();
    }

}
