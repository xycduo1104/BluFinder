package com.example.blufinder;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

public class HelpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		
		Button okB = (Button) findViewById(R.id.okButton);
	    // helpButton listener
		okB.setOnClickListener(new Button.OnClickListener() {
	         public void onClick(View v) {
	        	 finish();
	        	 overridePendingTransition(R.anim.animation_enter,
	                        R.anim.animation_leave);
	         }
	         
	    });
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_help, menu);
		return true;
	}*/

}
