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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chalmers.schmaps.SendToDB.GetQueue;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.content.Context;
<<<<<<< HEAD
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
=======
>>>>>>> c294522c375ed511652e5d6dc42de0eaead36e97

public class GoogleMapShowLocation extends MapActivity {
	private static final int MICROWAVEBUTTON = 1;
	private static final int RESTAURANTBUTTON = 2;
	private static final int ATMBUTTON = 3;
<<<<<<< HEAD
	private static final int LECTUREHALLBUTTON = 4;
	private static final int BOOKINGKEY = 5;
	private static final int BUSKEY = 6;
	private static final int SENDTODB = 7;
=======
>>>>>>> c294522c375ed511652e5d6dc42de0eaead36e97
	private static final int JOHANNESBERG = 40;

	private static final String DB_MICROWAVETABLE = "Microwaves"; //Name of our microwave table
	private static final String DB_RESTAURANTTABLE = "Restaurants"; //Name of our restaurants table
	private static final String DB_ATMTABLE = "Atm";				//Name of our ATM table
		
    private MapController mapcon;
	private LocationManager location_manager;
	private LocationListener location_listener;
	private List<Overlay> mapOverlays;
	private MapItemizedOverlay overlay;
	private MapView mapView;
	private SearchSQL search;
	private GeoPoint johannesbergLoc;
	private GeoPoint lindholmenLoc;
	private GPSPoint gpsPoint;
	private ArrayList<OverlayItem> locationList;
	private JSONObject jsonObject, returnedJsonObject;
	@Override
	/**
	 * Method for determining on creation how the map view will be shown, what locations should be drawn
	 * and assign the instances accordingly.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle setView = getIntent().getExtras();		
		setContentView(R.layout.activity_strippedmap);
		connectToDB();
		assignInstances();
		//If-check to see if it's Lindholmen or Johannesberg campus
		if(setView.getInt("Campus")==JOHANNESBERG)
			mapcon.animateTo(johannesbergLoc);
		
		else 
			mapcon.animateTo(lindholmenLoc);
		mapcon.setZoom(16);

		//Switch case to determine what series of locations to be drawn on map
		switch(setView.getInt("Show locations")){
		
		case SENDTODB:
			drawLocationList(DB_RESTAURANTTABLE);
			drawQueue();
			
		case RESTAURANTBUTTON:
			drawLocationList(DB_RESTAURANTTABLE);
		
		case MICROWAVEBUTTON:
			drawLocationList(DB_MICROWAVETABLE);
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
		locationList = search.getLocations(table);
		search.close();
		overlay.removeOverlay();
		for(OverlayItem item : locationList)
		{
			overlay.addOverlay(item);
			mapOverlays.add(overlay);

		}
		mapView.postInvalidate();
		
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_map, menu);
		return true;
	}
	

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
		location_manager.removeUpdates(location_listener);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			// Register the listener with the Location Manager to receive
			// location updates
			location_manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 100, location_listener);
			location_manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 100, location_listener);
		}
		catch (Exception e) {
			//print("Couldn't use the GPS: " + e + ", " + e.getMessage());
		}
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
		gpsPoint= new GPSPoint(); 
		
		location_manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		location_listener = new LocationListener(){

			public void onLocationChanged(Location location) { //metod som hämtar din position genom att anropa onResume

				
				int longitude = (int) (location.getLongitude() * 1E6);
				int latitude = (int) (location.getLatitude() * 1E6);

				GeoPoint point = new GeoPoint(latitude, longitude);

			}

			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub

			}

			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {

			}	
		};
		search = new SearchSQL(GoogleMapShowLocation.this);
		search.createDatabase();
	}
    /*
     * Menu-button to select the option where you can see all people queueing atm.
     *
     */
    public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
		
		case R.id.getqueue:
				getQueue();
		}
		
		return false;
    }
    
    
    /*
     * Method to set the GPSPoints for the restaurants by calling the GPSPoint class
     * where you set a proximity alert on each desired position.
     */
    private void getQueue(){
    	int id = 1;
    	for(OverlayItem item: locationList){
    	gpsPoint.setGPSPoints(item.getPoint().getLongitudeE6(), item.getPoint().getLatitudeE6(), id++);	//set the proximity alert of the spot on the (long, lat) place
    	}

    }
    
	/*
	 * Checks how many are queueing by checking the database on the server and returning this in the screen as a dialouge.
	 */
	
	private void drawQueue(){

		int code, nrOfCheckedIn;

		try {
			JSONArray result = jsonObject.getJSONArray("result");
			JSONObject numberOfCheckedInPeople;

			//loop through the jsonarray and extract all checked-in points
			//collect data, create geopoint and add to list of overlays that will be drawn on map
			for(int count = 0;count<result.length();count++){
				numberOfCheckedInPeople = result.getJSONObject(count);
				
				code = (int) numberOfCheckedInPeople.getInt("code");
				nrOfCheckedIn = (int)numberOfCheckedInPeople.getInt("number");
				//code and nrOfCheckedIn need to be saved and somehow send to a database

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void connectToDB(){
		returnedJsonObject = null;
		SendToDB sender = new SendToDB();
		GetQueue getQueue = sender.new GetQueue();
		getQueue.execute();
		
		while(returnedJsonObject == null){ //if json object not returned, sleep for 30 sec
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		
		sender.parseQueue(returnedJsonObject);
		
	}

}
