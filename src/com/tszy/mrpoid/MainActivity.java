package com.tszy.mrpoid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.tszy.core.Emulator;

public class MainActivity extends Activity {
	private Emulator emulator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        
        emulator = new Emulator(this);
        setContentView(emulator.getView());

    	int ret = emulator.loadMrp(intent.getStringExtra("mrpPath"));
    	if(ret == 0){
    		Toast.makeText(this, "启动成功！", Toast.LENGTH_LONG).show();
    	}else {
    		Toast.makeText(this, "启动失败！", Toast.LENGTH_LONG).show();
		}
    }
    
    @Override
    protected void onStart() {
    	
    	super.onStart();
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
}
