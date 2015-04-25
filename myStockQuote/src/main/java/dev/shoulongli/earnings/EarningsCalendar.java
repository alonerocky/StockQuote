package dev.shoulongli.earnings;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EarningsCalendar 
{
	private Date earningsDate;
	private ArrayList<EarningsCalendarItem> earnings;
	public EarningsCalendar()
	{
		Calendar c = Calendar.getInstance();
		earningsDate = c.getTime();
		earnings = new ArrayList<EarningsCalendarItem>();
	}
	public EarningsCalendar(Date earningsDate, ArrayList<EarningsCalendarItem> earnings)
	{
		this.earningsDate = earningsDate;
		this.earnings = earnings;
	}
	public Date getDate()
	{
		return earningsDate;
	}
	public ArrayList<EarningsCalendarItem> getEarningsList()
	{
		return earnings;
	}
	//like Tuesday, October 23
	public static Date parseToDate(String dateString)
	{
		SimpleDateFormat  format = new SimpleDateFormat("EEEEEEEEE, MMMM dd");
		
		Date d = new Date();
		
		try
		{
			d = format.parse(dateString);
			d.setYear(new Date(System.currentTimeMillis()).getYear());
		}
		catch(Exception e)
		{
			
		}
		return d;
	}
}
