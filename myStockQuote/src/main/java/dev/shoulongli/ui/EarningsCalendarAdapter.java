package dev.shoulongli.ui;

import java.util.ArrayList;

import dev.shoulongli.R;
import dev.shoulongli.earnings.EarningsCalendarItem;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EarningsCalendarAdapter extends ArrayAdapter<EarningsCalendarItem>
{
	private Context context;
	private int viewResourceId;
	private ArrayList<EarningsCalendarItem> earnings;
	private int fieldWidth;
	private int selectedIndex;

   
	public EarningsCalendarAdapter(Context context, int viewResourceId, ArrayList<EarningsCalendarItem> earnings)
	{
		super(context, viewResourceId,earnings);
		this.context = context;
		this.earnings = earnings;
		this.viewResourceId = viewResourceId;
		fieldWidth = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth()/4;
	}
	@Override
	public int getCount() 
	{
        return earnings.size();
    }

    public EarningsCalendarItem getItem(int position) 
    {
        return earnings.get(position);
    }

    public long getItemId(int position) 
    {
        return position;
    }
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View v = convertView;
		EarningsHolder holder;
		if(v == null)
		{
			LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(viewResourceId, null);
			holder = new EarningsHolder();
			holder.companyView = (TextView)v.findViewById(R.id.company);
			holder.symbolView = (TextView)v.findViewById(R.id.symbol);
			holder.epsView = (TextView)v.findViewById(R.id.epsestimate);
			holder.timeView = (TextView)v.findViewById(R.id.time);
			v.setTag(holder);
		}
		else
		{
			holder = (EarningsHolder)v.getTag();
		}
		
		EarningsCalendarItem item = (EarningsCalendarItem)earnings.get(position);
		holder.companyView.setText(item.getCompany());
		holder.companyView.setWidth(fieldWidth);
		holder.symbolView.setText(item.getSymbol());
		holder.symbolView.setWidth(fieldWidth);
		holder.epsView.setText(item.getEpsEstimate());
		holder.epsView.setWidth(fieldWidth);
		holder.timeView.setText(item.getTime());
		holder.timeView.setWidth(fieldWidth);
		// set selected item
        if (selectedIndex == position)
        {
        	holder.companyView.setTextColor(Color.YELLOW);
        	holder.symbolView.setTextColor(Color.YELLOW);
        	holder.epsView.setTextColor(Color.YELLOW);
        	holder.timeView.setTextColor(Color.YELLOW);
        	
        }
        else
        {
        	holder.companyView.setTextColor(Color.GRAY);
        	holder.symbolView.setTextColor(Color.GRAY);
        	holder.epsView.setTextColor(Color.GRAY);
        	holder.timeView.setTextColor(Color.GRAY);
        }
      

		return v;
	}
	public class EarningsHolder
	{
		TextView companyView;
		TextView symbolView;
		TextView epsView;
		TextView timeView;
	}
	public void setSelectedItem(int index)
	{
		this.selectedIndex = index;
	}

}
