package dev.shoulongli.option;

import android.os.Parcel;
import android.os.Parcelable;

public class Option implements Parcelable
{
	public static final byte OPTION_CALL = 0;
	public static final byte OPTION_PUT = 1;
	//Strike	Symbol	Last	Chg	Bid	Ask	Vol	Open Int
	private float strike; //like 45.0
	private String symbol; //SINA120519C00045000
	private float last; //like 22.85
	private float chg; //like 0.00
	private float bid; //like 13.35
	private float ask; //like 15.00
	private int volume; //1
	private int openInt;//1
	public Option()
	{
		
	}
	public Option(float strike,
				  String symbole,
				  float last,
				  float chg,
				  float bid,
				  float ask,
				  int volume,
				  int openInt)
	{
		this.strike = strike;
		this.symbol = symbole;
		this.last = last;
		this.chg = chg;
		this.bid = bid;
		this.ask = ask;
		this.volume = volume;
		this.openInt = openInt;
	}
	public float getStrike()
	{
		return this.strike;
	}
	public void setStrike(float strike)
	{
		this.strike = strike;
	}
	public void setStrike(String strike)
	{
		try
		{
			this.strike = (Float.parseFloat(strike));
		}
		catch(Exception e)
		{
			
		}
	}
	public String getSymbol()
	{
		return this.symbol;
	}
	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}
	public float getLast()
	{
		return this.last;
	}
	public void setLast(float last)
	{
		this.last = last;
	}
	public void setLast(String last)
	{
		try
		{
			this.last = Float.parseFloat(last);
		}
		catch(Exception e)
		{
			
		}
	}
	public float getChange()
	{
		return this.chg;
	}
	public void setChange(float change)
	{
		this.chg = change;
	}
	public void setChange(String change)
	{
		try
		{
			this.chg = Float.parseFloat(change);
		}
		catch(Exception e)
		{
			
		}
	}
	public float getAsk()
	{
		return this.ask;
	}
	public void setAsk(float ask)
	{
		this.ask = ask;
	}
	public void setAsk(String ask)
	{
		try
		{
			this.ask = Float.parseFloat(ask);
		}
		catch(Exception e)
		{
			
		}
	}
	public float getBid()
	{
		return this.bid;
	}
	public void setBid(float bid)
	{
		this.bid = bid;
	}
	public void setBid(String bid)
	{
		try
		{
			this.bid = Float.parseFloat(bid);
		}
		catch(Exception e)
		{
			
		}
	}
	public int getVolume()
	{
		return this.volume;
	}
	public void setVolume(int volume)
	{
		this.volume = volume;
	}
	public void setVolume(String volume)
	{
		try
		{
			this.volume = Integer.parseInt(volume);
		}
		catch(Exception e)
		{
			
		}
	}
	public int getOpenInt()
	{
		return this.openInt;
	}
	public void setOpenInt(int openInt)
	{
		this.openInt = openInt;
	}
	public void setOpenInt(String openInt)
	{
		try
		{
			this.openInt = Integer.parseInt(openInt);
		}
		catch(Exception e)
		{
			
		}
	}
	public byte getType()
	{
		if(symbol != null )
		{
			for(int i = symbol.length() - 1; i >= 0; i--)
			{
				if(Character.isDigit(symbol.charAt(i)))
				{
					continue;
				}
				if('P' == Character.toUpperCase(symbol.charAt(i)))
				{
					return OPTION_PUT;
				}
				else if('C' == Character.toUpperCase(symbol.charAt(i)))
				{
					return OPTION_CALL;
				}
						
			}
		}
		return -1;
	}
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		return sb.append("strike: "+strike).
		   append("\nsymbol: "+symbol).
		   append("\nlast: "+last).
		   append("\nchg: "+chg).
		   append("\nbid: "+bid).
		   append("\nask: "+ask).
		   append("\nvolume: "+volume).
		   append("\nopen Int: "+openInt).toString();
		  
	}
	
	@Override
	public int describeContents()
	{
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags)
	{	
		dest.writeFloat(this.strike);
		dest.writeString(this.symbol);
		dest.writeFloat(last);
		dest.writeFloat(chg);
		dest.writeFloat(bid);
		dest.writeFloat(ask);
		dest.writeInt(volume);
		dest.writeInt(openInt);
		
	}
	public void readFromParcel(Parcel src)
	{
		this.strike = src.readFloat();
		this.symbol = src.readString();
		this.last = src.readFloat();
		this.chg = src.readFloat();
		this.bid = src.readFloat();
		this.ask = src.readFloat();
		this.volume = src.readInt();
		this.openInt = src.readInt();
		
	}
	private Option(Parcel in)
	{
		readFromParcel(in);
	}
	public static final Parcelable.Creator<Option> CREATOR = 
		new Parcelable.Creator<Option>() 
		{
		@Override 
		public Option createFromParcel(Parcel in)
		{
			return new Option(in);
		}
		@Override 
		public Option[] newArray(int size)
		{
			return new Option[size];
		}
		};
}