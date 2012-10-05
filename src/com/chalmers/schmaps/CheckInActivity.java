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

package com.chalmers.schmaps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import com.google.android.maps.OverlayItem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



public class CheckInActivity extends MapActivity implements View.OnClickListener{

	private GeoPoint ourLocation;
	private LocationManager location_manager;
	private LocationListener location_listener;
	private JSONObject returnedJsonObject;

	private Criteria criteria;
	private String bestProvider;
	private Location location;
	private int longitude;
	private int latitude;
	private String username;
	private boolean checkin;
	private MapController mapcon;
	private MapView mapview;
	private Button checkInButton;
	private EditText enterName;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkin);
		returnedJsonObject = null;
		username = "";
		checkin = false;
		
		mapview = (MapView) findViewById(R.id.mapview);
		mapview.setBuiltInZoomControls(true);
		mapcon = mapview.getController(); 

		
		checkInButton = (Button) findViewById(R.id.checkinbutton);
		enterName = (EditText) findViewById(R.id.entername);
		checkInButton.setOnClickListener(this);
		
		location_manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		criteria = new Criteria(); //deafult criteria
		bestProvider = location_manager.getBestProvider(criteria, false); //best reception
		location = location_manager.getLastKnownLocation(bestProvider); //gets last known location from chosen provider
		
		if(location != null){ //if there is an provider that provides an location ->continue
			latitude = (int) (location.getLatitude()*1E6); //get the latitude
			longitude = (int) (location.getLongitude()*1E6); //get the longitude
			ourLocation = new GeoPoint(latitude, longitude); //greates an geopoint with our location
			
			mapcon.animateTo(ourLocation);
			mapcon.setZoom(17); //zoom level
			
		}

		location_listener = new LocationListener(){
			/**
			 * method is called when location is changed
			 */
			public void onLocationChanged(Location loc) { 
				latitude = (int) (location.getLatitude()*1E6); //get the latitude
				longitude = (int) (location.getLongitude()*1E6); //get the longitude

				ourLocation = new GeoPoint(latitude, longitude); //greates an geopoint with our location

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

		connectExternalDatabase();
		
		
		//overlayitemStudent = new OverlayItem(ourLocation,"" , ""); if we want name to be shown
	}
	
	private void connectExternalDatabase(){
		returnedJsonObject = null;
		GetCheckIn getCheckIn = new GetCheckIn();
		getCheckIn.execute(); //the method doInBackground() is executed

		while(returnedJsonObject == null){ //if json object not returned, sleep for 30 sec
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}	
		
		ArrayList <GeoPoint> listGeoPoint = parseJson(returnedJsonObject);
		Log.e("heckIN", "parsat");
		
	}
	
	/**
	 * Method parses through in parameter jsonObject and creates an array of geopoints
	 * each geopoint representing an checked-in person
	 * @param jsonObject
	 * @return arraylist of geopoints
	 */
	private ArrayList<GeoPoint> parseJson(JSONObject jsonObject){
		ArrayList<GeoPoint> returnedGeoPoints = new ArrayList<GeoPoint>();
		GeoPoint geopoint;
		int lat, lng;
		String name;
		try {
			JSONArray result = jsonObject.getJSONArray("result");
			JSONObject checkedInPerson;

			//loop through the array and extract all checked-in points
			//create an geopoint and and to the array of geopoints
			for(int count = 0;count<result.length();count++){
				checkedInPerson = result.getJSONObject(count);
				name = checkedInPerson.getString("name");
				lat = (int)checkedInPerson.getInt("lat");
				lng = (int)checkedInPerson.getInt("lng");
				geopoint = new GeoPoint(lat,lng);
				returnedGeoPoints.add(count, geopoint);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return returnedGeoPoints;
	}

	/**  When user enters her/his name and presses check-in the information is sent to an external server
	 *   Returened is a jsonobject with all postions where people have checked in the last hour
	 *   Start new activity that will show the information on a map
	 */
	public void onClick(View v) {
		checkin = true;

		username = enterName.getText().toString();
		username.trim(); //removes white signs
		username = username.replaceAll("[^a-öA-Ö0-9]+",""); //Removes illegal characters to prevent sql injection

		//if the user have not entered a name the name is set to unknown
		if(username.equals("")){
			username = "Unknown";
		}
		
		connectExternalDatabase();
		
		checkin =false;

	}


	/** this innerclass creates a new thread from where we can make a request
	 *  to google directions api - to get the directions
	 * 	inspired by
	 *  http://www.vogella.com/articles/AndroidPerformance/article.html
	 */
	private class GetCheckIn extends AsyncTask<Void, Void, JSONObject> {


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
			urlString.append("http://schmaps.scarleo.se/schmaps.php?name=");
			urlString.append(username); //from, your position, latitude
			urlString.append("&lat=");// to, where you are going
			urlString.append(Integer.toString((int) latitude)); //latitude
			urlString.append("&lng=");
			urlString.append(Integer.toString((int) longitude)); //longitude
			if(checkin){
				urlString.append("&insert=1");
			}
			

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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Log.e("CheckIN", "url done"); //print out jsonresponse
			InputStreamReader inputStream = new InputStreamReader(is);
			BufferedReader reader = new BufferedReader(inputStream);


			//read from the buffer line by line and save in response (a stringbuider)
			try{
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
			Log.e("CheckIN", jsonResponse); //print out jsonresponse

			//convert string to jsonobject and return the object
			try{
				Log.e("CheckIN", "hej");
				returnedJsonObject = new JSONObject(jsonResponse);
			}catch(JSONException e){

			}
			Log.e("CheckIN", "almost finished");
			return returnedJsonObject;
		}
	}


	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
