package com.app.kiory.spotify;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class TrackDetailActivity extends ActionBarActivity implements TrackDetailFragment.Callbacks {
    private boolean mTwoPane = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_detail);

        setTitle(getResources().getString(R.string.app_name));


        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            arguments.putParcelable(TrackDetailFragment.DETAIL_URI, getIntent().getData());

            TrackDetailFragment fragment = new TrackDetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    @Override
    public void onItemSelectedByTrackPosition(Uri contentUri) {
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        android.support.v4.app.Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
            TrackDetailFragment df = (TrackDetailFragment) prev;
            df.dismiss();
        }
        ft.addToBackStack(null);

        TrackDetailFragment player = TrackDetailFragment.newInstance(contentUri);
        player.show(ft, "dialog");
    }

}
