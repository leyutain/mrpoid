package com.tszy.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;


public class Emulator {
	private EmulatorView view;
	
	static {
		System.loadLibrary("emulator");
	}
	
	public Emulator(Context context) {
		view = new EmulatorView(context, this);
		init();
	}
	
	public EmulatorView getView() {
		return view;
	}

	private Rect rect = new Rect();
	public void flush(int x, int y, int w, int h){
		Log.i("Mrpoid", "flush");
		
		rect.set(x, y, x+w-1, y+h-1);
		view.invalidate(rect);
	}
	
	public native void init();
	public native void setBitmap(Bitmap bitmap);
	public native int getScnWidth();
	public native int getScnHeight();
	public native void touchDown(int x, int y);
	public native void touchMove(int x, int y);
	public native void touchUp(int x, int y);
	public native void onKeyDown(int key);
	public native void onKeyUp(int key);
	public native int loadMrp(String path);
	
	public native void render();
}
