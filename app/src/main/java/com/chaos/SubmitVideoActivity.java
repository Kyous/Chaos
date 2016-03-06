package com.chaos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.custom.CustomActivity;
import com.chaos.model.Video;
import com.chaos.utils.ExceptionHandler;
import com.chaos.utils.ReportType;
import com.chaos.web.WebAccess;
import com.jmolsmobile.landscapevideocapture.VideoCaptureActivity;
import com.jmolsmobile.landscapevideocapture.configuration.CaptureConfiguration;
import com.jmolsmobile.landscapevideocapture.configuration.PredefinedCaptureConfigurations.CaptureQuality;
import com.jmolsmobile.landscapevideocapture.configuration.PredefinedCaptureConfigurations.CaptureResolution;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The Class SplashScreen will launched at the start of the application. It will
 * be displayed for 3 seconds and than finished automatically and it will also
 * start the next activity of app.
 */
public class SubmitVideoActivity extends CustomActivity {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_USERNAME = "username";
    public final int SELECT_VIDEO = 200;
    public final int CAPTURE_IMAGE = 101;
    private final String JPEG_FILE_PREFIX = "IMG_";
    private final String VIDEO_FILE_PREFIX = "VID_";
    private final String JPEG_FILE_SUFFIX = ".jpg";
    private final String VIDEO_FILE_SUFFIX = ".mp4";
    private final String AUDIO_FILE_SUFFIX = ".3gp";
    private final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    // directory name to store captured images and videos
    private final String IMAGE_DIRECTORY_NAME = "Chaos";
    public int MEDIA_TYPE;
    public String SUBMIT_INFO_URL = "http://www.kyous.co.za/submitinfo.php";
    public String UPLOAD_VIDEO_URL = "http://www.kyous.co.za/submitvideo.php";
    public String UPLOAD_PICTURE_URL = "http://www.kyous.co.za/submitpicture.php";
    public String UPLOAD_THUMB_URL = "http://www.kyous.co.za/submitthumbpicture.php";
    String encodedString;
    RequestParams params = new RequestParams();
    private Spinner categoryType, subCategoryType;
    private Button btnAttach;
    private Button btnSubmit;
    private Context ctx;
    private Uri mImageUri;
    // Progress Dialog
    private ProgressDialog pDialog;
    private String mCurrentPhotoPath = "";
    private String encodedThumbString;
    private String mCurrentVideoPath = "";
    private String mCurrentFile;
    private String mThumbFile;
    private EditText edTitle;
    private EditText edDescription;
    private EditText edFeaturedNames;
    private EditText edLocation;
    private EditText edTags;
    private EditText edDuration;

    public static String getPath(Uri uri, Context context) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        String filePath = null;

        try {
            Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
            cursor.close();
        } catch (Exception e) {
            Log.d("Error", e.toString());
            filePath = uri.getPath();
        }


        if (filePath == null) {
            filePath = uri.getPath();
        }

        return filePath;
    }

    private static long copy(InputStream from, OutputStream to) throws IOException {

        byte[] buf = new byte[10240];
        long total = 0;
        while (true) {
            int r = from.read(buf);
            if (r == -1)
                break;

            to.write(buf, 0, r);
            total += r;
        }
        return total;
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_video);

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        this.ctx = this;

        categoryType = (Spinner) findViewById(R.id.categoryType);
        categorySpinner();

        subCategoryType = (Spinner) findViewById(R.id.subCategoryType);
        subCategorySpinner();

        edTitle = (EditText) findViewById(R.id.edTitle);
        edDescription = (EditText) findViewById(R.id.edDescription);
        edFeaturedNames = (EditText) findViewById(R.id.edFeaturedNames);
        edLocation = (EditText) findViewById(R.id.edLocation);
        edTags = (EditText) findViewById(R.id.edTags);
        edDuration = (EditText) findViewById(R.id.edDuration);

        btnAttach = (Button) findViewById(R.id.btnAttach);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        // Login button Click Event
        btnAttach.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                showFileChooser();

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {


                if (isValidated()) {
                    pDialog = new ProgressDialog(ctx);
                    pDialog.setMessage("Please wait while we submit your video...");
                    pDialog.setCancelable(false);
                    pDialog.show();

                    //submitMedia();
                    //new SubmitInfoTask().execute(SUBMIT_INFO_URL);
                    postImage(mCurrentVideoPath);
                }


            }
        });

    }

    private boolean isValidated() {

        Boolean isValid = false;
        if (categoryType.getSelectedItem().toString().trim().equals("Select Type")) {
            // categoryType.setError("contact number is required!");
            TextView errorText = (TextView) categoryType.getSelectedView();
            errorText.setError("Please select type");
            errorText.setTextColor(Color.RED);// just to highlight that
            // this is an error
            errorText.setText("Type not selected");
            categoryType.requestFocus();
            Toast.makeText(this, "Please select Category Type", Toast.LENGTH_SHORT);
            return isValid;

        }

        if (subCategoryType.getSelectedItem().toString().trim().equals("Select Sub-Category")) {

            TextView errorText = (TextView) subCategoryType.getSelectedView();
            errorText.setError("Please select sub category");
            errorText.setTextColor(Color.RED);// just to highlight that
            // this is an error
            errorText.setText("Sub Category not selected");
            Toast.makeText(this, "Please select Sub-Category", Toast.LENGTH_SHORT).show();
            return isValid;
        }

        if (edTitle.getText().toString().trim().equals("")) {
            edTitle.setError("Title is required!");
            Toast.makeText(this, "Please enter your Title", Toast.LENGTH_SHORT).show();
            return isValid;
        } else
            edTitle.setError(null);

        if (edFeaturedNames.getText().toString().trim().equals("")) {
            edFeaturedNames.setError("Feature names are required!");
            Toast.makeText(this, "Please enter your Feature names", Toast.LENGTH_SHORT).show();

            return isValid;
        } else
            edFeaturedNames.setError(null);

        if (edTags.getText().toString().trim().equals("")) {
            edTags.setError("Tags are required!");
            Toast.makeText(this, "Please enter your Tags", Toast.LENGTH_SHORT).show();
            return isValid;
        } else
            edTags.setError(null);

        if (edLocation.getText().toString().trim().equals("")) {
            edLocation.setError("Location is required!");
            Toast.makeText(this, "Please enter the location", Toast.LENGTH_SHORT).show();
            return isValid;
        } else
            edLocation.setError(null);

        if (edDescription.getText().toString().trim().equals("")) {
            edDescription.setError("Details are required!");
            Toast.makeText(this, "Please enter your Details", Toast.LENGTH_SHORT).show();
            return isValid;
        } else
            edDescription.setError(null);

        if (edDuration.getText().toString().trim().equals("")) {
            edDuration.setError("Duration is required!");
            Toast.makeText(this, "Please enter your Duration", Toast.LENGTH_SHORT).show();
            return isValid;
        } else
            edDuration.setError(null);


        return true;

    }

    private void startVideoCaptureActivity() {

        final CaptureConfiguration config = createCaptureConfiguration();

        // Create an video file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String videoFileName = VIDEO_FILE_PREFIX + timeStamp + VIDEO_FILE_SUFFIX;

        File file = createFolderIfNotExists();


        mCurrentVideoPath = file.getAbsolutePath() + "/" + videoFileName;

        Log.d("File", mCurrentVideoPath);

        int index = mCurrentVideoPath.lastIndexOf("/");
        String currentVideoPath = mCurrentVideoPath.substring(index + 1);
        params.put("filename", currentVideoPath);

        String filename = mCurrentVideoPath;

        Intent intent = new Intent(this.ctx, VideoCaptureActivity.class);

        intent.putExtra(VideoCaptureActivity.EXTRA_CAPTURE_CONFIGURATION, config);
        intent.putExtra(VideoCaptureActivity.EXTRA_OUTPUT_FILENAME, filename);
        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);

    }

    private CaptureConfiguration createCaptureConfiguration() {

        final CaptureResolution resolution = getResolution(2); // 360p
        final CaptureQuality quality = getQuality(2); // Low
        int fileDuration = 60;//CaptureConfiguration.NO_DURATION_LIMIT;

        int filesize = 10;//CaptureConfiguration.NO_FILESIZE_LIMIT;

        final CaptureConfiguration config = new CaptureConfiguration(resolution, quality, fileDuration, filesize);
        return config;

    }

    private CaptureResolution getResolution(int position) {
        final CaptureResolution[] resolution = new CaptureResolution[]{CaptureResolution.RES_1080P,
                CaptureResolution.RES_720P, CaptureResolution.RES_480P, CaptureResolution.RES_360P,
                CaptureResolution.RES_1440P};
        return resolution[position];
    }

    private CaptureQuality getQuality(int position) {
        final CaptureQuality[] quality = new CaptureQuality[]{CaptureQuality.HIGH, CaptureQuality.MEDIUM,
                CaptureQuality.LOW};
        return quality[position];
    }

    private File createFolderIfNotExists() {
        ContextWrapper cw = new ContextWrapper(this.ctx);
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed to create " + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        return mediaStorageDir;
    }

    public void postImage(String ImageLink) {

        RequestParams params = new RequestParams();

        try {
            params.put("uploaded_file", new File(ImageLink));

            Log.d("File", ImageLink);
        } catch (FileNotFoundException e) {
            System.out.println(e.toString());
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://www.kyous.co.za/uploadfile.php", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                System.out.println("statusCode " + statusCode);//statusCode 200

                try {
                    String str = new String(responseBody, "UTF-8");
                    System.out.println("Success " + str);
                } catch (UnsupportedEncodingException e) {
                    System.out.println("err " + e.toString());
                }

                new SubmitInfoTask().execute(SUBMIT_INFO_URL);

                new SubmitThumbNailTask().execute(UPLOAD_THUMB_URL);

                pDialog.hide();
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println("fail statusCode " + statusCode);
                try {

                    if (responseBody != null) {

                        String str = new String(responseBody, "UTF-8");
                        System.out.println("failure " + str);
                    }
                } catch (UnsupportedEncodingException e) {
                    System.out.println("err " + e.toString());
                }
            }

        });
    }

    private void showFileChooser() {


        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/* image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {

            attachMedia();
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    public void attachMedia() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.ctx);
        builder.setItems(R.array.attach_options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {

                    // Create an image file name
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = JPEG_FILE_PREFIX + timeStamp;
                    String ext = "." + JPEG_FILE_SUFFIX;

                    Intent captureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                    File photo;
                    try {
                        // place where to store camera taken picture
                        photo = createTemporaryFile(imageFileName, ext);
                        photo.delete();

                        mCurrentFile = photo.getName();

                        mImageUri = Uri.fromFile(photo);
                        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);

                        //set media type
                        MEDIA_TYPE = 1;

                        // start camera intent
                        startActivityForResult(captureIntent, CAPTURE_IMAGE);
                    } catch (Exception e) {
                        Log.v("Image Upload", "Can't create file to take picture!");

                    }

                } else if (item == 1) { // attach picture
                    //Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    //photoPickerIntent.setType("video/*");

                    //set media type
                    MEDIA_TYPE = 2;

                    startVideoCaptureActivity();


                    //startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Video"), SELECT_VIDEO);
                }
            }
        });

        builder.create().show();
    }

    private File createTemporaryFile(String part, String ext) throws Exception {
        File tempDir = Environment.getExternalStorageDirectory();
        tempDir = new File(tempDir.getAbsolutePath() + "/.temp/");
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }
        return File.createTempFile(part, ext, tempDir);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i("requestCode", String.valueOf(SELECT_VIDEO));
        switch (requestCode) {
            case SELECT_VIDEO:
                if (resultCode == RESULT_OK) {
                    try {

                        Bitmap thumbnail = getThumbnail(mCurrentVideoPath);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();

                        encodedThumbString = Base64.encodeToString(byteArray, 0);

                        Log.i("encoded Thumb String", encodedThumbString);

                        int index = mCurrentVideoPath.lastIndexOf("/");

                        mCurrentFile = mCurrentVideoPath.substring(index + 1);

                        mThumbFile = mCurrentFile.replace(".mp4", ".png");

                    } catch (Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(this.ctx, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                } else
                    mCurrentVideoPath = "";
                break;

            case CAPTURE_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    try {

                        this.grabImage();

                    } catch (Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                } else
                    mCurrentPhotoPath = "";
                break;

        }

    }

    private Bitmap getThumbnail(String Path) {
        if (Path == null)
            return null;
        return ThumbnailUtils.createVideoThumbnail(Path, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
    }

    public void grabImage() {
        this.getContentResolver().notifyChange(mImageUri, null);

        try {
            //bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, mImageUri);
            mCurrentPhotoPath = mImageUri.getPath();
            Log.d("mImageUri.getPath()", mImageUri.getPath());


        } catch (Exception e) {
            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
            Log.d("grabImage", "Failed to load", e);
        }
    }

    public void categorySpinner() {

        ReportType sTestArray[] = new ReportType[]{new ReportType("Select Type", "0"),
                new ReportType("Breaking News", "0")};


        ArrayAdapterItem Adapter = new ArrayAdapterItem(this, R.layout.report_item, sTestArray);

        categoryType.setAdapter(Adapter);

        categoryType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void subCategorySpinner() {

        ReportType sTestArray[] = new ReportType[]{new ReportType("Select Sub-Category", "0"),
                new ReportType("Politics", "0"), new ReportType("Sports", "0"), new ReportType("Finance", "0")};


        ArrayAdapterItem Adapter = new ArrayAdapterItem(this, R.layout.report_item, sTestArray);

        subCategoryType.setAdapter(Adapter);

        subCategoryType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    class SubmitInfoTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(urls[0]);
            try {

                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("title", edTitle.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("description", edDescription.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("featurednames", edFeaturedNames.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("location", edLocation.getText().toString()));

                SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                String username = pref.getString(PREF_USERNAME, null);

                nameValuePairs.add(new BasicNameValuePair("postedby", username));
                nameValuePairs.add(new BasicNameValuePair("tags", edTags.getText().toString()));

                if (MEDIA_TYPE == 1) {
                    nameValuePairs.add(new BasicNameValuePair("type", "1"));
                    nameValuePairs.add(new BasicNameValuePair("videopath", null));
                    nameValuePairs.add(new BasicNameValuePair("thumbpath", mCurrentFile));
                    nameValuePairs.add(new BasicNameValuePair("imagepath", mCurrentPhotoPath));
                } else {

                    String videothumbpath = String.format("http://www.kyous.co.za/thumb/%s", mThumbFile);
                    String videopath = String.format("http://www.kyous.co.za/upload/%s", mCurrentFile);

                    nameValuePairs.add(new BasicNameValuePair("type", "2"));
                    nameValuePairs.add(new BasicNameValuePair("videopath", videopath));
                    nameValuePairs.add(new BasicNameValuePair("thumbpath", videothumbpath));
                    nameValuePairs.add(new BasicNameValuePair("imagepath", null));
                }

                nameValuePairs.add(new BasicNameValuePair("duration", edDuration.getText().toString()));


                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

                String responseStr = EntityUtils.toString(response.getEntity());

                Log.d("Response", responseStr);

                return response.toString();
            } catch (Exception e) {
                this.exception = e;
                Log.d("Error", e.toString());
                return null;
            }
        }

        protected void onPostExecute(String response) {
            // TODO: check this.exception
            // TODO: do something with the feed

            if (this.exception != null)
                Log.d("Response", response);

            pDialog.hide();


            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setCancelable(true);
            builder.setTitle(".Chaos");
            builder.setMessage(
                    "Thank you. Your video is pending approval from the administrator.");
            builder.setInverseBackgroundForced(true);
            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    finish();

                    Intent i = new Intent(ctx, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    ArrayList<Video> al = WebAccess
                            .getVideosFromKyous("breaking news today south africa");
                    if (al.size() > 0) {
                        //Collections.shuffle(al);
                        VIDEOS.clear();
                        VIDEOS.addAll(al);
                    }

                    startActivity(i);
                    finish();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();


        }
    }

    class SubmitMediaTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(urls[0]);
            try {

                FileInputStream fin = null;
                try {
                    fin = new FileInputStream(mCurrentVideoPath);
                } catch (FileNotFoundException e) {
                    Log.i("encodedVideotoString", e.toString() + "\n");
                }
                BufferedInputStream bis = new BufferedInputStream(fin);
                DataInputStream dis = new DataInputStream(bis);

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                try {
                    copy(dis, out);
                } catch (IOException e) {
                    Log.i("encodedVideotoString", e.toString() + "\n");
                }
                byte fileContent[] = out.toByteArray();
                encodedString = Base64.encodeToString(fileContent, 0);

// Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("filename", mCurrentFile));
                if (MEDIA_TYPE == 1)
                    nameValuePairs.add(new BasicNameValuePair("image", encodedString));

                if (MEDIA_TYPE == 2) {
                    nameValuePairs.add(new BasicNameValuePair("video", encodedString));
                    nameValuePairs.add(new BasicNameValuePair("thumb", encodedThumbString));
                }


                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                Log.d("encoded String", encodedString);
                Log.d("mCurrentVideoPath", mCurrentVideoPath);
                Log.d("mCurrentFile", mCurrentFile);

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

                String responseStr = EntityUtils.toString(response.getEntity());

                Log.d("Response", responseStr);

                return response.toString();
            } catch (Exception e) {
                this.exception = e;
                Log.d("Error", e.toString());
                return null;
            }
        }

        protected void onPostExecute(String response) {
            // TODO: check this.exception
            // TODO: do something with the feed

            //if(this.exception!=null)
            //Log.d("Response",response);
            //else
            //    Log.d("Error",this.exception);


        }
    }

    class SubmitThumbNailTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(urls[0]);
            try {

// Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("filename", mThumbFile));

                nameValuePairs.add(new BasicNameValuePair("thumb", encodedThumbString));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                Log.d("encoded String", encodedThumbString);
                Log.d("Thumb File Name", mThumbFile);

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

                String responseStr = EntityUtils.toString(response.getEntity());

                Log.d("Response", responseStr);

                return response.toString();
            } catch (Exception e) {
                this.exception = e;
                Log.d("Error", e.toString());
                return null;
            }
        }

        protected void onPostExecute(String response) {
            // TODO: check this.exception
            // TODO: do something with the feed

            //if(this.exception!=null)
            //    Log.d("Response",response);
            //else
            //    Log.d("Error",this.exception);


        }
    }

    class ArrayAdapterItem extends ArrayAdapter<ReportType> {

        Context mContext;
        int layoutResourceId;
        ReportType data[] = null;

        public ArrayAdapterItem(Context mContext, int layoutResourceId, ReportType[] data) {

            super(mContext, layoutResourceId, data);

            this.layoutResourceId = layoutResourceId;
            this.mContext = mContext;
            this.data = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                // inflate the layout
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                convertView = inflater.inflate(layoutResourceId, parent, false);
            }

            // object item based on the position
            ReportType objectItem = data[position];

            // get the TextView and then set the text (item name) and tag (item
            // ID) values
            TextView textViewItem = (TextView) convertView.findViewById(R.id.typename);
            textViewItem.setText(objectItem.getName());
            textViewItem.setTag(objectItem.getId());
            textViewItem.setTextSize(12);

            return convertView;

        }

    }

}