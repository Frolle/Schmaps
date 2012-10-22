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

import android.app.AlertDialog;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.test.ActivityInstrumentationTestCase2;

import com.chalmers.schmaps.GoogleMapSearchLocation;
import com.chalmers.schmaps.MapItemizedOverlay;
import com.chalmers.schmaps.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;
/**
 * Test class for testing the MapItemizedOverlay by adding overlays,
 * removing overlays and testing what happens when you tap on the 
 * overlay items.
 * @author Frolle
 *
 */
public class MapItemizedOverlayTest extends ActivityInstrumentationTestCase2<GoogleMapSearchLocation> {
	private static final int THEOVERLAYITEMONMAP = 0;

	private static final int ARBITRARYLATVALUE = 67854516;

	private static final int ARBITRARYLONGVALUE = 20215681;

	private static final int ARBITRARYLATVALUE2 = 29385034;

	private static final int ARBITRARYLONGVALUE2 = 30482932;

	private GoogleMapSearchLocation activity;
	private MapItemizedOverlay mapOverlay;
	private Drawable drawable;


	
	public MapItemizedOverlayTest(){
		super(GoogleMapSearchLocation.class);
	}
	
	/**
	 * Set up instance variables
	 */
	@Override
	protected void setUp() 	throws Exception {
		super.setUp();
		this.activity = super.getActivity();
		drawable = activity.getResources().getDrawable(R.drawable.chalmersandroid);
		mapOverlay = new MapItemizedOverlay(drawable,activity);		
	}
	/**
	 * Test that all fields are assigned correct class.
	 */

	public void testPreConditions(){
		assertEquals(GoogleMapSearchLocation.class, activity.getClass());
		assertEquals(BitmapDrawable.class, drawable.getClass());
	}
	/**
	 * Tests that an overlay item is added by adding it to the the overlay
	 * and checking the size of it.
	 */
	public void testAddOverlay(){
		GeoPoint p = new GeoPoint(ARBITRARYLATVALUE,ARBITRARYLONGVALUE);
		OverlayItem item = new OverlayItem(p, "Hej Kiruna", "Här är det kallt");
		mapOverlay.addOverlay(item);
		assertEquals(1,mapOverlay.size());
	}	
	
	/**
	 * Works the same way as addOverlay but removes overlays instead.
	 */
	public void testRemoveOverlay(){
		GeoPoint p = new GeoPoint(ARBITRARYLATVALUE,ARBITRARYLONGVALUE);
		GeoPoint p2 = new GeoPoint(ARBITRARYLATVALUE2,ARBITRARYLONGVALUE2);
		OverlayItem item = new OverlayItem(p, "RAWR", "TESTRAWR");
		OverlayItem item2 = new OverlayItem(p2, "RAWR2", "TESTRAWR");
		mapOverlay.addOverlay(item);
		mapOverlay.addOverlay(item2);
		assertEquals(2,mapOverlay.size());
		mapOverlay.removeOverlay();
		assertEquals(0, mapOverlay.size());
		
	}
	
	/**
	 * Tests the onTap function by confirming that a dialog is shown
	 * when the overlay item was tapped.
	 * @throws InterruptedException
	 */
	public void testOnTap() throws InterruptedException{
		GeoPoint p = new GeoPoint(ARBITRARYLATVALUE,ARBITRARYLONGVALUE);
		OverlayItem item = new OverlayItem(p, "RAWR", "TESTRAWR");
		mapOverlay.addOverlay(item);
		mapOverlay.onTap(THEOVERLAYITEMONMAP);
		super.getInstrumentation().waitForIdleSync();
		//Get the alert dialog.
		AlertDialog dialogShowing = mapOverlay.getAlertDialog();
		assertTrue(dialogShowing.isShowing());
		}
}
