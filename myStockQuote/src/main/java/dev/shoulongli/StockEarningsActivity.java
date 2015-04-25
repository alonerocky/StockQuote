package dev.shoulongli;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import dev.shoulongli.earnings.EarningsCalendar;
import dev.shoulongli.earnings.EarningsCalendarItem;
import dev.shoulongli.earnings.EarningsLoaderCallback;
import dev.shoulongli.earnings.EarningsLoaderTask;
import dev.shoulongli.stock.Stock;
import dev.shoulongli.stock.StockQueryCallback;
import dev.shoulongli.stock.StockQueryTask;
import dev.shoulongli.ui.EarningsCalendarAdapter;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class StockEarningsActivity extends Activity implements EarningsLoaderCallback , StockQueryCallback
{
//	private static final String url = "http://www.earnings.com/company.asp?client=cb&ticker=sina";
//	private static final String url1 = "http://biz.yahoo.com/research/earncal/a/arm.l.html";
//	private static final String url2 = "http://biz.yahoo.com/research/earncal/today.html";
//	private static final String url3 = "http://biz.yahoo.com/research/earncal/20120424.html";

	public static final String KEY_STOCK_SYMBOL = "STOCK_SYMBOL";
	
	private ArrayAdapter<String> autoCompleteAdapter;
	
	private AutoCompleteTextView symbolView;
	private TextView dateView;
	//private EditText dateView;
	private ListView earningsView;
	private TextView earingsDateView;
	//private TextView earningsDateLabel;
	private Spinner queryType;
	//http://finance.yahoo.com/q/op?s=sina+Options
	//http://finance.yahoo.com/q/op?s=SINA&m=2012-05
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.earningscalendar);


		autoCompleteAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);


		TextWatcher textChecker = new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{

			}
			@Override
			public void afterTextChanged(Editable s)
			{

			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{

				if(s.length()  > 0)        		
					new StockQueryTask(StockEarningsActivity.this).execute(s.toString());
			}
		};

		symbolView = (AutoCompleteTextView)findViewById(R.id.symbol);
		dateView = (TextView)findViewById(R.id.querybydate);
		symbolView.setAdapter(autoCompleteAdapter);
		symbolView.addTextChangedListener(textChecker);
		
		earningsView = (ListView)findViewById(R.id.earningsList);
		earningsView.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
		    {
				
				EarningsCalendarItem item = (EarningsCalendarItem)earningsView.getItemAtPosition(position);
		    	if(item != null)
		    	{
		    		viewStockDetail(new Stock(item.getSymbol()));
		    	}
		    }
		});
		
		
		earingsDateView = (TextView)findViewById(R.id.earningsdate);
		// earningsDateLabel = (TextView)findViewById(R.id.earningsdatelabel);
		// earningsDateLabel.setText(new SimpleDateFormat("YYYY-MM-dd").format(Calendar.getInstance().getTime()));

		queryType = (Spinner)findViewById(R.id.querytype);
		ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,R.array.querytype,android.R.layout.simple_spinner_item);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		queryType.setAdapter(spinnerAdapter);
		queryType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
		{
			@Override 
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
			{
				//String[] optionTypeStr = getResources().getStringArray(R.array.optiontype);
				//String select = parent.getItemAtPosition(pos).toString();				
				if(pos == 0)//query by symbol
				{
					//symbolView.setText("");
					symbolView.setVisibility(View.VISIBLE);
					dateView.setVisibility(View.GONE);
				}
				else if(pos == 1)//query by date
				{
					Calendar c = Calendar.getInstance();
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
					Date currDate = c.getTime();
					symbolView.setVisibility(View.GONE);
					dateView.setVisibility(View.VISIBLE);
					dateView.setText(format.format(currDate));
					dateView.setHint(R.string.datehint);
				}
			}
			@Override
			public void onNothingSelected(AdapterView parent) 
			{
				// Do nothing.
			}

		});
		String symbol = getIntent().getStringExtra(StockQuoteMainActivity.KEY_STOCK_SYMBOL);
		if(symbol != null)
		{
			symbolView.setText(symbol);
			new EarningsLoaderTask(StockEarningsActivity.this,EarningsLoaderTask.QUERY_BY_SYMBOL).execute(symbol);
		}

		Button query = (Button)findViewById(R.id.query);
		query.setOnClickListener(new View.OnClickListener() 
		{

			@Override
			public void onClick(View arg0) 
			{
				//0 - query by symbol
				//1 - query by date
				int type = queryType.getSelectedItemPosition();
				if(0 == type)
				{
					String symbol = symbolView.getText().toString();
					new EarningsLoaderTask(StockEarningsActivity.this,EarningsLoaderTask.QUERY_BY_SYMBOL).execute(symbol);
				}
				else if(1 == type)
				{
					String date = dateView.getText().toString();
					new EarningsLoaderTask(StockEarningsActivity.this,EarningsLoaderTask.QUERY_BY_DATE).execute(date);
				}
			}
		});



	}
	private void viewStockDetail(Stock s)
	{
		Intent intent = new Intent(this,StockQuoteDetailActivity.class);	
		intent.putExtra(StockQuoteMainActivity.KEY_VIEW_STOCK, s);
		
		startActivity(intent);
	}
	public void onEarningsLoaded(EarningsCalendar  earningcalendar)
	{
		ArrayList<EarningsCalendarItem> earnings = earningcalendar.getEarningsList();
		if(earnings != null )
		{
			earningsView.setAdapter(new EarningsCalendarAdapter(this,R.layout.earningscalendaritem,earnings));
			int index = getIndex(symbolView.getText().toString(),earnings);
			earningsView.setSelection(index);
			((EarningsCalendarAdapter)earningsView.getAdapter()).setSelectedItem(index);
			
			SimpleDateFormat format = new SimpleDateFormat("EEEEEE, MMMM dd");
			earingsDateView.setText(format.format(earningcalendar.getDate()));
		}
	}
	private int getIndex(String symbol, ArrayList<EarningsCalendarItem> earnings)
	{
		int index = 0;
		for(int i = 0 ; i < earnings.size(); i++)
		{
			if(earnings.get(i).getSymbol().equalsIgnoreCase(symbol))
				return i;
		}
		return index;
	}
	@Override
	public void onQueryCompleted(ArrayList<String> lists)
	{
		autoCompleteAdapter.clear();
		if(lists != null)
		{
			autoCompleteAdapter = new ArrayAdapter<String>(StockEarningsActivity.this, android.R.layout.simple_dropdown_item_1line,lists);
			symbolView.setAdapter(autoCompleteAdapter);
		}
	}
}
