package com.tszy.mrpoid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.tszy.core.Emulator;

public class MainActivity extends Activity implements View.OnClickListener {
	private Emulator emulator;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_left: {
			emulator.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT);
			break;
		}
		case R.id.btn_right: {
			emulator.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT);

			break;
		}
		case R.id.btn_up: {
			emulator.onKeyDown(KeyEvent.KEYCODE_DPAD_UP);

			break;
		}
		case R.id.btn_down: {
			emulator.onKeyDown(KeyEvent.KEYCODE_DPAD_DOWN);

			break;
		}
		case R.id.btn_select: {
			emulator.onKeyDown(KeyEvent.KEYCODE_DPAD_CENTER);

			break;
		}
		case R.id.btn_softleft: {
			emulator.onKeyDown(KeyEvent.KEYCODE_SOFT_LEFT);

			break;
		}
		case R.id.btn_softright: {
			emulator.onKeyDown(KeyEvent.KEYCODE_SOFT_RIGHT);

			break;
		}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Intent intent = getIntent();
		emulator = new Emulator(this);

		FrameLayout layout = (FrameLayout) findViewById(R.id.frameLayout1);
		layout.addView(emulator.getView());

		for (int i = 0; i < ids.length; i++) {
			findViewById(ids[i]).setOnClickListener(this);
		}

		int ret = emulator.loadMrp(intent.getStringExtra("mrpPath"));
		if (ret == 0) {
			Toast.makeText(this, "启动成功！", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, "启动失败！", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onStart() {

		super.onStart();
	}

	@Override
	public void onBackPressed() {
		emulator.dispose();

		super.onBackPressed();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		return super.onTouchEvent(event);
	}

	@Override
	protected void onDestroy() {
		Log.i("Mrpoid", "Activity onDestroy ");

		super.onDestroy();
	}

	private static final int[] ids = { R.id.btn_left, R.id.btn_right, R.id.btn_up, R.id.btn_down, R.id.btn_softleft, R.id.btn_softright, R.id.btn_select };
}
