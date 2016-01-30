package com.example.aly.movie;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by aly on 17/12/2015.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SettingsPereferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return super.onCreateView(inflater, container, savedInstanceState);


    }


}
