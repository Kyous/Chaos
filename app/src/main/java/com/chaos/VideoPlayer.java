package com.chaos;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.chaos.custom.CustomActivity;
import com.chaos.utils.Const;
import com.chaos.utils.ExceptionHandler;
import com.chaos.web.WebAccess;

/**
 * The Class VideoPlayer is the Activity class that plays a video in full screen
 * mode in both Landscape and Portrait modes. It also handles the case that if
 * the video is failed to play in our player then it will launch the Youtube app.
 */
public class VideoPlayer extends CustomActivity
{

	/** The video view. */
	private VideoView vid;

	/* (non-Javadoc)
	 * @see com.chaos.custom.CustomActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video);

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		vid = (VideoView) findViewById(R.id.vid);
		MediaController mc = new MediaController(this);
		mc.setAnchorView(mc);
		vid.setMediaController(mc);

		/*vid.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra)
			{
				Toast.makeText(VideoPlayer.this,
						"Failed to play video. Trying on Youtube...",
						Toast.LENGTH_LONG).show();
				finish();

				Intent i = new Intent(Intent.ACTION_VIEW, Uri
						.parse("vnd.youtube://"
								+ getIntent().getStringExtra(Const.EXTRA_DATA)));
				startActivity(i);
				return true;
			}
		});
		vid.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp)
			{
				finish();
			}
		});*/
		getRTSPUrl();

	}

	/**
	 * Gets the RTSP url of a youtube video as to play streaming video from
	 * youtube, we need to find it's streaming url.
	 * 
	 * @return the RTSP url
	 */
	private void getRTSPUrl()
	{
		final ProgressDialog dia = ProgressDialog
				.show(this, null, "Loading...");
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				final String url = WebAccess.getUrlVideoRTSP(getIntent()
						.getStringExtra(Const.EXTRA_DATA));
				runOnUiThread(new Runnable() {
					@Override
					public void run()
					{
						dia.dismiss();
						try
						{
							vid.setVideoURI(Uri.parse(url));
							vid.requestFocus();
							vid.start();
						} catch (Exception e)
						{
							Toast.makeText(
									VideoPlayer.this,
									"Failed to play video",
									Toast.LENGTH_LONG).show();
							finish();

							//Intent i = new Intent(Intent.ACTION_VIEW, Uri
							//		.parse("vnd.youtube://"
							//				+ getIntent().getStringExtra(
							//						Const.EXTRA_DATA)));
							//startActivity(i);
						}

					}
				});

			}
		}).start();
	}
}
