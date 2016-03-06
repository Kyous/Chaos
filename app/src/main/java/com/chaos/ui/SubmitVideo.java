package com.chaos.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.chaos.R;
import com.chaos.VideoDetail;
import com.chaos.custom.CustomActivity;
import com.chaos.custom.CustomFragment;
import com.chaos.model.Video;
import com.chaos.utils.Const;
import com.chaos.utils.ImageLoader;
import com.chaos.utils.ImageLoader.ImageLoadedListener;
import com.chaos.utils.ImageUtils;

/**
 * The Class Home is the Fragment class that is launched when the user clicks on
 * Home option in Left navigation drawer and it is also main fragment that is
 * displayed when start the app and it simply shows a dummy list of Videos
 * loaded from youtube API. You can customize this to display actual contents.
 */
public class SubmitVideo extends CustomFragment
{

	private Button btnSubmit;
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.submit_video, null);


		return v;
	}

	/**
	 * Setup the large images slides.
	 *
	 * @param v1
	 *            the parent view
	 */
	private void setLargeImageSlides(View v1)
	{
		LinearLayout parent = (LinearLayout) v1.findViewById(R.id.vLarge);
		parent.removeAllViews();
		LayoutParams param = new LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		param.setMargins(0, 0, getDim(R.dimen.pad_5dp), 0);

		int w = getResources().getDisplayMetrics().widthPixels - 20;
		ImageLoader loader = new ImageLoader(w, w,
				ImageUtils.SCALE_ASPECT_WIDTH);
		for (int i = 0; i < 4; i++)
		{
			final ImageView img = new ImageView(getActivity());
			img.setBackgroundColor(Color.BLACK);
			img.setImageResource(R.drawable.img_large);
			if (i < 3)
				parent.addView(img, param);
			else
				parent.addView(img);
			if (i >= CustomActivity.VIDEOS.size())
				continue;

			Video vid = CustomActivity.VIDEOS.get(i);
			img.setTag(vid);
			setTouchNClick(img);

			Log.e("VURL", vid.getImageL());
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
	}

	/**
	 * Setup the streaming videos.
	 *
	 * @param v1
	 *           the parent view
	 */
	private void setStreamingVideos(View v1)
	{
		int ids[] = { R.id.video_mid1, R.id.video_mid2, R.id.video_mid3,
				R.id.video_mid4 };

		int w = (getResources().getDisplayMetrics().widthPixels - getDim(R.dimen.pad_5dp) * 3) / 2;
		ImageLoader loader = new ImageLoader(w, w,
				ImageUtils.SCALE_ASPECT_WIDTH);
		for (int i = 0; i < ids.length; i++)
		{
			if (i >= CustomActivity.VIDEOS.size())
				continue;
			View v = v1.findViewById(ids[i]);

			final ImageView img = (ImageView) v.findViewById(R.id.img);

			int x = CustomActivity.VIDEOS.size() < i + 4 ? i : i + 4;
			Video vid = CustomActivity.VIDEOS.get(x);
			v.setTag(vid);
			setTouchNClick(v);

			TextView lbl = (TextView) v.findViewById(R.id.title);
			lbl.setText(Html.fromHtml(vid.getTitle()));

			lbl = (TextView) v.findViewById(R.id.time);
			lbl.setText("0" + (int) (Math.random() * 10) + ":0"
					+ (int) (Math.random() * 10));

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
	}

	/**
	 * Setup the popular videos.
	 *
	 * @param v1
	 *            the parent view
	 */
	private void setPopularVideos(View v1)
	{
		LinearLayout parent = (LinearLayout) v1.findViewById(R.id.vPop);
		parent.removeAllViews();

		int w = (getResources().getDisplayMetrics().widthPixels - getDim(R.dimen.pad_5dp) * 4) / 3;
		LayoutParams param = new LayoutParams(w,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		param.setMargins(0, 0, getDim(R.dimen.pad_5dp), 0);
		ImageLoader loader = new ImageLoader(w, w,
				ImageUtils.SCALE_ASPECT_WIDTH);
		for (int i = 0; i < 4; i++)
		{
			View v = getLayoutInflater(null).inflate(R.layout.video_pop, null);
			final ImageView img = (ImageView) v.findViewById(R.id.img);
			parent.addView(v, param);
			if (i >= CustomActivity.VIDEOS.size())
				continue;

			int x = CustomActivity.VIDEOS.size() < i + 8 ? i : i + 8;
			Video vid = CustomActivity.VIDEOS.get(x);
			v.setTag(vid);
			setTouchNClick(v);

			TextView lbl = (TextView) v.findViewById(R.id.title);
			lbl.setText(Html.fromHtml(vid.getTitle()));

			Bitmap bm = loader.loadImage(vid.getImageM(),
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
	}

	/**
	 * Setup the all videos.
	 * 
	 * @param v1
	 *            the parent view
	 */
	private void setAllVideos(View v1)
	{
		LinearLayout parent = (LinearLayout) v1.findViewById(R.id.vAll);
		parent.removeAllViews();

		// LayoutParams param=new LayoutParams(LayoutParams.WRAP_CONTENT,
		// LayoutParams.WRAP_CONTENT);
		// param.setMargins(0, 0, getDim(R.dimen.pad_5dp), 0);
		ImageLoader loader = new ImageLoader(getDim(R.dimen.pad_125dp),
				getDim(R.dimen.pad_125dp), ImageUtils.SCALE_ASPECT_WIDTH);
		for (int i = CustomActivity.VIDEOS.size() <= 12 ? 0 : 12; i < CustomActivity.VIDEOS
				.size(); i++)
		{
			View v = getLayoutInflater(null).inflate(R.layout.video_list_item,
					null);
			parent.addView(v);
			final ImageView img = (ImageView) v.findViewById(R.id.img);

			Video vid = CustomActivity.VIDEOS.get(i);
			v.setTag(vid);
			setTouchNClick(v);

			TextView lbl = (TextView) v.findViewById(R.id.title);
			lbl.setText(Html.fromHtml(vid.getTitle()));

			lbl = (TextView) v.findViewById(R.id.time);
			lbl.setText(vid.getTitle().length() + ":0"
					+ (int) (Math.random() * 10));

			lbl = (TextView) v.findViewById(R.id.date);
			lbl.setText(vid.getDate());

			lbl = (TextView) v.findViewById(R.id.posted);
			lbl.setText("Posted by " + vid.getPostedBy());

			Bitmap bm = loader.loadImage(vid.getImageM(),
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
	}

	/* (non-Javadoc)
	 * @see com.chaos.custom.CustomFragment#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		if (v.getTag() == null)
			return;
		Video vid = (Video) v.getTag();
		startActivity(new Intent(getActivity(), VideoDetail.class).putExtra(
				Const.EXTRA_DATA, vid));
	}

}
