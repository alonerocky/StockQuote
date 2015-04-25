package dev.shoulongli.stock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Stock implements Cloneable, Parcelable, Comparable<Stock>
{
	public static final String COMPANY_NAME_TAG = "n";
	public static final String LAST_TRADE_TAG = "l1";
	public static final String CHANGE_VALUE_TAG = "c1";
	public static final String CHANGE_PERCENT_TAG = "p2";
	
	
	public static final String ASK_TAG = "a";
	public static final String BID_TAG = "b";
	
	
	public static final String PREV_CLOSE_TAG = "p";
	public static final String DAY_RANGE_TAG = "m";
	
	public static final String OPEN_TAG = "o";
	public static final String YEAR_RANGE_TAG = "w";
	
	public static final String VOLUME_TAG = "v";
	public static final String MARKET_CAP_TAG = "j1";
	
	public static final String PE_RATIO_TAG = "r";
	public static final String DIVIDEND_YIELD_TAG = "y";
	
	public static final String EPS_TAG = "e7";
	public static final String YEAR_TAGET_EST_TAG = "t8";
	
	public static final String LAST_TRADE_TIME_TAG = "t1";
	public static final String DAILY_AVG_VOLUME_TAG = "a2";
		
	
	private String symbol;
	//(tag,value)
	private HashMap<String, String> attributes = new HashMap<String, String>();
	
	public Stock(String symbol)
	{
		this.symbol = symbol;
	}
	
	public String getSymbol()
	{
		return this.symbol;
	}
	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}
	public HashMap<String, String> getAttributes()
	{
		return attributes;
	}
	public void setAttributes(HashMap<String, String> attr)
	{
		this.attributes = attr;
	}
	public String getAttributeByTag(String tag)
	{
		return attributes.get(tag);
	}
	public void setAttributeByTag(String tag, String value)
	{
		attributes.put(tag, value);
	}
	
	
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(symbol);
		sb.append("\n");
		Iterator iterator = attributes.keySet().iterator();
		while(iterator.hasNext())
		{
			String key = (String) iterator.next();
			sb.append(key);
			sb.append(": ");
			sb.append(attributes.get(key));
			sb.append("\n");
		}
		
		return sb.toString();
	}
	@Override
	public boolean equals(Object other)
	{
		if(other instanceof Stock)
		{
			Stock otherStock = (Stock)other;
			return otherStock.symbol.equals(symbol);
		}
		return false;
	}
	@Override
	public int hashCode()
	{
		return symbol.hashCode();
	}
	public String getCompanyName()
	{
		String name = symbol;
		try
		{
			name = getAttributeByTag(COMPANY_NAME_TAG);
		}
		catch(Exception e)
		{
			
		}			
		if(name == null && symbol != null)
		{
			name =  symbol;
		} 
		return name; 
	}
	public void setCompanyName(String name)
	{
		setAttributeByTag(COMPANY_NAME_TAG,name);
	}
	public double getAskPrice()
	{
		double ask = 0;
		try
		{
			ask =  Double.parseDouble(getAttributeByTag(ASK_TAG));
		}
		catch(Exception e)
		{
			
		}
		return ask;
	}
	public void setAskPrice(String ask)
	{
		setAttributeByTag(ASK_TAG,ask);
	}
	public double getBidPrice()
	{
		double bid = 0;
		try
		{
			bid = Double.parseDouble(getAttributeByTag(BID_TAG));
		}
		catch(Exception e)
		{
			
		}
		return bid;
	}
	public void setBidPrice(String bid)
	{
		setAttributeByTag(BID_TAG,bid);
	}
	public long getVolume()
	{
		long volume = 0;
		try
		{
			volume = Long.parseLong(getAttributeByTag(VOLUME_TAG));
		}
		catch(Exception e)
		{
			
		}
		return volume;
	}
	public String getVolumeStr()
	{
		String v = getAttributeByTag(VOLUME_TAG);
		try
		{
			long volume = Long.parseLong(getAttributeByTag(VOLUME_TAG));
			return NumberFormat.getInstance(Locale.US).format(volume);
		}
		catch(Exception e)
		{
			
		}
		return v;
	}
	public String getMarketCap()
	{
		//like 11.850B
		return getAttributeByTag(MARKET_CAP_TAG);
	}
	public void setMarketCap(String cap)
	{
		setAttributeByTag(MARKET_CAP_TAG, cap);
	}
	public void setVolume(String volume)
	{
		setAttributeByTag(VOLUME_TAG,volume);
	}
	public double getPERatio()
	{
		//like 63.51
		double pe = 0;
		try
		{
			pe = Double.parseDouble(getAttributeByTag(PE_RATIO_TAG));
		}
		catch(Exception e)
		{
			
		}
		return pe;
		
	}
	public void setPERatio(String pe)
	{
		setAttributeByTag(PE_RATIO_TAG,pe);
	}
	public void setPERatio(double pe)
	{
		setAttributeByTag(PE_RATIO_TAG,pe+"");
	}
	public double getEPS()
	{
		//like 0.7
		double eps = 0;
		try
		{
			eps = Double.parseDouble(getAttributeByTag(EPS_TAG));
		}
		catch(Exception e)
		{
			
		}
		return eps;
	}
	public void setEPS(String eps)
	{
		setAttributeByTag(EPS_TAG,eps);
	}
	public void setEPS(double eps)
	{
		setAttributeByTag(EPS_TAG,eps+"");
	}
	public double getDividendYield()
	{
		//like 0.7
		double dividend = 0;
		try
		{
			dividend = Double.parseDouble(getAttributeByTag(DIVIDEND_YIELD_TAG));
		}
		catch(Exception e)
		{
			
		}
		return dividend;
	}
	public void setDividendYield(String dividend)
	{
		setAttributeByTag(DIVIDEND_YIELD_TAG, dividend);
	}
	public void setDividendYield(double dividend)
	{
		setAttributeByTag(DIVIDEND_YIELD_TAG, dividend+"");
	}
	public double get1YearTarget()
	{
		//like 63.51
		double target = 0;
		try
		{
			target = Double.parseDouble(getAttributeByTag(YEAR_TAGET_EST_TAG));
		}
		catch(Exception e)
		{
			
		}
		return target;
	}
	public void set1YearTarget(double target)
	{
		setAttributeByTag(YEAR_TAGET_EST_TAG, target+"");
	}
	public void set1yearTarget(String target)
	{
		setAttributeByTag(YEAR_TAGET_EST_TAG, target);
	}
	public double getLastTrade()
	{
		//like  23.4
		try
		{
			return Double.parseDouble(getAttributeByTag(LAST_TRADE_TAG));
		}
		catch(Exception e)
		{
			
		}
		return 0.0;
		
	}
	public void setLastTrade(String lastTrade)
	{
		setAttributeByTag(LAST_TRADE_TAG,lastTrade);
	}
	public void setLastTrade(double lastTrade)
	{
		setLastTrade(lastTrade+"");
	}
	public double getOpenPrice()
	{
		//like  23.4
		try
		{
			return Double.parseDouble(getAttributeByTag(OPEN_TAG));
		}
		catch(Exception e)
		{
			
		}
		return 0.0;
	}
	public void setOpenPrice(double price)
	{
		setAttributeByTag(OPEN_TAG,price+"");
	}
	public void setOpenPrice(String price)
	{
		setAttributeByTag(OPEN_TAG,price);
	}
	public double getPrevClose()
	{
		//like 23.4
		try
		{
			return Double.parseDouble(getAttributeByTag(PREV_CLOSE_TAG));
		}
		catch(Exception e)
		{
			
		}
		return 0.0;
		
	}
	public void setPrevClose(String prevclose)
	{
		setAttributeByTag(PREV_CLOSE_TAG, prevclose);
	}
	
	public double getChangeValue()
	{
		//like "+3.21";	
		double result = 0.0;
		try
		{
			String changeValue = getAttributeByTag(CHANGE_VALUE_TAG);
			result =  Double.parseDouble(changeValue);
		}
		catch(Exception e)
		{
			
		}				
		return result;
	}
	public void setChangeValue(String changeValue)
	{
		setAttributeByTag(CHANGE_VALUE_TAG, changeValue);
	}
	public String getChangePercent()
	{		
		//like "+3.21%";				
		String percent = "N/A";
		try
		{
			percent = getAttributeByTag(CHANGE_PERCENT_TAG);
			if(percent == null || percent.trim().length() == 0)
			{
				percent = "N/A";
			}
		}
		catch(Exception e)
		{

		}
		return percent;
	}
	public double getChangePercentDouble( )
	{
		double result = 0.0;
		try
		{
			result = Double.parseDouble(getChangePercent().trim().replace("%", ""));
		}
		catch(Exception e)
		{
			
		}
		return result;
	}
	public void setChangePercent(String changePercent)
	{
		setAttributeByTag(CHANGE_PERCENT_TAG, changePercent);
	}
	public String getDaysRange()
	{
		//like 25.375 - 26.30
		return getAttributeByTag(DAY_RANGE_TAG);
	}
	public void setDaysRange(String range)
	{
		setAttributeByTag(DAY_RANGE_TAG, range);
	}
	public String get52wkRange()
	{
		//22.10 - 32.18
		return getAttributeByTag(YEAR_RANGE_TAG);
	}
	public void set52wkRange(String range)
	{
		setAttributeByTag(YEAR_RANGE_TAG, range);
	}
	public String getLastTradeTime()
	{
		return getAttributeByTag(LAST_TRADE_TIME_TAG);
	}
	public void setLastTradeTime(String lasttime)
	{
		setAttributeByTag(LAST_TRADE_TIME_TAG,lasttime);
	}
	public long getDailyAvgVolume()
	{
		long result = 0;
		try
		{
			String volume = getAttributeByTag(DAILY_AVG_VOLUME_TAG);
			result =  Long.parseLong(volume);
		}
		catch(Exception e)
		{
			
		}				
		return result;
	}
	public String getDailyAvgVolumeStr()
	{
		String volume = getAttributeByTag(DAILY_AVG_VOLUME_TAG);
		try
		{
			long result =  Long.parseLong(volume);
			return NumberFormat.getInstance(Locale.US).format(result);
		}
		catch(Exception e)
		{
			
		}				
		return volume;
	}
	public void setDailyAvgVolume(long avgvolume)
	{
		setAttributeByTag(DAILY_AVG_VOLUME_TAG,avgvolume+"");
	}
	public void setDailyAvgVolume(String avgvolume)
	{
		setAttributeByTag(DAILY_AVG_VOLUME_TAG,avgvolume);
	}
	@Override
	public int describeContents()
	{
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(this.symbol);
		dest.writeInt(attributes.size());
		
		for(String s: attributes.keySet())
		{
			dest.writeString(s);
			dest.writeString(attributes.get(s));
		}
		
	}
	public void readFromParcel(Parcel src)
	{
		this.symbol = src.readString();
		int count = src.readInt();
		for(int i = 0 ; i < count; i ++)
		{
			attributes.put(src.readString(), src.readString());
		}
	}
	private Stock(Parcel in)
	{
		readFromParcel(in);
	}
	public static final Parcelable.Creator<Stock> CREATOR = 
		new Parcelable.Creator<Stock>() 
		{
		@Override 
		public Stock createFromParcel(Parcel in)
		{
			return new Stock(in);
		}
		@Override 
		public Stock[] newArray(int size)
		{
			return new Stock[size];
		}
		};
	public static byte[] toByteArray(Stock s)
	{
		byte[] buf = new byte[0];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try
		{
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(s.getSymbol());
			oos.writeObject(s.getAttributes());
			buf = baos.toByteArray();
			oos.close();
			baos.close();
		}
		catch(Exception e)
		{
			
		}		
		return buf;
		
	}
	public static Stock fromByteArray(byte[] buff)
	{
		if(buff == null || buff.length == 0)
			return null;
		Stock s = null;
		ByteArrayInputStream bios = new ByteArrayInputStream(buff);
		ObjectInputStream ois = null;
		try
		{
			ois = new ObjectInputStream(bios);
			String symbol = (String)ois.readObject();
			HashMap<String, String> attr = (HashMap<String, String>)ois.readObject();
			s = new Stock(symbol);
			s.setAttributes(attr);
			ois.close();
			bios.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return s;
	}

	@Override
	public int compareTo(Stock another) 
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
