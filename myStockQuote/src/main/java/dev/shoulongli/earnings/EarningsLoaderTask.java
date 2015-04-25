package dev.shoulongli.earnings;

import java.util.ArrayList;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import android.os.AsyncTask;

public class EarningsLoaderTask extends AsyncTask<String, Void, EarningsCalendar>
{
	private EarningsLoaderCallback callback;
	public static final byte QUERY_BY_SYMBOL = 0;
	public static final byte QUERY_BY_DATE = 1;
	private byte queryType ;
	public EarningsLoaderTask(EarningsLoaderCallback callback, byte queryType)
	{
		this.callback = callback;
		this.queryType = queryType;
	}
	@Override
	public EarningsCalendar doInBackground(String... parameters )
	{
		EarningsCalendar earnings = new EarningsCalendar();
		
		String queryStr = parameters[0].trim();
		
		try
		{
			earnings = getEarningsCalendar(queryStr, queryType);
		}
		catch(Exception e)
		{
			
		}		
		
		return earnings;
	}
	@Override
	public void onPostExecute(EarningsCalendar earnings)
	{
		if(this.callback != null)
		{
			callback.onEarningsLoaded(earnings);
		}
	}
	public static EarningsCalendar getEarningsCalendar(String queryString, byte type)
	{
		String url = "http://biz.yahoo.com/research/earncal/today.html";
		if(type == QUERY_BY_SYMBOL)
		{
			//http://biz.yahoo.com/research/earncal/a/arm.l.html
			url = "http://biz.yahoo.com/research/earncal/"+queryString.charAt(0)+"/"+queryString+".html";
		}
		else if(QUERY_BY_DATE == type)
		{
			//http://biz.yahoo.com/research/earncal/20120722.html
			url = "http://biz.yahoo.com/research/earncal/"+queryString+".html";
		}
		ArrayList<EarningsCalendarItem> earnings = new ArrayList<EarningsCalendarItem>();
		Date d = new Date();
		try
		{
			Document doc = Jsoup.connect(url).get();

			Elements tables = doc.select("tr");
			for(Element table: tables)
			{
				Elements rows = table.select("td");
				if(rows.size() >= 4)
				{
					if(!rows.get(1).select("a").isEmpty() &&
							rows.get(1).select("a").attr("href").startsWith("http://finance.yahoo.com/q?s="))
					{
						EarningsCalendarItem item = new EarningsCalendarItem();
						item.setCompany(rows.get(0).text());
						item.setSymbol(rows.get(1).text());
						item.setEpsEstimate(rows.get(2).text());
						item.setTime(rows.get(3).text());
						earnings.add(item);
					}
					
				}
				
				else if(rows.size() == 1 && rows.get(0).text().startsWith("Earnings Announcements for" ))
				{
					//get the Date of Earnings
					//Tuesday, October 23
					String dateString = rows.get(0).text();
					System.out.println(dateString);
					String prefix = "Earnings Announcements for";
					int index = dateString.indexOf(prefix);
					dateString = dateString.substring(index +prefix.length()).trim();
					d = EarningsCalendar.parseToDate(dateString);
				}
				
				
			}
		
			 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		EarningsCalendar calendar = new EarningsCalendar(d, earnings);
		return calendar;
	}
}
