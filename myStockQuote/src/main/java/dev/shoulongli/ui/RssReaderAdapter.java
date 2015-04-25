package dev.shoulongli.ui;

import java.util.ArrayList;
import java.util.HashMap;

import dev.shoulongli.R;
import dev.shoulongli.rss.RssFeedParser;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RssReaderAdapter extends ArrayAdapter<HashMap<String,String>>
{
	private Context context;
	private ArrayList<HashMap<String, String>> lists;
	private int textViewResourceId ;
	public RssReaderAdapter(Context context, int textViewResourceId,ArrayList<HashMap<String, String>> lists)	
	{
		super(context,textViewResourceId, lists);
		this.context = context;
		this.lists = lists;
		this.textViewResourceId = textViewResourceId;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Log.i("RssReaderAdapter", "position : "+position);
		View view = convertView;
		RssItemHolder holder;
		if(view == null)
		{
			LayoutInflater inflater = (LayoutInflater)(this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
			view = (View)inflater.inflate(this.textViewResourceId, null);
			holder = new RssItemHolder();
			holder.title = (TextView)view.findViewById(R.id.title);
			holder.pubDate = (TextView)view.findViewById(R.id.date);
			//holder.desc = (TextView)view.findViewById(R.id.description);			
			view.setTag(holder);
		}
		else
		{
			holder = (RssItemHolder)view.getTag();
		}
		
		HashMap<String, String> data = lists.get(position);
		holder.title.setText(data.get(RssFeedParser.TITLE));
		holder.pubDate.setText(data.get(RssFeedParser.PUB_DATE));
		//holder.desc.setText(data.get(RssFeedParser.DESCRIPTION));
		holder.url = data.get(RssFeedParser.LINK);
		
		return view;
	}
	public static class RssItemHolder
	{
		TextView title;
		TextView pubDate;
		//TextView desc;
		String url;
		public String getUrl()
		{
			return url;
		}
	}
	
	
}
