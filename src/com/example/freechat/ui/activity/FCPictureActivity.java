package com.example.freechat.ui.activity;

import java.io.ByteArrayOutputStream;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.freechat.R;
import com.example.freechat.storage.FCFileHelper;
import com.example.freechat.ui.FCActionBarActivity;

public class FCPictureActivity extends FCActionBarActivity {

	private static int RESULT_LOAD_IMAGE = 1;
	private int picturesize_int;
	private String picturesize_str;
	private Bitmap bmp;
	private ImageView zx_imageView;
	private Button zx_button1;
	private Button zx_button2;

	private FCFileHelper m_fileHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// 调用android的图库
		startActivityForResult(i, RESULT_LOAD_IMAGE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA,
					MediaStore.Images.Media.SIZE };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			int index = cursor.getColumnIndex(filePathColumn[1]);
			String picturePath = cursor.getString(columnIndex); // 图片路径
			picturesize_str = cursor.getString(index); // 图片大小
			picturesize_int = Integer.parseInt(picturesize_str);
			cursor.close();

			BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();

			bmp = BitmapFactory.decodeFile(picturePath, bmpFactoryOptions);
			int option = 1;

			Canvas canvas = new Canvas();
			int maxheight = canvas.getMaximumBitmapHeight() / 8;
			int maxwidth = canvas.getMaximumBitmapWidth() / 8;
			while (bmp.getWidth() > maxwidth || bmp.getHeight() > maxheight) {
				option = option * 2;
				bmpFactoryOptions.inSampleSize = option;
				bmp = BitmapFactory.decodeFile(picturePath, bmpFactoryOptions);
				picturesize_int = picturesize_int / 4;
			}
			picturesize_str = String.valueOf(picturesize_int);

			setContentView(R.layout.zx_layout1);
			setActionBarCenterTitle("Choose a picture");
			zx_button1 = (Button) findViewById(R.id.zx_button1);
			zx_button2 = (Button) findViewById(R.id.zx_button2);
			zx_imageView = (ImageView) findViewById(R.id.zx_imageView1);
			zx_imageView.setImageBitmap(bmp);

			m_fileHelper = new FCFileHelper(getApplicationContext());

			zx_button1.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					byte[] data = compressBitmap();
					Intent intent = new Intent();
					String fileName = m_fileHelper.generateFileName();
					m_fileHelper.writeToFile(fileName, data);
					intent.putExtra("content", fileName);
					setResult(RESULT_OK, intent);
					finish();
				}
			});
			zx_button2.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// 调用android的图库
					startActivityForResult(i, RESULT_LOAD_IMAGE);
				}
			});
		} else {
			finish();
		}

	}

	public byte[] compressBitmap() {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos); // 如果签名是png的话，则不进行质量压缩

		if (bmp == null || picturesize_int <= 1024000) {
			return baos.toByteArray(); // 如果图片本身的大小已经小于这个大小了，就没必要进行压缩
		}

		float cor = (float) picturesize_int / (float) baos.toByteArray().length;
		int quality = 100;

		while ((float) baos.toByteArray().length * cor > 1024000f) {
			quality = quality - 5; // 每次减5
			baos.reset(); // 重置baos即清空baos
			if (quality <= 0) {
				break;
			}
			bmp.compress(Bitmap.CompressFormat.JPEG, quality, baos);
		}
		return baos.toByteArray();
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
