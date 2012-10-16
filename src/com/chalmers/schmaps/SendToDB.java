/*
 * Copyright [2012] [Emma Dirnberger]

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

import com.chalmers.schmaps.GPSPoint.myLocationListener;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

/*
 * Author: [Emma Dirnberger]
 * Class which handles the request from the GPSPoint class, handling the information received and
 * by doing so, showing the user how many person that are in the alerted areas.
 */

public class SendToDB extends BroadcastReceiver {


	private JSONObject returnedJsonObject, returnedJSON;
	private LocationManager manager;
	private int id;
	private StringBuilder builder;
	private Bundle bundle;
	private static final int thisid = 7;
	private Boolean entering;


	
	@Override
	public void onReceive(Context context, Intent intent) {
		bundle= intent.getExtras();
		id = bundle.getInt("restaurant");
		String intentKey = LocationManager.KEY_PROXIMITY_ENTERING;	//Taking the KEY_PROXMITY_ENTERING from GPSPoint and saving the boolean
		entering = intent.getBooleanExtra(intentKey, false); //Saving the boolean from KEY_PROXIMITY_ENTERING for further use
		connectToDB();
		

		Intent backtrack = new Intent("android.intent.action.GOOGLEMAPSHOWLOCATION");
		backtrack.putExtra("Show locations", thisid);
		PendingIntent pIntent = PendingIntent.getActivity(null, 0, backtrack, Intent.FLAG_ACTIVITY_NEW_TASK); //Send an intent back to GoogleMapShowLocation

		
	}

<<<<<<< HEAD
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}			
=======
	
	public void parseQueue(JSONObject jsonObject){

>>>>>>> 4ec93748dcc4d172a50059a5976fcf3c5d0f2a56
	}
	
	/**
	 * Connecting this class to the database on the server.
	 */
	private void connectToDB(){
		returnedJsonObject = null;
		GetQueue getQueue = new GetQueue();
		getQueue.execute();
		
		while(returnedJsonObject == null){ //if json object not returned, sleep for 30 sec
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		
		parseQueue(returnedJsonObject);	
	}
	



/**
 * @author Kya
 *Created a new class to connect the program to the external database with the restaurant table and their id:s
 *Not yet completed.
 */

	class GetQueue extends AsyncTask<Void, Void, JSONObject> {
		

	@Override
	protected JSONObject doInBackground(Void... params) {
		StringBuilder urlString = new StringBuilder();
		StringBuilder response = new StringBuilder();
		InputStream is = null;
		URL url = null;
		HttpURLConnection urlConnection = null;
		String line = null;
		String jsonResponse = "";
		

		urlString.append("http://schmaps.scarleo.se/rest.php?");
		if(entering){
			urlString.append("&insert=1");
		}else{
			urlString.append("&delete=1");
		}
		urlString.append("&code=");
		urlString.append(id);
		
		
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

		//convert string to jsonobject and return the object
		try{
			returnedJSON = new JSONObject(jsonResponse);
		}catch(JSONException e){

		}

		return returnedJSON;
	}
	}
}



