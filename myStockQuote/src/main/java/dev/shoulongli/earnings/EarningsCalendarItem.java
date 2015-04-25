package dev.shoulongli.earnings;

public class EarningsCalendarItem 
{
	private String company;
	private String symbol;
	private String epsEstimate;
	private String time;
	public EarningsCalendarItem()
	{
		
	}
	public EarningsCalendarItem(String company,
								String symbol,
								String epsEstimate,
								String time)
	{
		this.company = company;
		this.symbol = symbol;
		this.epsEstimate = epsEstimate;
		this.time = time;		
	}
	public String getCompany()
	{
		return this.company;
	}
	public void setCompany(String company)
	{
		this.company = company;
	}
	public String getSymbol()
	{
		return this.symbol;
	}
	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}
	public String getEpsEstimate()
	{
		return this.epsEstimate;
	}
	public void setEpsEstimate(String epsEstimate)
	{
		this.epsEstimate = epsEstimate;
	}
	public String getTime()
	{
		return this.time;
	}
	public void setTime(String time)
	{
		this.time = time;
	}
	
}
