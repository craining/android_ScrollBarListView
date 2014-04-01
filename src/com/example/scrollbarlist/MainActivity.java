package com.example.scrollbarlist;

import com.example.scrollbar.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 测试activity
 * 
 * @Author ZGY
 * @Date:2014-3-27
 * @version
 * @since 欢迎加群88130145进行开发交流
 */
public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		Button btnOne = (Button) findViewById(R.id.btn_activity_one);
		Button btnTwo = (Button) findViewById(R.id.btn_activity_two);
		Button btnAbout = (Button) findViewById(R.id.btn_about);
		btnOne.setOnClickListener(this);
		btnTwo.setOnClickListener(this);
		btnAbout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_activity_one:
			startActivity(new Intent(MainActivity.this, OneActivity.class));
			break;
		case R.id.btn_activity_two:
			startActivity(new Intent(MainActivity.this, TwoActivity.class));
			break;
		case R.id.btn_about:
			new AlertDialog.Builder(MainActivity.this).setCancelable(false).setMessage("\r\n\r\n作者：ZGY\r\n仅供技术交流\r\n欢迎加qq群讨论：88130145\r\n\r\n").setPositiveButton("OK", null).create().show();
			break;
		default:
			break;
		}
	}

}
