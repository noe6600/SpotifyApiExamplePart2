package com.app.kiory.spotify.webservice;

/**
 * Created by darknoe on 10/6/15.
 */

import android.content.ContentResolver;
import android.content.ContentValues;

import com.app.kiory.spotify.database.DataContract;

import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 *  REST webservice to fetch Spotify web api
 */
public class Webservice {

    public static void searchArtist(String token, String name, final ContentResolver mContentResolver){
        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(token);
        SpotifyService spotify = api.getService();

        spotify.searchArtists(name, new Callback<ArtistsPager>() {
            @Override
            public void success(ArtistsPager artistsPager, Response response) {
                mContentResolver.delete(DataContract.ArtistEntry.CONTENT_URI, null, null);
                int i = 0;
                ContentValues[] values = new ContentValues[artistsPager.artists.items.size() + 1];
                for(Artist artist : artistsPager.artists.items){
                    String thumbnail = "";
                    int thumbnail_size = 0;
                    for(int j=0; j<artist.images.size(); j++){
                        //We will save the bigger thumbnail image so it can have good resolution on tablet
                        if(thumbnail_size < artist.images.get(j).width){
                            thumbnail_size = artist.images.get(j).width;
                            thumbnail = artist.images.get(j).url;
                        }
                    }
                    values[i] = setArtistValues(artist.id, artist.name, thumbnail);
                    i++;
                }
                mContentResolver.bulkInsert(DataContract.ArtistEntry.CONTENT_URI, values);
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    private static ContentValues setArtistValues(String spotifyId, String name, String thumbnail) {
        ContentValues values = new ContentValues();
        values.put(DataContract.ArtistEntry.COLUMN_SPOTIFY_ID, spotifyId);
        values.put(DataContract.ArtistEntry.COLUMN_NAME, name);
        values.put(DataContract.ArtistEntry.COLUMN_THUMBNAIL, thumbnail);

        return values;
    }

    public static void searchTopTracks(String token, String spotifyId, final ContentResolver mContentResolver){
        final String artistId = spotifyId;
        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(token);
        SpotifyService spotify = api.getService();

        Map options = new HashMap<String, String>();
        options.put("country", "us");

        spotify.getArtistTopTrack(spotifyId, options, new Callback<Tracks>() {
            @Override
            public void success(Tracks tracks, Response response) {
                mContentResolver.delete(DataContract.TrackEntry.CONTENT_URI, null, null);
                int i = 0;
                ContentValues[] values = new ContentValues[tracks.tracks.size() + 1];

                for(Track track : tracks.tracks){
                    String thumbnail = "";
                    int thumbnail_size = 0;
                    for (int j = 0; j < track.album.images.size(); j++) {
                        //We will save the bigger thumbnail image so it can have good resolution on tablet
                        if (thumbnail_size < track.album.images.get(j).width) {
                            thumbnail_size = track.album.images.get(j).width;
                            thumbnail = track.album.images.get(j).url;
                        }
                    }
                    values[i] = setTrackValues(artistId, track.id, track.name, track.album.name, thumbnail, track.preview_url, i);
                    i++;
                }
                mContentResolver.bulkInsert(DataContract.TrackEntry.CONTENT_URI, values);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private static ContentValues setTrackValues(String artistId, String spotifyId, String name, String album, String thumbnail, String previewUrl, int trackPosition) {
        ContentValues values = new ContentValues();
        values.put(DataContract.TrackEntry.COLUMN_SPOTIFY_ID, spotifyId);
        values.put(DataContract.TrackEntry.COLUMN_ARTIST_ID, artistId);
        values.put(DataContract.TrackEntry.COLUMN_NAME, name);
        values.put(DataContract.TrackEntry.COLUMN_ALBUM, album);
        values.put(DataContract.TrackEntry.COLUMN_THUMBNAIL, thumbnail);
        values.put(DataContract.TrackEntry.COLUMN_PREVIEW_URL, previewUrl);
        values.put(DataContract.TrackEntry.COLUMN_TRACK_POSITION, trackPosition);

        return values;
    }

}
