package dev.shoulongli.rss;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public interface RssFeedParser 
{
	public static final String RSS = "rss";
	public static final String CHANNEL = "channel";
	public static final String ITEM = "item";
	
	public static final String TITLE = "title";
	public static final String LINK = "link";
	public static final String DESCRIPTION  = "description";	
	public static final String PUB_DATE = "pubDate";
	
	
	public ArrayList<HashMap<String, String>> parse(InputStream is);
	
	
	/*
	 * Top-Level Elements
Element 	Description
xml 	The Yahoo! Finance company news RSS feed conforms to XML 1.0. No child elements.
rss 	The Yahoo! Finance company news RSS feed conforms to RSS 2.0.
Child elements: channel

Channel Elements

The channel element contains metadata about the feed and its contents.
Element 	Description
	title 	The title of the feed, which includes the ticker symbols in the request. For example "Yahoo! Finance: YHOO News"
	link 	A URL for the Yahoo! Finance News page for the ticker symbols in the request. For example http://finance.yahoo.com/q/h?s=yhoo
	language 	The language of the news items, for example, en-us for US English.
	description 	The overall description of the feed including the company names for the ticker symbols in the request, for example "Latest Financial News for YAHOO INC
	copyright 	The Yahoo! copyright statement for this feed.  
	lastBuildDate 	The last time the feed was updated (the current date and time). The format is in the date format defined by RFC822 Section 5, for example Mon, 256 Sep 17:25:18 -0700.
	image 	The image used to identify this feed. See Image Elements for element descriptions
			Child elements: url, title, link, width, height
			item 	A news headline. Each Yahoo! Finance company news feed contains multiple item elements, one for each headline. See Item Elements for element descriptions.
			Child elements: title, link, description, guid, pubDate

Image Elements

The image element describes an image or icon used to identify the feed.
Element 	Description
	title 	The title of the image, for example "Yahoo! Finance"
	link 	The URL of the current search (same as the link element in the parent container)
	url 	The URL of the image
	width 	The width of the image, in pixels
	height 	The height of the image, in pixels

Item Elements

Each item element contains a news item. If the request contained multiple ticker 
symbols to search, news items for all companies are returned as individual item elements. Note that the feed simply returns the most recent 25 news items for all the ticker symbols in the request; it does not distinguish which item goes with which company.
Element 	Description
	title 	The news headline, for example "Yahoo! Announces Quarterly Earnings"
	link 	The Yahoo! Finance URL for this news item.
	description 	If given, a short summary of the news item. Many news items include an empty description element.
	guid 	Unique identifier for the item. For news items the guid is the Yahoo! Finance ID for the listing. The attribute isPermaLink is false.
	pubDate 	The date and time this news item was posted, in the date format defined by RFC822 Section 5, Mon, 256 Sep 17:25:18 -0700. 
	 
	 * 
	 * 
	 */
}
