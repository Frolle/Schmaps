package com.chalmers.schmaps.test;

import java.util.List;

import com.chalmers.schmaps.GoogleMapShowLocation;
import com.chalmers.schmaps.R;
import com.chalmers.schmaps.SearchSQL;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.test.ActivityInstrumentationTestCase2;

public class GoogleMapShowLocationTest extends
		ActivityInstrumentationTestCase2<GoogleMapShowLocation> {

	private GoogleMapShowLocation showLocationActivity;
	private MapView mapView;
	private SearchSQL tester;


	public GoogleMapShowLocationTest() {
		super(GoogleMapShowLocation.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		this.showLocationActivity = super.getActivity();
		this.mapView = (MapView)showLocationActivity.findViewById(R.id.mapview);
		tester = new SearchSQL(showLocationActivity);
		tester.createDatabase();
		tester.openRead();
	}
	
	public void testDrawLocations(){
		//showLocationActivity.drawLocationList("Microwaves");
		List<Overlay> overlay = mapView.getOverlays();
		assertEquals(2,overlay.size());
	}

}