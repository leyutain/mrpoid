package com.tszy.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class EmulatorView extends View {
	private Bitmap bitmap;
	private Canvas canvas2;
	private Paint paint;
	private Emulator emulator;
	private int scnX, scnY; // ÆÁÄ»Î»ÖÃÆ«ÒÆ

	public EmulatorView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public EmulatorView(Context context, Emulator emulator) {
		super(context);
		this.emulator = emulator;

		// TODO Auto-generated constructor stub
		bitmap = Bitmap.createBitmap(emulator.getScnWidth(), emulator.getScnHeight(), Config.RGB_565);
		canvas2 = new Canvas(bitmap);
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.emulator.setBitmap(bitmap);

		setFocusableInTouchMode(true);
		setFocusable(true);

	}

	public void setScnPosition(int x, int y) {
		scnX = x;
		scnY = y;
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		rectF.set(0, 0, w, h);

		super.onSizeChanged(w, h, oldw, oldh);
	}

	RectF rectF = new RectF();

	@Override
	public void draw(Canvas canvas) {
		// canvas.scale(1, 1);
		// canvas.drawBitmap(bitmap, scnX, scnY, null);
		canvas.drawBitmap(bitmap, null, rectF, null);
		// canvas.scale(-1, -1);

		super.draw(canvas);
	}

	public void setTextSize(int font) {
		if (font == 0)
			paint.setTextSize(12);
		else if (font == 2)
			paint.setTextSize(20);
		else
			paint.setTextSize(16);
	}

	public void drawText(String pcText, int x, int y, int r, int g, int b, int font) {
		paint.setColor(Color.rgb(r, g, b));
		paint.setTextSize(font);
		canvas2.drawText(pcText, x, y, paint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			emulator.touchDown(x, y);
			break;

		case MotionEvent.ACTION_MOVE:
			emulator.touchMove(x, y);
			break;

		case MotionEvent.ACTION_UP:
			emulator.touchUp(x, y);
			break;
		}

		return super.onTouchEvent(event);
	}

	private boolean handled = false;
	static int lastX, lastY;

	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();

		if (!handled) {
			lastX = (int) event.getX();
			lastY = (int) event.getY();
		} else {
			if (x < lastX)
				emulator.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT);
			else if (x > lastX)
				emulator.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT);
			else if (y > lastY)
				emulator.onKeyDown(KeyEvent.KEYCODE_DPAD_DOWN);
			else if (y < lastY)
				emulator.onKeyDown(KeyEvent.KEYCODE_DPAD_UP);
		}

		return super.onTrackballEvent(event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		emulator.onKeyUp(keyCode);

		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		emulator.onKeyDown(keyCode);

		return super.onKeyDown(keyCode, event);
	}
}
