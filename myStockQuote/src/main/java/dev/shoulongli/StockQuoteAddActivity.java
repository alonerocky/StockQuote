package dev.shoulongli;

import java.util.ArrayList;

import dev.shoulongli.datamanager.StockQuoteDBHelper;
import dev.shoulongli.stock.StockQueryCallback;
import dev.shoulongli.stock.StockQueryTask;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class StockQuoteAddActivity extends Activity implements StockQueryCallback
{
	private ArrayAdapter<String> autoCompleteAdapter;
	private AutoCompleteTextView stock;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.stockquoteadd);
        stock = (AutoCompleteTextView)findViewById(R.id.lookup);
        
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
        			new StockQueryTask(StockQuoteAddActivity.this).execute(s.toString());
        	}
        };
        stock.setAdapter(autoCompleteAdapter);
        stock.addTextChangedListener(textChecker);
        
        
        Button ok = (Button)findViewById(R.id.ok);
        Button cancel = (Button)findViewById(R.id.cancel);
        
        
        View.OnClickListener listener = new View.OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				switch(v.getId())
				{
				case R.id.ok:					
					Intent intent = new Intent();
					intent.putExtra(StockQuoteDBHelper.DB_FIELD_SYMBOL, stock.getText().toString());
					setResult(RESULT_OK, intent);	
					finish();
					break;
				case R.id.cancel:
					finish();
					break;
				}
			}
		};
		ok.setOnClickListener(listener);
		cancel.setOnClickListener(listener);
	}
	

	@Override
	public void onQueryCompleted(ArrayList<String> lists)
	{
		autoCompleteAdapter.clear();
		if(lists != null)
		{
			autoCompleteAdapter = new ArrayAdapter<String>(StockQuoteAddActivity.this, android.R.layout.simple_dropdown_item_1line,lists);
			stock.setAdapter(autoCompleteAdapter);
		}
	}
}
