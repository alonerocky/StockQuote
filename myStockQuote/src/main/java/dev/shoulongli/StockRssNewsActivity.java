package dev.shoulongli;

import java.util.ArrayList;
import java.util.HashMap;

import dev.shoulongli.rss.RssLoaderCallback;
import dev.shoulongli.rss.RssLoaderTask;
import dev.shoulongli.stock.Stock;
import dev.shoulongli.stock.StockQuoteConfigure;
import dev.shoulongli.ui.StockNewsFeedAdapter;
import dev.shoulongli.util.UrlIntentListener;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;

public class StockRssNewsActivity extends ListActivity implements RssLoaderCallback
{
	private Handler mHandler = new Handler();
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.relatednewslist);
		this.getListView().setOnItemClickListener(new UrlIntentListener());
		Bundle bundle = getIntent().getExtras();
		if(bundle != null)
		{			
			String symbol = bundle.getString(StockQuoteMainActivity.KEY_VIEW_NEWS);
			loadRssNews(symbol);
		}
	}
	@Override
	public void onRssLoaded(ArrayList<HashMap<String, String>> result)
	{
		StockNewsFeedAdapter adapter = new StockNewsFeedAdapter(
				this,
				R.layout.relatednews,
				result);
		setListAdapter(adapter);
	}
	
	private void loadRssNews(String symbol)
	{
		final String rss_url = StockQuoteConfigure.STOCK_NEWS_URI + symbol;
		mHandler.post(new Runnable()
		{
			@Override
			public void run()
			{
				new RssLoaderTask(StockRssNewsActivity.this).execute(rss_url);
			}
		} );
	}
}
