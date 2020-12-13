package com.goldenappstudio.coachinginstitutes2020;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

import java.net.URI;
import java.time.Duration;

public class VideoPlayer extends AppCompatActivity {

    StyledPlayerView playerView;
    SimpleExoPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_player);

        playerView = findViewById(R.id.player_view);
        player = new SimpleExoPlayer.Builder(VideoPlayer.this).build();
        playerView.setPlayer(player);

        Log.d("ERROR", "This is a test error message");

        StorageReference reference = FirebaseStorage.getInstance().getReference("store/videos");
        reference.child(getIntent().getExtras().getString("video_id") + ".mp4")
                .getDownloadUrl().addOnSuccessListener(uri -> {
            // Build the media item.
            MediaItem mediaItem = MediaItem.fromUri(uri);
            // Set the media item to be played.
            player.setMediaItem(mediaItem);
            // Prepare the player.
            player.prepare();
            // Start the playback.
            player.play();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.release();
    }
}