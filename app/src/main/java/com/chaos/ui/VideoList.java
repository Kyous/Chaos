package com.chaos.ui;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chaos.VideoDetail;
import com.chaos.custom.CustomActivity;
import com.chaos.custom.CustomFragment;
import com.chaos.model.Video;
import com.chaos.utils.Const;
import com.chaos.utils.ImageLoader;
import com.chaos.utils.ImageLoader.ImageLoadedListener;
import com.chaos.R;

/**
 * The Class VideoList is the Fragment class that is launched when the user
 * clicks on Watch list, history, watch later options in Left navigation drawer
 * and it simply shows a dummy a dummy and random list of videos. You can
 * customize this to display actual contents.
 */
public class VideoList extends CustomFragment
{

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.video_list, null);

		ListView list = (ListView) v.findViewById(R.id.list);
		list.setAdapter(new VideoAdapter());
		return v;
	}
	
	/**
	 * The Class VideoAdapter is the Adapter class for displaying video in
	 * ListView. It simply shows the video thumb image, title,date posted etc.
	 */
	private class VideoAdapter extends BaseAdapter
	{

		/** The video list. */
		private ArrayList<Video> vList;

		/** The image loader to load images one by one in async mode. */
		private ImageLoader loader = new ImageLoader(getDim(R.dimen.pad_125dp),
				getDim(R.dimen.pad_125dp), ImageLoader.SCALE_ASPECT_WIDTH);

		/**
		 * Instantiates a new video adapter.
		 */
		public VideoAdapter()
		{
			vList = new ArrayList<Video>(CustomActivity.VIDEOS);
			Collections.shuffle(vList);
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount()
		{
			return vList.size();
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Video getItem(int position)
		{
			return vList.get(position);
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int position)
		{
			return position;
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View v = getLayoutInflater(null).inflate(R.layout.video_list_item,
					null);
			final ImageView img = (ImageView) v.findViewById(R.id.img);

			Video vid = getItem(position);
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
			return v;
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
