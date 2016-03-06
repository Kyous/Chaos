package com.chaos.utils;

import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;

/**
 * The Class ImageUtils is the utility class that holds many methods related to
 * image processing.
 */
public class ImageUtils
{

	/** The Constant SCALE_NONE. */
	public static final int SCALE_NONE = 0;

	/** The Constant SCALE_FITXY. */
	public static final int SCALE_FITXY = 1;// exact size

	/** The Constant SCALE_ASPECT. */
	public static final int SCALE_ASPECT = 2;// aspect ratio

	/** The Constant SCALE_CROP. */
	public static final int SCALE_CROP = 3;// crop and fill empty with color

	/** The Constant SCALE_FIT_CENTER. */
	public static final int SCALE_FIT_CENTER = 4;// scale and put at center and
													// fill empty with color
	/** The Constant SCALE_FIT_CENTER_NO_COL. */
	public static final int SCALE_FIT_CENTER_NO_COL = 5;// SCALE_FIT_CENTER and
	// not fill empty with
	// color
	/** The Constant SCALE_FIT_MIN_WH. */
	public static final int SCALE_FIT_MIN_WH = 6;// scale to min width/height

	/** The Constant SCALE_ASPECT_WIDTH. */
	public static final int SCALE_ASPECT_WIDTH = 7;// aspect ratio with width

	/** The Constant SCALE_ASPECT_HEIGHT. */
	public static final int SCALE_ASPECT_HEIGHT = 8;// aspect ratio with height

	/** The Constant SCALE_FIT_WIDTH. */
	public static final int SCALE_FIT_WIDTH = 9;// fit the width and scale the
												// height accordingly

	/**
	 * Gets the compressed bm.
	 * 
	 * @param b
	 *            the b
	 * @param w
	 *            the w
	 * @param h
	 *            the h
	 * @param scaleType
	 *            the scale type
	 * @return the compressed bm
	 */
	public static final Bitmap getCompressedBm(byte b[], int w, int h,
			int scaleType)
	{

		try
		{
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(b, 0, b.length, options);
			final int height = options.outHeight;
			final int width = options.outWidth;
			int inSampleSize = 1;

			if (height > h || width > w)
			{
				final int heightRatio = Math.round((float) height / (float) h);
				final int widthRatio = Math.round((float) width / (float) w);

				inSampleSize = Math.min(heightRatio, widthRatio);

			}
			options.inSampleSize = inSampleSize;
			options.inJustDecodeBounds = false;
			Bitmap bm = BitmapFactory.decodeByteArray(b, 0, b.length, options);

			return getCompressedBm(bm, scaleType, w, h);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Gets the compressed bm.
	 * 
	 * @param file
	 *            the file
	 * @param w
	 *            the w
	 * @param h
	 *            the h
	 * @param scaleType
	 *            the scale type
	 * @return the compressed bm
	 */
	public static final Bitmap getCompressedBm(File file, int w, int h,
			int scaleType)
	{

		try
		{
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(file.getAbsolutePath(), options);
			final int height = options.outHeight;
			final int width = options.outWidth;
			int inSampleSize = 1;

			if (height > h || width > w)
			{
				final int heightRatio = Math.round((float) height / (float) h);
				final int widthRatio = Math.round((float) width / (float) w);

				inSampleSize = Math.min(heightRatio, widthRatio);

			}
			options.inSampleSize = inSampleSize;
			options.inJustDecodeBounds = false;
			Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath(),
					options);

			return getCompressedBm(bm, scaleType, w, h);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets the compressed bm.
	 * 
	 * @param bm
	 *            the bm
	 * @param scaleType
	 *            the scale type
	 * @param w
	 *            the w
	 * @param h
	 *            the h
	 * @return the compressed bm
	 */
	private static Bitmap getCompressedBm(Bitmap bm, int scaleType, int w, int h)
	{
		if (scaleType == SCALE_ASPECT || scaleType == SCALE_ASPECT_HEIGHT
				|| scaleType == SCALE_ASPECT_WIDTH)
		{
			final int bitmapWidth = bm.getWidth();
			final int bitmapHeight = bm.getHeight();
			if (bitmapHeight > h || bitmapWidth > w)
			{
				float scale;
				if (scaleType == SCALE_ASPECT)
					scale = Math.max((float) w / (float) bitmapWidth, (float) h
							/ (float) bitmapHeight);
				else if (scaleType == SCALE_ASPECT_WIDTH)
					scale = (float) w / (float) bitmapWidth;
				else
					scale = (float) h / (float) bitmapHeight;
				int scaledWidth = Math.round(bitmapWidth * scale);
				int scaledHeight = Math.round(bitmapHeight * scale);

				return Bitmap.createScaledBitmap(bm, scaledWidth, scaledHeight,
						true);
			}
			else if (scaleType == SCALE_ASPECT_HEIGHT)// remove if not want
														// exact height
			{
				Bitmap bm1 = Bitmap.createBitmap(bitmapWidth, h,
						Config.ARGB_8888);
				Canvas c = new Canvas(bm1);
				c.drawBitmap(bm, 0f, (h - bitmapHeight) / 2f, null);
				return bm1;
			}
			else if (scaleType == SCALE_ASPECT_WIDTH)// remove if not want exact
														// width
			{
				Bitmap bm1 = Bitmap.createBitmap(w, bitmapHeight,
						Config.ARGB_8888);
				Canvas c = new Canvas(bm1);
				c.drawBitmap(bm, (w - bitmapWidth) / 2f, 0f, null);
				return bm1;
			}

		}
		else if (scaleType == SCALE_FIT_WIDTH)
		{
			final int bitmapWidth = bm.getWidth();
			final int bitmapHeight = bm.getHeight();
			float scale = (float) w / (float) bitmapWidth;
			int scaledWidth = Math.round(bitmapWidth * scale);
			int scaledHeight = Math.round(bitmapHeight * scale);

			return Bitmap.createScaledBitmap(bm, scaledWidth, scaledHeight,
					true);
		}
		else if (scaleType == SCALE_CROP || scaleType == SCALE_FIT_CENTER
				|| scaleType == SCALE_FIT_MIN_WH
				|| scaleType == SCALE_FIT_CENTER_NO_COL)
		{
			final int bitmapWidth = bm.getWidth();
			final int bitmapHeight = bm.getHeight();
			if (bitmapHeight > h || bitmapWidth > w)
			{
				float scale;
				if (scaleType == SCALE_CROP)
					scale = Math.max((float) w / (float) bitmapWidth, (float) h
							/ (float) bitmapHeight);
				else
					scale = Math.min((float) w / (float) bitmapWidth, (float) h
							/ (float) bitmapHeight);
				int scaledWidth = Math.round(bitmapWidth * scale);
				int scaledHeight = Math.round(bitmapHeight * scale);

				bm = Bitmap.createScaledBitmap(bm, scaledWidth, scaledHeight,
						true);
				if (scaleType == SCALE_FIT_MIN_WH)
					return bm;
			}

			float x = 0;
			float y = 0;
			if (bm.getWidth() < w)
				x = (w - bm.getWidth()) / 2f;
			if (bm.getHeight() < h)
				y = (h - bm.getHeight()) / 2f;

			Bitmap bm1 = Bitmap.createBitmap(w, h, Config.ARGB_8888);
			Canvas c = new Canvas(bm1);
			if (scaleType != SCALE_FIT_CENTER_NO_COL)
				c.drawColor(bm.getPixel(10, 10));
			c.drawBitmap(bm, x, y, null);
			return bm1;
		}
		else if (scaleType == SCALE_FITXY)
			return Bitmap.createScaledBitmap(bm, w, h, true);
		return bm;
	}

	/**
	 * Gets the orientation fixed image.
	 * 
	 * @param f
	 *            the f
	 * @param w
	 *            the w
	 * @param h
	 *            the h
	 * @param scaleType
	 *            the scale type
	 * @return the orientation fixed image
	 */
	public static Bitmap getOrientationFixedImage(File f, int w, int h,
			int scaleType)
	{

		try
		{
			ExifInterface exif = new ExifInterface(f.getPath());
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			int angle = 0;

			if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
			{
				angle = 90;
			}
			else if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
			{
				angle = 180;
			}
			else if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
			{
				angle = 270;
			}

			Matrix mat = new Matrix();
			mat.postRotate(angle);

			Bitmap bmp = getCompressedBm(f, w, h, scaleType);
			Bitmap correctBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
					bmp.getHeight(), mat, true);
			return correctBmp;
		} catch (OutOfMemoryError oom)
		{
		} catch (Exception e)
		{
		}
		return getCompressedBm(f, w, h, scaleType);
	}

	/**
	 * Save compressed bm.
	 * 
	 * @param source
	 *            the source
	 * @param dest
	 *            the dest
	 */
	public static final void saveCompressedBm(String source, String dest)
	{

		try
		{
			Options opt = new Options();
			opt.inSampleSize = 4;
			Bitmap bm = BitmapFactory.decodeFile(source, opt);
			bm.compress(CompressFormat.PNG, 100, new FileOutputStream(dest));

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * Save orientation fix image.
	 * 
	 * @param src
	 *            the src
	 * @param dest
	 *            the dest
	 * @param w
	 *            the w
	 * @param h
	 *            the h
	 * @param scaleType
	 *            the scale type
	 */
	public static final void saveOrientationFixImage(File src, File dest,
			int w, int h, int scaleType)
	{

		try
		{
			Bitmap bm = getOrientationFixedImage(src, w, h, scaleType);
			if (bm != null)
				bm.compress(CompressFormat.JPEG, 100,
						new FileOutputStream(dest));

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}
}
