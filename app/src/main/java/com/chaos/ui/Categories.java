package com.chaos.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chaos.VideoDetail;
import com.chaos.custom.CustomActivity;
import com.chaos.custom.CustomFragment;
import com.chaos.model.Video;
import com.chaos.utils.Const;
import com.chaos.utils.ImageLoader;
import com.chaos.utils.ImageUtils;
import com.chaos.utils.ImageLoader.ImageLoadedListener;
import com.chaos.R;

/**
 * The Class Categories is the Fragment class that is launched when the user
 * clicks on Categories option in Left navigation drawer and it simply shows a
 * dummy list of Video Categories. You can customize this to display actual
 * contents.
 */
public class Categories extends CustomFragment
{

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.categories, null);

		int ids[] = { R.id.video_mid1, R.id.video_mid2, R.id.video_mid3,
				R.id.video_mid4 };
		setVideoGrids(v, ids, true);

		//ids = new int[] { R.id.video_mid11, R.id.video_mid21, R.id.video_mid31,
		//		R.id.video_mid41 };
		//setVideoGrids(v, ids, false);

		//setAllVideos(v);
		return v;
	}

	/**
	 * Setup the video grids.
	 * 
	 * @param v1
	 *            the parent view
	 * @param ids
	 *            the ids of categories
	 * @param latest
	 *            if these relates to latest Categories section.
	 */
	private void setVideoGrids(View v1, int ids[], boolean latest)
	{
		int w = (getResources().getDisplayMetrics().widthPixels - getDim(R.dimen.pad_5dp) * 3) / 2;
		ImageLoader loader = new ImageLoader(w, w,
				ImageUtils.SCALE_ASPECT_WIDTH);
		for (int i = 0; i < ids.length; i++)
		{
			if (i >= CustomActivity.VIDEOS.size())
				continue;
			View v = v1.findViewById(ids[i]);

			final ImageView img = (ImageView) v.findViewById(R.id.img);

			int x = latest ? i : CustomActivity.VIDEOS.size() < i + 4 ? i
					: i + 4;
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
	 * Setup the list of all videos Category
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
		for (int i = CustomActivity.VIDEOS.size() <= 8 ? 0 : 8; i < CustomActivity.VIDEOS
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
