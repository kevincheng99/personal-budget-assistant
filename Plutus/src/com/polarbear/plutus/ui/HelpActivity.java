package com.polarbear.plutus.ui;

import com.example.plutus.R;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.widget.VideoView;

public class HelpActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.help);
	VideoView vv = (VideoView) findViewById(R.id.help_vv);
	vv.setVideoPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath() + "/Foo.mp4");
	vv.setMediaController(null);
	vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mp) 
        {
        	finish();
        }
    });
	vv.start();
  }


}
