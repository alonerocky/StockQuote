package dev.shoulongli;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class StockQuoteAboutActivity extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		WebView web = (WebView)findViewById(R.id.about);
		web.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		web.loadUrl("file:///android_asset/about.html");
				
	}
}
