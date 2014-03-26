package com.android.brainslam.mainscreen;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.brainslam.R;

public class ToggleForwardBack extends Fragment{
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
		View view = inflater.inflate(R.layout.mainscreen_crew_myscrew_stream, container, false);
        return view;
    }

}
