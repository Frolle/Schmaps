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


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chalmers.schmaps.GPSPoint.myLocationListener;
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
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;

import android.content.Context;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class GoogleMapShowLocation extends MapActivity implements View.OnClickListener {
	private static final int MICROWAVEBUTTON = 1;
	private static final int RESTAURANTBUTTON = 2;
	private static final int ATMBUTTON = 3;

	private static final int LECTUREHALLBUTTON = 4;
	private static final int BOOKINGKEY = 5;
	private static final int BUSKEY = 6;
	private static final int SENDTODB = 7;

	private static final int JOHANNESBERG = 40;
	private static final int LINDHOLMEN = 42;	

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
	private ArrayList<OverlayItem> locationList;
	private Button queueButton;
	private JSONObject returnedJSON;
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
		if(setView.getInt("Campus")==JOHANNESBERG){
			mapcon.animateTo(johannesbergLoc);
		}

		else if(setView.getInt("Campus")==LINDHOLMEN){
			mapcon.animateTo(lindholmenLoc);
		}else{
			mapcon.animateTo(johannesbergLoc);
		}
		mapcon.setZoom(16);


		//Switch case to determine what series of locations to be drawn on map
		switch(setView.getInt("Show locations")){

		case SENDTODB:
			drawLocationList(DB_RESTAURANTTABLE);
			break;

		case RESTAURANTBUTTON:
			drawLocationList(DB_RESTAURANTTABLE);
			break;

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
			//location_manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 100, location_listener);
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
		queueButton = (Button) findViewById(R.id.queuebutton);
		queueButton.setOnClickListener(this);
		
		location_manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		LocationProvider provider = location_manager.getProvider(LocationManager.NETWORK_PROVIDER);		//Using NETWORK_PROVIDER to save battery-time
		location_listener = new LocationListener()
		{

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
	 * Method to set the GPSPoints for the restaurants by calling the GPSPoint class
	 * where you set a proximity alert on each desired position.
	 */
	private void getQueue(){
		Location location = location_manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		int id = 1;
		if(location == null){
			Toast.makeText(this, "No last known location", Toast.LENGTH_LONG).show();
			return;
		}
		for(OverlayItem item: locationList){
			setGPSPoints(item.getPoint().getLongitudeE6(), item.getPoint().getLatitudeE6(), id++);	//set the proximity alert of the spot on the (long, lat) place
		}
		setGPSPoints(57714860,12000497, id++);
		Log.e("showlocation", "getqueue");
		connectToDB();


	}
	/*
	 * Add a proximity alert to the specified long and lat, adding an id for use in the database on our server.
	 */

	public void setGPSPoints(int lng, int lat, int id){
		Log.e("showlocation", "setGPSPoints");
		int distance = 20;
		PendingIntent intent;
		Intent intDB = new Intent(this, SendToDB.class);	//new intent from this class to the SendToDB class
		intDB.putExtra("restaurant", id);
		intent = PendingIntent.getActivity(this, 0, intDB, Intent.FLAG_ACTIVITY_NEW_TASK);	//creating a PendingIntent which will act like startActivity(intent) for the SendToDB class
		location_manager.addProximityAlert(lng, lat, distance, -1, intent);		//add a proximity alert on the specified longitude and latitude, on a radius of distance, without time-limit for the intent.

	}

	/*
	 * Checks how many are queueing by checking the database on the server and returning this in the screen as a dialouge.
	 */

	private void parseQueue(JSONObject jsonObject){
		
		Log.e("showlocation", "parse");
		
		ArrayList <PairOfCodeAndCheckedin> list = new ArrayList<PairOfCodeAndCheckedin>();

		int codeOfRest, nrOfCheckedIn;

		try {
			JSONArray result = jsonObject.getJSONArray("result");
			JSONObject numberOfCheckedInPeople;

			//loop through the jsonarray and extract all checked-in points
			//collect data, create geopoint and add to list of overlays that will be drawn on map
			for(int count = 0;count<result.length();count++){
				numberOfCheckedInPeople = result.getJSONObject(count);

				codeOfRest = (int) numberOfCheckedInPeople.getInt("code");
				nrOfCheckedIn = (int)numberOfCheckedInPeople.getInt("number");
				PairOfCodeAndCheckedin pair = new PairOfCodeAndCheckedin(codeOfRest,nrOfCheckedIn);
				list.add(pair);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		Log.e("showlocation", "parse finish");
		
		//tex return list till connecttoDB
		
		
	}

	private void connectToDB(){
		Log.e("showlocation", "connecttodb");
		returnedJSON = null;
		ConnectionToServer connection = new ConnectionToServer();
		connection.execute();

		while(returnedJSON == null){ //if json object not returned, sleep for 30 sec
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		
		Log.e("showlocation", "finish connecttodb");

		parseQueue(returnedJSON);
		
		//Här får man på ta list visa vilka som är inloggade, antigen direct i showlocation eller i en lista (ny intent)

	}


	public void onClick(View v) {

		if(v == queueButton){
			Log.e("showlocation", "onclick");
			getQueue();
		}
	}

	/**
	 * @author Kya
	 *Created a new class to connect the program to the external database with the restaurant table and their id:s
	 *Not yet completed.
	 */

	private class ConnectionToServer extends AsyncTask<Void, Void, JSONObject> {


		@Override
		protected JSONObject doInBackground(Void... params) {
			Log.e("showlocation", "doinbackground");
			StringBuilder urlString = new StringBuilder();
			StringBuilder response = new StringBuilder();
			InputStream is = null;
			URL url = null;
			HttpURLConnection urlConnection = null;
			String line = null;
			String jsonResponse = "";

			Log.e("show location", "hej1");
			urlString.append("http://schmaps.scarleo.se/rest.php?&insert=1&code=79");
			
//			Här kan man ska en int enter som är true ifall man går in i området och false och man går ut
//			Detta för att kunna skicka relevant info till databsen så att antalet van justeras
//			int code visar vilken restaurang det gäller, det var man ska justera antalet	
//			urlString.append("http://schmaps.scarleo.se/rest.php?");			
//			if(enter){ 
//				urlString.append("&insert=1");
//			}else{
//				urlString.append("&delete=1");
//			}
//			urlString.append("&code=");
//			urlString.append(code);

			try {
				url = new URL(urlString.toString());
				urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setRequestMethod("GET");
				urlConnection.setDoOutput(true);
				urlConnection.setDoInput(true);
				is = urlConnection.getInputStream();
				urlConnection.connect();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			

			//read from the buffer line by line and save in response (a stringbuider)
			try{
				InputStreamReader inputStream = new InputStreamReader(is);
				BufferedReader reader = new BufferedReader(inputStream);
				while((line = reader.readLine()) != null){
					response.append(line);
				}
				//Close the reader, stream & connection
				reader.close();
				inputStream.close();
				urlConnection.disconnect();
			}catch(Exception e) {
				Log.e("Buffer Error", "Error converting result " + e.toString());
			}
			Log.e("show location", jsonResponse);

			
			jsonResponse = response.toString();
			Log.e("show location", jsonResponse);
			//convert string to jsonobject and return the object
			try{
				returnedJSON = new JSONObject(jsonResponse);
			}catch(JSONException e){

			}
			
			Log.e("showlocation", "finish doinbackground");

			return returnedJSON;
		}
	}


}
