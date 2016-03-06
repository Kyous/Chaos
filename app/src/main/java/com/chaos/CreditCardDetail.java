package com.chaos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
public class CreditCardDetail extends CustomActivity
{

	/** The video. */
	private Video vid;

	private Button btnLinkToMainScreen;

	private Context ctx;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.creditcard_detail);

		ctx = this;

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		btnLinkToMainScreen = (Button) findViewById(R.id.btnLinkToMainScreen);

		// Link to Login Screen
		btnLinkToMainScreen.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivity(i);
				finish();
			}
		});


	}

	/* (non-Javadoc)
	 * @see com.chaos.custom.CustomActivity#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		if (v.getTag() == null) {


			return;
		}


		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setCancelable(true);
		builder.setTitle(".Kyous");
		builder.setMessage(
				"Thank you. We will process the payment and send you the video link.");
		builder.setInverseBackgroundForced(true);
		builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				//startActivity(new Intent(ctx, MainActivity.class));

				startActivity(new Intent(ctx, MainActivity.class).setFlags(
				Intent.FLAG_ACTIVITY_CLEAR_TOP));
			}
		});

		AlertDialog alert = builder.create();
		alert.show();



	}

}
