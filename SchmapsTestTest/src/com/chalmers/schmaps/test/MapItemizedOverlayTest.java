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

public class MapItemizedOverlayTest extends ActivityInstrumentationTestCase2<GoogleMapSearchLocation> {
	
	ArrayList<OverlayItem> arrayOverlays;
	
	GoogleMapSearchLocation activity;
	MapItemizedOverlay mapOverlay;
	Drawable drawable;
	
	
	public MapItemizedOverlayTest(){
		super(GoogleMapSearchLocation.class);
	}
	
	@Override
	protected void setUp() 	throws Exception {
		super.setUp();
		this.activity = super.getActivity();
		drawable = activity.getResources().getDrawable(R.drawable.androidgubbemini);
		
		mapOverlay = new MapItemizedOverlay(drawable,activity);
		arrayOverlays = new ArrayList<OverlayItem>();
		

	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	
	public void testAddOverlay(){
		GeoPoint p = new GeoPoint(67854516,20215681);
		OverlayItem item = new OverlayItem(p, "Hej Kiruna", "Här är det kallt");
		mapOverlay.addOverlay(item);
		Integer i = mapOverlay.size();
		String s = i.toString();
		Log.e("testmapitomized", s);
		assertEquals(1,mapOverlay.size());
	}	
}
