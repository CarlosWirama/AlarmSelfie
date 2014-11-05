package com.alarm.alarmselfie;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alarmselfie.R;

public class Alarm extends Activity {

	public MediaPlayer mMediaPlayer;
    Button buttonstartSetDialog;
    TextView textAlarmOpen;
    TimePickerDialog timePickerDialog;
    final static int RQS_1 = 0;
    
    //CONSTANT
    public static final int CAMERA_REQUEST = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);
		
	    playSound(getBaseContext(), getAlarmUri());
    	textAlarmOpen = (TextView) findViewById(R.id.alarmopen);
    	buttonstartSetDialog = (Button) findViewById(R.id.stopAlarm);
        buttonstartSetDialog.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
            	Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            	intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            	startActivityForResult(intent, CAMERA_REQUEST);
            }
        });
	}
	
	
	private void saveImage(Bitmap photo) {

	    String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
	    OutputStream outStream = null;
	    File file = new File(extStorageDirectory, "selfie.JPEG");
	    
		
	    try {
	    	outStream = new FileOutputStream(file);
			photo.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
			outStream.flush();
			outStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
	
	private int scanFaces(Bitmap bitmap){
		int facesDetected = 0;
		FaceDetector detector = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), 2);
		Face[] faces = new FaceDetector.Face[2];
		facesDetected = detector.findFaces(bitmap, faces);
		
		return facesDetected;
	}
	
	private Bitmap getBitmap(Bitmap bitmap, Bitmap.Config config) {
	    Bitmap convertedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), config);
	    Canvas canvas = new Canvas(convertedBitmap);
	    Paint paint = new Paint();
	    paint.setFilterBitmap(true);
	    canvas.drawBitmap(bitmap, 0, 0, paint);
	    return convertedBitmap;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){
			Toast.makeText(getBaseContext(), "UDAH FOTO", Toast.LENGTH_SHORT).show();
			Bitmap photo = (Bitmap) data.getExtras().get("data");
			
			if(scanFaces(getBitmap(photo, Bitmap.Config.RGB_565)) > 0) {
				Toast.makeText(getBaseContext(), "ADA MUKA", Toast.LENGTH_SHORT).show();
				saveImage(photo);
				//upload instagram
				mMediaPlayer.stop();
			}else {
				Toast.makeText(getBaseContext(), "GA ADA MUKA", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            	intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            	startActivityForResult(intent, CAMERA_REQUEST);
			}
			
//			Intent intent = getPackageManager().getLaunchIntentForPackage("com.instagram.android");
//	        if (intent != null)
//	        {
//	            Intent shareIntent = new Intent();
//	            shareIntent.setAction(Intent.ACTION_SEND);
//	            shareIntent.setPackage("com.instagram.android");
//	            try {
//	                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), imagePath, "I am Happy", "Share happy !")));
//	            } catch (FileNotFoundException e) {
//	                // TODO Auto-generated catch block
//	                e.printStackTrace();
//	            }
//	            shareIntent.setType("image/jpeg");
//
//	            startActivity(shareIntent);
//	        }
//	        else
//	        {
//	            // bring user to the market to download the app.
//	            // or let them choose an app?
//	            intent = new Intent(Intent.ACTION_VIEW);
//	            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	            intent.setData(Uri.parse("market://details?id="+"com.instagram.android"));
//	            startActivity(intent);
//	        }
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void playSound(Context context, Uri alert) {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (IOException e) {
            System.out.println("OOPS");
        }
    }
    

	//Get an alarm sound. Try for an alarm. If none set, try notification,
    //Otherwise, ringtone.
    private Uri getAlarmUri() {
        Uri alert = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null) {
            alert = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null) {
                alert = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return alert;
    }
    
}
