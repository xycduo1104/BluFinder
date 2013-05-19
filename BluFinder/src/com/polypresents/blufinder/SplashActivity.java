package com.polypresents.blufinder;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class SplashActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		Thread splashThread = new Thread() {
	         @Override
	         public void run() {
	        	 Intent i = new Intent();
	        	 i.setClassName("com.polypresents.blufinder",
                              	"com.polypresents.blufinder.MainActivity");
	        	 startActivity(i);
	        	 finish();
	         }
	      };
	      splashThread.start();
	}
}
