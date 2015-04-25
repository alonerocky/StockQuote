package dev.shoulongli.ui;

import java.lang.ref.WeakReference;

import dev.shoulongli.util.ImageDownloadTask;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

public class DownloadedDrawable extends ColorDrawable
{
	private WeakReference<ImageDownloadTask> weakRefImageDownloaderTask;
	public DownloadedDrawable(ImageDownloadTask task)
	{
		super(Color.BLACK);
		weakRefImageDownloaderTask = new WeakReference<ImageDownloadTask>(task);
	}
	
	public ImageDownloadTask getImageDownloaderTask()
	{
		return weakRefImageDownloaderTask.get();
	}
}
