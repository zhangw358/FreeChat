
package com.example.freechat.ui.activity;

import java.io.FileInputStream;

import com.example.freechat.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.freechat.ui.FCActionBarActivity;



public class FCBrowseActivity extends FCActionBarActivity {
	private ImageView imageView;
	private String picaddr;
	private Bitmap browbmp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		picaddr = getIntent().getExtras().getString("address");
		Log.v("picture browser", picaddr);
		setContentView(R.layout.zx_browse);
		setActionBarCenterTitle("Browse");
		imageView = (ImageView)findViewById(R.id.zx_browse);
		
		try {
			FileInputStream fin = new FileInputStream(picaddr);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			browbmp = BitmapFactory.decodeByteArray(buffer, 0, length);
			fin.close();
			imageView.setImageBitmap(browbmp);
		}
		catch (Exception e) {
			Toast.makeText(getBaseContext(), "No such file", Toast.LENGTH_LONG).show();
			finish();
		}

		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
            public void onClick(View v) {
            	finish();
            }
        });

	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
        return super.onOptionsItemSelected(item);
    }
}
