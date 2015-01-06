package com.example.cottageofsweets_lite;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ToggleButton;

//import com.egco.camera.api.R;
public class MainActivity extends Activity {

	final Context context = this;
	MediaPlayer mpBgm;
	ToggleButton tbBGM;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		//////////////////////////HEY///////////////////////////
	

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ActionBar actionBar = getActionBar();
		actionBar.hide();

		mpBgm = MediaPlayer.create(MainActivity.this, R.raw.bgm_main);
		mpBgm.setLooping(true);
		mpBgm.start();

		 /////////// Logo animate////////////////

		ImageView image2 = (ImageView) findViewById(R.id.imageView2);
		Animation animation = AnimationUtils.loadAnimation(context,
				R.anim.rotate);
		image2.startAnimation(animation);

		 //////////////////////////////////////////

		Button startBtn = (Button) findViewById(R.id.btnStart);
		
		tbBGM = (ToggleButton)findViewById(R.id.muteButton);
        tbBGM.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if(arg1)
                    mpBgm.start();
                else 
                    mpBgm.pause();
            }
            
        });			
        
		startBtn.setOnClickListener(new View.OnClickListener() {		

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				MediaPlayer mpEffect = MediaPlayer.create(MainActivity.this, R.raw.button_click);		
				mpEffect.start();
				
						
				new CountDownTimer(1000, 500) {

					@Override
					public void onFinish() {

						Intent i = new Intent(getApplicationContext(), GamePlay.class);
						startActivity(i);
					}

					@Override
					public void onTick(long millisUntilFinished) {
						// TODO Auto-generated method stub						
					}					
				}.start();
			}

		});

	

	}
	
	public void onResume() {
        super.onResume();
        if(tbBGM.isChecked())
            mpBgm.start();
    }
    
    public void onPause() {
        super.onPause();
        mpBgm.pause();
    }
	
	public void onDestroy() {
		
		super.onDestroy();
		mpBgm.stop();
		mpBgm.release();
		mpBgm = null;
		
	}

	public void onClickExit(View view) {
		
		MediaPlayer mpEffect = MediaPlayer.create(MainActivity.this, R.raw.button_click);		
		mpEffect.start();

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		// set title
		alertDialogBuilder.setTitle("Quit");

		// set dialog message
		alertDialogBuilder
				.setMessage("Are you sure?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, close
								// current activity
								MainActivity.this.finish();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}

	
	}

