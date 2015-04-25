package dev.shoulongli.rss;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.xml.sax.Attributes;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Log;
import android.util.Xml;

public class AndroidSaxRssFeedParser implements RssFeedParser
{

	//private HashMap<String ,String> map;
	public ArrayList<HashMap<String, String>> parse(InputStream is)
	{
		final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		final HashMap<String ,String> map = new HashMap<String, String>();
		RootElement root = new RootElement(RSS);
		Element channel = root.getChild(CHANNEL);
		Element item = channel.getChild(ITEM);
		
		item.setStartElementListener(new StartElementListener(){
			@Override
			public void start(Attributes attr)
			{
				map.clear();
			}
		});
		item.setEndElementListener(new EndElementListener()
		{
			@Override
			public void end()
			{
				list.add((HashMap<String, String>)(map.clone()));
			}
		});
		item.getChild(TITLE).setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				map.put(TITLE, body);
			}
		});
		item.getChild(LINK).setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				map.put(LINK, body);
			}
		});
		item.getChild(PUB_DATE).setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				map.put(PUB_DATE, body);
			}
		});
		item.getChild(DESCRIPTION).setEndTextElementListener(new EndTextElementListener()
		{
			@Override
			public void end(String body)
			{
				map.put(DESCRIPTION, body);
			}
		});
		
		
		
		try
		{
			Xml.parse(is, Xml.Encoding.UTF_8, root.getContentHandler());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}
}
