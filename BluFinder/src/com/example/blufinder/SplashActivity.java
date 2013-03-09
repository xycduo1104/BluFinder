package com.example.blufinder;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;

public class SplashActivity extends Activity {

	final private int SPLASH_DISPLAY_TIME = 2000;	//2.5 sec
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		new Handler().postDelayed(new Runnable() {

	        public void run() {
	            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
	            SplashActivity.this.startActivity(mainIntent);
	            SplashActivity.this.finish();
	            overridePendingTransition(R.anim.mainfadein,
	                    R.anim.splashfadeout);
	        }
	    }, SPLASH_DISPLAY_TIME);
	}
}
