package com.pgizka.gsenger.conversationView.mediaView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.pgizka.gsenger.R;

public class MediaDetailPlayVideoActivity extends AppCompatActivity {

    public static final String VIDEO_PATH_ARGUMENT = "videoPathArgument";

    private VideoView videoView;
    private String videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_media_detail_play_video);

        videoView = (VideoView) findViewById(R.id.video_view);
        videoPath = getIntent().getStringExtra(VIDEO_PATH_ARGUMENT);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        videoView.setVideoPath(videoPath);
        videoView.requestFocus();

        videoView.start();
    }
}
