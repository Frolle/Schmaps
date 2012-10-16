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

import com.chalmers.schmaps.GoogleMapSearchLocation;
import com.chalmers.schmaps.PathOverlay;
import com.chalmers.schmaps.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

import android.graphics.Canvas;
import android.test.ActivityInstrumentationTestCase2;


public class PathOverlayTest extends ActivityInstrumentationTestCase2<GoogleMapSearchLocation> {
	
	private GoogleMapSearchLocation activity;
	private MapView mapview;
	private PathOverlay pathoverlay;
	private GeoPoint p1, p2, p3, p4;
	private ArrayList<GeoPoint> geolist = new ArrayList<GeoPoint>();
	private Canvas canvas;

	public PathOverlayTest() {
		super(GoogleMapSearchLocation.class);
	
	}

	protected void setUp() throws Exception {
		super.setUp();
		this.activity = super.getActivity();
		p1 = new GeoPoint(57715450, 11999690);
		geolist.add(p1);
		
		p2 = new GeoPoint(57715350, 11999310);
		geolist.add(p2);
		
		p3 = new GeoPoint(57714780, 11999840);
		geolist.add(p3);
		
		p4 = new GeoPoint(57714380, 11996780);
		geolist.add(p4);
		pathoverlay = new PathOverlay(geolist);
		
		canvas = new Canvas();
		this.mapview = (MapView) this.activity.findViewById(R.id.mapview);
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	/**
	 * Tests that all four geopoints are added when pathoverlay-activity is started 
	 */
	public void testNrOfGeoPoints(){
		assertEquals(4,pathoverlay.getSize());
	}
	
	
	/**
	 * Tests that all lines are drawn, that it loops through the geopoints and in that loop
	 * calls drawLine which draws the line between these points 
	 */
	public void testDrawPath(){
		pathoverlay.draw(canvas,mapview,true);
		
		assertEquals(3,pathoverlay.getIndexSize());
		
	}

}
