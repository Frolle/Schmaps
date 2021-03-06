/*
 * Copyright [2012] [Dina Zuko]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. 
 */
package com.chalmers.schmaps.test;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.test.ActivityInstrumentationTestCase2;

import com.chalmers.schmaps.GoogleMapSearchLocation;
import com.chalmers.schmaps.PathOverlay;
import com.chalmers.schmaps.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;


public class PathOverlayTest extends ActivityInstrumentationTestCase2<GoogleMapSearchLocation> {
	
	private static final int ARBITRARYLATVALUE1 = 57715450;
	private static final int ARBITRARYLONGVALUE1 = 11999690;
	private static final int ARBITRARYLATVALUE2 = 57715350;
	private static final int ARBITRARYLONGVALUE2 = 11999310;
	private static final int ARBITRARYLATVALUE3 = 57714780;
	private static final int ARBITRARYLONGVALUE3 = 11999840;
	private static final int ARBITRARYLATVALUE4 = 57714380;
	private static final int ARBITRARYLONGVALUE4 = 11996780;
	private static final int SIZEOFPATHOVERLAY = 4;
	private static final int SIZEOFPATHINDEX = 3;
	private GoogleMapSearchLocation activity;
	private MapView mapview;
	private PathOverlay pathoverlay;
	private List<GeoPoint> geolist = new ArrayList<GeoPoint>();
	private Canvas canvas;

	public PathOverlayTest() {
		super(GoogleMapSearchLocation.class);
	
	}
	/**
	 * Setup method for instantiating the variables.
	 */
	protected void setUp() throws Exception {
		super.setUp();
		GeoPoint p1, p2, p3, p4;
		this.activity = super.getActivity();
		p1 = new GeoPoint(ARBITRARYLATVALUE1, ARBITRARYLONGVALUE1);
		geolist.add(p1);
		
		p2 = new GeoPoint(ARBITRARYLATVALUE2, ARBITRARYLONGVALUE2);
		geolist.add(p2);
		
		p3 = new GeoPoint(ARBITRARYLATVALUE3, ARBITRARYLONGVALUE3);
		geolist.add(p3);
		
		p4 = new GeoPoint(ARBITRARYLATVALUE4, ARBITRARYLONGVALUE4);
		geolist.add(p4);
		pathoverlay = new PathOverlay(geolist);
		
		canvas = new Canvas();
		this.mapview = (MapView) this.activity.findViewById(R.id.mapview);
	}
	
	/**
	 * Test that all fields are assigned correct class.
	 */
	public void testPreConditions(){
		assertEquals(GoogleMapSearchLocation.class, activity.getClass());
		assertEquals(MapView.class, mapview.getClass());
	}
	/**
	 * Tests that all four geopoints are added when pathoverlay-activity is started 
	 */
	public void testNrOfGeoPoints(){
		assertEquals(SIZEOFPATHOVERLAY,pathoverlay.getSize());
	}
	
	
	/**
	 * Tests that all lines are drawn, that it loops through the geopoints and in that loop
	 * calls drawLine which draws the line between these points 
	 */
	public void testDrawPath(){
		pathoverlay.draw(canvas,mapview,true);
		
		assertEquals(SIZEOFPATHINDEX,pathoverlay.getIndexSize());
		
	}

}
