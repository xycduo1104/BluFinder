package com.example.blufinder;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	
	// Constants
	final private String SETTING = "Setting";			// User settings
	final private String RINGTONE_BUTTON_KEY = "Ringtone";
	final private String RINGTONE_CHECK_KEY = "RingtoneChecked";
	final private String VIBRATE_CHECK_KEY = "VibrateChecked";
	final private String RINGTONE_FOLDER = "/system/media/audio/ringtones";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Widgets
		Button rtButton = (Button) findViewById(R.id.ringtoneButton);
		Button helpButton = (Button) findViewById(R.id.helpButton);
		ToggleButton serviceButton= (ToggleButton) findViewById(R.id.serviceButton);
		CheckBox rtCheck = (CheckBox)findViewById(R.id.ringtoneCheck);
		CheckBox vbCheck = (CheckBox)findViewById(R.id.vibrateCheck);
		
		// Set checkbox status
		rtCheck.setChecked(getCheckBoxStatus(RINGTONE_CHECK_KEY));
		vbCheck.setChecked(getCheckBoxStatus(VIBRATE_CHECK_KEY));
		
		// ringtoneButton listener
		rtButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	if(existFolder(RINGTONE_FOLDER)){
            		// Open ringtone selector
            		Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
					intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
					intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Ringtone");
					// Return ringtone URI to this activity
					startActivityForResult(intent, 0);
            	}
            }
        });
		
		// serviceButton listener
		serviceButton.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {  
            @Override  
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                	// start service
                	
                	
                	
                } else {
                	// stop service
                	
                	
                	
                }  
            }  
              
        });
		
		// helpButton listener
		helpButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	// Show help info
            	
            	
            	
            }
              
        });
		
		// ringtone checkbox listener
		rtCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				saveCheckBoxStatus(RINGTONE_CHECK_KEY, isChecked);
			}
		});
		
		// vibrate checkbox listener
		vbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				saveCheckBoxStatus(VIBRATE_CHECK_KEY, isChecked);
			}
		});
		
	}
	
	// Return select ringtone
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode != RESULT_OK)
		{
			return;
		}
		try
		{
			// Get ringtone Uri
			Uri pickedUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
			if (pickedUri != null)
			{
				// Save ringtone only for this app
				saveRingtoneUri(pickedUri);
			}
		}
		catch (Exception e){}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// Check if folder exists
	private boolean existFolder(String folder) {
		boolean exist = false;
		File f = new File(folder);
		if (!f.exists())
		{
			if (f.mkdirs()){exist = true;}
			else{exist = false;}
		}
		else{
			exist = true;
		}
		return exist;
	}
	
	private boolean getCheckBoxStatus(String key){
		boolean status;
		Context cxt = getBaseContext();
        SharedPreferences rtone = cxt.getSharedPreferences(SETTING, MODE_PRIVATE);
        status = rtone.getBoolean(key, false);
		return status;
	}
	
	// Save checkbox status
	private void saveCheckBoxStatus(String key, boolean checked){
		Context ctx = MainActivity.this;
		SharedPreferences sta = ctx.getSharedPreferences(SETTING, MODE_PRIVATE);
		SharedPreferences.Editor editor = sta.edit();
		editor.putBoolean(key, checked);
		editor.commit();
	}
	
	// Get current ringtone
	private Uri getRingtoneUri(){
		Uri rtUri;
		String ringtoneUri = "";
		Context cxt = getBaseContext();
        SharedPreferences rtone = cxt.getSharedPreferences(SETTING, MODE_PRIVATE);
        ringtoneUri = rtone.getString(RINGTONE_BUTTON_KEY, "");
		if(ringtoneUri == ""){	//if no ringtone data found
			//get system default ringtone
			rtUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
		}
		else{
			rtUri = Uri.parse(ringtoneUri);
		}
		return rtUri;
	}
	
	// Save ringtone Uri
	private void saveRingtoneUri(Uri pickedUri){
		Context ctx = MainActivity.this;
		SharedPreferences rtone = ctx.getSharedPreferences(SETTING, MODE_PRIVATE);
		SharedPreferences.Editor editor = rtone.edit();
		editor.putString(RINGTONE_BUTTON_KEY, pickedUri.toString());
		editor.commit();
	}
	// disable options menu
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.activity_main, menu);
//		return true;
//	}
	
	

}
