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

import com.google.android.maps.GeoPoint;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

/*
 * Authour [Emma Dirnberger]
 * This is a class which receives an intent and after receiving this, adding a proximity alert
 * on the desired area. Receives this request from the GoogleMapShowLocation class
 */

public class GPSPoint extends Activity {	
	private LocationManager manager;
	private PendingIntent intent;
	int distance = 20;
	private GeoPoint myLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		assignInstances();
	}
	
	private void assignInstances() {
		manager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		LocationProvider provider = manager.getProvider(LocationManager.NETWORK_PROVIDER);		//Using NETWORK_PROVIDER to save battery-time
		LocationListener listener = new myLocationListener();
		manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);

	}
	/*
	 * Add a proximity alert to the specified long and lat, adding an id for use in the database on our server.
	 */
	public void setGPSPoints(int lng, int lat, int id){
		Intent intDB = new Intent(this, SendToDB.class);	//new intent from this class to the SendToDB class
		intDB.putExtra("restaurant", id);
		PendingIntent.getActivity(this, 0, intDB, Intent.FLAG_ACTIVITY_NEW_TASK);	//creating a PendingIntent which will act like startActivity(intent) for the SendToDB class
		manager.addProximityAlert(lng, lat, distance, -1, intent);		//add a proximity alert on the specified longitude and latitude, on a radius of distance, without time-limit for the intent.

	}
	
	class myLocationListener implements LocationListener{

		public void onLocationChanged(Location location) {
			if(location != null){
				int longitude = (int)location.getLongitude();
				int latitude = (int)location.getLatitude();
				myLocation = new GeoPoint(latitude, longitude);
			}
			// TODO Auto-generated method stub
			
		}

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
		
	}


	

}
