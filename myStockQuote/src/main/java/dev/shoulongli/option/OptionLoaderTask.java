package dev.shoulongli.option;

import java.util.ArrayList;

import dev.shoulongli.stock.StockQuoteParser;

import android.os.AsyncTask;

public class OptionLoaderTask extends AsyncTask<String, Void, ArrayList[]>
{
	private OptionLoaderCallback callback;
	public OptionLoaderTask(OptionLoaderCallback callback)
	{
		this.callback = callback;
	}
	
	@Override
	public ArrayList[] doInBackground(String... parameters)
	{
		ArrayList[] options = new ArrayList[2];
		String symbol = parameters[0].trim();
		String date = parameters[1].trim();
		
		try
		{
			options = StockQuoteParser.getOptionChain(symbol, date);
		}
		catch(Exception e)
		{
			
		}		
		
		return options;
	}
	@Override
	public void onPostExecute(ArrayList[] options)
	{
		if(this.callback != null)
		{
			callback.onOptionLoaded(options);
		}
	}
}
