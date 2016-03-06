package com.chaos.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chaos.custom.CustomFragment;
import com.chaos.R;

/**
 * The Class Settings is the Fragment class that is launched when the user
 * clicks on Settings option in Left navigation drawer and it simply shows a few
 * Settings options . You can customize this to display actual contents.
 */
public class Settings extends CustomFragment
{

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.settings, null);

		return v;
	}

}
