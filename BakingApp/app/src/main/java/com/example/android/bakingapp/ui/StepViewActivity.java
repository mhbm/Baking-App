package com.example.android.bakingapp.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.data.StepModel;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by lsitec101.macedo on 06/12/17.
 */

public class StepViewActivity extends AppCompatActivity implements ExoPlayer.EventListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    static String SELECTED_STEP = "Selected_STEP";
    static String SELECTED_POSITION = "Selected_POSITION";
    static String SELECTED_TIME_VIDEO_EXOPLAYER = "Selected_TIME_EXOPLAYER";


    private static MediaSessionCompat mMediaSession;
    static ArrayList<StepModel> mSteps;
    static int mPosition;
    static  long mPositionVideo;


    static Button mPrevButton;
    static Button mNextButton;
    static Toast toast;
    static TextView mTextViewStepDescription;
    private static SimpleExoPlayer mExoPlayer;
    private static SimpleExoPlayerView mPlayerView;
    private static PlaybackStateCompat.Builder mStateBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_view);

        mTextViewStepDescription = findViewById(R.id.tv_step_description);
        mPrevButton = findViewById(R.id.button_prev);
        mNextButton = findViewById(R.id.button_next);

        // Initialize the Media Session.
        initializeMediaSession(getBaseContext());

        // Initialize the player view.
        mPlayerView = findViewById(R.id.playerView);


        if (savedInstanceState != null) {
            mPosition = savedInstanceState.getInt(SELECTED_POSITION);
            mSteps = savedInstanceState.getParcelableArrayList(SELECTED_STEP);
            mPositionVideo = savedInstanceState.getLong(SELECTED_TIME_VIDEO_EXOPLAYER);
        } else {
            mSteps = getIntent().getParcelableArrayListExtra(SELECTED_STEP);
            mPosition = getIntent().getIntExtra(SELECTED_POSITION, 0);
        }


        ///put the thumbnail into ExoPlayer
        if (mSteps.get(mPosition).getThumbnailURL() != null && !mSteps.get(mPosition).getThumbnailURL().equals("")) {
            final Uri thumbnailUri = makeURIVideo(mSteps.get(mPosition).getThumbnailURL()).buildUpon().build();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Bitmap thumbnail;
                        thumbnail = Picasso.with(getBaseContext()).load(thumbnailUri).get();
                        mPlayerView.setDefaultArtwork(thumbnail);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        if (!mSteps.get(mPosition).getVideoURL().isEmpty()) {
            initializePlayer(makeURIVideo(mSteps.get(mPosition).getVideoURL()), false);
        } else {
            toast.makeText(getBaseContext(), "This step doesn't have a video!", Toast.LENGTH_SHORT).show();
        }

        try {
            setDesignThisActivity();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        mPrevButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mPositionVideo = 0;
                if (mPosition < 1) {
                    toast.makeText(getBaseContext(), "This is the first step!", Toast.LENGTH_SHORT).show();
                } else {
                    mExoPlayer.stop();

//                    initializePlayer(Uri.parse(String.valueOf(uri)), false);
                    mPosition--;
                    if (!mSteps.get(mPosition).getVideoURL().isEmpty()) {
                        initializePlayer(makeURIVideo(mSteps.get(mPosition).getVideoURL()), true);
                    } else {
                        mExoPlayer.stop();
                        toast.makeText(getBaseContext(), "This step doesn't have a video!", Toast.LENGTH_SHORT).show();
                    }
                    try {
                        setDesignThisActivity();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mPositionVideo = 0;
                if (mPosition > mSteps.size() - 2) {
                    toast.makeText(getBaseContext(), "This is the last step!", Toast.LENGTH_SHORT).show();
                } else {
                    mPosition++;
                    System.out.println("erooooo" + mSteps.get(mPosition).getVideoURL());
                    if (!mSteps.get(mPosition).getVideoURL().isEmpty()) {
                        initializePlayer(makeURIVideo(mSteps.get(mPosition).getVideoURL()), true);
                    } else {
                        mExoPlayer.stop();
                        toast.makeText(getBaseContext(), "This step doesn't have a video!", Toast.LENGTH_SHORT).show();
                    }
                    try {
                        setDesignThisActivity();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public static Uri makeURIVideo(String urlString) {
        return Uri.parse(urlString);
    }

    public static void setDesignThisActivity() throws MalformedURLException, URISyntaxException {
        System.out.println(mSteps.toString());
        System.out.println(mPosition);
        System.out.println(mSteps.get(mPosition).getDescription());
        mTextViewStepDescription.setText(mSteps.get(mPosition).getDescription());
        System.out.println("saiuuuuuuuuuu");
    }



    @Override
    public void onStop() {
        super.onStop();
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
        }
    }

    @Override
    public void onPause() {
        SharedPreferences sharedPreferences = getSharedPreferences("positionVideo", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putLong("timePositionVideo", mExoPlayer.getCurrentPosition());
        edit.commit();
        super.onPause();
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
        }
    }

    @Override
    protected void onResume() {
        SharedPreferences sharedPreferences = getSharedPreferences("positionVideo", MODE_PRIVATE);
        long test  = sharedPreferences.getLong("timePositionVideo", 0);
        mPositionVideo = test;
//        initializePlayer(makeURIVideo(mSteps.get(mPosition).getVideoURL()), false);
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle bundleOut) {
        if (mSteps != null) {
            bundleOut.putParcelableArrayList(SELECTED_STEP, mSteps);
            bundleOut.putInt(SELECTED_POSITION, mPosition);
        }
        if (mExoPlayer != null)
            bundleOut.putLong(SELECTED_TIME_VIDEO_EXOPLAYER, mExoPlayer.getCurrentPosition());

        super.onSaveInstanceState(bundleOut);
    }

    public static void initializeMediaSession(Context context) {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(context, TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }


    private void initializePlayer(Uri mediaUri, boolean work) {
        if (mExoPlayer == null) {
            System.out.println("aquiii");
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(this, "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    this, userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.seekTo(mPositionVideo);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        } else if (work) {

            // Prepare the MediaSource.
            mPlayerView.setPlayer(mExoPlayer);
            String userAgent = Util.getUserAgent(this, "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    this, userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.seekTo(mPositionVideo);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);

        } else {
            ///THIS CODE WORKS WHEN THE APP IS ONPAUSED AND AFTER ON RESUME
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter());
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getBaseContext(), trackSelector);
            mPlayerView.setPlayer(mExoPlayer);
            String userAgent = Util.getUserAgent(this, "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    this, userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.seekTo(mPositionVideo);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
//        showNotification(mStateBuilder.build());

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }


    private static class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }


}
