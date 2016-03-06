package com.chaos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.chaos.custom.CustomActivity;
import com.chaos.model.Video;
import com.chaos.utils.Const;
import com.chaos.utils.ExceptionHandler;
import com.chaos.utils.ImageLoader;
import com.chaos.utils.ImageLoader.ImageLoadedListener;
import com.chaos.utils.Utils;
import com.chaos.web.WebAccess;

import java.util.ArrayList;

/**
 * The main Search screen, launched when user click on Search button on Top
 * Action bar. Write your code inside onQueryTextSubmit/onQueryTextChange
 * method(s) to perform required work. The current implementation simply search
 * for Videos from Youtube. You can customize this code to search from your API
 * or any TV API.
 */
public class Search extends CustomActivity
{

	/** The Video list. */
	private ArrayList<Video> vList;

	/** The list. */
	private ListView list;

	/** The Video list adapter. */
	private VideoAdapter adapter;

	/** The search view. */
	private SearchView searchView;
	/**
	 * If query is running.
	 */
	private boolean running;
	/**
	 * The last searched query.
	 */
	private String last;

	/* (non-Javadoc)
	 * @see com.food.custom.CustomActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setLogo(R.drawable.trans1);

		list = (ListView) findViewById(R.id.list);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3)
			{
				Video vid = vList.get(pos);
				startActivity(new Intent(Search.this, VideoDetail.class)
						.putExtra(Const.EXTRA_DATA, vid));
			}
		});

		list.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Utils.hideKeyboard(Search.this, searchView);
				searchView.clearFocus();
				return false;
			}
		});

	}

	/* (non-Javadoc)
	 * @see com.chaos.custom.CustomActivity#onResume()
	 */
	@Override
	protected void onResume()
	{
		super.onResume();
		if (searchView != null)
		{
			Utils.hideKeyboard(this, searchView);
			searchView.clearFocus();
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.search_exp, menu);

		setupSearchView(menu);
		return true;
	}

	/**
	 * Setup the up search view for ActionBar search. The current implementation
	 * simply search for Videos from Youtube. You can customize this code to
	 * search from your API or any TV API.
	 *
	 * @param menu
	 *            the ActionBar Menu
	 */
	private void setupSearchView(Menu menu)
	{
		searchView = (SearchView) menu.findItem(R.id.menu_search)
				.getActionView();
		searchView.setIconifiedByDefault(false);
		searchView.setQueryHint("Search...");
		searchView.requestFocusFromTouch();
		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query)
			{
				Utils.hideKeyboard(Search.this);
				return true;
			}

			@Override
			public boolean onQueryTextChange(String query)
			{
				if (query.trim().length() >= 3)
				{
					searchVideos(query);
				}
				return false;
			}
		});

		setupSearchViewTheme(searchView);
	}

	/**
	 * Sets the up search view theme.
	 *
	 * @param searchView
	 *            the new up search view theme
	 */
	private void setupSearchViewTheme(SearchView searchView)
	{
		int id = searchView.getContext().getResources()
				.getIdentifier("android:id/search_src_text", null, null);
		EditText searchPlate = (EditText) searchView.findViewById(id);
		searchPlate.setHintTextColor(getResources().getColor(R.color.white));

		id = searchView.getContext().getResources()
				.getIdentifier("android:id/search_close_btn", null, null);
		((ImageView) searchView.findViewById(id))
				.setImageResource(R.drawable.ic_close);

		id = searchView.getContext().getResources()
				.getIdentifier("android:id/search_mag_icon", null, null);
		((ImageView) searchView.findViewById(id))
				.setImageResource(R.drawable.ic_search_small);

		id = searchView.getContext().getResources()
				.getIdentifier("android:id/search_plate", null, null);
		searchView.findViewById(id).setBackgroundResource(
				R.drawable.edittext_bg);

	}

	/**
	 * Search videos from Youtube API with the query text entered by user. This
	 * method is called as soon as user start typing but this will send only one
	 * request at a time to prevent useless calls to serevr.
	 * 
	 * @param query
	 *            the search query
	 */
	private void searchVideos(final String query)
	{
		if (running)
		{
			last = query;
			return;
		}
		running = true;
		findViewById(R.id.progress).setVisibility(View.VISIBLE);
		new Thread(new Runnable() {
			@Override
			public void run() {
				final ArrayList<Video> al = WebAccess.getVideosFromKyous(query);

				runOnUiThread(new Runnable() {
					@Override
					public void run()
					{
						findViewById(R.id.progress).setVisibility(View.GONE);
						running = false;
						list.setAdapter(null);
						if (al.size() == 0)
						{
							return;
						}

						vList = new ArrayList<Video>(al);
						adapter = new VideoAdapter();
						list.setAdapter(adapter);
						if (last != null)
						{
							String str = last;
							last = null;
							searchVideos(str);
						}
					}
				});

			}
		}).start();

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
			finish();
		return super.onOptionsItemSelected(item);
	}

	/**
	 * The Class VideoAdapter is the Adapter class for displaying video in
	 * ListView. It simply shows the video thumb image, title,date posted etc.
	 */
	private class VideoAdapter extends BaseAdapter
	{

		/** The image loader to load images one by one in async mode. */
		private ImageLoader loader = new ImageLoader(getDim(R.dimen.pad_125dp),
				getDim(R.dimen.pad_125dp), ImageLoader.SCALE_ASPECT_WIDTH);

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
			View v = convertView;
			if (v == null)
				v = getLayoutInflater().inflate(R.layout.video_list_item, null);
			final ImageView img = (ImageView) v.findViewById(R.id.img);

			Video vid = getItem(position);

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

}
