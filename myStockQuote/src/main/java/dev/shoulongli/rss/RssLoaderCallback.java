package dev.shoulongli.rss;

import java.util.ArrayList;
import java.util.HashMap;

public interface RssLoaderCallback 
{
	public void onRssLoaded(ArrayList<HashMap<String, String>> result);
}
