package com.chaos.web;

import android.util.Log;

import com.chaos.model.Video;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * The Class WebAccess holds all the method related to Web-service/API communication.
 */
public class WebAccess
{

	/**
	 * Search youtube video.
	 *
	 * @param keyword the keyword
	 * @return the array list
	 */
	public static ArrayList<Video> getVideosFromKyous(String keyword)
	{
		ArrayList<Video> al = new ArrayList<Video>();
		try
		{
			//String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&order=relevance"
			//		+ "&maxResults=50&videoEmbeddable=true&fields=items(id,snippet)&key=";
			//String key = "AIzaSyB19AHY3Fpnd1_EE7TlQEcGnbbWB0weY4o";// "AIzaSyB19AHY3Fpnd1_EE7TlQEcGnbbWB0weY4o";
			//String q = "&q=" + Uri.encode("" + keyword);
			//url = url + key + q;

			String url = "http://www.kyous.co.za/viewmedia.php";

			Log.d("Url",url);

			URLConnection uc = new URL(url).openConnection();
			InputStream in = uc.getInputStream();

			BufferedReader buf = new BufferedReader(new InputStreamReader(in));
			String s;
			StringBuffer sb = new StringBuffer();
			while ((s = buf.readLine()) != null)
				sb.append(s);
			buf.close();
			in.close();

			JSONArray arr = new JSONObject(sb.toString()).getJSONArray("media_info");
			for (int i = 0; i < arr.length(); i++)
			{
				JSONObject obj = arr.getJSONObject(i);


				Video v = new Video();
				v.setDesc(obj.getString("description"));
				v.setImageS(obj.getString("thumbpath"));
				v.setImageM(obj.getString("thumbpath"));
				v.setImageL(obj.getString("thumbpath"));
				v.setTitle(obj.getString("title"));

				v.setUrl(obj.getString("videopath"));
				v.setDate(obj.getString("createdon"));
				v.setPostedBy(obj.getString("postedby"));
				al.add(v);
			}
		} catch (Exception e)
		{
			Log.d("", e.toString());
		}
		return al;
	}

	/**
	 * Gets the url of video rtsp.
	 *
	 * @param id the id
	 * @return the url video rtsp
	 */
	public static String getUrlVideoRTSP(String id)
	{
		/*try
		{
			String gdy = "http://gdata.youtube.com/feeds/api/videos/";
			DocumentBuilder documentBuilder = DocumentBuilderFactory
					.newInstance().newDocumentBuilder();
			URL url = new URL(gdy + id);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			Document doc = documentBuilder.parse(connection.getInputStream());
			Element el = doc.getDocumentElement();
			NodeList list = el.getElementsByTagName("media:content");// /media:content
			String cursor = "http://www.youtube.com/watch?v=" + id;
			for (int i = 0; i < list.getLength(); i++)
			{
				Node node = list.item(i);
				if (node != null)
				{
					NamedNodeMap nodeMap = node.getAttributes();
					HashMap<String, String> maps = new HashMap<String, String>();
					for (int j = 0; j < nodeMap.getLength(); j++)
					{
						Attr att = (Attr) nodeMap.item(j);
						maps.put(att.getName(), att.getValue());
					}
					if (maps.containsKey("yt:format"))
					{
						String f = maps.get("yt:format");
						if (maps.containsKey("url"))
						{
							cursor = maps.get("url");
						}
						if (f.equals("6"))
							return cursor;
					}
				}
			}
			return cursor;
		} catch (Exception ex)
		{
			Log.e("Get Url Video RTSP Exception======>>", ex.toString());
		}*/
		return id;//"http://www.youtube.com/watch?v=" + id;

	}
}
