package com.app.kiory.spotify.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by darknoe on 10/6/15.
 */
public class DataContract {
    public static final String CONTENT_AUTHORITY = "com.app.kiory.spotify";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_ARTISTS = "artists";
    public static final String PATH_TRACKS = "tracks";


    public static final class ArtistEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ARTISTS).build();


        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ARTISTS;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ARTISTS;


        public static final String TABLE_NAME = "artists";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SPOTIFY_ID = "spotify_id";
        public static final String COLUMN_THUMBNAIL = "thumbnail";

        public static Uri buildArtistWithSpotifyIdUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildArtistNameUri(String spotifyId) {
            return CONTENT_URI.buildUpon().appendPath(spotifyId).build();
        }

        public static String getArtistSpotigyId(Uri uri) {
            return uri.getPathSegments().get(1);
        }

    }

    public static final class TrackEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRACKS).build();


        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRACKS;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRACKS;


        public static final String TABLE_NAME = "tracks";

        public static final String COLUMN_NAME= "name";
        public static final String COLUMN_ALBUM= "album";
        public static final String COLUMN_ARTIST_ID= "artist_id";
        public static final String COLUMN_SPOTIFY_ID = "spotify_id";
        public static final String COLUMN_THUMBNAIL = "thumbnail";
        public static final String COLUMN_PREVIEW_URL = "preview_url";
        public static final String COLUMN_TRACK_POSITION = "track_position";


        public static Uri buildTrackWithIdUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildTrackUri(String spotifyId) {
            return CONTENT_URI.buildUpon().appendPath(spotifyId).build();
        }

        public static Uri buildTrackDetailUri(String spotifyId) {
            return CONTENT_URI.buildUpon().appendPath(spotifyId).appendPath("detail").build();
        }

        public static Uri buildTrackDetailUriByTrackPosition(String position) {
            return CONTENT_URI.buildUpon().appendPath(position).appendPath("detail").appendPath("bytrackposition").build();
        }

        public static String getTrackSpotifyId(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getTrackSpotifyIdByTrackPosition(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
