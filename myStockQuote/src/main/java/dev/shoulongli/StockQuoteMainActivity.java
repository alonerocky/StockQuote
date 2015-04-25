package dev.shoulongli;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;


import dev.shoulongli.datamanager.StockQuoteDBHelper;
import dev.shoulongli.datamanager.StockSortType;
import dev.shoulongli.stock.Stock;
import dev.shoulongli.stock.StockLoaderCallback;
import dev.shoulongli.stock.StockLoaderTask;
import dev.shoulongli.stock.StockQuoteConfigure;
import dev.shoulongli.stock.StockQuoteUtil;
import dev.shoulongli.ui.StockQuoteAdapter;


import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.TextView;

public class StockQuoteMainActivity extends ListActivity implements StockLoaderCallback
{
	
	private static final String KEY_DB_VERSION = "STOCKQUOTE_DB_VERSION";
	private static final int ADD_STOCK = 0;
	private StockQuoteDBHelper dbHelper;
	public static final String KEY_VIEW_STOCK = "VIEW_STOCK";
	public static final String KEY_OPTION_CHAIN = "OPTION_CHAIN";
	public static final String KEY_STOCK_SYMBOL = "EARNING_CALENDAR";
	public static final String KEY_VIEW_NEWS = "VIEW_NEWS";
	
	private Handler mHandler = new Handler();
	private Runnable refreshTask;
	
	private PullToRefreshListView mPullRefreshListView;
	
	
	private View symbolView;
	private View changePercentView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
   
        symbolView = findViewById(R.id.symbolbtn);
        changePercentView = findViewById(R.id.changepercentbtn);
        // Set a listener to be invoked when the list should be refreshed.
        mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() 
        {
        	@Override
        	public void onRefresh(PullToRefreshBase<ListView> refreshView) 
        	{
        		String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
        				DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

        		// Update the LastUpdatedLabel
        		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

        		// Do work to refresh the list here. 
        		refresh();
        	}
        });

        // Add an end-of-list listener
        mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() 
        {

        	@Override
        	public void onLastItemVisible()
        	{
        		Toast.makeText(StockQuoteMainActivity.this, "End of List!", Toast.LENGTH_SHORT).show();
        	}
        });

        ListView actualListView = mPullRefreshListView.getRefreshableView();
        // Need to use the Actual ListView when registering for Context Menu
        registerForContextMenu(actualListView);
        
        /**
		 * Add Sound Event Listener
		 */
//		SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(this);
//		soundListener.addSoundEvent(State.PULL_TO_REFRESH, R.raw.pull_event);
//		soundListener.addSoundEvent(State.RESET, R.raw.reset_sound);
//		soundListener.addSoundEvent(State.REFRESHING, R.raw.refreshing_sound);
//		mPullRefreshListView.setOnPullEventListener(soundListener);
        
//        int fieldWidth = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth()/4;
//        
//        ((Button)findViewById(R.id.symbol)).setWidth(fieldWidth);
//        ((Button)findViewById(R.id.lasttrade)).setWidth(fieldWidth);
//        ((Button)findViewById(R.id.changevalue)).setWidth(fieldWidth);
//        ((Button)findViewById(R.id.changepercent)).setWidth(fieldWidth); 
       
        dbHelper = new StockQuoteDBHelper(this);
        refreshTask = new Runnable()
		{
			@Override
			public void run()
			{
				refresh();
				mHandler.postDelayed(this, 60*1000L);
			}
		};
		
		View.OnClickListener sortByNumberListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(StockSortType.getLastSortTypeOfNumber(StockQuoteMainActivity.this) == StockSortType.ASCEND_NUMBER)
				{
					StockSortType.setSortType(StockQuoteMainActivity.this, StockSortType.DESCEND_NUMBER);
					populateView();
				}
				else if(StockSortType.getLastSortTypeOfNumber(StockQuoteMainActivity.this) == StockSortType.DESCEND_NUMBER)
				{
					StockSortType.setSortType(StockQuoteMainActivity.this, StockSortType.ASCEND_NUMBER);
					populateView();
				}
			}
		};
		changePercentView.setOnClickListener(sortByNumberListener);
		View.OnClickListener sortByStringListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(StockSortType.getLastSortTypeOfStr(StockQuoteMainActivity.this) == StockSortType.ASCEND_STRING)
				{
					StockSortType.setSortType(StockQuoteMainActivity.this, StockSortType.DESCEND_STRING);
					populateView();
				}
				else if(StockSortType.getLastSortTypeOfStr(StockQuoteMainActivity.this) == StockSortType.DESCEND_STRING)
				{
					StockSortType.setSortType(StockQuoteMainActivity.this, StockSortType.ASCEND_STRING);
					populateView();
				}
			}
		};
		symbolView.setOnClickListener(sortByStringListener);
		checkDbVersion();
        load();
        
    }
    private void checkDbVersion()
    {
    	//once db upgraded, all the data will be deleted, so we add some stocks
    	int latestDbVersion = StockQuoteDBHelper.DATABASE_VERSION;
    	int currentDbVersion = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getInt(KEY_DB_VERSION, 0);
    	if(latestDbVersion > currentDbVersion)
    	{
    		dbHelper.addStock(new Stock("AAPL"));
    		dbHelper.addStock(new Stock("MSFT"));
    		dbHelper.addStock(new Stock("GOOG"));
    		getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putInt(KEY_DB_VERSION, latestDbVersion).commit();
    	}

    }
    private void load()
    {		 

    	/**
    	 * Add Sound Event Listener
    	 */
//    	SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(this);
//    	soundListener.addSoundEvent(State.PULL_TO_REFRESH, R.raw.pull_event);
//    	soundListener.addSoundEvent(State.RESET, R.raw.reset_sound);
//    	soundListener.addSoundEvent(State.REFRESHING, R.raw.refreshing_sound);
//    	mPullRefreshListView.setOnPullEventListener(soundListener);

    	// You can also just use setListAdapter(mAdapter) or
    	// mPullRefreshListView.setAdapter(mAdapter)
    	//mPullRefreshListView.getRefreshableView().setAdapter(mAdapter)

    	populateView( );
    	//setListAdapter(adapter); 
    }
	public double getChangePercent(String percentage)
	{
		double result = 0.0;
		try
		{
			result = Double.parseDouble(percentage.trim().replace("%", ""));
		}
		catch(Exception e)
		{
			
		}
		return result;
	}
	private void populateView( )
	{
		ArrayList<Stock> stocks = dbHelper.fetchAllToList();
		//fix the can't refresh after "delte all"  2012-05-05
		if(stocks == null )
			return;
		int type = StockSortType.getSortType(this);
		Comparator<Stock> comparator = null;
		
		if(type == StockSortType.ASCEND_NUMBER)
		{
			comparator = new Comparator<Stock>()
					{
						@Override
						public int compare(Stock s1, Stock s2)
						{
							 
							try
							{
								double c1 = s1.getChangePercentDouble();
								double c2 = s2.getChangePercentDouble();
								if(c1 < c2)
									return -1;
								else if(c1 > c2)
									return 1;
								else 
									return 0;
								 
							}
							catch(Exception e)
							{

							}
							return 0;
						}
					};
		}
		else if(type == StockSortType.DESCEND_NUMBER)
		{
			comparator = new Comparator<Stock>()
					{
						@Override
						public int compare(Stock s1, Stock s2)
						{
							try
							{
								double c1 = s1.getChangePercentDouble();
								double c2 = s2.getChangePercentDouble();
								if(c1 < c2)
									return 1;
								else if(c1 > c2)
									return -1;
								else
									return 0;
							}
							catch(Exception e)
							{

							}
							return 0;
						}
					};
		}
		else if(type == StockSortType.ASCEND_STRING)
		{
			comparator = new Comparator<Stock>()
					{
						@Override
						public int compare(Stock s1, Stock s2)
						{
							try
							{
								return s1.getSymbol().compareTo(s2.getSymbol());
							}
							catch(Exception e)
							{

							}
							return 0;
						}
					};
		}
		else if(type == StockSortType.DESCEND_STRING)
		{
			comparator = new Comparator<Stock>()
					{
						@Override
						public int compare(Stock s1, Stock s2)
						{
							try
							{
								return s2.getSymbol().compareTo(s1.getSymbol());
							}
							catch(Exception e)
							{

							}
							return 0;
						}
					};
		}
		else
		{
			//default
			//no sorting
		}
		
		//revert the ascend/descend  sign
		if(StockSortType.getLastSortTypeOfNumber( this) == StockSortType.ASCEND_NUMBER)
		{
			Drawable image = getResources().getDrawable( R.drawable.sort_up_red ); 
			//image.setBounds( 0, 0, 20, 20 );
//			Button btn = ((Button)findViewById(R.id.changepercent));
//			if(btn != null)
//				btn.setCompoundDrawables(null, null, image, null);
			ImageView btn = (ImageView)findViewById(R.id.sortbychange);
			if(btn != null)
				btn.setImageDrawable(image);
		}
		else if(StockSortType.getLastSortTypeOfNumber( this) == StockSortType.DESCEND_NUMBER)
		{
			Drawable image = getResources().getDrawable( R.drawable.sort_down_red );
			//image.setBounds( 0, 0, 20, 20 );
//			Button btn = ((Button)findViewById(R.id.changepercent));
//			if(btn != null)
//				btn.setCompoundDrawables(null, null, image, null); 
			ImageView btn = (ImageView)findViewById(R.id.sortbychange);
			if(btn != null)
				btn.setImageDrawable(image);
		}

		if(StockSortType.getLastSortTypeOfStr( this) == StockSortType.ASCEND_STRING)
		{
			Drawable image = getResources().getDrawable( R.drawable.sort_ascend );
			//image.setBounds( 0, 0, 20, 20 );
//			Button btn = ((Button)findViewById(R.id.symbol));
//			if(btn != null)
//				btn.setCompoundDrawables(null, null, image, null);
			ImageView btn = (ImageView)findViewById(R.id.sortbysymbol);
			if(btn != null)
				btn.setImageDrawable(image);
		}
		else if(StockSortType.getLastSortTypeOfStr( this) == StockSortType.DESCEND_STRING)
		{
			Drawable image = getResources().getDrawable( R.drawable.sort_descend);
			//image.setBounds( 0, 0, 20, 20 );
//			Button btn = ((Button)findViewById(R.id.symbol));
//			if(btn != null)
//				btn.setCompoundDrawables(null, null, image, null);
			ImageView btn = (ImageView)findViewById(R.id.sortbysymbol);
			if(btn != null)
				btn.setImageDrawable(image);
		}
		
		Collections.sort(stocks,comparator);
		StockQuoteAdapter adapter = new StockQuoteAdapter(StockQuoteMainActivity.this,R.layout.stockquote,stocks);
		mPullRefreshListView.getRefreshableView().setAdapter(adapter);
	}
	 
	private void refresh()
	{
		String symbol = dbHelper.getSymbolList();		
		if(symbol.trim().length() > 0)
		{
			String url = StockQuoteUtil.getStockQuoteUrl(symbol);
			new StockLoaderTask(this).execute(url,symbol,StockQuoteConfigure.TAGS);
		}
	}
	
	@Override
	public void onCreateContextMenu (ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
	{
		menu.setHeaderTitle("Options");
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.contextmenumain, menu);
	}
	@Override
	public boolean onContextItemSelected (MenuItem item)
	{
		AdapterContextMenuInfo info;
		switch(item.getItemId())
		{
		case R.id.delete:			
			info = (AdapterContextMenuInfo)item.getMenuInfo();			
			delete((Stock) mPullRefreshListView.getRefreshableView().getItemAtPosition(info.position));
			break;
		case R.id.menu_new:
			addStock();
			break;
		case R.id.optionchain:
			info = (AdapterContextMenuInfo)item.getMenuInfo();			
			viewOptionChain((Stock) mPullRefreshListView.getRefreshableView().getItemAtPosition(info.position));
			break;		
		case R.id.earnings:		
			info = (AdapterContextMenuInfo)item.getMenuInfo();			
			viewEarningCalendar((Stock) mPullRefreshListView.getRefreshableView().getItemAtPosition(info.position));
			break;
		case R.id.news:
			info = (AdapterContextMenuInfo)item.getMenuInfo();			
			viewNews((Stock) mPullRefreshListView.getRefreshableView().getItemAtPosition(info.position));
		}
		return true;
	}
    @Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
    	MenuInflater inflater = this.getMenuInflater();
    	inflater.inflate(R.menu.optionsmenu, menu);
		return true;
	}
    @Override 
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch(item.getItemId())
    	{
    	case R.id.add:
    		addStock();
    		break;
    	case R.id.deleteall:
    		deleteAll();
    		break;
    	case R.id.refresh:
    		refresh();
    		break;
    	case R.id.about:
    		showAbout();
    		break;
    	}
    	return true;
    }	
    private void addStock()
	{
		Intent intent = new Intent(this, StockQuoteAddActivity.class);
		startActivityForResult(intent,ADD_STOCK);
		
		
	}
	private void viewStock(Stock s)
	{
		Intent intent = new Intent(this,StockQuoteDetailActivity.class);	
		intent.putExtra(KEY_VIEW_STOCK, s);
		
		startActivity(intent);
	}
	private void delete(Stock s)
	{
		dbHelper.deleteStock(s.getSymbol());
		load(); 
	}
    private void viewOptionChain(Stock s)
    {
    	Intent intent = new Intent(this, StockOptionChainActivity.class);
    	intent.putExtra(KEY_OPTION_CHAIN, s.getSymbol());
    	startActivity(intent); 
    }
    private void viewEarningCalendar(Stock s )
    {    	    		
    	Intent intent = new Intent(this, StockEarningsActivity.class);
    	intent.putExtra(KEY_STOCK_SYMBOL, s.getSymbol());
    	startActivity(intent); 
    }
    private void viewNews(Stock s)
    {   	 
    	Intent intent = new Intent(this,StockRssNewsActivity.class);
    	intent.putExtra(KEY_VIEW_NEWS, s.getSymbol());
    	startActivity(intent); 
    }
    private void deleteAll()
    {
    	dbHelper.deleteAll();
    	load();
    }
    private void showAbout()
    {
    	Intent intent = new Intent(this,StockQuoteAboutActivity.class);
    	startActivity(intent);
    }
    @Override
	protected void onListItemClick(ListView parent, View v, int position , long id)
	{
    	super.onListItemClick(parent, v, position, id);
    	//Stock one = (Stock) getListAdapter().getItem(position-1); 

    	Stock s = (Stock) mPullRefreshListView.getRefreshableView().getItemAtPosition(position); 
    	//ArrayList<Stock> stocks = dbHelper.fetchAllToList();
    	//Stock s = stocks.get(position);
    	viewStock(s);
	}
	
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(ADD_STOCK == requestCode)
		{
			if(RESULT_OK == resultCode)
			{
				Bundle extras = data.getExtras();
				String symbol = extras.getString(StockQuoteDBHelper.DB_FIELD_SYMBOL).toUpperCase();
				if(symbol.trim().length() > 0)
					dbHelper.addStock(new Stock(symbol));
				load();
			}
		}
	}
	
    @Override
    public void onStockLoaded(ArrayList<Stock> stocks)
    {
    	if(stocks != null)
		{
			for(Stock s: stocks)
			{
				dbHelper.updateStock(s);				
			}
		}
    	if(stocks != null && stocks.size() > 0)
    	{
    		populateView();
    	}
    	// Call onRefreshComplete when the list has been refreshed.
    	mPullRefreshListView.onRefreshComplete();
    }
    
    
    @Override
	public void onPause()
	{
		super.onPause();
		Log.i(StockQuoteMainActivity.class.getName(), "on Paused");
		mHandler.removeCallbacks(refreshTask);
	}
	@Override
	public void onResume()
	{
		super.onResume();
		Log.i(StockQuoteMainActivity.class.getName(), "on onResume");
		mHandler.post(refreshTask);
	}
	@Override
    public void onDestroy()
    {
    	super.onDestroy();
    	dbHelper.close();
    	Log.i(StockQuoteMainActivity.class.getName(), "on onDestroy");
    }
    
    
    
}