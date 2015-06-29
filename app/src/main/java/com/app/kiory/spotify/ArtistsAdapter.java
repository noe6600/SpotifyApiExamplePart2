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
public class ArtistsAdapter extends CursorAdapter {

    public static class ViewHolder {
        public final ImageView icon;
        public final TextView title;


        public ViewHolder(View view) {
            icon = (ImageView) view.findViewById(R.id.list_item_icon);
            title = (TextView) view.findViewById(R.id.list_item_title);
        }
    }


    public ArtistsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Choose the layout type
        View view = LayoutInflater.from(context).inflate(R.layout.main_listview_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String title = cursor.getString(cursor.getColumnIndex(DataContract.ArtistEntry.COLUMN_NAME));
        viewHolder.title.setText(title);

        String img_url = cursor.getString(cursor.getColumnIndex(DataContract.ArtistEntry.COLUMN_THUMBNAIL));
        if(img_url.compareTo("")!=0)
            Picasso.with(context).load(img_url).into(viewHolder.icon);
    }

}
