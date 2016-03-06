package com.chaos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chaos.custom.CustomActivity;
import com.chaos.model.Video;
import com.chaos.utils.ExceptionHandler;

/**
 * The Class VideoDetail is the Activity class that is launched when the user
 * clicks on a Video item in the list and it simply shows large video image,
 * it;s title, date posted etc. You can customize this to display the contents
 * as per your need.
 */
public class RequestVideo extends CustomActivity
{

	/** The video. */
	private Video vid;

	private Button btnRequestVideo;

	private Context ctx;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.request_video);

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		this.ctx = this;

		btnRequestVideo = (Button) findViewById(R.id.btnRequestVideo);

		btnRequestVideo.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {

				Intent i = new Intent(RequestVideo.this, CreditCardDetail.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				finish();
			}
		});
	}


}
