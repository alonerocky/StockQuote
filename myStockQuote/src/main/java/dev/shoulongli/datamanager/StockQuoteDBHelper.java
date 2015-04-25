package dev.shoulongli.datamanager;

import java.util.ArrayList;

import dev.shoulongli.stock.Stock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class StockQuoteDBHelper extends SQLiteOpenHelper
{
	private SQLiteDatabase db;
	//change to 2, becuase now we store the Stock as byte array
	//and we want to upgrade the db
	//2012-04-24
	//2  - change to Blob
	//3  - add "prev close , pe, eps , etc"
	public static final int DATABASE_VERSION = 2;
	private static final String DB_NAME = "stockquote.db";
	private static final String TABLE_NAME = "stocks";
	
	public static final String DB_FIELD_ROWID = "_id";
	public static final String DB_FIELD_SYMBOL = "symbol";
	public static final String DB_FIELD_DATA = "stockdata";
//	public static final String DB_FIELD_LAST_TRADE = "lastTrade";
//	public static final String DB_FIELD_CHANGE_VALUE = "changeValue";
//	public static final String DB_FIELD_CHANGE_PERCENT = "changePercent";
//	
//	public static final String DB_FIELD_NAME = "name";
//	public static final String DB_FIELD_ASK = "ask";
//	public static final String DB_FIELD_BID = "bid";
//	public static final String DB_FIELD_VOLUME = "volume";
	
	
	public StockQuoteDBHelper(Context context)
	{
		super(context, DB_NAME, null, DATABASE_VERSION);
		db = this.getWritableDatabase();
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(
				"CREATE TABLE "+TABLE_NAME+
				" (" + DB_FIELD_ROWID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
				" "+DB_FIELD_SYMBOL+" TEXT NOT NULL, "+
				" "+DB_FIELD_DATA +" BLOB);"
		
		);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    	onCreate(db);
	}
	
//	public void addStock2(Stock stock)
//	{
//		deleteStock(stock.getSymbol());
//		db.execSQL(
//				"INSERT INTO "+TABLE_NAME+
//				"( \'"+DB_FIELD_SYMBOL+"\' , "+
//				"\'" + DB_FIELD_DATA+"\') " +
//				"VALUES ("+
//				"\'"+stock.getSymbol()+ "\' , "+
//				"\'"+Stock.toByteArray(stock)+"\')"
//				);
//	}
	//the same as addStock , but use db.insert()
	public long addStock(Stock stock)
	{
		deleteStock(stock.getSymbol());
		ContentValues content = new ContentValues();
		content.put(DB_FIELD_SYMBOL, stock.getSymbol());
		content.put(DB_FIELD_DATA, Stock.toByteArray(stock));
		return db.insert(TABLE_NAME,null,content);
	}
	//FIXME
	public boolean deleteStock(String symbol)
	{
		int result = 0;
		try
		{
			String where = DB_FIELD_SYMBOL + " = ?";
			String[] whereArgs = {symbol.toString()};
			result = db.delete(TABLE_NAME, where, whereArgs);
			//result = db.delete(TABLE_NAME, DB_FIELD_SYMBOL+"= '"+symbol+"'", null);
		}
		catch(Exception e)
		{
			
		}
		return result > 0;
	}
	public int updateStock(Stock stock)
	{
		ContentValues content = new ContentValues();
		content.put(DB_FIELD_SYMBOL, stock.getSymbol());
		content.put(DB_FIELD_DATA, Stock.toByteArray(stock));
		int result = 0;
		try
		{
			String where = DB_FIELD_SYMBOL + " = ?";
			String[] whereArgs = {stock.getSymbol().toString()};
			result =  db.update(TABLE_NAME, content, where, whereArgs) ;
			//return (db.update(TABLE_NAME, content,  DB_FIELD_SYMBOL + " = '" + symbol+"'",null) > 0);
		}
		catch(Exception e)
		{
			
		}
		return result;
		
	}
	
	public Cursor fetchAllToCursor()
	{
		return db.query(TABLE_NAME, 
				new String[]{DB_FIELD_ROWID, 
							DB_FIELD_SYMBOL,
							DB_FIELD_DATA}, 
				null, //SQL WHERE 
				null, //Selection Args 
				null, //SQL GROUP BY 
				null, //SQL HAVING 
				DB_FIELD_ROWID);
	}
	public String getSymbolList()
	{
		StringBuilder sb = new StringBuilder();
		Cursor cursor = fetchAllToCursor();
		if(cursor.moveToFirst())
		{
			do
			{
				sb.append(cursor.getString(1));
				sb.append("+");		
				
			}
			while(cursor.moveToNext());
		}
		if(cursor != null && !cursor.isClosed())
		{
			cursor.close();
		}
		if(sb.toString().endsWith("+"))
		{
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
	public ArrayList<Stock> fetchAllToList()
	{
		ArrayList<Stock> list = new ArrayList<Stock>();
		
		Cursor cursor = fetchAllToCursor();
		if(cursor.moveToFirst())
		{
			do
			{				
				String symbol = cursor.getString(1);//symbol
				byte[] data = cursor.getBlob(2);
				Stock s = Stock.fromByteArray(data);	
				list.add(s);
				
			}
			while(cursor.moveToNext());
		}
		//the same thing
//		cursor.moveToFirst();
//		while (!cursor.isAfterLast()) 
//		{
//			Stock s = new Stock(cursor.getString(1));//symbol
//			s.setLastTrade(cursor.getString(2)); //last trade
//			s.setChangeValue(cursor.getString(3)); //change value
//			s.setChangePercent(cursor.getString(4)); //change percentage
//			list.add(s);
//			cursor.moveToNext();
//		}
		
		
		if(cursor != null && !cursor.isClosed())
		{
			cursor.close();
		}
		
		return list;
	}
	
	public void deleteAll()
	{
		db.delete(TABLE_NAME, null, null);
	}
	
	
}


