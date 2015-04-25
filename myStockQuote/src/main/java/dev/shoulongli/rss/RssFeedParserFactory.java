package dev.shoulongli.rss;

public class RssFeedParserFactory 
{
	public static RssFeedParser getParser()
	{
		return getParser(RssFeedParserType.ANDROID_SAX);
	}
	public static RssFeedParser getParser(RssFeedParserType type)
	{
		switch(type)
		{		
		case ANDROID_SAX:
			return new AndroidSaxRssFeedParser();
		}
		return null;
	}
}
