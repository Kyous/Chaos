package com.chaos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.chaos.custom.CustomActivity;
import com.chaos.model.Video;
import com.chaos.utils.ExceptionHandler;
import com.chaos.utils.Utils;
import com.chaos.web.WebAccess;

import java.util.ArrayList;

/**
 * The Class SplashScreen will launched at the start of the application. It will
 * be displayed for 3 seconds and than finished automatically and it will also
 * start the next activity of app.
 */
public class SplashScreen extends CustomActivity
{

	/** Check if the app is running. */
	private boolean isRunning;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		isRunning = true;

		startSplash();

	}

	/**
	 * Starts the count down timer for 3-seconds. It simply sleeps the thread
	 * for 3-seconds. However this method is also loading the Video listing from
	 * youtube in background.
	 */
	private void startSplash()
	{

		new Thread(new Runnable() {
			@Override
			public void run()
			{

				try
				{
					long time = System.currentTimeMillis();
					if (Utils.isOnline())
					{
						ArrayList<Video> al = WebAccess
								.getVideosFromKyous("breaking news today south africa");
						if (al.size() > 0)
						{
							//Collections.shuffle(al);
							VIDEOS.clear();
							VIDEOS.addAll(al);
						}
					}
					time = 3000 - (System.currentTimeMillis() - time);
					if (time > 0)
						Thread.sleep(time);

				} catch (Exception e)
				{
					Log.d("Splash", e.toString());
				} finally
				{
					runOnUiThread(new Runnable() {
						@Override
						public void run()
						{
							doFinish();
						}
					});
				}
			}
		}).start();
	}

	/**
	 * If the app is still running than this method will start the MainActivity
	 * activity and finish the Splash. It also shows the alert if Video list is empty.
	 */
	private synchronized void doFinish()
	{

		if (isRunning)
		{

			Intent i = new Intent(SplashScreen.this, LoginActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{

		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			isRunning = false;
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}