package com.chaos.utils;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 10/25/15.
 */
public class LoginAsyncTask extends AsyncTask<String, Void, Boolean> {

    JSONParser jsonParser = new JSONParser();

    // url to report a crime
    private String url_login;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_UID = "uid";

    @Override
    protected Boolean doInBackground(String... params) {


        try {

            String username = params[0];
            String password = params[1];

            // Building Parameters
            List<NameValuePair> crimereportparams = new ArrayList<NameValuePair>();
            crimereportparams.add(new BasicNameValuePair("username", username));
            crimereportparams.add(new BasicNameValuePair("password",
                    password));

            url_login = ChaosApplication.serverURL() + "/login.php";

            // getting JSON Object
            JSONObject json = jsonParser.makeHttpRequest(url_login,
                    "POST", crimereportparams);

            Log.d("Login - UID", json.getString(TAG_UID));
            // check for success tag
            try {

                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {

                    return true;
                } else
                    return false;

            } catch (JSONException e) {
                Log.e("ERROR", e.toString());

                return false;
            }

        } catch (Exception e) {
            Log.e("ERROR", e.toString());

            return false;
        } finally {

        }
    }
}
