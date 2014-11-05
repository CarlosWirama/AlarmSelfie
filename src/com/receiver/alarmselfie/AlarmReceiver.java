package com.receiver.alarmselfie;

import java.io.IOException;

import com.alarm.alarmselfie.Alarm;
import com.example.alarmselfie.R;
import com.example.alarmselfie.R.id;
import com.example.alarmselfie.R.layout;
import com.example.alarmselfie.MainActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context k1, Intent k2) {
        // TODO Auto-generated method stub
        Toast.makeText(k1, "Alarm received!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.setClass(k1, Alarm.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        k1.startActivity(intent);

    }
    
}