package com.chalmers.schmaps;

import java.util.ArrayList;

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
	private SearchSQL db;
	private LocationManager manager;
	private PendingIntent intent;

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
	
	public void setGPSPoints(){
		Intent intDB = new Intent(this, SendToDB.class);
		intent.getActivity(this, 0, intDB, Intent.FLAG_ACTIVITY_NEW_TASK);
		manager.addProximityAlert(57688771, 11979179, 50, -1, intent);		

	}
	
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


	

}
