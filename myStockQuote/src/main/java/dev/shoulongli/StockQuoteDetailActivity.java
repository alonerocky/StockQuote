package dev.shoulongli;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;



import dev.shoulongli.rss.RssLoaderCallback;
import dev.shoulongli.rss.RssLoaderTask;
import dev.shoulongli.stock.Stock;
import dev.shoulongli.stock.StockLoaderCallback;
import dev.shoulongli.stock.StockLoaderTask;
import dev.shoulongli.stock.StockQuoteConfigure;
import dev.shoulongli.stock.StockQuoteUtil;
import dev.shoulongli.ui.StockNewsFeedAdapter;
import dev.shoulongli.util.UrlIntentListener;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class StockQuoteDetailActivity extends Activity implements 
StockLoaderCallback, RssLoaderCallback
{

	
	
	private Handler mHandler = new Handler();
	private Runnable refreshTask;
	private String symbol;
	private ImageView stockChart ;
	
	private ListView relatedNewsView;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		setContentView(R.layout.stockquotedetail);
		stockChart = (ImageView)findViewById(R.id.stockChart);
		relatedNewsView = (ListView)findViewById(R.id.relatednewslist);
		TextView newsTitle = (TextView)findViewById(R.id.newslabel);
		newsTitle.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				viewNews();
			}
		});
		ImageButton expand = (ImageButton)findViewById(R.id.expand);
		expand.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewNews();
			}
		});
		if(extras != null)
		{
			final Stock s = extras.getParcelable(StockQuoteMainActivity.KEY_VIEW_STOCK);
			symbol = s.getSymbol(); 
			
			updateUi(s);
			refresh();
			refreshTask = new Runnable()
			{
				@Override
				public void run()
				{
					refresh();
					mHandler.postDelayed(this, 60*1000L);
				}
			};			
			
		}
		
		
		

	}
	private void refresh()
	{
		if(symbol != null && symbol.trim().length() > 0)
		{
			String url = StockQuoteUtil.getStockQuoteUrl(symbol);
			new StockLoaderTask(this).execute(url,symbol,StockQuoteConfigure.TAGS);
			final String rss_url = StockQuoteConfigure.STOCK_NEWS_URI + symbol; 
			new RssLoaderTask(this).execute(rss_url); 
		}
	}
	
	@Override
	public void onRssLoaded(ArrayList<HashMap<String, String>> result)
	{
		StockNewsFeedAdapter adapter = new StockNewsFeedAdapter(
				this,
				R.layout.relatednews,
				result);
		relatedNewsView.setAdapter(adapter);
		relatedNewsView.setOnItemClickListener(new UrlIntentListener());
	}
	
	private void updateUi(Stock s)
	{
		String symbol = s.getSymbol();
		double price = s.getLastTrade();
		double changeValue = s.getChangeValue();
		String changePercent = s.getChangePercent();

		int textColor = Color.WHITE;
		if(changeValue > 0)
		{
			textColor = Color.GREEN;
		}
		else if(changeValue < 0)
		{
			textColor = Color.RED;
		}

		TextView nameView = (TextView)findViewById(R.id.companyName);
		TextView priceView = (TextView)findViewById(R.id.stockPrice);
		TextView changeValueView = (TextView)findViewById(R.id.stockChangeValue);
		TextView changePercentView = (TextView)findViewById(R.id.stockChangePercent);
		
		TextView askView = (TextView)findViewById(R.id.askPrice);
		TextView bidView = (TextView)findViewById(R.id.bidPrice);
		
		
		nameView.setText(s.getCompanyName());
		priceView.setText(price+"");
		priceView.setTextColor(textColor);
		
		changeValueView.setText(changeValue+"");
		changeValueView.setTextColor(textColor);
		
		changePercentView.setText(changePercent+"");
		changePercentView.setTextColor(textColor);
		
		askView.setText(s.getAskPrice()+"");
		bidView.setText(s.getBidPrice()+"");
		

		//update detail
		TextView last = (TextView)findViewById(R.id.lastValue);
		last.setText(s.getLastTrade()+"");
		TextView lasttime = (TextView)findViewById(R.id.timeValue);
		lasttime.setText(s.getLastTradeTime());
		TextView volume = (TextView)findViewById(R.id.volumeValue);
		volume.setText(s.getVolumeStr());
		TextView avgvolume = (TextView)findViewById(R.id.avgVolumeValue);
		avgvolume.setText(s.getDailyAvgVolumeStr());
		
		TextView prevClose = (TextView)findViewById(R.id.prevcloseValue);
		prevClose.setText(s.getPrevClose()+"");
		//TextView open = (TextView)findViewById(R.id.ope)
		TextView pe = (TextView)findViewById(R.id.peValue);
		pe.setText(s.getPERatio()+"");
		TextView marketCap = (TextView)findViewById(R.id.marketcapValue);
		marketCap.setText(s.getMarketCap());
		TextView dayrange = (TextView)findViewById(R.id.dayrangeValue);
		dayrange.setText(s.getDaysRange());
		TextView yearrange = (TextView)findViewById(R.id.yearrangeValue);
		yearrange.setText(s.get52wkRange());
		TextView eps = (TextView)findViewById(R.id.epsValue);
		eps.setText(s.getEPS()+"");
		TextView dividend = (TextView)findViewById(R.id.dividendValue);
		dividend.setText(s.getDividendYield()+"");
		TextView yeartarget = (TextView)findViewById(R.id.yeartargetValue);
		yeartarget.setText(s.get1YearTarget()+"");
		//ImageDownloadManager.getInstance().download(StockQuoteConfigure.STOCK_CHART_ONE_DAY + symbol, stockChart);//download(StockQuoteDetailActivity.this.getApplicationContext(),StockQuoteConfigure.STOCK_CHART_ONE_DAY + symbol, stockChart);
        Glide.with(this).load(StockQuoteConfigure.STOCK_CHART_ONE_DAY + symbol).fitCenter()
                .placeholder(R.drawable.stock_chart_placeholder)
                .crossFade()
                .into(stockChart);
		
	}
//	private void updateBottom(Stock s)
//	{
//		/*
//		 * Prev Close: 
//		 * Open:
//		 * 
//		 * Market Cap:
//		 * Volume:
//		 *
//		 * PE:
//		 * Day's Range:
//		 * 
//		 * EPS
//		 * 52wk Range:
//		 * 
//		 * Dividend Yield
//		 * 1 Year Target:
//		 * 
//		 */
//		String html = htmlToString("detail.html");
//		String result = String.format(html, s.getPrevClose(),
//											s.getOpenPrice(),
//											s.getMarketCap(),
//											s.getVolume(),
//											s.getPERatio(),
//											s.getDaysRange(),
//											s.getEPS(),	
//											s.get52wkRange(),																					
//											s.getDividendYield(),											
//											s.get1YearTarget());
//		TextView textView = new TextView(StockQuoteDetailActivity.this); 
//		String color = Integer.toHexString(textView.getCurrentTextColor());
//		// get the rgb 
//		if( color.length() > 6 ) {
//			color = color.substring( color.length() -6 );
//		}
//		StringBuilder decoratedHtml = new StringBuilder();
//		decoratedHtml.append("<html><head>");
//		//decoratedHtml.append("<meta name=\"viewport\" content=\"target-densitydpi=device-dpi\" />");
//        decoratedHtml.append("<style type=\"text/css\">");
//        decoratedHtml.append(".label { font-weight:bold; }\n ");
//        decoratedHtml.append(".left { text-align:left; }\n ");
//        decoratedHtml.append(".right {text-align:left; }\n ");
//        decoratedHtml.append("body {color: white; background-color:black; }\n ");
//        decoratedHtml.append("</style>");
//		decoratedHtml.append("</head><body>");
//		decoratedHtml.append(result);
//		decoratedHtml.append("</body></html>");
//		detailView.loadDataWithBaseURL("about:blank", decoratedHtml.toString(),"text/html", "utf-8", "");
//		//detailView.loadUrl("file:///android_asset/detail.html");
//	}
	public String htmlToString(String fileName) 
	{ 
		
		try 
		{
            InputStream is = getAssets().open(fileName);

            // We guarantee that the available method returns the total
            // size of the asset...  of course, this does mean that a single
            // asset can't be more than 2 gigs.
            int size = is.available();

            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            // Convert the buffer into a string.
            String text = new String(buffer);
            return text;
        } 
		catch (IOException e) 
		{
            // Should never happen!
           
        }
		return "";

	}  

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.optionsondetail, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case R.id.refresh:
			refresh();
			break;
		case R.id.optionchain:
			viewOptionChain();
			break;
		case R.id.oneday:
			//ImageDownloadManager.getInstance().download(StockQuoteConfigure.STOCK_CHART_ONE_DAY + symbol, stockChart);
            Glide.with(this).load(StockQuoteConfigure.STOCK_CHART_ONE_DAY + symbol).fitCenter()
                    .placeholder(R.drawable.stock_chart_placeholder)
                    .crossFade()
                    .into(stockChart);
			break;
		case R.id.fiveday:
			//ImageDownloadManager.getInstance().download(StockQuoteConfigure.STOCK_CHART_FIVE_DAY + symbol, stockChart);
            Glide.with(this).load(StockQuoteConfigure.STOCK_CHART_FIVE_DAY + symbol).fitCenter()
                    .placeholder(R.drawable.stock_chart_placeholder)
                    .crossFade()
                    .into(stockChart);
			break;
		case R.id.threemonth:
			//ImageDownloadManager.getInstance().download(StockQuoteConfigure.STOCK_CHART_THREE_MONTH + symbol, stockChart);
            Glide.with(this).load(StockQuoteConfigure.STOCK_CHART_THREE_MONTH + symbol).fitCenter()
                    .placeholder(R.drawable.stock_chart_placeholder)
                    .crossFade()
                    .into(stockChart);
            break;
		case R.id.sixmonth:
			//ImageDownloadManager.getInstance().download(StockQuoteConfigure.STOCK_CHART_SIX_MONTH + symbol, stockChart);
            Glide.with(this).load(StockQuoteConfigure.STOCK_CHART_SIX_MONTH + symbol).fitCenter()
                    .placeholder(R.drawable.stock_chart_placeholder)
                    .crossFade()
                    .into(stockChart);
            break;
		case R.id.fiveyear:
			//ImageDownloadManager.getInstance().download(StockQuoteConfigure.STOCK_CHART_FIVE_YEAR + symbol, stockChart);
            Glide.with(this).load(StockQuoteConfigure.STOCK_CHART_FIVE_YEAR + symbol).fitCenter()
                    .placeholder(R.drawable.stock_chart_placeholder)
                    .crossFade()
                    .into(stockChart);
            break;
		case R.id.news:
			viewNews();
			break;
		case R.id.earnings:
			viewEarningsCalendar();
			break;
		}
		return true;
	}
	private void viewOptionChain()
	{
		Intent intent = new Intent(this, StockOptionChainActivity.class);
		intent.putExtra(StockQuoteMainActivity.KEY_OPTION_CHAIN, symbol);
		startActivity(intent);
	}
	private void viewEarningsCalendar()
	{
		Intent intent = new Intent(this, StockEarningsActivity.class);
		intent.putExtra(StockQuoteMainActivity.KEY_STOCK_SYMBOL, symbol);
		startActivity(intent);
	}
	private void viewNews()
	{
		Intent intent = new Intent(this,StockRssNewsActivity.class);	
		intent.putExtra(StockQuoteMainActivity.KEY_VIEW_NEWS, symbol);		
		startActivity(intent);
	}
	@Override
	public void onStockLoaded(ArrayList<Stock> stocks)
	{
		if(stocks != null && stocks.size() > 0)
		{
			Stock s = stocks.get(0);
			if(s.getSymbol().equals(symbol))
			{
				Log.i(StockQuoteDetailActivity.class.getName(), "stock detail update "+System.currentTimeMillis());
				updateUi(s);			
			}
		}
	}

	
	@Override
	public void onPause()
	{
		super.onPause();
		Log.i(StockQuoteDetailActivity.class.getName(), "on Paused");
		mHandler.removeCallbacks(refreshTask);
	}
	@Override
	public void onResume()
	{
		super.onResume();
		Log.i(StockQuoteDetailActivity.class.getName(), "on onResume");
		mHandler.postDelayed(refreshTask,60*1000L);
	}
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		Log.i(StockQuoteDetailActivity.class.getName(), "on onDestroy");
	}
}
