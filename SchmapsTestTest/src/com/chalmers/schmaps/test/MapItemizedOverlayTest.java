package com.chalmers.schmaps.test;

import java.util.ArrayList;


import com.chalmers.schmaps.GoogleMapSearchLocation;
import com.chalmers.schmaps.MapItemizedOverlay;
import com.chalmers.schmaps.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.test.ActivityInstrumentationTestCase2;
import android.test.AndroidTestCase;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class MapItemizedOverlayTest extends AndroidTestCase {
	
	ArrayList<OverlayItem> arrayOverlays;
	
	MapItemizedOverlay activity;
	GoogleMapSearchLocation searchLocation;
	Drawable drawable;
	
	
	public MapItemizedOverlayTest(){
		super();
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		searchLocation = new GoogleMapSearchLocation();
		drawable = searchLocation.getResources().getDrawable(R.drawable.ic_launcher);
		
		activity = new MapItemizedOverlay(drawable,searchLocation);
		arrayOverlays = new ArrayList<OverlayItem>();
		

	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	
	public void testAddOverlay(){
		GeoPoint p = new GeoPoint(67854516,20215681);
		OverlayItem item = new OverlayItem(p, "Hej Kiruna", "Här är det kallt");
		activity.addOverlay(item);
		Integer i = activity.size();
		String s = i.toString();
		Log.e("testmapitomized", s);
		assertEquals(1,activity.size());
	}	
}
