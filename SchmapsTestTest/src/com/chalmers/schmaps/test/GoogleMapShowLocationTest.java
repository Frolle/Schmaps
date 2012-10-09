package com.chalmers.schmaps.test;

import java.util.List;

import com.chalmers.schmaps.GoogleMapShowLocation;
import com.chalmers.schmaps.R;
import com.google.android.maps.*;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

public class GoogleMapShowLocationTest extends
		ActivityInstrumentationTestCase2<GoogleMapShowLocation> {

	private GoogleMapShowLocation showLocationActivity;
	private MapView mapView;
	private List<Overlay> overlay;
	private static final int JOHANNESBERG = 40;
	private static final int MICROWAVEBUTTON = 1;
	
	public GoogleMapShowLocationTest() {
		super(GoogleMapShowLocation.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityIntent(new Intent("android.intent.action.CAMPUSMENUACTIVITY").putExtra("Show locations", MICROWAVEBUTTON).putExtra("Campus", JOHANNESBERG));
		setActivityInitialTouchMode(false);
		this.showLocationActivity = super.getActivity();
		this.mapView = (MapView) this.showLocationActivity.findViewById(R.id.mapview);
		overlay = mapView.getOverlays();
	}
	
	public void testDrawLocationsMicrowaves(){
		this.showLocationActivity.drawLocationList("Microwaves");
		super.getInstrumentation().waitForIdleSync();
		assertEquals(4,overlay.size());
	}
	
	public void testDrawLocationsRestaurants(){
		this.showLocationActivity.drawLocationList("Restaurants");
		super.getInstrumentation().waitForIdleSync();
		assertEquals(25,overlay.size());
	}
	
	public void testDrawLocationsAtms(){
		this.showLocationActivity.drawLocationList("Atm");
		super.getInstrumentation().waitForIdleSync();
		assertEquals(7,overlay.size());
	}
	
	public void testDrawLocationsRooms(){
		this.showLocationActivity.drawLocationList("Rooms");
		super.getInstrumentation().waitForIdleSync();
		assertEquals(263, overlay.size());
	}

}