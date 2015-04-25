package dev.shoulongli.stock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class StockQuoteUtil 
{
	
	private final static String[] TAG_TABLE = 
	{
		"a" ,  "Ask", 	
		"a2" , "Average Daily Volume", 	
		"a5" , "Ask Size",
		"b" ,  "Bid", 	
		"b2" , "Ask (Real-time)", 	
		"b3" , "Bid (Real-time)",
		"b4" , "Book Value", 	
		"b6" , "Bid Size", 	
		"c" ,  "Change & Percent Change",
		"c1" , "Change", 	
		"c3" , "Commission", 	
		"c6" , "Change (Real-time)",
		"c8" , "After Hours Change (Real-time)", 	
		"d" ,  "Dividend/Share", 	
		"d1" , "Last Trade Date",
		"d2" , "Trade Date", 	
		"e" ,  "Earnings/Share", 	
		"e1" , "Error Indication (returned for symbol changed / invalid)",
		"e7" , "EPS Estimate Current Year", 	
		"e8" , "EPS Estimate Next Year", 	
		"e9" , "EPS Estimate Next Quarter",
		"f6" , "Float Shares", 	
		"g" ,  "Day's Low",	
		"g1" , "Holdings Gain Percent",
		"g3" , "Annualized Gain",	
		"g4" , "Holdings Gain", 	
		"g5" , "Holdings Gain Percent (Real-time)",
		"g6" , "Holdings Gain (Real-time)", 	
		"h" ,  "Day's High",
		"i" ,  "More Info", 	
		"i5" , "Order Book (Real-time)",
		"j" ,  "52-week Low", 	
		"j1" , "Market Capitalization",	
		"j3" , "Market Cap (Real-time)", 	
		"j4" , "EBITDA",
		"j5" , "Change From 52-week Low", 	
		"j6" , "Percent Change From 52-week Low", 	
		"k" ,  "52-week High", 	
		"k1" , "Last Trade (Real-time) With Time",
		"k2" , "Change Percent (Real-time)", 	
		"k3" , "Last Trade Size", 	
		"k4" , "Change From 52-week High",
		"k5" , "Percebt Change From 52-week High", 	
		"l" ,  "Last Trade (With Time)", 	
		"l1" , "Last Trade (Price Only)",
		"l2" , "High Limit", 	
		"l3" , "Low Limit", 	
		"m" ,  "Day's Range",
		"m2" , "Day's Range (Real-time)", 	
		"m3" , "50-day Moving Average", 	
		"m4" , "200-day Moving Average",
		"m5" , "Change From 200-day Moving Average",
		"m6" , "Percent Change From 200-day Moving Average", 	
		"m7" , "Change From 50-day Moving Average",
		"m8" , "Percent Change From 50-day Moving Average", 	
		"n" ,  "Name",
		"n4" , "Notes",
		"o" ,  "Open", 	
		"p" ,  "Previous Close", 
		"p1" , "Price Paid",
		"p2" , "Change in Percent", 	
		"p5" , "Price/Sales", 	
		"p6" , "Price/Book",
		"q" ,  "Ex-Dividend Date", 	
		"r" ,  "P/E Ratio", 	
		"r1" , "Dividend Pay Date",
		"r2" , "P/E Ratio (Real-time)", 	
		"r5" , "PEG Ratio", 	
		"r6" , "Price/EPS Estimate Current Year",
		"r7" , "Price/EPS Estimate Next Year", 	
		"s" ,  "Symbol", 	
		"s1" , "Shares Owned",
		"s7" , "Short Ratio", 	
		"t1" , "Last Trade Time", 	
		"t6" , "Trade Links",
		"t7" , "Ticker Trend", 	
		"t8" , "1 yr Target Price", 	
		"v" ,  "Volume",
		"v1" , "Holdings Value", 	
		"v7" , "Holdings Value (Real-time)", 	
		"w" ,  "52-week Range",
		"w1" , "Day's Value Change", 	
		"w4" , "Day's Value Change (Real-time)", 	
		"x" ,  "Stock Exchange",
		"y" ,  "Dividend Yield"
	};

	
	public static int getIndexByTag(String tag)
	{
		if(tag != null)
		{
			for(int i = 0 ; i < TAG_TABLE.length;i+=2)
			{
				if(tag.trim().equalsIgnoreCase(TAG_TABLE[i].trim()))
				{
					return i;
				}
			}
		}
		return -1;
	}
	public static String getDescriptionByTag(String tag)
	{
		if(tag != null)
		{
			for(int i = 0 ; i < TAG_TABLE.length;i+=2)
			{
				if(tag.trim().equalsIgnoreCase(TAG_TABLE[i].trim()))
				{
					return TAG_TABLE[i+1];
				}
			}
		}
		return "Invalid Tag";
	}
	//FIXME
	//use FSM
	public static ArrayList<String> getTags(String stream)
	{
		ArrayList<String> tags = new ArrayList<String>();
		int start = 0 ;
		int end = start +1 ;
		if(stream != null)
		{
			char c;
			while(start < stream.length() )
			{
				/*
				 * 
				 * if next is digit
				 * 			keep moving
				 * else
				 * 			get one tag
				 * 
				 */
				c = stream.charAt(start);
				if(c < 'a' || c > 'z')
				{
					break;
				}
				if(end >= stream.length())
				{
					tags.add(stream.substring(start,end));
					break;
				}
				else
				{
					while(end < stream.length() && Character.isDigit((c = stream.charAt(end))) && end < stream.length())
					{
						end++;
					}
					tags.add(stream.substring(start,end));
					start=end;
					end++;
				}
							
			}
			
		}
		return tags;
	}
	public static String getStockQuoteUrl(String symbol)
	{
		return String.format(StockQuoteConfigure.STOCK_QUOTE_URL, symbol,StockQuoteConfigure.TAGS);
	}
	public static String getStockQuoteQueryUrl(String queryStr)
	{
		String str = queryStr.replace(" ", "%20");  
		return String.format(StockQuoteConfigure.STOCK_QUOTE_QUERY, str);
	}
	
	public static Date getLastFriday( int year , int month) 
	{
		Calendar cal = Calendar.getInstance();
		cal.set( year, month + 1, 1 );
		cal.add( Calendar.DAY_OF_MONTH, -( cal.get( Calendar.DAY_OF_WEEK ) % 7 + 1 ) );
		return cal.getTime();
	}
	public static Date getOptionExpireDate(int year, int month)
	{
		int day = getNthDay(3, Calendar.FRIDAY, year, month);
		return new Date(year, month, day);
	}
	public static int getNthDay(int n, int dayType, int year, int month)
	{
	    GregorianCalendar firstDay = new GregorianCalendar(year, month, 1);
	    int firstDayType = firstDay.get(Calendar.DAY_OF_WEEK);
	    boolean isInFirstWeek = firstDayType <= dayType;

	    int dayOfMonth = dayType;
	    dayOfMonth += ((n - 1) * 7);
	    dayOfMonth -= (firstDayType - 1);
	    if (!isInFirstWeek)
	    {
	        dayOfMonth += 7;
	    }
	    
	    return dayOfMonth;
	}
	
}
