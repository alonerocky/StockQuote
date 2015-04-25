package dev.shoulongli.rss;

public class RssItem 
{
	private String title;
	private String pubDate;
	private String desc;
	private String url;
	
	public RssItem(String title, String pubDate, String desc, String url)
	{
		this.title = title;
		this.pubDate = pubDate;
		this.desc = desc;
		this.url = url;
	}
}
