package com.example.cottageofsweets_lite;

import java.util.List;

import android.R.string;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

//import com.egco.camera.api.R;
public class MainActivity extends Activity {

	final Context context = this;
	private Context mContext = this;
	MediaPlayer mpBgm;
	ToggleButton tbBGM;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ActionBar actionBar = getActionBar();
		actionBar.hide();

		mpBgm = MediaPlayer.create(MainActivity.this, R.raw.bgm_main02);
		mpBgm.setLooping(true);
		mpBgm.start();

		// ///////// Logo animate////////////////

		ImageView image2 = (ImageView) findViewById(R.id.imageView1);
		Animation animation = AnimationUtils.loadAnimation(context,
				R.anim.rotate);
		image2.startAnimation(animation);

		// ////////////////////////////////////////

		tbBGM = (ToggleButton) findViewById(R.id.muteButton);

		tbBGM.setVisibility(View.GONE);

		tbBGM.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					mpBgm.start();

				} else
					mpBgm.pause();

			}

		});

		final Button startBtn = (Button) findViewById(R.id.btnStart);

		startBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				MediaPlayer mpEffect = MediaPlayer.create(MainActivity.this,
						R.raw.button_click);
				mpEffect.start();

				Intent i = new Intent(getApplicationContext(),
						SelectStage.class);
				startActivity(i);

			}

		});

		// /////Button Disabled///////

		Button btnContinue = (Button) findViewById(R.id.btnContinue);
		btnContinue.setEnabled(false);

		Button btnHowtoPlay = (Button) findViewById(R.id.btnHowtoPlay);
		btnHowtoPlay.setEnabled(false);

		// /////////////////////////

	}

	public void onResume() {
		super.onResume();
		if (tbBGM.isChecked())
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

	// public void onClickContinue(View view) {
	//
	//
	// }

	// public void onClickHowtoPlay(View view) {
	//
	//
	// }

	public void onClickExit(View view) {

		final Button btnExit = (Button) findViewById(R.id.btnExit);

		MediaPlayer mpEffect = MediaPlayer.create(MainActivity.this,
				R.raw.button_click);
		mpEffect.start();

		// custom dialog
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom);

		// set the custom dialog components - text, image and button
		TextView text = (TextView) dialog.findViewById(R.id.text);

		Typeface custom_font = Typeface.createFromAsset(getAssets(),
				"fonts/sp_aftershock.ttf");

		text.setTypeface(custom_font);

		text.setText("        ARE YOU SURE ?");

		Button dialogButtonOk = (Button) dialog
				.findViewById(R.id.dialogButtonOk);
		Button dialogButtonCancel = (Button) dialog
				.findViewById(R.id.dialogButtonCancel);
		// if button is clicked, close the custom dialog

		dialogButtonOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				MainActivity.this.finish();
			}
		});

		dialogButtonCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();

			}
		});

		dialog.show();

		// AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
		// context);
		//
		//
		// // set title
		// alertDialogBuilder.setTitle("Quit");
		//
		// // set dialog message
		// alertDialogBuilder
		// .setMessage("Are you sure?")
		// .setCancelable(false)
		// .setPositiveButton("Yes",
		// new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int id) {
		// // if this button is clicked, close
		// // current activity
		// MainActivity.this.finish();
		// }
		// })
		// .setNegativeButton("No", new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int id) {
		// // if this button is clicked, just close
		// // the dialog box and do nothing
		// dialog.cancel();
		// }
		// });
		//
		// // create alert dialog
		// AlertDialog alertDialog = alertDialogBuilder.create();
		//
		// // show it
		// alertDialog.show();

	}

	
}
