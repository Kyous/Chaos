package com.chaos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaos.custom.CustomActivity;
import com.chaos.model.Video;
import com.chaos.utils.Const;
import com.chaos.utils.ExceptionHandler;
import com.chaos.utils.ImageLoader;
import com.chaos.utils.ImageLoader.ImageLoadedListener;
import com.chaos.utils.ImageUtils;

/**
 * The Class VideoDetail is the Activity class that is launched when the user
 * clicks on a Video item in the list and it simply shows large video image,
 * it;s title, date posted etc. You can customize this to display the contents
 * as per your need.
 */
public class VideoDetail extends CustomActivity
{

	/** The video. */
	private Video vid;

	private Button btnRequestVideo;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_detail);

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		init(getIntent().getSerializableExtra(Const.EXTRA_DATA));

		btnRequestVideo = (Button) findViewById(R.id.btnRequestVideo);

		btnRequestVideo.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				startActivity(new Intent(VideoDetail.this, RequestVideo.class));

			}
		});
	}

	/**
	 * Initialize the layout components for given Video.
	 * 
	 * @param v
	 *            the video object
	 */
	private void init(Object v)
	{
		vid = (Video) v;
		showVideoDetail();
		//showSimilarVideos();
	}

	/**
	 * This method Shows the video details that is, it maps the video
	 * information to corresponding view.
	 */
	private void showVideoDetail()
	{

		int w = getResources().getDisplayMetrics().widthPixels;
		ImageLoader loader = new ImageLoader(w, w,
				ImageUtils.SCALE_ASPECT_WIDTH);

		final ImageView img = (ImageView) findViewById(R.id.img);
		img.setOnTouchListener(CustomActivity.TOUCH);
		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				startActivity(new Intent(VideoDetail.this, VideoPlayer.class)
						.putExtra(Const.EXTRA_DATA, vid.getUrl()));
			}
		});

		TextView lbl = (TextView) findViewById(R.id.title);
		lbl.setText(Html.fromHtml(vid.getTitle()));

		lbl = (TextView) findViewById(R.id.time);
		lbl.setText(vid.getTitle().length() + ":0" + (int) (Math.random() * 10));

		lbl = (TextView) findViewById(R.id.date);
		lbl.setText(vid.getDate());

		lbl = (TextView) findViewById(R.id.posted);
		lbl.setText("Posted by " + vid.getPostedBy());

		lbl = (TextView) findViewById(R.id.desc);
		lbl.setText(Html.fromHtml(vid.getDesc()));

		Bitmap bm = loader.loadImage(vid.getImageL(),
				new ImageLoadedListener() {

					@Override
					public void imageLoaded(Bitmap bm)
					{
						if (bm != null)
							img.setImageBitmap(bm);
					}
				});
		if (bm != null)
			img.setImageBitmap(bm);

	}

	/* (non-Javadoc)
	 * @see com.chaos.custom.CustomActivity#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		if (v.getTag() == null)
			return;
		startActivity(new Intent(this, VideoDetail.class).putExtra(
				Const.EXTRA_DATA, (Video) v.getTag()).setFlags(
				Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}

}
