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

import org.json.JSONException;
import org.json.JSONObject;

import com.chalmers.schmaps.GPSPoint.myLocationListener;
import com.google.android.maps.Overlay;

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
	private GetQueue getQueue;
	private JSONObject jsonobject, returnedJSON;
	private LocationManager manager;
	private int id;
	private StringBuilder builder;
	private Bundle bundle;
	private static final int thisid = 7;

	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		bundle= intent.getExtras();
		id = bundle.getInt("restaurant");
		String intentKey = LocationManager.KEY_PROXIMITY_ENTERING;	//Taking the KEY_PROXMITY_ENTERING from GPSPoint and saving the boolean
		Boolean entering = intent.getBooleanExtra(intentKey, false); //Saving the boolean from KEY_PROXIMITY_ENTERING for further use
		connectToDB();
		
		if(entering==true){			//If the boolean is true ---> A person i inside the alerted area
			if(id>=1&&id<=34){
				builder.append("http://schmaps.scarleo.se/rest.php?insert=1&code=" + id); //here you add one to your restaurant-field.
			}else{
				System.out.println("Error, not known id");		//Simple error msg
			}
			
		}else{					//If false --> person is leaving the alerted area
			if(id>=1&&id<=34){
			builder.append("http://schmaps.scarleo.se/rest.php?delete=1&code=" + id); //here you remove one from your restaurant-field on the external database
			}else{
				System.out.println("Error, not known id");
			}
		}
		Intent backtrack = new Intent("android.intent.action.GOOGLEMAPSHOWLOCATION");
		backtrack.putExtra("Show locations", thisid);
		PendingIntent pIntent = PendingIntent.getActivity(null, 0, backtrack, Intent.FLAG_ACTIVITY_NEW_TASK); //Send an intent back to GoogleMapShowLocation
		
	}

	private void parseJSON(JSONObject jsonObject){
		int i;
		
		
		
	}
	
	/*
	 * Checks how many are queueing by checking the database on the server and returning this in the screen as a dialouge.
	 */
	
	private void drawQueue(){
		
		// http://schmaps.scarleo.se/rest.php kollar incheckade
		
	}
	
	private void connectToDB(){
		
	}
	
	/**
	 * @author Kya
	 *Created a new class to connect the program to the external database with the restaurant table and their id:s
	 *Not yet completed.
	 */
	
	private class GetQueue extends AsyncTask<Void, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Void... params) {
			StringBuilder urlString = new StringBuilder();
			StringBuilder response = new StringBuilder();
			InputStream is = null;
			URL url = null;
			HttpURLConnection urlConnection = null;
			String line = null;
			String jsonResponse = "";
			

			urlString.append("http://schmaps.scarleo.se/schmaps.php?name=");
			
			
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

			//convert string to jsonobject and return the object
			try{
				Log.e("CheckIN", "hej");
				returnedJSON = new JSONObject(jsonResponse);
			}catch(JSONException e){

			}
			Log.e("CheckIN", "almost finished");
			return returnedJSON;
		}
	}





}
