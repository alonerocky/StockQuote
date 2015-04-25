package dev.shoulongli.stock;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.util.Log;
import dev.shoulongli.StockQuoteAddActivity;

public class StockQueryTask extends AsyncTask<String,Void, ArrayList<String>>
{		
	private StockQueryCallback callback;
	public StockQueryTask(StockQueryCallback callback)
	{
		this.callback = callback;
	}
	@Override
	public ArrayList<String> doInBackground(String... queryStr)
	{
		ArrayList<String> lists = null;
		String url = StockQuoteUtil.getStockQuoteQueryUrl(queryStr[0]);
		try
		{
			InputStream is = new URL(url).openConnection().getInputStream();
			lists = StockQuoteParser.getInstance().getStockQuoteSymbolList(is);
		}
		catch(Exception e)
		{
			Log.i(StockQuoteAddActivity.class.getName(),e.getMessage());
		}
		return lists;
	}
	@Override
	public void onPostExecute(ArrayList<String> lists)
	{
		if(this.callback != null)
		{
			callback.onQueryCompleted(lists);
		}
	}
}
