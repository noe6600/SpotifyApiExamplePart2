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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.app.kiory.spotify.database.DataContract;
import com.app.kiory.spotify.datastructure.SharedPreferences;
import com.app.kiory.spotify.webservice.Webservice;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private int listPosition = ListView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";
    private static final int ARTISTS_LOADER = 0;
    private ArtistsAdapter artistsAdapter;
    private ContentResolver mContentResolver;

    public MainFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mContentResolver = getActivity().getContentResolver();

        artistsAdapter = new ArtistsAdapter(getActivity(), null, 0);
        ListView artistListView = (ListView) rootView.findViewById(R.id.listView);
        artistListView.setAdapter(artistsAdapter);

        artistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
            if (cursor != null) {
                ((Callbacks)getActivity()).onItemSelected(DataContract.TrackEntry.buildTrackUri(cursor.getString(cursor.getColumnIndex(DataContract.ArtistEntry.COLUMN_SPOTIFY_ID))));
            }
            listPosition = position;
            }
        });

        if ( (savedInstanceState != null) && (savedInstanceState.containsKey(SELECTED_KEY)) ){
            listPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        EditText artistNameSearch = (EditText) rootView.findViewById(R.id.editText);
        artistNameSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Webservice.searchArtist(SharedPreferences.getAccessToken(getActivity()), s.toString(), mContentResolver);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(ARTISTS_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    public interface Callbacks {
        public void onItemSelected(Uri postUri);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(),
                DataContract.ArtistEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data.getCount() == 0) Toast.makeText(getActivity(), getActivity().getString(R.string.artist_not_found), Toast.LENGTH_SHORT).show();
        artistsAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        artistsAdapter.swapCursor(null);
    }
}
