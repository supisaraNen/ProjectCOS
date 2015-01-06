package com.example.cottageofsweets_lite;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GamePlay extends Activity {
	public final static String DEBUG_TAG = "MakePhotoActivity";
	public Camera camera;
	private TextView textTimeLeft; // time left field
	private int cameraId = 0;
	final Context context = this;
	PhotoHandler ph = new PhotoHandler(context);
	MediaPlayer mpBgm;
	int score = 0;
	int start = 0;

	public Vector<Integer> checkQuiz = new Vector<Integer>();
	String[] setQuiz = { "2,4,3,6,5,0,1,9,8,7", "0,2,4,9,1,6,3,5,7,8",
			"6,9,7,8,5,2,4,0,3,1", "3,6,0,8,5,2,1,4,9,7",
			"7,0,1,3,6,2,4,9,5,8", "5,3,4,8,7,1,6,0,2,9",
			"1,0,2,7,8,9,3,6,4,5", "8,1,3,9,5,2,7,0,6,4",
			"4,9,1,5,8,3,7,0,6,2", "4,8,1,2,3,6,7,0,5,9" };
	int Low = 0;
	int High = 10;
	int chooseQuiz;
	int round = 0;
	Random r = new Random();
	DataBase myquiz = new DataBase(context);

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gameplay);
		TextView Show = (TextView) findViewById(R.id.textCheck);

		mpBgm = MediaPlayer.create(GamePlay.this, R.raw.bgm_sunnyside);
		mpBgm.setLooping(true);
		mpBgm.start();

		Show.setText(" ");
		selectQuiz();

		ActionBar actionBar = getActionBar();
		actionBar.hide();

		textTimeLeft = (TextView) findViewById(R.id.textTimeLeft);

		// do we have a camera?
		if (!getPackageManager()
				.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG)
					.show();
		} else {
			cameraId = findFrontFacingCamera();
			if (cameraId < 0) {
				Toast.makeText(this, "No front facing camera found.",
						Toast.LENGTH_LONG).show();
			} else {
				camera = Camera.open(cameraId);
				camera.setDisplayOrientation(90);
				Toast.makeText(this, "Camera Open", Toast.LENGTH_LONG).show();

			}
		}
	}

	// After click answer button
	public void onClickStart(View view) {
		final Button btnAnswer = (Button) findViewById(R.id.btnStart);
		MediaPlayer mpEffect = MediaPlayer.create(GamePlay.this,
				R.raw.button_click);
		mpEffect.start();
		TextView Show = (TextView) findViewById(R.id.textCheck);

		Show.setText(" ");

		new CountDownTimer(14000, 1000) {

			@Override
			public void onFinish() {
				btnAnswer.setAlpha((float) 1);
				btnAnswer.setEnabled(true);
			}

			@Override
			public void onTick(long millisUntilFinished) {

				btnAnswer.setAlpha((float) 0.5);
				btnAnswer.setEnabled(false);
			}

		}.start();
		// 5000ms=5s at intervals of 1000ms=1s so that means it lasts 5 seconds
		new CountDownTimer(5000, 1000) {

			@Override
			public void onFinish() {
				// count finished application will take photo automatically.
				takePicture();
				textTimeLeft.setText("Picture Taken");
				// show the result
				showResult();
			}

			@Override
			public void onTick(long millisUntilFinished) {
				// every time 1 second passes
				textTimeLeft.setText("" + millisUntilFinished / 1000);
			}

		}.start();

	}

	public void takePicture() {
		// go to PhotoHandler class to manage the photo and check the answer.
		Parameters camParameters = camera.getParameters();
		camParameters.setWhiteBalance(Parameters.WHITE_BALANCE_FLUORESCENT);
		camParameters.setSceneMode(Parameters.SCENE_MODE_AUTO);
		camera.setParameters(camParameters);

		camera.takePicture(null, null, null, ph);
		textTimeLeft.setText("take picture");

	}

	// show the result
	public void showResult() {
		new CountDownTimer(3000, 1000) {

			@Override
			public void onFinish() {
				TextView Show = (TextView) findViewById(R.id.textCheck);
				TextView ShowScore = (TextView) findViewById(R.id.textScore);
				// the answer is correct.
				if (ph.getResultAnswer()) {
					Show.setText("CORRECT!!!");
					MediaPlayer mpEffect = MediaPlayer.create(GamePlay.this,
							R.raw.correct);
					mpEffect.start();
					score += 1;
					ShowScore.setText("SCORE " + score);
				} else {
					MediaPlayer mpEffect = MediaPlayer.create(GamePlay.this,
							R.raw.wrong);
					mpEffect.start();
					Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
					v.vibrate(300);
					Show.setText("WRONG!!!");
				}

				textTimeLeft.setText("Next Quiz!!");
				waitForSelectQuiz();
			}

			@Override
			public void onTick(long millisUntilFinished) {

			}

		}.start();
	}

	public void waitForSelectQuiz() {
		new CountDownTimer(2000, 1000) {

			@Override
			public void onFinish() {
				TextView Show = (TextView) findViewById(R.id.textCheck);

				selectQuiz();
				textTimeLeft.setText("");
				Show.setText(" ");
			}

			@Override
			public void onTick(long millisUntilFinished) {
			}

		}.start();
	}

	private int findFrontFacingCamera() {
		int cameraId = -1;
		// Search for the front facing camera
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
				Log.d(DEBUG_TAG, "Camera found");
				cameraId = i;
				break;
			}
		}
		return cameraId;
	}

	// select quiz
	public void selectQuiz() {
		// import Database (Table Color and Table Quiz)
		if (start == 0) {
			myquiz.getWritableDatabase();
			myquiz.createTableQuiz();
			myquiz.createTableColor();
			start++;
		}
		Vector<getQuiz> quiz_list = myquiz.getAllQuiz();
		Vector<getColor> color_list = myquiz.getAllColor();

		// set textView and ImageView
		ImageView quiz = (ImageView) findViewById(R.id.imageView1);
		TextView ShowStage = (TextView) findViewById(R.id.textStage);
		TextView ShowScore = (TextView) findViewById(R.id.textScore);
		Resources resources = this.getResources();
		int resourceID;

		// it will random set of quiz at first play and will random the set of
		// quiz again if the player answer 10 quiz
		if (round == 0)
			chooseQuiz = r.nextInt(High - Low) + Low;

		String[] selectQuiz = setQuiz[chooseQuiz].split(",");

		// check that round is not more than 10 quiz , if not it will show alert
		// dialog and set round , score to 0
		if (round < selectQuiz.length) {
			Log.i("Total", "R,G,B - Quiz : " + round);
			ShowStage.setText("STAGE " + (round));
			// sent quiz and all color to PhotoHandler class.
			ph.setQuiz(color_list,
					quiz_list.get(Integer.parseInt(selectQuiz[round])));
			// get the quiz picture that has select in floder drawable and show
			// it.
			resourceID = resources.getIdentifier(
					quiz_list.get(Integer.parseInt(selectQuiz[round]))
							.getQuiz(), "drawable",
					"com.example.cottageofsweets_lite");
			quiz.setImageResource(resourceID);
			round = round + 1;
			ShowStage.setText("STAGE " + (round));
		} else {

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					context);

			// set title
			alertDialogBuilder.setTitle("Finish");

			// set dialog message
			alertDialogBuilder
					.setMessage("Thank You!! Your Srore is : " + score)
					.setCancelable(false)
					.setPositiveButton("Main Menu",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// if this button is clicked, just close
									// the dialog box and do nothing
									finish();
									System.exit(0);
								}

							});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			round = 0;
			score = 0;
			ShowStage.setText("STAGE ");
			ShowScore.setText("SCORE " + score);

			// show it
			alertDialog.show();
		}
	}

	public void onResume() {
		super.onResume();
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

	@Override
	public void onBackPressed() {

		// super.onBackPressed(); // Comment this super call to avoid calling
		// finish()
	}

}
