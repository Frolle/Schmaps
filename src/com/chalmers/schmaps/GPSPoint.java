package com.chalmers.schmaps;

import java.util.ArrayList;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

public class GPSPoint extends Activity {	
	private SearchSQL search;
	private LocationManager manager;
	private PendingIntent intent;
	private static final String DB_RESTTABLE = "Restaurants";

    private List<Integer> restaurants = new ArrayList<Integer>();

	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		assignInstances();
	}
	
	private void assignInstances() {
		manager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		LocationProvider provider = manager.getProvider(LocationManager.NETWORK_PROVIDER);
		LocationListener listener = new myLocationListener();
		manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);

	}
/**
 * läser in alla gps punkter i restaurant och kollar en float radius på dom och jämför med nån lista och 
 */
	public void setGPSPoints(){
		Intent intDB = new Intent(this, SendToDB.class);
		intent.getActivity(this, 0, intDB, Intent.FLAG_ACTIVITY_NEW_TASK);
		
		//Sets a proximity alert for the location given by the position (latitude, longitude) and the given radius.
		//(double latitude, double longitude, float radius, long expiration, PendingIntent intent)
	
		manager.addProximityAlert(57800628, 12031362, 10, -1, intent);	
		}

	}
	
	/*
	private void getTab(){
		search.openRead();
		search.getLat(DB_RESTTABLE);
		search.getLong(DB_RESTTABLE);
	
	}
	*/
	
	class myLocationListener implements LocationListener{
  
		public void onLocationChanged(Location location) {
			if(location != null){
				double longitude = location.getLongitude();
				double latitude = location.getLatitude();
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

