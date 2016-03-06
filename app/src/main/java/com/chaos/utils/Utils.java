package com.chaos.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * The Class Utils holds many different Utility methods.
 */
public class Utils
{

	/**
	 * Show dialog.
	 * 
	 * @param ctx
	 *            the ctx
	 * @param msg
	 *            the msg
	 * @param btn1
	 *            the btn1
	 * @param btn2
	 *            the btn2
	 * @param listener1
	 *            the listener1
	 * @param listener2
	 *            the listener2
	 * @return the alert dialog
	 */
	public static AlertDialog showDialog(Context ctx, String msg, String btn1,
			String btn2, DialogInterface.OnClickListener listener1,
			DialogInterface.OnClickListener listener2)
	{

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		// builder.setTitle(R.string.app_name);
		builder.setMessage(msg).setCancelable(false)
				.setPositiveButton(btn1, listener1);
		if (btn2 != null && listener2 != null)
			builder.setNegativeButton(btn2, listener2);

		AlertDialog alert = builder.create();
		alert.show();
		return alert;

	}

	/**
	 * Show dialog.
	 * 
	 * @param ctx
	 *            the ctx
	 * @param msg
	 *            the msg
	 * @param btn1
	 *            the btn1
	 * @param btn2
	 *            the btn2
	 * @param listener1
	 *            the listener1
	 * @param listener2
	 *            the listener2
	 * @return the alert dialog
	 */
	public static AlertDialog showDialog(Context ctx, int msg, int btn1,
			int btn2, DialogInterface.OnClickListener listener1,
			DialogInterface.OnClickListener listener2)
	{

		return showDialog(ctx, ctx.getString(msg), ctx.getString(btn1),
				ctx.getString(btn2), listener1, listener2);

	}

	/**
	 * Show dialog.
	 * 
	 * @param ctx
	 *            the ctx
	 * @param msg
	 *            the msg
	 * @param btn1
	 *            the btn1
	 * @param btn2
	 *            the btn2
	 * @param listener
	 *            the listener
	 * @return the alert dialog
	 */
	public static AlertDialog showDialog(Context ctx, String msg, String btn1,
			String btn2, DialogInterface.OnClickListener listener)
	{

		return showDialog(ctx, msg, btn1, btn2, listener,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id)
					{

						dialog.dismiss();
					}
				});

	}

	/**
	 * Show dialog.
	 * 
	 * @param ctx
	 *            the ctx
	 * @param msg
	 *            the msg
	 * @param btn1
	 *            the btn1
	 * @param btn2
	 *            the btn2
	 * @param listener
	 *            the listener
	 * @return the alert dialog
	 */
	public static AlertDialog showDialog(Context ctx, int msg, int btn1,
			int btn2, DialogInterface.OnClickListener listener)
	{

		return showDialog(ctx, ctx.getString(msg), ctx.getString(btn1),
				ctx.getString(btn2), listener);

	}

	/**
	 * Show dialog.
	 * 
	 * @param ctx
	 *            the ctx
	 * @param msg
	 *            the msg
	 * @param listener
	 *            the listener
	 * @return the alert dialog
	 */
	public static AlertDialog showDialog(Context ctx, String msg,
			DialogInterface.OnClickListener listener)
	{

		return showDialog(ctx, msg, ctx.getString(android.R.string.ok), null,
				listener, null);
	}

	/**
	 * Show dialog.
	 * 
	 * @param ctx
	 *            the ctx
	 * @param msg
	 *            the msg
	 * @param listener
	 *            the listener
	 * @return the alert dialog
	 */
	public static AlertDialog showDialog(Context ctx, int msg,
			DialogInterface.OnClickListener listener)
	{

		return showDialog(ctx, ctx.getString(msg),
				ctx.getString(android.R.string.ok), null, listener, null);
	}

	/**
	 * Show dialog.
	 * 
	 * @param ctx
	 *            the ctx
	 * @param msg
	 *            the msg
	 * @return the alert dialog
	 */
	public static AlertDialog showDialog(Context ctx, String msg)
	{

		return showDialog(ctx, msg, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id)
			{

				dialog.dismiss();
			}
		});

	}

	/**
	 * Show dialog.
	 * 
	 * @param ctx
	 *            the ctx
	 * @param title
	 *            the title
	 * @param msg
	 *            the msg
	 */
	public static void showDialog(Context ctx, String title, String msg)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle(title);
		builder.setMessage(msg).setCancelable(false)
				.setPositiveButton(ctx.getString(android.R.string.ok), null);

		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Show dialog.
	 * 
	 * @param ctx
	 *            the ctx
	 * @param msg
	 *            the msg
	 * @return the alert dialog
	 */
	public static AlertDialog showDialog(Context ctx, int msg)
	{

		return showDialog(ctx, ctx.getString(msg));

	}

	/**
	 * Show dialog.
	 * 
	 * @param ctx
	 *            the ctx
	 * @param title
	 *            the title
	 * @param msg
	 *            the msg
	 * @param listener
	 *            the listener
	 */
	public static void showDialog(Context ctx, int title, int msg,
			DialogInterface.OnClickListener listener)
	{

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(msg).setCancelable(false)
				.setPositiveButton(android.R.string.ok, listener);
		builder.setTitle(title);
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Checks if is online.
	 * 
	 * @return true, if is online
	 */
	public static final boolean isOnline()
	{
		return Utils.isOnline(Const.ctx);
	}

	/**
	 * Checks if is online.
	 * 
	 * @param ctx
	 *            the ctx
	 * @return true, if is online
	 */
	public static final boolean isOnline(Context ctx)
	{

		ConnectivityManager conMgr = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (conMgr.getActiveNetworkInfo() != null

		&& conMgr.getActiveNetworkInfo().isAvailable()

		&& conMgr.getActiveNetworkInfo().isConnected())
			return true;
		return false;
	}

	/**
	 * Hide keyboard.
	 * 
	 * @param ctx
	 *            the ctx
	 */
	public static final void hideKeyboard(Activity ctx)
	{

		if (ctx.getCurrentFocus() != null)
		{
			InputMethodManager imm = (InputMethodManager) ctx
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(ctx.getCurrentFocus().getWindowToken(),
					0);
		}
	}

	/**
	 * Hide keyboard.
	 * 
	 * @param ctx
	 *            the ctx
	 * @param v
	 *            the v
	 */
	public static final void hideKeyboard(Activity ctx, View v)
	{

		try
		{
			InputMethodManager imm = (InputMethodManager) ctx
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Show keyboard.
	 * 
	 * @param ctx
	 *            the ctx
	 * @param v
	 *            the v
	 */
	public static final void showKeyboard(Activity ctx, View v)
	{

		try
		{
			v.requestFocus();
			InputMethodManager imm = (InputMethodManager) ctx
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
			// imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
			// InputMethodManager.SHOW_IMPLICIT);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Copy file.
	 * 
	 * @param src
	 *            the src
	 * @param dst
	 *            the dst
	 */
	public static void copyFile(File src, File dst)
	{

		try
		{
			if (!dst.exists())
				dst.createNewFile();
			FileInputStream in = new FileInputStream(src);
			FileOutputStream out = new FileOutputStream(dst);

			int size = (int) src.length();
			byte[] buf = new byte[size];
			in.read(buf);
			out.write(buf);
			out.flush();
			out.close();
			in.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Creates the no media file.
	 * 
	 * @param dir
	 *            the dir
	 */
	public static void createNoMediaFile(File dir)
	{

		try
		{
			File f = new File(dir, ".nomedia");
			if (!f.exists())
				f.createNewFile();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
