package dev.shoulongli.stock;

public class StockQuoteConfigure 
{
	//symbol,  
	//Last trade (Price only) l1, 
	//Change c1, 
	//Change(Real time) c6  , 
	//Change Percent (Real Time) k2,   
	//Change in Percent p2,  
	//Change & Percent Change c
	// Name n 
	
	//Ask a
	//Bid b
	//Volume  v
	
	//Previous Close  p	
	//Day's Range  m
	
	//Open  o
	//52-week Range w
	
	//Volume  v
	//Market Cap  j1
	
	//P/E Ratio    r	
	//Dividend Yield      y
	
	//EPS   e7	
	//1y Target Est  t8
	
	//Last Trade Time  t1
	//Average Daily Volume  a2
	
	
	//"After Hours Change (Real-time)  c8
	
	
	
	public static final String TAGS = "sl1nabc1p2pmowvj1rye7t8t1a2";
	public static final String STOCK_QUOTE_URL = "http://finance.yahoo.com/d/quotes.csv?s=%s&f=%s";
	
	public static final String STOCK_QUOTE_QUERY = "http://d.yimg.com/autoc.finance.yahoo.com/autoc?query=%s&callback=YAHOO.Finance.SymbolSuggest.ssCallback";
	
	/*
		Small chart:

		1 day: http://ichart.yahoo.com/t?s=IBM
		5 days: http://ichart.yahoo.com/v?s=IBM
		1 year: http://ichart.finance.yahoo.com/c/bb/m/ibm

		Big chart:

		1 day: http://ichart.finance.yahoo.com/b?s=IBM
		5 days: http://ichart.finance.yahoo.com/w?s=IBM
		3 months: http://chart.finance.yahoo.com/c/3m/ibm
		6 months: http://chart.finance.yahoo.com/c/6m/ibm
		1 year: http://chart.finance.yahoo.com/c/1y/ibm
		2 years: http://chart.finance.yahoo.com/c/2y/ibm
		5 years: http://chart.finance.yahoo.com/c/5y/ibm
		max: http://chart.finance.yahoo.com/c/my/ibm
	*/
	public static final String STOCK_CHART_SMALL_ONE_DAY = "http://ichart.yahoo.com/t?s=";
	public static final String STOCK_CHART_SMALL_FIVE_DAY = "http://ichart.yahoo.com/v?s=";
	public static final String STOCK_CHART_SMALL_ONE_YEAR = "http://ichart.finance.yahoo.com/c/bb/m/";
	
	public static final String STOCK_CHART_ONE_DAY = "http://ichart.finance.yahoo.com/b?s=";
	public static final String STOCK_CHART_FIVE_DAY = "http://ichart.finance.yahoo.com/w?s=";
	public static final String STOCK_CHART_THREE_MONTH = "http://chart.finance.yahoo.com/c/3m/";
	public static final String STOCK_CHART_SIX_MONTH = "http://chart.finance.yahoo.com/c/6m/";
	public static final String STOCK_CHART_ONE_YEAR = "http://chart.finance.yahoo.com/c/1y/";
	public static final String STOCK_CHART_TWO_YEAR = "http://chart.finance.yahoo.com/c/2y/";
	public static final String STOCK_CHART_FIVE_YEAR = "http://chart.finance.yahoo.com/c/5y/";
	public static final String STOCK_CHART_MAX = "http://chart.finance.yahoo.com/c/my/";
	
	
	/*
	 * 
	 * rss news
	 * 
	 *  	http://finance.yahoo.com/rss/headline?s=ticker(s)
		You can find out the ticker symbol for a specific company using the 
		Symbol Lookup feature on Yahoo! Finance. For example, to get news 
		items for Yahoo!, use the ticker symbol YHOO and this request URL:

			http://finance.yahoo.com/rss/headline?s=yhoo

		Ticker symbols are case insensitive. You can separate multiple ticker 
		symbols with a comma:

			http://finance.yahoo.com/rss/headline?s=yhoo,msft,tivo 
	 */
	
	public static final String STOCK_NEWS_URI = "http://finance.yahoo.com/rss/headline?s=";
	/*
	 * 
	 * http://finance.yahoo.com/q/op?s=msft&m=2012-05
	 * http://finance.yahoo.com/q/op?s=AAPL+Options
	 * http://finance.yahoo.com/q/op?s=aapl&m=2012-04-27
	 * 
	 */
	public static final String OPTION_URL = "http://finance.yahoo.com/q/op?s=%s&m=%s";
	/*
	 * 
	 * http://biz.yahoo.com/research/earncal/a/arm.l.html
	 */
	//FIXME
	public static final String CALENDAR_URL = "http://biz.yahoo.com/research/earncal/a/arm.l.html";
	
	
	
	//stockcharts.com
	//http://stockcharts.com/c-sc/sc?s=AAPL&p=D&b=5&g=0&i=p67002939069&r=1336247253958
	//http://stockcharts.com/h-sc/ui?s=AAPL&p=D&b=5&g=0&id=p670029390
}
