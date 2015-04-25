package dev.shoulongli.ui;

import java.util.ArrayList;

import dev.shoulongli.R;
import dev.shoulongli.option.Option;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class OptionChainAdapter extends ArrayAdapter<Option>
{
	private Context context;
	private int textViewResourceId;
	private ArrayList<Option> options;
	private int fieldWidth;
	public OptionChainAdapter(Context context, int textViewResourceId, ArrayList<Option> options)
	{
		super(context, textViewResourceId, options);
		this.context = context;
		this.textViewResourceId = textViewResourceId;
		this.options = options;
		fieldWidth = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth()/7;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View v = convertView;
		OptionHolder holder;
		if(v == null)
		{
			LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(this.textViewResourceId, null);
			holder = new OptionHolder();
			holder.strikeView = (TextView)v.findViewById(R.id.option_strike);
			holder.lastView = (TextView)v.findViewById(R.id.option_last);
			holder.chgView = (TextView)v.findViewById(R.id.option_chg);
			holder.bidView = (TextView)v.findViewById(R.id.option_bid);
			holder.askView = (TextView)v.findViewById(R.id.option_ask);
			holder.volumeView = (TextView)v.findViewById(R.id.option_volume);
			holder.openintView = (TextView)v.findViewById(R.id.option_openint);
			v.setTag(holder);
		}
		else
		{
			holder = (OptionHolder)v.getTag();
		}
		Option option = (Option)options.get(position);
		holder.strikeView.setText(option.getStrike()+"");
		holder.strikeView.setWidth(fieldWidth);
		holder.lastView.setText(option.getLast()+"");	
		holder.lastView.setWidth(fieldWidth);
		holder.chgView.setText(option.getChange()+"");
		holder.chgView.setWidth(fieldWidth);
		holder.bidView.setText(option.getBid()+"");
		holder.bidView.setWidth(fieldWidth);
		holder.askView.setText(option.getAsk()+"");
		holder.askView.setWidth(fieldWidth);
		holder.volumeView.setText(option.getVolume()+"");
		holder.volumeView.setWidth(fieldWidth);
		holder.openintView.setText(option.getOpenInt()+"");	
		holder.openintView.setWidth(fieldWidth);
		if(option.getChange() > 0)
		{
			holder.chgView.setTextColor(Color.GREEN);
		}
		else if(option.getChange() < 0)
		{
			holder.chgView.setTextColor(Color.RED);
		}
		else
		{
			holder.chgView.setTextColor(Color.WHITE);
		}
		return v;
	}
	
	class OptionHolder
	{
		private TextView strikeView;
		private TextView lastView;
		private TextView chgView;
		private TextView bidView;
		private TextView askView;
		private TextView volumeView;
		private TextView openintView;
	}
}
