package com.goldenappstudio.coachinginstitutes2020;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

import java.net.URI;
import java.time.Duration;

public class VideoPlayer extends AppCompatActivity {


    private ProgressBar _BufferProgress;
    View mBottomLayout;
    View mVideoLayout;
    UniversalVideoView mVideoView;
    UniversalMediaController mMediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        _BufferProgress = findViewById(R.id.buffer_progress);

        mVideoLayout = findViewById(R.id.video_layout);
        mVideoView = (UniversalVideoView) findViewById(R.id.videoView);
        mMediaController = (UniversalMediaController) findViewById(R.id.media_controller);
        mVideoView.setMediaController(mMediaController);

        Uri _VideoURI = Uri.parse("https://firebasestorage.googleapis.com/v0/b/coaching-institute-project.appspot.com/o/video_courses%2Fvideos%2Fsample-mp4-file.mp4?alt=media&token=cc0740f6-9827-496f-a0f9-7c6c76550b82");

        mVideoView.start();
        mMediaController.hideLoading();
        mMediaController.setTitle("Air Force full course");
        mVideoView.requestFocus();
        mVideoView.canSeekForward();
        mVideoView.setVideoURI(_VideoURI);
        mVideoView.setAutoRotation(true);
        mVideoView.setVideoViewCallback(new UniversalVideoView.VideoViewCallback() {
            private static final String TAG = "VIDEO_VIEW";

            @Override
            public void onScaleChange(boolean isFullscreen) {
                if (isFullscreen) {
                    ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    mVideoLayout.setLayoutParams(layoutParams);
                   // mVideoView.getCurrentPosition();
                    mVideoView.seekTo(8000);
                    mVideoView.start();
                    //GONE the unconcerned views to leave room for video and controller
                    //  mBottomLayout.setVisibility(View.GONE);
                } else {
                    ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = 265;
                    mVideoLayout.setLayoutParams(layoutParams);
                    // mBottomLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPause(MediaPlayer mediaPlayer) { // Video pause
                Log.d(TAG, "onPause UniversalVideoView callback");
            }

            @Override
            public void onStart(MediaPlayer mediaPlayer) { // Video start/resume to play
                Log.d(TAG, "onStart UniversalVideoView callback");
            }

            @Override
            public void onBufferingStart(MediaPlayer mediaPlayer) {// steam start loading
                Log.d(TAG, "onBufferingStart UniversalVideoView callback");
            }

            @Override
            public void onBufferingEnd(MediaPlayer mediaPlayer) {// steam end loading
                Log.d(TAG, "onBufferingEnd UniversalVideoView callback");
            }

        });

        // Check if Video is buffering..
        mVideoView.setOnInfoListener((mp, what, extra) -> {
            switch (what) {
                case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: {
                    _BufferProgress.setVisibility(View.GONE);
                    return true;
                }
                case MediaPlayer.MEDIA_INFO_BUFFERING_START: {
                    _BufferProgress.setVisibility(View.VISIBLE);
                    return true;
                }
                case MediaPlayer.MEDIA_INFO_BUFFERING_END: {
                    _BufferProgress.setVisibility(View.INVISIBLE);
                    return true;
                }
            }
            return false;
        });

        /*_VideoView.setOnPreparedListener(mp -> {
            _Duration = mp.getDuration() / 1000; // converted in seconds by dividing 1000
            _TimeDuration.setText(String.format("%02d:%02d", _Duration / 60, _Duration % 60));
        });

        _VideoView.start();
        _IsPlaying = true;
        _PlayButton.setImageResource(R.drawable.pause_button);

        // Play & Pause button in action..
        _PlayButton.setOnClickListener(v -> {
            if (_IsPlaying) {
                _VideoView.pause();
                _IsPlaying = false;
                _PlayButton.setImageResource(R.drawable.play_button);
            } else {
                _VideoView.start();
                _IsPlaying = true;
                _PlayButton.setImageResource(R.drawable.pause_button);
            }
        });*/

    }
}