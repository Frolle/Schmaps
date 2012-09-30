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
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class GoogleMapActivity extends MapActivity implements View.OnClickListener{

	private static String TAG = "GoogleMapActivity";

	private Button editButton;
	private EditText lectureEdit;

	private GeoPoint ourLocation, roomLocation;
	private LocationManager location_manager;
	private LocationListener location_listener;
	private List<Overlay> mapOverlays;
	private MapItemizedOverlay overlay, overlay2;
	private String roomToFind;
	private MapView mapView;
	private SearchSQL search;
	private Criteria criteria;
	private String bestProvider;
	private Location location;
	private int longitude;
	private int latitude;
	private JSONObject jsonObject;
	private ArrayList<GeoPoint> getDir = new ArrayList<GeoPoint>();
	private OverlayItem overlayitemStudent, overlayItemRoom;
	private Drawable room, student;
	private MapController mapcon;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_map); 
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);

		mapOverlays = mapView.getOverlays();
		room = this.getResources().getDrawable(R.drawable.dot); 
		student = this.getResources().getDrawable(R.drawable.tomte);
		overlay = new MapItemizedOverlay(room, this);
		overlay2 = new MapItemizedOverlay(student, this);

		assignInstances();

		location_manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		criteria = new Criteria(); //deafult criteria
		bestProvider = location_manager.getBestProvider(criteria, false); //best reception
		location = location_manager.getLastKnownLocation(bestProvider);

		if(location != null){
			longitude = (int) (location.getLongitude()*1E6);
			latitude = (int) (location.getLatitude()*1E6);
			
			ourLocation = new GeoPoint(latitude, longitude);
			getDir.add(0,ourLocation);
			mapcon = mapView.getController();
			mapcon.animateTo(ourLocation);
			mapcon.setZoom(18); //zoom level
			overlayitemStudent = new OverlayItem(ourLocation, "Your position", "I'm in Mexico City!");
			overlay2.addOverlay(overlayitemStudent);
			mapOverlays.add(overlay2);
		}



		location_listener = new LocationListener(){
			public void onLocationChanged(Location loc) { //gets your curent position by calling onResume		
				longitude = (int) (location.getLongitude()*1E6);
				latitude = (int) (location.getLatitude()*1E6);

				ourLocation = new GeoPoint(latitude, longitude);
				getDir.add(0,ourLocation);
				mapcon = mapView.getController();
				mapcon.animateTo(ourLocation);
				mapcon.setZoom(18); //zoom level
				
				OverlayItem overlayitem = new OverlayItem(ourLocation, "Hey amigo", "This is your position!");
				overlay2.addOverlay(overlayitem);
				mapOverlays.add(overlay2);
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

		search = new SearchSQL(GoogleMapActivity.this);
		search.createDatabase();

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
		super.onPause();
		location_manager.removeUpdates(location_listener);
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			// Register the listener with the Location Manager to receive
			// location updates
			//location_manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, location_listener);
			location_manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, location_listener);
		}
		catch (Exception e) {
		}
	}

	private void assignInstances() {
		editButton = (Button) findViewById(R.id.edittextbutton);
		lectureEdit = (EditText) findViewById(R.id.edittextlecture);
		editButton.setOnClickListener(this);
	}

	public void onClick(View v) {
		roomToFind = lectureEdit.getText().toString();
		roomToFind.toLowerCase().trim(); //removes white signs and converts to lower case
		roomToFind = roomToFind.replaceAll("[^a-öA-Ö0-9]+",""); //Removes illegal characters to prevent sql injection
		search.openRead(); //open database in read mode
		if(search.exists(roomToFind)){
			roomLocation = new GeoPoint(search.getLat(roomToFind),search.getLong(roomToFind)); //create a geopoint
			getDir.add(1,roomLocation);
			mapcon = mapView.getController();
			mapcon.animateTo(roomLocation);
			mapcon.setZoom(18); //zoom level
			overlayItemRoom = new OverlayItem(roomLocation, search.getAddress(roomToFind), search.getLevel(roomToFind)); //address and level is shown in the dialog

			search.close();
			overlay.removeOverlay();
			overlay.addOverlay(overlayItemRoom);
			mapOverlays.add(overlay);
			mapView.postInvalidate();
		}else{
			Dialog dialog = new Dialog(GoogleMapActivity.this);
			dialog.setTitle("Sorry, can not find the room :(");
			dialog.setCancelable(true);
			dialog.show();

		}
		/*
		jsonObject = null;
		
		
		
		
		GetDirections directions = new GetDirections();
		directions.execute();
		Log.e(TAG, "ute");

		while(jsonObject == null){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}


		


		try {
			JSONArray routes = jsonObject.getJSONArray("routes");
			JSONObject route = routes.getJSONObject(0);
			// Take all legs from the route
			JSONArray legs = route.getJSONArray("legs");
			// Grab first leg
			JSONObject leg = legs.getJSONObject(0);

			JSONArray steps = leg.getJSONArray("steps");

			JSONObject step,start_location,end_location;

			int srcLat,srcLng,destLat,destLng;

			GeoPoint geo;
			ArrayList<GeoPoint> geoList = new ArrayList<GeoPoint>();
			
			for(int count = 1;count<steps.length();count++){
				step = steps.getJSONObject(count);
				if(count == 1){
					start_location = step.getJSONObject("start_location");
					srcLat = (int)(start_location.getDouble("lat")*1E6);
					srcLng = (int)(start_location.getDouble("lng")*1E6);
					geo = new GeoPoint(srcLat,srcLng);
					geoList.add(0, geo);
				}
				end_location = step.getJSONObject("end_location");
				destLat = (int)(end_location.getDouble("lat")*1E6);
				destLng = (int)(end_location.getDouble("lng")*1E6);
				geo = new GeoPoint(destLat,destLng);
				geoList.add(count, geo);
			}
			PathOverlay pathOverlay = new PathOverlay(geoList);
			Canvas canvas = new Canvas();
			
			mapOverlays.add(pathOverlay);
			pathOverlay.draw(canvas, mapView, false);
				

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	*/
	}

	
	private class GetDirections extends AsyncTask<Void, Void, JSONObject> {
		/** this class creates a new thread 
		 * 	inspired by
		 *  http://www.vogella.com/articles/AndroidPerformance/article.html
		 */
		

		@Override
		protected JSONObject doInBackground(Void... params) {
			/** when called makes a request to google directions api (json format) 
			 *  gets the response back
			 *  convertes the response to a jsonobject
			 */
			StringBuilder urlString = new StringBuilder();
			StringBuilder response = new StringBuilder();
			InputStream is = null;
			URL url = null;
			HttpURLConnection urlConnection = null;
			String line = null;
			String jsonResponse = "";
			
			//Create a string with the right start and end possition
			urlString.append("http://maps.googleapis.com/maps/api/directions/json?origin=");
			urlString.append(Double.toString((double) getDir.get(0).getLatitudeE6() / 1.0E6)); //from, your position, latitude
			urlString.append(",");
			urlString.append(Double.toString((double) getDir.get(0).getLongitudeE6() / 1.0E6));//longitude
			urlString.append("&destination=");// to, where you are goint
			urlString.append(Double.toString((double) getDir.get(1).getLatitudeE6() / 1.0E6)); //latitude
			urlString.append(",");
			urlString.append(Double.toString((double) getDir.get(1).getLongitudeE6() / 1.0E6)); //ongitude
			urlString.append("&sensor=false&avoid=highways&mode=walking"); //we want the walking directions
			
			
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
			Log.e(TAG, jsonResponse);
			
			//convert string to jsonobject
			try{
				jsonObject = new JSONObject(jsonResponse);
			}catch(JSONException e){

			}
			return jsonObject;
		}
	}
}
