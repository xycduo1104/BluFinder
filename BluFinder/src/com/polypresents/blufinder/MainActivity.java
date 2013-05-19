package com.polypresents.blufinder;

import java.io.File;
import java.util.Set;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.*;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	
	// Constants
	final public String SETTING = "Setting";			// User settings
	final public String RINGTONE_BUTTON_KEY = "Ringtone";
	final public String RINGTONE_CHECK_KEY = "RingtoneChecked";
	final public String VIBRATE_CHECK_KEY = "VibrateChecked";
	final public String RINGTONE_FOLDER = "/system/media/audio/ringtones";
	
	private BluetoothAdapter mBluetoothAdapter = null;	//Bluetooth Adapter
    private boolean serviceBound = false;
	
	private Button rtButton ;
	private Button helpButton ;
	private ToggleButton serviceButton;
	private CheckBox rtCheck ;
	private CheckBox vbCheck ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Widgets
		rtButton = (Button) findViewById(R.id.ringtoneButton);
		helpButton = (Button) findViewById(R.id.helpButton);
		serviceButton= (ToggleButton) findViewById(R.id.serviceButton);
		rtCheck = (CheckBox)findViewById(R.id.ringtoneCheck);
		vbCheck = (CheckBox)findViewById(R.id.vibrateCheck);

		// Set checkbox status-----moved this block to onResume method ---sheetal

		//Set bluetooth Adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		
		// ringtoneButton listener
		rtButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	if(existFolder(RINGTONE_FOLDER)){
            		// Open ringtone selector
            		Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
					intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
					intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Ringtone");
					intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, getRingtoneUri());
					// Return ringtone URI to this activity
					startActivityForResult(intent, 0);
            	}
            }
        });
		
		// serviceButton listener
		serviceButton.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {  
            @Override  
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            	
            	saveToggleStatus(serviceButton.isChecked());
            	
                if (isChecked) {
                	// start service
                		//Check whether Bluetooth is paired to any device.
                		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        				if(pairedDevices.size() == 0)
        				{
        					Toast.makeText(getBaseContext(), "Please pair a Bluetooth device before starting service", Toast.LENGTH_SHORT).show();
        					buttonView.toggle();
        					return;
        				}else
        				{
                            doBindService();
	        				Toast.makeText(getBaseContext(), "Service Started", Toast.LENGTH_SHORT).show();
        				}
        				
        		 } else {
                	 // stop service
        			 doUnbindService();
        			 Toast.makeText(getBaseContext(), "Service Stoped", Toast.LENGTH_SHORT).show();
                }
            }
        
        });
		
		// helpButton listener
		helpButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	// Show help info
            	Intent helpActivity = new Intent(MainActivity.this, HelpActivity.class);
            	startActivity(helpActivity);
            	overridePendingTransition(R.anim.animation_enter,
                        R.anim.animation_leave);
            }
        
        });
		
		// ringtone checkbox listener
		rtCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				saveCheckBoxStatus(RINGTONE_CHECK_KEY, isChecked);
				
				Intent ringtone_intent = new Intent("RINGTONE_CHECKED_CHANGE");
				ringtone_intent.putExtra("RINGTONE_CHECK_KEY", isChecked);
				sendBroadcast(ringtone_intent);
			}
		});
		
		// vibrate checkbox listener
		vbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				saveCheckBoxStatus(VIBRATE_CHECK_KEY, isChecked);
				
				Intent vibrate_intent = new Intent("VIBRATE_CHECKED_CHANGE");
				vibrate_intent.putExtra("VIBRATE_CHECK_KEY", isChecked);
				sendBroadcast(vibrate_intent);
			}
		});
		
	}

    /**
     * Class for interacting with the main interface of the service.
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
//            Toast.makeText(getBaseContext(), "Local service connected",
//                    Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
//            Toast.makeText(getBaseContext(), "Local service disconnected",
//                    Toast.LENGTH_SHORT).show();
        }
    };


    /**
     * Return selected ringtone
     */
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
		catch (Exception e){e.printStackTrace();}
		super.onActivityResult(requestCode, resultCode, data);
	}

    /**
     * // Check if folder exists
     * @return
     */
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

	// Get checkbox status
	private boolean getCheckBoxStatus(String key){
		Context cxt = getBaseContext();
        SharedPreferences value = cxt.getSharedPreferences(SETTING, MODE_PRIVATE);
        return value.getBoolean(key, true);
	}
	
	// Save checkbox status
	private void saveCheckBoxStatus(String key, boolean checked){
		Context ctx = this;
		SharedPreferences sta = ctx.getSharedPreferences(SETTING, MODE_PRIVATE);
		SharedPreferences.Editor editor = sta.edit();
		editor.putBoolean(key, checked);
		editor.commit();
	}
	
	// Save ToggleButton status
	private void saveToggleStatus(boolean checked){
		Context ctx = getBaseContext();
		SharedPreferences sta = ctx.getSharedPreferences(SETTING, MODE_PRIVATE);
		SharedPreferences.Editor editor = sta.edit();
		editor.putBoolean("TOGGLE_STATE", checked);
		editor.commit();
	}

	
	private boolean getToggleStatus(){
		Context cxt = getBaseContext();
        SharedPreferences rtone = cxt.getSharedPreferences(SETTING, MODE_PRIVATE);
        return rtone.getBoolean("TOGGLE_STATE", false);
	}
	
	// Get current ringtone----Moved the method getRingtoneUri to service class --Sheetal
	// Still need this method here to set default ringtone when open Ringtone Dialog  --Yachen
	private Uri getRingtoneUri(){
		String ringtoneUri = "";
		Context cxt = getBaseContext();
        SharedPreferences rtone = cxt.getSharedPreferences(SETTING, MODE_PRIVATE);
        ringtoneUri = rtone.getString(RINGTONE_BUTTON_KEY, "");
		if(ringtoneUri == ""){	//if no ringtone data found
			//get system default ringtone
			return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
		}
        return Uri.parse(ringtoneUri);
	}
	
	// Save ringtone Uri
	private void saveRingtoneUri(Uri pickedUri){
		Context ctx = MainActivity.this;
		SharedPreferences rtone = ctx.getSharedPreferences(SETTING, MODE_PRIVATE);
		SharedPreferences.Editor editor = rtone.edit();
		editor.putString(RINGTONE_BUTTON_KEY, pickedUri.toString());
		editor.commit();
	}

    void doBindService() {
        // Bind to the service
        bindService(new Intent(this, BluetoothAlertService.class), mConnection,
                Context.BIND_AUTO_CREATE);
        serviceBound = true;
    }

    void doUnbindService() {
        if (serviceBound) {
            // Detach our existing connection.
            unbindService(mConnection);
            serviceBound = false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!serviceBound)
            doBindService();
    }

	@Override
	protected void onPause() {
		super.onPause();
		//Save current values to shared preferences
		saveCheckBoxStatus("RINGTONE_CHECK_KEY", rtCheck.isChecked());
		saveCheckBoxStatus("VIBRATE_CHECK_KEY", vbCheck.isChecked());
		saveToggleStatus(serviceButton.isChecked());
	}

	@Override
	protected void onResume() {
		super.onResume();
			//Get values from shared preferences
			rtCheck.setChecked(getCheckBoxStatus(RINGTONE_CHECK_KEY));
			vbCheck.setChecked(getCheckBoxStatus(VIBRATE_CHECK_KEY));

			if(mBluetoothAdapter.isEnabled())
				serviceButton.setChecked(getToggleStatus());
			else
				serviceButton.setChecked(false);
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }
	
	// disable options menu
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.activity_main, menu);
//		return true;
//	}
	
}
