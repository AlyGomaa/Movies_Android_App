package com.example.aly.movie;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/****************************A.G.A**************************
*DetailActivity for present details for movies or tv series
*****************************A.G.A**************************
*/

public class DetailActivity extends AppCompatActivity {

    private ActionProvider mShareActionProvider ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
           Fragment newfragment = new DetailActivityFragment();
            newfragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().replace(android.R.id.content, newfragment, "").commit();
        }
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);

        MenuItem item = (MenuItem) menu.findItem(R.id.action_share);

        mShareActionProvider = MenuItemCompat.getActionProvider(item);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // start settings activity
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
       Fragment f = (Fragment) getSupportFragmentManager().findFragmentById(R.id.fragment_detail);
        if (f != null)
            getSupportFragmentManager().beginTransaction().remove(f).commit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_detail);
    }

    @Override
    protected void onResume() {
        super.onResume();

        DetailActivityFragment f = (DetailActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_detail);

    }

}
