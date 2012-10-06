package com.chalmers.schmaps;

import java.util.ArrayList;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

public class GPSPoint extends Activity {
	private SearchSQL db;
	private MapItemizedOverlay overlay;
	private LocationManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		assignInstances();
	}
	
	private void assignInstances() {
		manager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		LocationProvider provider = manager.getProvider(LocationManager.GPS_PROVIDER);
		LocationListener listener = new myLocationListener();
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);

	}
	
	private void setGPSPoints(String table){
		db.openRead();
		ArrayList<OverlayItem> locationList = db.getLocations(table);
		db.close();
		for(OverlayItem item : locationList)
		{
		//	manager.addProximityAlert(item.getLat(), item.getLong(), 5000.000, PendingIntent intent );

	

		}

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
