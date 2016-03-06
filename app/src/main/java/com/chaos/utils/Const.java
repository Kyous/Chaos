package com.chaos.utils;

import java.io.File;

import android.content.Context;
import android.os.Environment;

/**
 * The Class Const holds all the application level constants.
 */
public class Const
{

	/** The Constant REQ_RELOAD. */
	public static final int REQ_RELOAD = 1;
	
	/** The Constant REQ_RELOAD1. */
	public static final int REQ_RELOAD1 = 2;

	/** The Constant EXTRA_DATA. */
	public static final String EXTRA_DATA = "extraData";
	
	/** The Constant EXTRA_DATA1. */
	public static final String EXTRA_DATA1 = "extraData1";

	/** The Constant ROOT_DIR. */
	public static final File ROOT_DIR;
	
	/** The Constant WEB_DIR. */
	public static final File WEB_DIR;
	
	/** The Constant CACHE_DIR. */
	public static final File CACHE_DIR;
	
	/** The Context. */
	public static Context ctx;

	static
	{

		ROOT_DIR = new File(Environment.getExternalStorageDirectory(), "ChaOS");
		if (!ROOT_DIR.exists())
		{
			ROOT_DIR.mkdirs();
			Utils.createNoMediaFile(ROOT_DIR);
		}
		WEB_DIR = new File(ROOT_DIR, "web");
		if (!WEB_DIR.exists())
			WEB_DIR.mkdirs();

		CACHE_DIR = new File(ROOT_DIR, "Cache");
		if (!CACHE_DIR.exists())
			CACHE_DIR.mkdirs();

		/*File[] f=CACHE_DIR.listFiles();
		for(File ff:f)
			ff.delete();*/

	}

}
