package com.app.kiory.spotify;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.kiory.spotify.database.DataContract;
import com.squareup.picasso.Picasso;

/**
 * Created by darknoe on 11/6/15.
 */
public class TrackAdapter extends CursorAdapter {

    public static class ViewHolder {
        public final ImageView icon;
        public final TextView album;
        public final TextView track;


        public ViewHolder(View view) {
            icon = (ImageView) view.findViewById(R.id.list_item_icon);
            album = (TextView) view.findViewById(R.id.list_item_album);
            track = (TextView) view.findViewById(R.id.list_item_track);
        }
    }


    public TrackAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Choose the layout type
        View view = LayoutInflater.from(context).inflate(R.layout.detail_listview_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String track = cursor.getString(cursor.getColumnIndex(DataContract.TrackEntry.COLUMN_NAME));
        viewHolder.track.setText(track);

        String album = cursor.getString(cursor.getColumnIndex(DataContract.TrackEntry.COLUMN_ALBUM));
        viewHolder.album.setText(album);

        String img_url = cursor.getString(cursor.getColumnIndex(DataContract.ArtistEntry.COLUMN_THUMBNAIL));
        if(img_url.compareTo("")!=0)
            Picasso.with(context).load(img_url).into(viewHolder.icon);
    }

}

