package com.example.cottageofsweets_lite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.CalendarContract.Colors;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class PhotoHandler implements PictureCallback {

	public final Context context;
	public TextView Show;
	Vector<getColor> listColor = new Vector<getColor>();
	getQuiz quiz;
	int score = 0;

	String ans = "Correct";
	GamePlay GP;
	Vector<Integer> vAnsColor = new Vector<Integer>();
	Vector<Integer> vUserColor = new Vector<Integer>();
	Vector<Integer> vUserAnswer = new Vector<Integer>();
	int answerNumber = -1;
	int answerUserNumber = -2;

	public PhotoHandler(Context context) {
		this.context = context;
	}

	// @Override
	public void onPictureTaken(byte[] data, Camera camera) {

		File pictureFileDir = getDir();

		if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

			Log.d(GamePlay.DEBUG_TAG, "Can't create directory to save image.");
			Toast.makeText(context, "Can't create directory to save image.",
					Toast.LENGTH_LONG).show();

		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
		String date = dateFormat.format(new Date());
		String photoFile = "Picture_" + date + ".jpg";

		String filename = pictureFileDir.getPath() + File.separator + photoFile;

		File pictureFile = new File(filename);

		try {
			FileOutputStream fos = new FileOutputStream(pictureFile);
			fos.write(data);
			fos.close();

			Toast.makeText(context, "New Image saved:" + photoFile,
					Toast.LENGTH_LONG).show();

			// <!-- Check Color -->

			// /////////////////////////////////////////////////////////////

			Bitmap b = BitmapFactory.decodeFile(filename);
			//---------------------------------------------//
			
		//	Bitmap b2 = Bitmap.createBitmap(changeColorTo16(b));
			
			
		Bitmap b2 = b.copy(Bitmap.Config.RGB_565, true);
			
			FileOutputStream out = null;
			try {
			    out = new FileOutputStream("storage/emulated/0/Pictures/testpic3/test.png");
			    b2.compress(Bitmap.CompressFormat.JPEG, 0, out); // bmp is your Bitmap instance
			    // PNG is a lossless format, the compression factor (100) is ignored
			} catch (Exception e) {
			    e.printStackTrace();
			} finally {
			    try {
			        if (out != null) {
			            out.close();
//			            Log.i("Bitmap", "Color Pos can save");
			        }
			    } catch (IOException e) {
			        e.printStackTrace();
//			        Log.i("Bitmap", "Color Pos can't save");
			    }
			}
			
			//---------------------------------------------//
			
			Toast.makeText(context, "Coverted to Bitmap :" + photoFile,
					Toast.LENGTH_LONG).show();

			Log.i(GamePlay.DEBUG_TAG, "quiz : " + quiz.getQuiz()
					+ " totalcolor : " + listColor.size());

//			checkColorAnswer(listColor, quiz, b2);

			// /////////////////////////////////////////////////////

//			pictureFile.delete();

			Toast.makeText(context, "Image Removed:" + photoFile,
					Toast.LENGTH_LONG).show();

		} catch (Exception error) {
			Log.d(GamePlay.DEBUG_TAG,
					"File" + filename + "not saved: " + error.getMessage());
			Toast.makeText(context, "Image could not be saved.",
					Toast.LENGTH_LONG).show();
		}

	}

	private Bitmap changeColorTo16(Bitmap b) {
		int[][] bColor = {{255,255,255},{0,255,255},{255,0,255},{0,0,255},{192,192,192},{128,128,128},{0,128,128},{128,0,128},{0,0,128},{255,255,0},{0,255,0},{128,128,0},{0,128,0},{255,0,0},{128,0,0},{0,0,0}};
		int x,y,i;
		int minDiff = 0,minPos = 0,temp;
		int newR = 0,newG = 0,newB = 0;
		
		for(x=0;x<b.getWidth();x++){
			for(y=0;y<b.getHeight();y++){
				int pixel = b.getPixel(x, y);
				int redValue = Color.red(pixel);
				int blueValue = Color.blue(pixel);
				int greenValue = Color.green(pixel);
				for(i=0;i<bColor.length;i++){
					temp = Math.abs((redValue - bColor[i][0])) + Math.abs((greenValue - bColor[i][1])) + Math.abs((blueValue - bColor[i][2]));
					if(minDiff == 0){
						minDiff = temp;
						minPos = 0;
					}
					else{
						if(minDiff > temp){
							minDiff = temp;
							minPos = i;
						}
					}
				}
				newR = bColor[minPos][0];
				newG = bColor[minPos][1];
				newB = bColor[minPos][2];
				Log.d("Bitmap", "Color Position : "+minPos);
				b.setPixel(x, y, Color.rgb(newR, newG, newB)); //<--- Problem can't fix
				minPos = 0;
				minDiff = 0;
				Log.d("Bitmap", "Color Position : "+minPos);
			}
		}

		return b;
	}

	private File getDir() {
		File sdDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		return new File(sdDir, "testpic3");
	}

	// check that picture answer that user input.
	void checkColorAnswer(Vector<getColor> listColor, getQuiz quiz,
			Bitmap bitmap) {
		// set default
		ans = "Correct";
		int x = 0;
		int y = 0;
		int i = 0, j = 0;
		int redChange = 0, greenChange = 0, blueChange = 0;
		vAnsColor.clear();
		vUserAnswer.clear();
		vUserColor.clear();
		answerNumber = -1;
		
		// get answer of select quiz (sent from GamePlay class : function
		// selectQuiz) from database.
		String[] Ans;
		Ans = quiz.getAnswer().split(",");

		// get the answer color into vAnsColor (this answer color will be
		// correct because it from database)
		for (i = 0; i < listColor.size(); i++) {

			if (j >= Ans.length)
				break;

			else {

				if (i == Integer.parseInt(Ans[j])) {
					answerNumber = i;
					i = 0;
					j = j + 1;

				}
			}
		}

		// check that it has the black answer board.
		boolean isBG = false;

		// detect a pixel in picture that have the same color answer or not.
		int[] targetcount = new int[10];
		for (i = 0; i < targetcount.length; i++) {
			targetcount[i] = 0;
		}

		int ci = 0;
		for (y = (9 * bitmap.getHeight() / 10); y > (15 * (bitmap.getHeight() / 100));) {

			for (x = (9 * bitmap.getWidth() / 10); x > (6 * (bitmap.getWidth() / 10));) {

				// get color of each pixel.
				int pixel = bitmap.getPixel(x, y);
				int redValue = Color.red(pixel);
				int blueValue = Color.blue(pixel);
				int greenValue = Color.green(pixel);
				
				Log.d("Color", "no."+ci+" R,G,B : "+redValue+","+greenValue+","+blueValue);
				ci++;
//--------------------------- IMCOMPLETE
//				if ((0 <= redValue && redValue <= 70)
//						&& (0 <= greenValue && greenValue <= 101)
//						&& (0 <= blueValue && blueValue <= 110) && !isBG) {
//					isBG = true;
//					redChange = abs(redValue, 20);
//					greenChange = abs(greenValue, 41);
//					blueChange = abs(blueValue, 44);
//					Log.d("BG", "R,G,B : Find");
//					x = 9 * bitmap.getWidth() / 10;
//					y = 9 * bitmap.getHeight() / 10;
//					if (redChange < 20 || redChange > 40)
//						redChange = 20;
//					if (blueChange < 20 || blueChange > 40)
//						blueChange = 20;
//					if (greenChange < 20 || greenChange > 40)
//						greenChange = 20;
//
//				}
				for (i = 0; i < listColor.size(); i++) {
					if ((listColor.get(i).getRed() - redChange <= redValue && redValue <= listColor
							.get(i).getRed() + redChange)
							&& (listColor.get(i).getGreen() - greenChange <= greenValue && greenValue <= listColor
									.get(i).getGreen() + greenChange)
							&& (listColor.get(i).getBlue() - blueChange <= blueValue && blueValue <= listColor
									.get(i).getBlue() + blueChange)) {
						targetcount[i]++;
					}
				}
				x = x - 10;
			}
			y = y - 10;
		}

		for (i = 0; i < targetcount.length; i++) {
			if (targetcount[i] >= 30) {
				vUserAnswer.add(i);
			}
		}

		if (vUserAnswer.size() == 1) {
			answerUserNumber = vUserAnswer.get(0);
		} else if (vUserAnswer.size() == 2) {
			answerUserNumber = (vUserAnswer.get(1) * 10) + vUserAnswer.get(0);
		} else if (vUserAnswer.size() == 3) {
			answerUserNumber = (vUserAnswer.get(2) * 100)
					+ (vUserAnswer.get(1) * 10) + vUserAnswer.get(0);
		}

		if (answerNumber == answerUserNumber)
			ans = "Correct";
		else
			ans = "Wrong";
	
		Log.d("Ans", "R,G,B : " + ans);
	}

//	private int abs(int i, int j) {
//		if (i > j)
//			return i - j;
//		else
//			return j - i;
//	}

	public boolean getResultAnswer() {
		if (ans.equalsIgnoreCase("Correct")) {
			return true;
		} else {
			return false;
		}
	}

	public void setQuiz(Vector<getColor> Color, getQuiz selectQuiz) {
		listColor = Color;
		quiz = selectQuiz;
	}

}
