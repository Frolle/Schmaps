/*
 * Copyright [2012] [Simon Fransson, Martin Augustsson]

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
package com.chalmers.schmaps;


import java.util.ArrayList;
import java.util.List;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

public class GoogleMapShowLocation extends MapActivity {
	private static final int MICROWAVEBUTTON = 1;
	private static final int RESTAURANTBUTTON = 2;
	private static final int ATMBUTTON = 3;
	private static final int JOHANNESBERG = 40;

	private static final String DB_MICROWAVETABLE = "Microwaves"; //Name of our microwave table
	private static final String DB_RESTAURANTTABLE = "Restaurants"; //Name of our restaurants table
	private static final String DB_ATMTABLE = "Atm";				//Name of our ATM table
		
    private MapController mapcon;
	private List<Overlay> mapOverlays;
	private MapItemizedOverlay overlay;
	private MapView mapView;
	private SearchSQL search;
	private GeoPoint johannesbergLoc;
	private GeoPoint lindholmenLoc;
	@Override
	/**
	 * Method for determining on creation how the map view will be shown, what locations should be drawn
	 * and assign the instances accordingly.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle setView = getIntent().getExtras();		
		setContentView(R.layout.activity_strippedmap);
		assignInstances();
		//If-check to see if it's Lindholmen or Johannesberg campus
		if(setView.getInt("Campus")==JOHANNESBERG)
		{
			mapcon.animateTo(johannesbergLoc);
		}
		else 
		{
			mapcon.animateTo(lindholmenLoc);
		}
		mapcon.setZoom(16);

		//Switch case to determine what series of locations to be drawn on map
		switch(setView.getInt("Show locations")){
		case MICROWAVEBUTTON:
			drawLocationList(DB_MICROWAVETABLE);
			break;
		
		case RESTAURANTBUTTON:
			drawLocationList(DB_RESTAURANTTABLE);
			break;
			
		case ATMBUTTON:
			drawLocationList(DB_ATMTABLE);
			break;
		}
		

	}

/**
 * Draws locations (overlayitems) from specified table.
 * @param table - table containing the locations to be drawn.
 */
	public void drawLocationList(String table) {
		search.openRead();
		ArrayList<OverlayItem> locationList = (ArrayList<OverlayItem>) search.getLocations(table);
		search.close();
		overlay.removeOverlay();
		for(OverlayItem item : locationList)
		{
			overlay.addOverlay(item);
			mapOverlays.add(overlay);

		}
		mapView.postInvalidate();
		
	}

	/**
	 *Method for telling if route displayed. 
	 *Shouldn't display a route in this activity so is always false.
	 */
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
		
	/**
	 * Simple method to assign all instance variables and initiate the settings for map view.
	 */
    private void assignInstances() {

		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(false);
		mapcon = mapView.getController();
		mapOverlays = mapView.getOverlays();
		lindholmenLoc = new GeoPoint(57706434, 11937214);
		johannesbergLoc = new GeoPoint(57688678, 11977136);
		Drawable drawable = this.getResources().getDrawable(R.drawable.dot); 
		overlay = new MapItemizedOverlay(drawable, this);
		search = new SearchSQL(GoogleMapShowLocation.this);
		search.createDatabase();
	}
}
