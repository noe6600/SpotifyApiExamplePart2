package com.app.kiory.spotify.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by darknoe on 10/6/15.
 */
public class DataDbHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "kiory-spotify.db";

    public DataDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createArtistTable());
        db.execSQL(createTracksTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.ArtistEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.TrackEntry.TABLE_NAME);
        onCreate(db);
    }

    private String createArtistTable(){
        final String sql = "CREATE TABLE " + DataContract.ArtistEntry.TABLE_NAME + " (" +
                DataContract.ArtistEntry._ID + " INTEGER PRIMARY KEY," +
                DataContract.ArtistEntry.COLUMN_SPOTIFY_ID + " TEXT, " +
                DataContract.ArtistEntry.COLUMN_NAME + " TEXT, " +
                DataContract.ArtistEntry.COLUMN_THUMBNAIL + " TEXT, " +
                " UNIQUE ("+DataContract.ArtistEntry.COLUMN_SPOTIFY_ID+")"+
                " ON CONFLICT REPLACE "+
                " );";
        return sql;
    }

    private String createTracksTable(){
        final String sql = "CREATE TABLE " + DataContract.TrackEntry.TABLE_NAME + " (" +
                DataContract.TrackEntry._ID + " INTEGER PRIMARY KEY," +
                DataContract.TrackEntry.COLUMN_SPOTIFY_ID + " TEXT, " +
                DataContract.TrackEntry.COLUMN_ARTIST_ID + " TEXT, " +
                DataContract.TrackEntry.COLUMN_NAME + " TEXT, " +
                DataContract.TrackEntry.COLUMN_ALBUM + " TEXT, " +
                DataContract.TrackEntry.COLUMN_THUMBNAIL + " TEXT, " +
                DataContract.TrackEntry.COLUMN_PREVIEW_URL + " TEXT, " +
                DataContract.TrackEntry.COLUMN_TRACK_POSITION + " INTEGER " +
                " );";
        return sql;
    }
}
