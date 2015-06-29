package com.app.kiory.spotify;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.app.kiory.spotify.datastructure.SharedPreferences;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;


public class MainActivity extends ActionBarActivity  implements MainFragment.Callbacks, DetailFragment.Callbacks, TrackDetailFragment.Callbacks{
    private static final int REQUEST_CODE = 1337;
    private static final String REDIRECT_URI = "http://kiory.com";
    private static final String DETAILVIEWFRAGMENT_TAG = "DVGTAG";
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.detail_container) != null){
            mTwoPane = true;

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, new DetailFragment(), DETAILVIEWFRAGMENT_TAG)
                    .commit();
        }else{
            mTwoPane = false;
        }

        // Request code will be used to verify if result comes from the login activity. Can be set to any integer.
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(this.getString(R.string.spotify_client_id), AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    SharedPreferences.setAccessToken(getApplicationContext() ,response.getAccessToken());
                break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }

    @Override
    public void onItemSelected(Uri contentUri) {
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.DETAIL_URI, contentUri);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, fragment, DETAILVIEWFRAGMENT_TAG)
                    .commit();
        }else{
            Intent intent = new Intent(this, DetailActivity.class).setData(contentUri);
            startActivity(intent);
        }
    }

    @Override
    public void onTrackSelected(Uri contentUri) {
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
