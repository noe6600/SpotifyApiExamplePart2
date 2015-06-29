package com.app.kiory.spotify;

/**
 * Created by darknoe on 11/6/15.
 */

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.app.kiory.spotify.database.DataContract;
import com.app.kiory.spotify.datastructure.SharedPreferences;
import com.app.kiory.spotify.webservice.Webservice;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String SELECTED_KEY = "selected_position";
    private int listPosition = ListView.INVALID_POSITION;
    private static final int TRACKS_LOADER = 1;
    static final String DETAIL_URI = "URI";
    private TrackAdapter trackAdapter;
    ContentResolver mContentResolver;
    private Uri mUri;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mContentResolver = getActivity().getContentResolver();
        Bundle arguments = getArguments();
        if (arguments != null){
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
            Webservice.searchTopTracks(SharedPreferences.getAccessToken(getActivity()), DataContract.TrackEntry.getTrackSpotifyId(mUri), mContentResolver);
        }

        trackAdapter = new TrackAdapter(getActivity(), null, 0);
        ListView trackList = (ListView) rootView.findViewById(R.id.detail_listView);
        trackList.setAdapter(trackAdapter);

        trackList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
            if (cursor != null) {
                ((Callbacks)getActivity()).onTrackSelected(DataContract.TrackEntry.buildTrackDetailUri(cursor.getString(cursor.getColumnIndex(DataContract.TrackEntry.COLUMN_SPOTIFY_ID))));
            }
            listPosition = position;
            }
        });

        if ( (savedInstanceState != null) && (savedInstanceState.containsKey(SELECTED_KEY)) ){
            listPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return rootView;
    }

    public interface Callbacks {
        public void onTrackSelected(Uri postUri);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(TRACKS_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(mUri != null){
            return new CursorLoader(getActivity(),
                    mUri,
                    null,
                    null,
                    null,
                    null);
        }
        else return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        trackAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        trackAdapter.swapCursor(null);
    }
}
