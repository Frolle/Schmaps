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
import org.json.JSONTokener;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.AlertDialog;
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

	private GeoPoint ourLocation;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_map); 
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);

		mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.dot); 
		Drawable drawable2 = this.getResources().getDrawable(R.drawable.tomte);
		overlay = new MapItemizedOverlay(drawable, this);
		overlay2 = new MapItemizedOverlay(drawable2, this);

		assignInstances();

		location_manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		criteria = new Criteria(); //deafult criteria
		bestProvider = location_manager.getBestProvider(criteria, false); //best reception
		location = location_manager.getLastKnownLocation(bestProvider);

		if(location != null){
			longitude = (int) (location.getLongitude()*1E6);
			latitude = (int) (location.getLatitude()*1E6);

			ourLocation = new GeoPoint(latitude, longitude);
			OverlayItem overlayitem = new OverlayItem(ourLocation, "Your position", "I'm in Mexico City!");
			overlay2.addOverlay(overlayitem);
			mapOverlays.add(overlay2);
		}



		location_listener = new LocationListener(){

			public void onLocationChanged(Location loc) { //metod som hämtar din position genom att anropa onResume		
				longitude = (int) (location.getLongitude()*1E6);
				latitude = (int) (location.getLatitude()*1E6);

				ourLocation = new GeoPoint(latitude, longitude);
				OverlayItem overlayitem = new OverlayItem(ourLocation, "Your position", "I'm in Mexico City!");
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
		// TODO Auto-generated method stub
		super.onPause();
		location_manager.removeUpdates(location_listener);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			// Register the listener with the Location Manager to receive
			// location updates
			//location_manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, location_listener);
			location_manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, location_listener);
		}
		catch (Exception e) {
			//print("Couldn't use the GPS: " + e + ", " + e.getMessage());
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
		search.openRead(); //öppnar databasen för läsafrån den
		if(search.exists(roomToFind)){
			GeoPoint gp = new GeoPoint(search.getLat(roomToFind),search.getLong(roomToFind)); //create a geopoint

			MapController mapcon = mapView.getController();
			mapcon.animateTo(gp);
			mapcon.setZoom(18); //zoom level
			OverlayItem over = new OverlayItem(gp, search.getAddress(roomToFind), search.getLevel(roomToFind)); //address and level is shown in the dialog

			search.close();
			overlay.removeOverlay();
			overlay.addOverlay(over);
			mapOverlays.add(overlay);
			mapView.postInvalidate();
		}else{
			Dialog dialog = new Dialog(GoogleMapActivity.this);
			dialog.setTitle("Sorry, can not find the room :(");
			dialog.setCancelable(true);
			dialog.show();

		}

		jsonObject = null;
		DownloadWebPageTask task = new DownloadWebPageTask();
		task.execute(new String[] {});
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


			int a = steps.length();

			Integer i = a;
			String s = i.toString();
			Log.e(TAG, s);
			
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
				destLat = (int)(end_location.getDouble("lat"));
				destLng = (int)(end_location.getDouble("lng"));
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

	}

	private class DownloadWebPageTask extends AsyncTask<String, Void, JSONObject> {
		//http://www.vogella.com/articles/AndroidPerformance/article.html

		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			StringBuilder urlString = new StringBuilder();

			urlString.append("http://maps.googleapis.com/maps/api/directions/json?origin=57.715431,11.999704&destination=57.714222,11.996904&sensor=false&avoid=highways&mode=walking");

			/*
			urlString.append("http://maps.google.com/maps?f=d&hl=en");
			urlString.append("&saddr=");
			urlString.append(Double.toString((double) src.getLatitudeE6() / 1.0E6));
			urlString.append(",");
			urlString.append(Double.toString((double) src.getLongitudeE6() / 1.0E6));
			urlString.append("&daddr=");// to
			urlString.append(Double.toString((double) dest.getLatitudeE6() / 1.0E6));
			urlString.append(",");
			urlString.append(Double.toString((double) dest.getLongitudeE6() / 1.0E6));
			urlString.append("&ie=UTF8&0&om=0&output=kml"); 
			 */
			InputStream is = null;
			URL url = null;
			HttpURLConnection urlConnection = null;

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
			BufferedReader r = new BufferedReader(inputStream);
			String line = null;
			StringBuilder response = new StringBuilder();
			String jsonResponse = "";

			Log.e(TAG, "och nu buffer");

			try{
				while((line = r.readLine()) != null){
					response.append(line);
				}
				//Close the reader, stream & connection
				r.close();
				inputStream.close();
				urlConnection.disconnect();
				jsonResponse = response.toString();
			}catch(Exception e) {
				Log.e("Buffer Error", "Error converting result " + e.toString());
			}

			Log.e(TAG, jsonResponse);

			Log.e(TAG, "och nu json");

			try{
				Log.e(TAG, "hej");
				jsonObject = new JSONObject(jsonResponse);
				Log.e(TAG, "hej2");
			}catch(JSONException e){

			}
			Log.e(TAG, "klar");
			return jsonObject;

		}

	}

}
