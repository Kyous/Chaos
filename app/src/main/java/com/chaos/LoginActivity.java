package com.chaos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chaos.custom.CustomActivity;
import com.chaos.model.Video;
import com.chaos.utils.ExceptionHandler;
import com.chaos.utils.Utils;
import com.chaos.web.WebAccess;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The Class SplashScreen will launched at the start of the application. It will
 * be displayed for 3 seconds and than finished automatically and it will also
 * start the next activity of app.
 */
public class LoginActivity extends CustomActivity
{

	public static final String PREFS_NAME = "MyPrefsFile";
	private static final String PREF_USERNAME = "username";
	private static final String PREF_PASSWORD = "password";
	/** Check if the app is running. */
	private boolean isRunning;
	private Button btnLogin;
	private Button btnLinkToRegister;
	private EditText inputEmail;
	private EditText inputPassword;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		Log.d("login","called");

		inputEmail = (EditText) findViewById(R.id.email);
		inputPassword = (EditText) findViewById(R.id.password);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

		SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
		String username = pref.getString(PREF_USERNAME, null);
		String password = pref.getString(PREF_PASSWORD, null);

		//String[] params = new String[2];
		//params[0] = username;
		//params[1] = password;

		//new LoginAsyncTask().execute(params);

		if (username != null && password != null) {
			//Prompt for username and password
			// Launch main activity
			Intent intent = new Intent(LoginActivity.this,
					MainActivity.class);
			startActivity(intent);
			finish();
		}

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();

                // Check for empty data in the form
                if (email.trim().length() > 0 && password.trim().length() > 0) {
                    // login user
					getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
							.edit()
							.putString(PREF_USERNAME, email)
							.putString(PREF_PASSWORD, password)
							.commit();

                    // Launch main activity
                    Intent intent = new Intent(LoginActivity.this,
                            MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

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
							Collections.shuffle(al);
							VIDEOS.clear();
							VIDEOS.addAll(al);
						}
					}
					time = 3000 - (System.currentTimeMillis() - time);
					if (time > 0)
						Thread.sleep(time);

				} catch (Exception e)
				{
					e.printStackTrace();
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
			if (VIDEOS.size() == 0)
				Toast.makeText(
						this,
						"Unable to load the videos. "
								+ "Check your internet connectivity...",
						Toast.LENGTH_LONG).show();
			isRunning = false;
			//Intent i = new Intent(LoginActivity.this, CreditCardDetail.class);
			Intent i = new Intent(LoginActivity.this, MainActivity.class);
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