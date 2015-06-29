package com.app.kiory.spotify.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by darknoe on 10/6/15.
 */
public class DataProvider extends ContentProvider{
    private static final String LOG_TAG = DataProvider.class.getSimpleName();
    private DataDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DataDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (Routes.resolveRoute(uri)) {
            case Routes._ARTISTS: {
                retCursor = dbHelper.getReadableDatabase().query(
                        DataContract.ArtistEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case Routes._ARTIST_NAME: {
                String idPost = DataContract.TrackEntry.getTrackSpotifyId(uri);
                String[] mySelectionArgs = {idPost};

                String[] myProyection= {DataContract.TrackEntry.COLUMN_NAME};

                retCursor = dbHelper.getReadableDatabase().query(
                        DataContract.ArtistEntry.TABLE_NAME,
                        myProyection,
                        DataContract.ArtistEntry.COLUMN_SPOTIFY_ID + " = ?",
                        mySelectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case Routes._TRACKS: {
                String spotifyId = DataContract.TrackEntry.getTrackSpotifyId(uri);
                String[] mySelectionArgs = {spotifyId};

                retCursor = dbHelper.getReadableDatabase().query(
                        DataContract.TrackEntry.TABLE_NAME,
                        projection,
                        DataContract.TrackEntry.COLUMN_ARTIST_ID + " = ?",
                        mySelectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case Routes._TRACK_BY_SPOTIFY_ID: {
                String spotifyId = DataContract.TrackEntry.getTrackSpotifyId(uri);
                String[] mySelectionArgs = {spotifyId};

                retCursor = dbHelper.getReadableDatabase().query(
                        DataContract.TrackEntry.TABLE_NAME,
                        projection,
                        DataContract.TrackEntry.COLUMN_SPOTIFY_ID + " = ?",
                        mySelectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case Routes._TRACK_BY_TRACK_POSITION: {
                String trackPosition = DataContract.TrackEntry.getTrackSpotifyIdByTrackPosition(uri);
                String[] mySelectionArgs = {trackPosition};

                retCursor = dbHelper.getReadableDatabase().query(
                        DataContract.TrackEntry.TABLE_NAME,
                        projection,
                        DataContract.TrackEntry.COLUMN_TRACK_POSITION + " = ?",
                        mySelectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri returnUri;

        switch (Routes.resolveRoute(uri)) {
            case Routes._ARTISTS: {
                long _id = 0;
                if(values != null) {
                    _id = db.insert(DataContract.ArtistEntry.TABLE_NAME, null, values);
                }
                else{

                }
                if ( _id > 0 )
                    returnUri = DataContract.ArtistEntry.buildArtistWithSpotifyIdUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case Routes._TRACKS_ALL: {
                long _id = 0;
                if(values != null) {
                    _id = db.insert(DataContract.TrackEntry.TABLE_NAME, null, values);
                }
                else{

                }
                if ( _id > 0 )
                    returnUri = DataContract.TrackEntry.buildTrackWithIdUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (Routes.resolveRoute(uri)) {
            case Routes._ARTISTS:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        if(value != null){
                            long _id = db.insert(DataContract.ArtistEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            case Routes._TRACKS_ALL:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        if(value != null){
                            long _id = db.insert(DataContract.TrackEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted;

        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (Routes.resolveRoute(uri)) {
            case Routes._ARTISTS:
                rowsDeleted = db.delete(DataContract.ArtistEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case Routes._TRACKS_ALL:
                rowsDeleted = db.delete(DataContract.TrackEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

}

