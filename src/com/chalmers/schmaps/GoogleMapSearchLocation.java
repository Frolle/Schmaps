/*
 * Copyright [2012] [Dina Zuko, Simon Fransson]

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

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/*****************************************************
 * Class displays a google maps activity with a textfield and search button where user can search for classrooms.
 * Displays users position, rooms position if found and get directions from google directions api if user wants directions.
 *********************************************************/
public class GoogleMapSearchLocation extends MapActivity implements View.OnClickListener {

	private static final double CONVERTTOGEOPOINTVALUE = 1E6;
	private static final int OVERVIEWZOOMVALUE = 18;
	private static final long UPDATEFREQUENCYINMS = 1000;
	private static final float UPDATEAAREA = 10;
	private static final long SLEEPTIMEINMS = 500;
	private Button editButton, directionsButton;
	private EditText lectureEdit;

	private GeoPoint ourLocation, roomLocation;
	private LocationManager location_manager;
	private LocationListener location_listener;
	private List<Overlay> mapOverlays;
	private MapItemizedOverlay mapItemizedRoom, mapItemizedStudent;
	private String roomToFind;
	private MapView mapView;
	private SearchSQL search;
	private Criteria criteria;
	private String bestProvider;
	private Location location;
	private int longitude;
	private int latitude;
	private JSONObject jsonObject;

	private OverlayItem overlayitemStudent, overlayItemRoom;
	private Drawable room, student;
	private MapController mapcon;
	private PathOverlay pathOverlay;
	private boolean roomSearched;
	private Dialog dialog;
	private List<GeoPoint> geoList;

	private boolean running;

	/**
	 * onCreate method for determining what the activity does on creation.
	 * Sets the right view for the user and assigns fields used by the activity.
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//initiate the variables used in this class
		assignInstances(); 
		//if there is an provider that provides an location ->continue
		if(location != null){ 
			//get the latitude
			latitude = (int) (location.getLatitude()*CONVERTTOGEOPOINTVALUE); 
			//get the longitude
			longitude = (int) (location.getLongitude()*CONVERTTOGEOPOINTVALUE); 
			//greates an geopoint with our location
			ourLocation = new GeoPoint(latitude, longitude); 

			mapcon = mapView.getController(); 
			mapcon.animateTo(ourLocation);
			//zoom level
			mapcon.setZoom(OVERVIEWZOOMVALUE); 

			//creates a MapItemizedOverlay-object and adds it to the list mapOverlays
			overlayitemStudent = new OverlayItem(ourLocation, "Hey amigo", "This is your position!");
			mapItemizedStudent.addOverlay(overlayitemStudent);
			mapOverlays.add(mapItemizedStudent);
		}

		location_listener = new LocationListener(){
			/**
			 * method is called when location is changed
			 */
			public void onLocationChanged(Location loc) { 
				//get the latitude
				latitude = (int) (location.getLatitude()*CONVERTTOGEOPOINTVALUE); 
				//get the longitude
				longitude = (int) (location.getLongitude()*CONVERTTOGEOPOINTVALUE); 
				//greates an geopoint with our location
				ourLocation = new GeoPoint(latitude, longitude); 

				//creates a MapItemizedOverlay-object and adds it to the list mapOverlays
				overlayitemStudent = new OverlayItem(ourLocation, "Hey amigo", "This is your position!");
				mapItemizedStudent.addOverlay(overlayitemStudent);
				mapOverlays.add(mapItemizedStudent);
			}

			public void onProviderDisabled(String arg0) {

			}

			public void onProviderEnabled(String provider) {

			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {

			}

		};
		//creates a SQLLite object
		search = new SearchSQL(GoogleMapSearchLocation.this); 
		//creates an database
		search.createDatabase(); 
	}

	/**
	 * A simple check to see if a route is currently displayed
	 * @return - boolean says if route displayed or not
	 */
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	/**
	 * Method to define what the activity does on pause. Removes updates from the
	 * location manager
	 */
	@Override
	protected void onPause() {
		super.onPause();
		location_manager.removeUpdates(location_listener);
	}

	/**
	 * Method to define what the activity does on resume.
	 * Updates the coordinates of the current position.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		try {
			location_manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, UPDATEFREQUENCYINMS, UPDATEAAREA, location_listener);
		}
		catch (Exception e) {
		}
	}

	/**
	 *  method is called from onCreate() and it initiates the variables
	 *  used in GoogleMapSearchLocation
	 */
	private void assignInstances() {
		setContentView(R.layout.activity_map); 
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);

		mapOverlays = mapView.getOverlays();
		room = this.getResources().getDrawable(R.drawable.dot); 
		student = this.getResources().getDrawable(R.drawable.chalmersandroid);
		mapItemizedRoom = new MapItemizedOverlay(room, this);
		mapItemizedStudent = new MapItemizedOverlay(student, this);

		editButton = (Button) findViewById(R.id.edittextbutton);
		directionsButton = (Button) findViewById(R.id.directionbutton);
		lectureEdit = (EditText) findViewById(R.id.edittextlecture);
		editButton.setOnClickListener(this);
		directionsButton.setOnClickListener(this);

		location_manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		//deafult criteria
		criteria = new Criteria(); 
		//best reception
		bestProvider = location_manager.getBestProvider(criteria, false); 
		//gets last known location from chosen provider
		location = location_manager.getLastKnownLocation(bestProvider); 

		roomSearched = false;
		running = false;

	}


	/**
	 * If the enter button is clicked a room search is done
	 * If the get directions button is pressed you get the path drawn on map
	 * but you have to search for a room first
	 */
	public void onClick(View v) {
		switch(v.getId()){

		case R.id.edittextbutton:
			//Removes the key when finish typing
			InputMethodManager imm = (InputMethodManager)getSystemService(
					Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(lectureEdit.getWindowToken(), 0);
			//removes the path drawn if there is one
			mapOverlays.remove(pathOverlay);
			//removes the dot that point to a previous room found
			mapOverlays.remove(mapItemizedRoom);
			roomToFind = lectureEdit.getText().toString();
			//removes white signs and converts to lower case
			roomToFind.toLowerCase().trim(); 
			//Removes illegal characters to prevent sql injection
			roomToFind = roomToFind.replaceAll("[^[a-zåäö][A-ZÅÄÖ][0-9]]",""); 
			//open database in read mode
			search.openRead(); 
			//if we find room show room on map, if not show dialog 
			if(search.exists(roomToFind)){
				//create a geopoint
				roomLocation = new GeoPoint(search.getLat(roomToFind),search.getLong(roomToFind)); 
				mapcon = mapView.getController();
				mapcon.animateTo(roomLocation);
				//zoom level
				mapcon.setZoom(OVERVIEWZOOMVALUE); 
				//address and level is shown in the dialog
				overlayItemRoom = new OverlayItem(roomLocation, search.getAddress(roomToFind), 
						search.getLevel(roomToFind)); 
				mapItemizedRoom.removeOverlay();
				mapItemizedRoom.addOverlay(overlayItemRoom);
				mapOverlays.add(mapItemizedRoom);
				mapView.postInvalidate();
				//now someone has searched for a room, set the boolean to true
				roomSearched = true; 
			}else{
				//dilaog pops up if room not found
				dialog = new Dialog(GoogleMapSearchLocation.this);
				dialog.setTitle("Sorry, can not find the room :(");
				dialog.setCanceledOnTouchOutside(true);
				dialog.show();
			}
			//close database
			search.close(); 
			break;

		case R.id.directionbutton:
			Log.e("roomsearched", "in");
			//if there there is roomLocation then search for a path
			//if not a roomLocation then the user has not searched for a room, do not give directions
			if(gotInternetConnection()){ 
				Log.e("roomsearched", "inin");
				if(roomSearched){
					walkningDirections ();
					roomSearched = false;
				}else{
					Context context = getApplicationContext();
					Toast.makeText(context, "Search for a room first to get directions", Toast.LENGTH_LONG).show();	

				}
			}else
			{
				Context context = getApplicationContext();
				Toast.makeText(context, "Internet connection needed for this option", Toast.LENGTH_LONG).show();
			}

			break;

		}


	}

	/**
	 * @return the size of geoList with geopoints used to draw path
	 */
	public int returnNrOfGeopoints(){
		return geoList.size();
	}



	/***********************************************
	 * creates a new thread (from where we get the directions) and calls it
	 * waits for the new thread to return a json object
	 * when json object returned parse it and extract directions in parseJson()
	 ********************************************/
	public void walkningDirections (){

		jsonObject = null;

		GetDirections directions = new GetDirections();
		//the method doInBackground() in GetDirections is executed
		directions.execute(); 
		//if json object not returned, sleep for 0,5 sec
		while(jsonObject == null){ 
			try {
				Thread.sleep(SLEEPTIMEINMS);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}

		running = true;

		parseJson(jsonObject);
	}

	/**
	 * Parses json response from google directions api
	 * @param returnedJson
	 */
	public void parseJson(JSONObject returnedJson){

		JSONObject step,start_location,end_location;
		int srcLat,srcLng,destLat,destLng;
		GeoPoint geo;
		geoList = new ArrayList<GeoPoint>();

		try {
			JSONArray routes = returnedJson.getJSONArray("routes");
			JSONObject route = routes.getJSONObject(0);
			// Take all legs from the route
			JSONArray legs = route.getJSONArray("legs");
			// Grab first leg
			JSONObject leg = legs.getJSONObject(0);
			//Grab all the steps from the led
			JSONArray steps = leg.getJSONArray("steps");


			for(int count = 0;count<steps.length();count++){
				Log.e("Json", "b");
				//the json returns start and end for each step, we only want one geopoint of each position
				//that is why we only get the start once and then get the end
				// we add the geopoint to an array of geopoints
				if(count == 0){
					step = steps.getJSONObject(0);
					start_location = step.getJSONObject("start_location");
					srcLat = (int)(start_location.getDouble("lat")*CONVERTTOGEOPOINTVALUE);
					srcLng = (int)(start_location.getDouble("lng")*CONVERTTOGEOPOINTVALUE);
					geo = new GeoPoint(srcLat,srcLng);
					geoList.add(0, geo);
				}
				step = steps.getJSONObject(count);
				end_location = step.getJSONObject("end_location");
				destLat = (int)(end_location.getDouble("lat")*CONVERTTOGEOPOINTVALUE);
				destLng = (int)(end_location.getDouble("lng")*CONVERTTOGEOPOINTVALUE);
				geo = new GeoPoint(destLat,destLng);
				geoList.add(count+1, geo);
			}

			//creata an overlay and canvas so we can draw the path
			pathOverlay = new PathOverlay(geoList);
			Canvas canvas = new Canvas();

			//adds the overay to the list of overlays and calls the draw-method to dra it
			mapOverlays.add(pathOverlay);
			pathOverlay.draw(canvas, mapView, true);
			mapView.postInvalidate();



		} catch (JSONException e) {
			e.printStackTrace();
		}

	}


	/**
	 * @return true if the doinbackground() in asynktask has executed
	 */
	public boolean getIsAsyncTaskRunning(){
		return running;
	}



	/** this innerclass creates a new thread from where we can make a request
	 *  to google directions api - to get the directions
	 * 	inspired by
	 *  http://www.vogella.com/articles/AndroidPerformance/article.html
	 */
	private class GetDirections extends AsyncTask<Void, Void, JSONObject> {


		private static final double CONVERTTOJSONVALUE = 1.0E6;

		/** when called makes a request to google directions api (json format) 
		 *  gets the response back
		 *  convertes the response to a jsonobject
		 */
		@Override
		protected JSONObject doInBackground(Void... params) {
			StringBuilder urlString = new StringBuilder();
			StringBuilder response = new StringBuilder();
			InputStream is = null;
			URL url = null;
			HttpURLConnection urlConnection = null;
			String line = null;
			String jsonResponse = "";

			//Create a string with the right start and end position
			urlString.append("http://maps.googleapis.com/maps/api/directions/json?origin=");
			//from, your position, latitude
			urlString.append(Double.toString((double) ourLocation.getLatitudeE6() / CONVERTTOJSONVALUE)); 
			urlString.append(",");
			//longitude
			urlString.append(Double.toString((double) ourLocation.getLongitudeE6() / CONVERTTOJSONVALUE));
			// to, where you are going
			urlString.append("&destination=");
			//latitude
			urlString.append(Double.toString((double) roomLocation.getLatitudeE6() / CONVERTTOJSONVALUE)); 
			urlString.append(",");
			//longitude
			urlString.append(Double.toString((double) roomLocation.getLongitudeE6() / CONVERTTOJSONVALUE)); 
			//we want the walking directions
			urlString.append("&sensor=false&avoid=highways&mode=walking"); 


			//establish a connection with google directions api
			try {
				url = new URL(urlString.toString());
				urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setRequestMethod("GET");
				urlConnection.setDoOutput(true);
				urlConnection.setDoInput(true);
				is = urlConnection.getInputStream();
				urlConnection.connect();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
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

			jsonResponse = response.toString();

			//convert string to jsonobject and return the object
			try{
				jsonObject = new JSONObject(jsonResponse);
			}catch(JSONException e){

			}
			return jsonObject;
		}
	}

	/**
	 * Check if the device is connected to internet.
	 * Need three if-statements because getActiveNetworkInfo() may return null
	 * and end up with a force close. So thats the last thing to check.
	 * @return true if there is an internet connection
	 */
	public boolean gotInternetConnection()
	{
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null && wifiNetwork.isConnected()) {
			return true;
		}

		NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetwork != null && mobileNetwork.isConnected()) {
			return true;
		}

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			return true;
		}

		return false;
	}

}
