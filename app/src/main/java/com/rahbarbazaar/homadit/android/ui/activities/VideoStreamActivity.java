package com.rahbarbazaar.homadit.android.ui.activities;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.danikula.videocache.HttpProxyCacheServer;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.utilities.CustomBaseActivity;


import org.jetbrains.annotations.NotNull;

import static android.view.View.GONE;

public class VideoStreamActivity extends CustomBaseActivity {

    SimpleExoPlayer exoPlayer;
    PlayerView playerView;

    ImageView fullscreenButton;
    boolean fullscreen = false;

    String videoURL;


    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  // to hidden status bar
        setContentView(R.layout.activity_video_stream);


        videoURL = getIntent().getStringExtra("url");

        initView();
//
//        playerView= findViewById(R.id.exoplayerView);
//
//
//        // for access to id we need to add implementation 'com.google.android.exoplayer:exoplayer-ui:2.10.4' to gradle
//        fullscreenButton = playerView.findViewById(R.id.exo_fullscreen_icon);
//
//        exoPlayer = ExoPlayerFactory.newSimpleInstance(getApplicationContext());


        HttpProxyCacheServer proxyServer = new HttpProxyCacheServer.Builder(getApplicationContext()).maxCacheSize(1024 * 1024 * 1024).build();

        String proxyURL = proxyServer.getProxyUrl(videoURL);


        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(),
                Util.getUserAgent(getApplicationContext(), getApplicationContext().getPackageName()));


        playerView.setPlayer(exoPlayer);
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
//        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);

        exoPlayer.prepare(new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(proxyURL)));

        exoPlayer.setPlayWhenReady(true);
        exoPlayer.getPlaybackState();


        fullscreenButton.setOnClickListener(view -> {
            if(fullscreen) {
                fullscreenButton.setImageDrawable(ContextCompat.getDrawable(VideoStreamActivity.this, R.drawable.ic_fullscreen_open));

                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

                if(getSupportActionBar() != null){
                    getSupportActionBar().show();
                }

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
//                    params.width = params.MATCH_PARENT;
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                    params.height = (int) ( 300 * getApplicationContext().getResources().getDisplayMetrics().density);
                params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                playerView.setLayoutParams(params);

                fullscreen = false;
            }else{
                fullscreenButton.setImageDrawable(ContextCompat.getDrawable(VideoStreamActivity.this, R.drawable.ic_fullscreen_close));

                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                        |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

                if(getSupportActionBar() != null){
                    getSupportActionBar().hide();
                }

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;


//                    DisplayMetrics displayMetrics = new DisplayMetrics();
//                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//                    params.height = displayMetrics.heightPixels;
                params.height = ViewGroup.LayoutParams.MATCH_PARENT;
//                    params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                playerView.setLayoutParams(params);

                fullscreen = true;
            }
        });


        exoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(@NotNull TrackGroupArray trackGroups, @NotNull TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

                if(isLoading){
                    progressBar.setVisibility(View.VISIBLE);
                }else{

                    progressBar.setVisibility(GONE);
                }
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {

                    case Player.STATE_BUFFERING:

                        if (progressBar != null) {
                            progressBar.setVisibility(View.VISIBLE);
                        }

                        break;
                    case Player.STATE_ENDED:
                        Log.d("TAG", "onPlayerStateChanged: Video ended.");

//                            exoPlayer.seekTo(0);
//                            exoPlayer.release();

                        break;
                    case Player.STATE_IDLE:

                        break;
                    case Player.STATE_READY:
                        Log.e("TAG", "onPlayerStateChanged: Ready to play.");


                        if (progressBar != null) {
                            progressBar.setVisibility(GONE);
                        }

                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(@NotNull ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(@NotNull PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });




        //to pause other application playing music while our app is playing
        AudioManager mAudioManager_ = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = mAudioManager_.requestAudioFocus(focusChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
        }



    }

    private void initView() {

        exoPlayer = ExoPlayerFactory.newSimpleInstance(getApplicationContext());
        playerView= findViewById(R.id.exoplayerView);
        progressBar = findViewById(R.id.progressbar);
        // for access to id we need to add implementation 'com.google.android.exoplayer:exoplayer-ui:2.10.4' to gradle
        fullscreenButton = playerView.findViewById(R.id.exo_fullscreen_icon);

    }

    //to pause other application playing music while our app is playing
    AudioManager.OnAudioFocusChangeListener focusChangeListener = new AudioManager.OnAudioFocusChangeListener()
    {   @Override
    public void onAudioFocusChange (int focusChange)
    {   switch (focusChange)
    {   case AudioManager.AUDIOFOCUS_GAIN:
        Log.e("DEBUG", "##### AUDIOFOCUS_GAIN");
        break;
        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
            Log.e("DEBUG", "##### AUDIOFOCUS_LOSS_TRANSIENT");
            break;
        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
            Log.e("DEBUG", "##### AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
            break;
        case AudioManager.AUDIOFOCUS_LOSS:
            Log.e("DEBUG", "##### AUDIOFOCUS_LOSS");
            break;
    }
    }
    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exoPlayer.stop();
    }

    @Override
    public void onPause() {
        super.onPause();
        exoPlayer.setPlayWhenReady(false);
    }

    @Override
    public void onDestroy() {
        exoPlayer.release();
        super.onDestroy();
    }

}