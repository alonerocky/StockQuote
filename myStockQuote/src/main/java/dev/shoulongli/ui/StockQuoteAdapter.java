package dev.shoulongli.ui;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import dev.shoulongli.R;
import dev.shoulongli.stock.Stock;

public class StockQuoteAdapter extends ArrayAdapter<Stock>
{
	private Context context;
	private int textViewResourceId;
	private ArrayList<Stock> stocks;
	private int fieldWidth ;
	public StockQuoteAdapter(Context context, int textViewResourceId, ArrayList<Stock> stocks)
	{
		super(context, textViewResourceId, stocks);
		this.context = context;
		this.textViewResourceId = textViewResourceId;
		this.stocks = stocks;
		fieldWidth = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth()/4;

	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View v = convertView;
		StockHolder holder;
		if(v == null)
		{
			LayoutInflater inflater =(LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(this.textViewResourceId, null);
			
			holder = new StockHolder();			
			holder.symbolView = (TextView)v.findViewById(R.id.symbol);
			holder.lastTrade = (TextView)v.findViewById(R.id.lasttrade);
			holder.changeValue = (TextView)v.findViewById(R.id.changevalue);
			holder.changePercent = (TextView)v.findViewById(R.id.changepercent);
			v.setTag(holder);			
		}
		else
		{
			holder = (StockHolder)v.getTag();
		}
		Stock s = (Stock)stocks.get(position);
		//String symbol = s.getSymbol();
		//String lasttrade =""+ s.getLastTrade();
		double changeValue = s.getChangeValue();
		//String changepercent = s.getChangePercent();
		holder.symbolView.setText(s.getSymbol());
		holder.symbolView.setWidth(fieldWidth);
		holder.lastTrade.setText(""+ s.getLastTrade());
		holder.lastTrade.setWidth(fieldWidth);
		holder.changeValue.setText(""+changeValue);
		holder.changeValue.setWidth(fieldWidth);
		holder.changePercent.setText(s.getChangePercent());
		holder.changePercent.setWidth(fieldWidth);
		if(changeValue > 0)
		{
			holder.symbolView.setTextColor(Color.GREEN);
			holder.lastTrade.setTextColor(Color.GREEN);
			holder.changeValue.setTextColor(Color.GREEN);
			holder.changePercent.setTextColor(Color.GREEN);
		}
		else if(changeValue < 0)
		{
			holder.symbolView.setTextColor(Color.RED);
			holder.lastTrade.setTextColor(Color.RED);
			holder.changeValue.setTextColor(Color.RED);
			holder.changePercent.setTextColor(Color.RED);
		}
		else 
		{
			holder.symbolView.setTextColor(Color.WHITE);
			holder.lastTrade.setTextColor(Color.WHITE);
			holder.changeValue.setTextColor(Color.WHITE);
			holder.changePercent.setTextColor(Color.WHITE);
		}
		
		return v;
		
	}
	class StockHolder
	{
		private TextView symbolView;
		private TextView lastTrade;
		private TextView changeValue;
		private TextView changePercent;
	}
}
