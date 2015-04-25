package dev.shoulongli.rss;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.os.AsyncTask;
import android.util.Log;


public class RssLoaderTask extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>>
{
	private RssLoaderCallback callback;
	public RssLoaderTask(RssLoaderCallback callback)
	{
		this.callback = callback;
	}
	
	@Override
	public ArrayList<HashMap<String, String>> doInBackground(String... params)
	{
		
		ArrayList<HashMap<String, String>> list = new  ArrayList<HashMap<String, String>>();
		//InputStream is = getResources().openRawResource(R.raw.rssfeed);
		String url = params[0].trim();
		try
		{
			InputStream is = new URL(url).openConnection().getInputStream();
			list = RssFeedParserFactory.getParser().parse(is);
		}
		catch(Exception e)
		{
			Log.i(RssLoaderTask.class.getName(), e.getMessage());
		}
		return list;
	}
	@Override
	public void onPostExecute(ArrayList<HashMap<String, String>> result)
	{
		if(!this.isCancelled() && 
				result != null &&
				result.size() > 0 &&
				callback != null)
		{	
			callback.onRssLoaded(result);
			
		}
		
	}
}
