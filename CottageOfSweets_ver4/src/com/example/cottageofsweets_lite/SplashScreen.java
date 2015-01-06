package com.example.cottageofsweets_lite;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends Activity {

	// Splash screen timer
	private static int SPLASH_TIME_OUT = 4000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		MediaPlayer mpEffect = MediaPlayer.create(SplashScreen.this,
				R.raw.title);
		mpEffect.start();

		ImageView image = (ImageView) findViewById(R.id.imageLogo);

		Animation animationFadeIn = AnimationUtils.loadAnimation(this,
				R.anim.fadein);

		image.startAnimation(animationFadeIn);

		new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

			@Override
			public void run() {
				// This method will be executed once the timer is over
				// Start your app main activity

				Intent i = new Intent(SplashScreen.this, MainActivity.class);
				startActivity(i);

				// close this activity
				finish();
			}
		}, SPLASH_TIME_OUT);
	}

}