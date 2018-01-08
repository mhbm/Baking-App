package com.example.android.bakingapp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.NavUtils;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapter.DetailRecipeAdapter;
import com.example.android.bakingapp.data.IngredientModel;
import com.example.android.bakingapp.data.RecipeModel;
import com.example.android.bakingapp.data.StepModel;
import com.example.android.bakingapp.fragment.DetailRecipeFragment;
import com.example.android.bakingapp.widget.UpdateServiceWidget;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
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

import static com.example.android.bakingapp.ui.StepViewActivity.mPosition;
import static com.example.android.bakingapp.ui.StepViewActivity.mPositionVideo;
import static com.example.android.bakingapp.ui.StepViewActivity.mSteps;
import static com.example.android.bakingapp.ui.StepViewActivity.makeURIVideo;
import static com.example.android.bakingapp.ui.StepViewActivity.setDesignThisActivity;


public class DetailRecipeActivity extends AppCompatActivity implements DetailRecipeAdapter.ListItemClickListener, Player.EventListener {


    private static final String TAG = DetailRecipeActivity.class.getSimpleName();
    static String SELECTED_RECIPES = "Selected_Recipes";
    static String SELECTED_STEP = "Selected_STEP";
    static String SELECTED_POSITION = "Selected_POSITION";
    private static SimpleExoPlayer mExoPlayer;
    RecipeModel recipeModel = new RecipeModel();
    DetailRecipeFragment detailRecipeFragment;
    TextView mTextViewIngredient;
    Button mPrevButton;
    Button mNextButton;
    Toast toast;
    TextView mTextViewStepDescription;
    private boolean tabletLayout;
    private SimpleExoPlayerView mPlayerView;

    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        tabletLayout = getResources().getBoolean(R.bool.tabletLayout);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe);

        mTextViewIngredient = findViewById(R.id.tv_recipe_ingredients);

        recipeModel = getIntent().getParcelableExtra(SELECTED_RECIPES);

        if (getSupportFragmentManager().findFragmentByTag(SELECTED_RECIPES) == null) {

            Bundle bundle = new Bundle();
            bundle.putParcelable(SELECTED_RECIPES, recipeModel);
            detailRecipeFragment = new DetailRecipeFragment();
            detailRecipeFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_container, detailRecipeFragment, SELECTED_RECIPES)
                    .addToBackStack(null)
                    .commit();

            setIngredientList();
        }

        this.savedInstanceState = savedInstanceState;

        if (recipeModel != null) {
            putIngredientIntoWidget(recipeModel.getIngredients());
        }

        if (tabletLayout) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            Toolbar menuToolbar = findViewById(R.id.menu_toolbar);
            setSupportActionBar(menuToolbar);
            getSupportActionBar().setTitle(getString(R.string.menu_title));

            menuToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                    onBackPressed();
                }
            });
        }


    }




    public void putIngredientIntoWidget(ArrayList<IngredientModel> ingredientListParameter) {

        if (ingredientListParameter != null) {
            ArrayList<String> ingredientListString = new ArrayList<>();

            //Title of RECIPE
            ingredientListString.add(recipeModel.getName() + "\n");

            for (int i = 0; i < ingredientListParameter.size(); i++) {
                IngredientModel ingredientModel = ingredientListParameter.get(i);

                String aux = "Ingredient : " + ingredientModel.getIngredient() + "; Measure " + ingredientModel.getMeasure() + "; Quantity: " + ingredientModel.getQuantity();

                if (i < recipeModel.getIngredients().size() - 1) {
                    aux += "\n";
                }

                ingredientListString.add(aux);
            }
            UpdateServiceWidget.startWdigetService(ingredientListString, getBaseContext());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        setIngredientList();
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            setIngredientList();
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void setIngredientList() {
        String aux = "";
        for (int i = 0; i < recipeModel.getIngredients().size(); i++) {
            aux += "Ingredient : " + recipeModel.getIngredients().get(i).getIngredient() + "; Measure " + recipeModel.getIngredients().get(i).getMeasure() + "; Quantity: " + recipeModel.getIngredients().get(i).getQuantity();

            if (i < recipeModel.getIngredients().size() - 1) {
                aux += "\n\n";
            }
        }

        mTextViewIngredient.setText(aux);
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
        if (mExoPlayer != null) {
            edit.putLong("timePositionVideo", mExoPlayer.getCurrentPosition());
        } else {
            edit.putLong("timePositionVideo", 0);
        }
        edit.commit();
        super.onPause();
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
        }
    }

    boolean getPlaying = false;

    @Override
    protected void onResume() {
        if (mPlayerView != null) {
            SharedPreferences sharedPreferences = getSharedPreferences("positionVideo", MODE_PRIVATE);
            long test = sharedPreferences.getLong("timePositionVideo", 0);
            getPlaying = sharedPreferences.getBoolean("getPlayisPlaying", false);
            mPositionVideo = test;
            if (mSteps != null) {
                initializePlayer(makeURIVideo(mSteps.get(mPosition).getVideoURL()), false);
            }
        }
        super.onResume();
    }


    /// More Information -> https://developer.android.com/training/implementing-navigation/ancestral.html
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(ArrayList<StepModel> stepModel, int clickItemIndex, String recipeName) {
        Bundle selectedRecipeBundle = new Bundle();
        selectedRecipeBundle.putParcelableArrayList(SELECTED_STEP, stepModel);
        selectedRecipeBundle.putInt(SELECTED_POSITION, clickItemIndex);

        if (!tabletLayout) {
            final Intent intent = new Intent(this, StepViewActivity.class);
            intent.putExtras(selectedRecipeBundle);
            startActivity(intent);
        } else {
            mPositionVideo = 0;
            mSteps = stepModel;
            mPosition = clickItemIndex;
            initializeTabletLayout();
        }

    }


    public void initializeTabletLayout() {
        StepViewActivity.mTextViewStepDescription = findViewById(R.id.tv_step_description);
        mPrevButton = findViewById(R.id.button_prev);
        mNextButton = findViewById(R.id.button_next);
        StepViewActivity.initializeMediaSession(getBaseContext());

        // Initialize the player view.
        mPlayerView = findViewById(R.id.playerView);

        ///put the thumbnail into ExoPlayer
        System.out.println(mPosition);
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


    private void initializePlayer(Uri mediaUri, boolean work) {
        if (mExoPlayer == null) {

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
            mExoPlayer.seekTo(StepViewActivity.mPositionVideo);
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
//        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
//            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
//                    mExoPlayer.getCurrentPosition(), 1f);
//        } else if ((playbackState == ExoPlayer.STATE_READY)) {
//            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
//                    mExoPlayer.getCurrentPosition(), 1f);
//        }
//        mMediaSession.setPlaybackState(mStateBuilder.build());
////        showNotification(mStateBuilder.build());

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
