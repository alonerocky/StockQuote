package dev.shoulongli.stock;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import android.os.AsyncTask;

public class StockLoaderTask extends AsyncTask<String,Void,ArrayList<Stock>>
{
	private StockLoaderCallback callback;
	public StockLoaderTask(StockLoaderCallback callback)
	{
		this.callback = callback;
	}
	@Override
	public ArrayList<Stock> doInBackground(String... parameters)
	{
		ArrayList<Stock> result = new ArrayList<Stock>();
		String url = parameters[0].trim();
		String symbol = parameters[1].trim();
		String tags = parameters[2].trim();
		try
		{
			InputStream is = new URL(url).openConnection().getInputStream();
			result = StockQuoteParser.getInstance().parse(is, symbol, tags);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	@Override
	public void onPostExecute(ArrayList<Stock> stocks)
	{ 
		if(!isCancelled() && 
				stocks != null && 
				stocks.size() > 0 && 
				this.callback != null)
			callback.onStockLoaded(stocks);
	}
	    	
}