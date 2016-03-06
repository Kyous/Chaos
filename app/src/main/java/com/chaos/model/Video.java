package com.chaos.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * The Class Video is a simple Java Bean that is used to hold all the
 * information of a Video such as Video Name, Detail, images, date, author, url
 * etc
 */
public class Video implements Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4112656724950462978L;

	/** The url. */
	private String url;

	/** The title. */
	private String title;

	/** The desc. */
	private String desc;

	/** The image small. */
	private String imageS;

	/** The image medium. */
	private String imageM;

	/** The image large. */
	private String imageL;

	/** The date. */
	private String date;

	/** The posted by. */
	private String postedBy;

	/**
	 * Gets the url.
	 * 
	 * @return the url
	 */
	public String getUrl()
	{

		return url;
	}

	/**
	 * Sets the url.
	 * 
	 * @param url
	 *            the new url
	 */
	public void setUrl(String url)
	{

		this.url = url;
	}

	/**
	 * Gets the title.
	 * 
	 * @return the title
	 */
	public String getTitle()
	{

		return title;
	}

	/**
	 * Sets the title.
	 * 
	 * @param title
	 *            the new title
	 */
	public void setTitle(String title)
	{

		this.title = title;
	}

	/**
	 * Gets the desc.
	 * 
	 * @return the desc
	 */
	public String getDesc()
	{

		return desc;
	}

	/**
	 * Sets the desc.
	 * 
	 * @param desc
	 *            the new desc
	 */
	public void setDesc(String desc)
	{

		this.desc = desc;
	}

	/**
	 * Gets the image s.
	 * 
	 * @return the image s
	 */
	public String getImageS()
	{
		return imageS;
	}

	/**
	 * Sets the image s.
	 * 
	 * @param imageS
	 *            the new image s
	 */
	public void setImageS(String imageS)
	{
		this.imageS = imageS;
	}

	/**
	 * Gets the image m.
	 * 
	 * @return the image m
	 */
	public String getImageM()
	{
		return imageM;
	}

	/**
	 * Sets the image m.
	 * 
	 * @param imageM
	 *            the new image m
	 */
	public void setImageM(String imageM)
	{
		this.imageM = imageM;
	}

	/**
	 * Gets the image l.
	 * 
	 * @return the image l
	 */
	public String getImageL()
	{
		return imageL;
	}

	/**
	 * Sets the image l.
	 * 
	 * @param imageL
	 *            the new image l
	 */
	public void setImageL(String imageL)
	{
		this.imageL = imageL;
	}

	/**
	 * Gets the date.
	 * 
	 * @return the date
	 */
	public String getDate()
	{
		return date;
	}

	/**
	 * Sets the date.
	 * 
	 * @param date
	 *            the new date
	 */
	public void setDate(String date)
	{
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
			SimpleDateFormat output = new SimpleDateFormat("dd MMM yy HH:mm",
					Locale.US);
			Date d = sdf.parse(date);
			this.date = output.format(d);
		} catch (Exception e)
		{
			e.printStackTrace();
			this.date = date;
		}
	}

	/**
	 * Gets the posted by.
	 * 
	 * @return the posted by
	 */
	public String getPostedBy()
	{
		return postedBy;
	}

	/**
	 * Sets the posted by.
	 * 
	 * @param postedBy
	 *            the new posted by
	 */
	public void setPostedBy(String postedBy)
	{
		this.postedBy = postedBy;
	}

}
