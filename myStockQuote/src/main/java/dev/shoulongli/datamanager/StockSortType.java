package dev.shoulongli.datamanager;

import android.content.Context;

public class StockSortType {

	public static final String PREFERENCE = "dev.shoulongli.stockquote.datamanager.preference";
	public static final String SORT_TYPE = "sort_type";
	public static final String SORT_STR =  "sort_string";
	public static final String SORT_NUM = "sort_number";
	public static final int DEFAULT = 0;
	public static final int ASCEND_NUMBER = 1;
	public static final int DESCEND_NUMBER = 2;
	public static final int ASCEND_STRING = 3;
	public static final int DESCEND_STRING = 4;
	
	public static int getSortType(Context context)
	{
		return context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).getInt(SORT_TYPE, DEFAULT);
	}
	public static int getLastSortTypeOfNumber(Context context)
	{
		return context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).getInt(SORT_NUM, ASCEND_NUMBER);
	}
	public static int getLastSortTypeOfStr(Context context)
	{
		return context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).getInt(SORT_STR, ASCEND_STRING);
	}
	public static void setSortType(Context context,int type)
	{
		if(type >= DEFAULT && type <= DESCEND_STRING)
		{
			context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).edit().putInt(SORT_TYPE, type).commit();
			if(type == ASCEND_NUMBER || type == DESCEND_NUMBER)
			{
				context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).edit().putInt(SORT_NUM, type).commit();
				
			}
			else if(type == ASCEND_STRING || type == DESCEND_STRING)
			{
				context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).edit().putInt(SORT_STR, type).commit();
				
			}
		}
	}
}
