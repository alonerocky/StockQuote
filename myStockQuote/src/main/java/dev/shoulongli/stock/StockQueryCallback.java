package dev.shoulongli.stock;

import java.util.ArrayList;

public interface StockQueryCallback 
{
	public void onQueryCompleted(ArrayList<String> lists);
}
