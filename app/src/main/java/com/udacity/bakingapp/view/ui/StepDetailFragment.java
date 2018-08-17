package com.udacity.bakingapp.view.ui;

import android.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.udacity.bakingapp.R;
import com.udacity.bakingapp.service.model.Recipe;
import com.udacity.bakingapp.service.model.Step;
import com.udacity.bakingapp.utility.DisplayMetricUtils;
import com.udacity.bakingapp.view.adapter.RecipeListAdapter;
import com.udacity.bakingapp.viewmodel.RecipeListViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class StepDetailFragment extends Fragment implements ExoPlayer.EventListener {


    //private static MediaSessionCompat mMediaSession;
    @BindView(R.id.tv_step_short_description)
    TextView tvStepShortDescription;
    @BindView(R.id.tv_step_description)
    TextView tvStepDescription;
    @BindView(R.id.video_player_view)
    SimpleExoPlayerView videoPlayerView;
    @BindView(R.id.pb_buffering)
    ProgressBar pbBuffering;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    private Step step;
    private boolean isTablet;
    private SimpleExoPlayer mExoPlayer;
    //private StepActionListener stepActionListener;
    private TrackSelector trackSelector;
    //private PlaybackStateCompat.Builder mStateBuilder;
    private String videoUrl;
    private long currentVideoPosition;
    private Unbinder unbinder;
    private long position;


    public StepDetailFragment() {
        // Required empty public constructor
    }

    /*public static StepDetailFragment newInstance(Step step, boolean isTablet) {
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("step", step);
        bundle.putBoolean("isTablet", isTablet);
        stepDetailFragment.setArguments(bundle);

        return stepDetailFragment;
    }*/

    public Step getCurrentStep() {
        return step;
    }

    public void setOrientation(int orientation) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ViewGroup.LayoutParams layoutParams =
                    videoPlayerView.getLayoutParams();
            layoutParams.width = DisplayMetricUtils.getDeviceWidth(getActivity());
            layoutParams.height = DisplayMetricUtils.getDeviceHeight(getActivity());
            videoPlayerView.setLayoutParams(layoutParams);

            tvStepDescription.setVisibility(View.GONE);
            tvStepShortDescription.setVisibility(View.GONE);
        } else {
            ViewGroup.LayoutParams layoutParams =
                    videoPlayerView.getLayoutParams();
            layoutParams.width = DisplayMetricUtils.getDeviceWidth(getActivity());
            layoutParams.height = (int) (9.0f / 16.0f * layoutParams.width);
            videoPlayerView.setLayoutParams(layoutParams);

            tvStepDescription.setVisibility(View.VISIBLE);
            tvStepShortDescription.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);
        unbinder = ButterKnife.bind(this,view);

        if (savedInstanceState != null && savedInstanceState.containsKey("VideoPosition")) {
            position = savedInstanceState.getLong("VideoPosition");
            //mPlayWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY_KEY);
        }

        Bundle bundle = this.getArguments();
        step = getArguments().getParcelable("step");
        isTablet = getArguments().getBoolean("isTablet");

        tvStepShortDescription.setText(step.getShortDescription());
        tvStepDescription.setText(step.getDescription());

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            videoPlayerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            videoPlayerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        }

        videoUrl = step.getVideoURL();
        initializePlayer();
        setData();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong("VideoPosition", position);
        //outState.putBoolean(PLAY_WHEN_READY_KEY, mPlayWhenReady);
    }

    private void setData(){

    }

    private void initializePlayer() {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            videoPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            // exoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getActivity(), getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoUrl), new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);

            if(position != 0)
                mExoPlayer.seekTo(position);
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            //mPlayWhenReady = mExoPlayer.getPlayWhenReady();
            position = mExoPlayer.getCurrentPosition();

            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initializePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        //initializeMediaSession();
        //initializePlayer();
        releasePlayer();
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

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }
}
