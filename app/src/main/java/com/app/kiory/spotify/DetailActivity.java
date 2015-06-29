package com.app.kiory.spotify;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.app.kiory.spotify.database.DataContract;


public class DetailActivity extends ActionBarActivity implements DetailFragment.Callbacks, TrackDetailFragment.Callbacks{
    private static final String DETAILVIEWFRAGMENT_TAG = "DVGTAG";
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setTitle(getResources().getString(R.string.top_tracks_title));

        Bundle arguments = new Bundle();
        arguments.putParcelable(DetailFragment.DETAIL_URI, getIntent().getData());

        String artistSpotifyId = DataContract.TrackEntry.getTrackSpotifyId(getIntent().getData());
        String artistName = "";

        Cursor artistNameCursor = getContentResolver().query(DataContract.ArtistEntry.buildArtistNameUri(artistSpotifyId), null, null, null, null);

        if ((artistNameCursor != null) && artistNameCursor.moveToFirst()) {
            artistName= artistNameCursor.getString(artistNameCursor.getColumnIndex(DataContract.ArtistEntry.COLUMN_NAME));
        }
        getSupportActionBar().setSubtitle(artistName);

        if (findViewById(R.id.track_detail_container) != null){
            mTwoPane = true;

            if (savedInstanceState == null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.track_detail_container, new TrackDetailFragment(), DETAILVIEWFRAGMENT_TAG)
                        .commit();
            }
        }else{
            mTwoPane = false;
        }

    }


    @Override
    public void onItemSelectedByTrackPosition(Uri contentUri) {
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        android.support.v4.app.Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        TrackDetailFragment player = TrackDetailFragment.newInstance(contentUri);
        player.show(ft, "dialog");
    }

    @Override
    public void onTrackSelected(Uri contentUri) {
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        android.support.v4.app.Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        TrackDetailFragment player = TrackDetailFragment.newInstance(contentUri);
        player.show(ft, "dialog");
    }

}
