package dev.shoulongli;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


import dev.shoulongli.option.Option;
import dev.shoulongli.option.OptionLoaderCallback;
import dev.shoulongli.option.OptionLoaderTask;
import dev.shoulongli.stock.StockQuoteUtil;
import dev.shoulongli.ui.OptionChainAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
//FIXME
//expire date
//weekly option
public class StockOptionChainActivity extends Activity implements OptionLoaderCallback
{
	public static final String KEY_CALL_OPTION_CHAIN = "callOptionChain";
	public static final String KEY_PUT_OPTION_CHAIN = "putOptionChain";
	private ArrayList<Option> callOptions;
	private ArrayList<Option> putOptions;
	private TextView symbolView;
	private TextView expiredateView;
	private TextView optionTitle;
	private ListView optionchainView;
	private Spinner  optionType;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.optionchain);

		symbolView = (TextView)findViewById(R.id.symbol);
		expiredateView = (TextView)findViewById(R.id.expiredate);		
		expiredateView.setText(new SimpleDateFormat("yyyy-MM").format(Calendar.getInstance().getTime()));
		
		optionchainView = (ListView) findViewById(R.id.optionchain);
		optionType = (Spinner)findViewById(R.id.optiontype);
		ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,R.array.optiontype,android.R.layout.simple_spinner_item);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		optionType.setAdapter(spinnerAdapter);
		
		optionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
		{
			@Override 
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
			{
				//String[] optionTypeStr = getResources().getStringArray(R.array.optiontype);
				//String select = parent.getItemAtPosition(pos).toString();				
				refreshOptionChain(pos);
			}
			@Override
			public void onNothingSelected(AdapterView parent) 
			{
				// Do nothing.
			}

		});
		
		optionTitle = (TextView)findViewById(R.id.optionTitle);
		
		String symbol = getIntent().getStringExtra(StockQuoteMainActivity.KEY_OPTION_CHAIN);
		if(symbol != null)
		{			
			symbolView.setText(symbol);
			String date = expiredateView.getText().toString();
			new OptionLoaderTask(this).execute(symbol,date);
		}
		
		Button query = (Button)findViewById(R.id.query);
		query.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				String symbol = symbolView.getText().toString();
				String date = expiredateView.getText().toString();
				new OptionLoaderTask(StockOptionChainActivity.this).execute(symbol,date);
			}
		});
		query.requestFocus();
		refreshOptionChain(optionType.getSelectedItemPosition());
	}
	private void refreshOptionChain(int type)
	{
		//0  call
		//1  put
		if(type == 0 )
		{
			optionTitle.setText(R.string.calloption); 
			if(callOptions != null)
				optionchainView.setAdapter(new OptionChainAdapter(this,R.layout.optionitem,callOptions));
		}
		else if(type == 1)
		{
			optionTitle.setText(R.string.putoption);
			if(putOptions != null)
				optionchainView.setAdapter((new OptionChainAdapter(this,R.layout.optionitem,putOptions)));
		}
	}
	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		if(this.callOptions != null)
		{
			outState.putParcelableArrayList(KEY_CALL_OPTION_CHAIN, callOptions);
		}
		if(this.putOptions != null)
		{
			outState.putParcelableArrayList(KEY_PUT_OPTION_CHAIN, putOptions);
		}
	}
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		this.callOptions = savedInstanceState.getParcelableArrayList(KEY_CALL_OPTION_CHAIN);
		this.putOptions = savedInstanceState.getParcelableArrayList(KEY_PUT_OPTION_CHAIN);
	}
	@Override
	public void onOptionLoaded(ArrayList[] options)
	{
		if(options != null && options.length == 2)
		{
			callOptions = (ArrayList<Option>)options[0];
			putOptions = (ArrayList<Option>)options[1];
			refreshOptionChain(optionType.getSelectedItemPosition());
			
			
		}
	}
}
