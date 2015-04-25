package dev.shoulongli.util;

import dev.shoulongli.ui.StockNewsFeedAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * A listener which expects a URL as a tag of the view it is associated with. It then opens the URL
 * in the browser application.
 */
public class UrlIntentListener implements OnItemClickListener 
{

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
    {
    	if(view.getTag() instanceof StockNewsFeedAdapter.RssItemHolder)
    	{
    		StockNewsFeedAdapter.RssItemHolder tag = (StockNewsFeedAdapter.RssItemHolder)view.getTag();
    		final String url = tag.getUrl();
    		if(url != null )
    		{
    			final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    			final Context context = parent.getContext();
    			context.startActivity(intent);
    		}
    	}
    }

}