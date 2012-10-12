package com.chalmers.schmaps;

import com.chalmers.schmaps.GPSPoint.myLocationListener;
import com.google.android.maps.Overlay;

import android.app.Activity;
import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

public class SendToDB extends Activity {
	private CheckInActivity sendToDB;
	private Overlay overlay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		assignInstances();
	}
	
	private void assignInstances() {
		sendToDB = new CheckInActivity();

	}

}
