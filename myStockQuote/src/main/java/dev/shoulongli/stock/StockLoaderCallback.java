package dev.shoulongli.stock;

import java.util.ArrayList;

public interface StockLoaderCallback 
{
	public void onStockLoaded(ArrayList<Stock> stocks);
}
