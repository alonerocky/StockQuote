package dev.shoulongli.stock;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import dev.shoulongli.option.Option;

public class StockQuoteParser 
{
	private static StockQuoteParser instance = new StockQuoteParser();
	private StockQuoteParser()
	{

	}
	public static StockQuoteParser getInstance()
	{
		return instance;
	}
	public ArrayList<Stock> parse(InputStream is, String symbol, String tags)
	{
		ArrayList<Stock> result = new ArrayList<Stock>();
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String[] symbols = symbol.split("[+]");
			for(int i = 0 ; i < symbols.length;i++)
			{
				String quote = reader.readLine();
				if(quote == null)
					break;
				ArrayList<String> tag = StockQuoteUtil.getTags(tags);
				//"ARMH",27.79,"ARM Holdings, plc",27.79,27.78,27.64,28.06,1363751,"+0.55","+2.02%"
				String[] values = split(quote);
				Stock s = new Stock(symbols[i].trim().toUpperCase());
				if(tag != null && values != null && tag.size() == values.length)
				{
					for(int j = 0 ; j < tag.size();j++)
					{
						s.setAttributeByTag(tag.get(j), removeQuotes(values[j]));
					}
				}
				result.add(s);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	private static byte[] sBuffer = new byte[512];
	/*
    http://d.yimg.com/autoc.finance.yahoo.com/autoc?query=Axis%20Bank&callback=YAHOO.Finance.SymbolSuggest.ssCallback  

        YAHOO.Finance.SymbolSuggest.ssCallback(  
        {"ResultSet":  
           {"Query":"axis bank",  
           "Result":[  
           {"symbol":"AXBB.L",		"name": "AXIS BANK S GDR (REPR 1 EQTY SH",	"exch": "LSE", 	"type": "S","exchDisp":"London","typeDisp":"Equity"},  
           {"symbol":"AXISBANK.BO",	"name": "AXIS BANK",						"exch": "BSE",	"type": "S","exchDisp":"Bombay","typeDisp":"Equity"}  
           ]  
           }  
        }  
        )  

	 */
	public ArrayList<String> getStockQuoteSymbolList(InputStream is)
	{
		ArrayList<String> lists = new ArrayList<String>();
		try
		{
			ByteArrayOutputStream content = new ByteArrayOutputStream();

			// Read response into a buffered stream
			int readBytes = 0;
			while ((readBytes = is.read(sBuffer)) != -1) {
				content.write(sBuffer, 0, readBytes);
			}

			// Return result from buffered stream
			String  res = new String(content.toByteArray());
			if(!res.startsWith("Error"))
			{
				res = res.substring(res.indexOf('(')+1, res.length()-1);  
				JSONObject mainObject = new JSONObject(res);
				JSONObject resultSet = mainObject.getJSONObject("ResultSet");
				JSONArray result = resultSet.getJSONArray("Result");
				if(result.length() > 0)
				{
					for(int i = 0 ; i < result.length();i++)
					{
						JSONObject item = result.getJSONObject(i);
						lists.add(item.getString("symbol"));
					}
				}
			}			
		}
		catch(Exception e)
		{

		}
		return lists;
	}

	public static String[] split(String line)
	{
		//"ARMH",27.79,"ARM Holdings, plc",27.79,27.78,27.64,28.06,1363751,"+0.55","+2.02%"
		//String regressExpression = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";//"[,]";

		String otherThanQuote = " [^\"] ";
		String quotedString = String.format(" \" %s* \" ", otherThanQuote);
		String regex = String.format("(?x) "+ // enable comments, ignore white spaces
				",                         "+ // match a comma
				"(?=                       "+ // start positive look ahead
				"  (                       "+ //   start group 1
				"    %s*                   "+ //     match 'otherThanQuote' zero or more times
				"    %s                    "+ //     match 'quotedString'
				"  )*                      "+ //   end group 1 and repeat it zero or more times
				"  %s*                     "+ //   match 'otherThanQuote'
				"  $                       "+ // match the end of the string
				")                         ", // stop positive look ahead
				otherThanQuote, quotedString, otherThanQuote);
		if(line != null)
		{
			return line.split(regex);
		}
		return null;

	}
	public static final String UP = "Up";
	public static final String DOWN = "Down";
	public static ArrayList[] getOptionChain(String symbol, String date)
	{
		String url = String.format(StockQuoteConfigure.OPTION_URL, symbol,date);
		ArrayList<Option> call = new ArrayList<Option>();
		ArrayList<Option> put = new ArrayList<Option>();
		try
		{
			Document doc = Jsoup.connect(url).get();

			Elements tables = doc.select("tr");
			int count = 0;
			for(Element table: tables)
			{
				Elements rows = table.select("td");

				if(rows.size() == 8)
				{
					count ++;
					Option option = new Option();
					option.setStrike(rows.get(0).text());
					option.setSymbol(rows.get(1).text());
					option.setLast(rows.get(2).text());
					option.setChange(rows.get(3).text());

					Elements imgs = rows.get(3).select("img");
					if(!imgs.isEmpty())
					{
						if(!imgs.select("[alt]").isEmpty())
						{
							String attr = imgs.get(0).attr("alt");
							if(attr.equalsIgnoreCase(DOWN))
							{
								option.setChange(option.getChange() <= 0 ? option.getChange() : -option.getChange());
							}
							else if(attr.equalsIgnoreCase(UP))
							{
								option.setChange(option.getChange() >= 0 ? option.getChange() : -option.getChange());
							}
							
						}
					}
					option.setBid(rows.get(4).text());
					option.setAsk(rows.get(5).text());
					option.setVolume(rows.get(6).text());
					option.setOpenInt(rows.get(7).text());
					if(option.getType() == Option.OPTION_CALL)
					{
						call.add(option);
					}
					else if(option.getType() == Option.OPTION_PUT)
					{
						put.add(option);
					}
				}

			}

		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return  new ArrayList[]{call, put};
	}
	public static String removeQuotes(String str)
	{
		return str.replaceAll("^\"|\"$", "");
	}
}
