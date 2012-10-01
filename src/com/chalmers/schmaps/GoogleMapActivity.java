package com.chalmers.schmaps;


import java.util.ArrayList;
import java.util.List;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GoogleMapActivity extends MapActivity implements View.OnClickListener {
	private static final int MICROWAVEBUTTON = 1;
	private static final int RESTAURANTBUTTON = 2;
	private static final int ATMBUTTON = 3;
	private static final int LECTUREHALLBUTTON = 4;
	private static final int BOOKINGKEY = 5;
	private static final int BUSKEY = 6;
	private static final int JOHANNESBERG = 40;
	private static final int LINDHOLMEN = 42;
	

	private static final String DATABASE_NAME = "SchmapsDB"; //namnet på vår databas
	private static final String DATABASE_TABLE = "Salar"; //namnet på vår tabell (kan ha flera tabeller)
	private static final String DB_MICROWAVETABLE = "Microwaves"; //Name of our microwave table
	
	private static String TAG = "GoogleMapActivity";
	
	private Button editButton;
    private EditText lectureEdit;
	
    private MapController mapcon;
	private LocationManager location_manager;
	private LocationListener location_listener;
	private List<Overlay> mapOverlays;
	private MapItemizedOverlay overlay;
	private String roomToFind;
	private MapView mapView;
	private SearchSQL search;
	private GeoPoint johannesbergLoc;
	private GeoPoint lindholmenLoc;
	@Override
	/**
	 * Method for determining on create how the mapview will be shown and assign the instances accordingly.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		Bundle setView = getIntent().getExtras();
		//First if-check to see what mapview should be drawn and assign variables accordingly.
		if(setView.getBoolean("Show searchfield")){
			setContentView(R.layout.activity_map); 
			assignInstances();
	        editButton = (Button) findViewById(R.id.edittextbutton);
	        lectureEdit = (EditText) findViewById(R.id.edittextlecture);
	        editButton.setOnClickListener(this);
		}		
		
	        else {
    			setContentView(R.layout.activity_strippedmap);
	    		assignInstances();
    			//Second if-check to see if it's Lindholmen or Johannesberg campus
	        	if(setView.getInt("Campus")==JOHANNESBERG)
	        		mapcon.animateTo(johannesbergLoc);
	        	else 
	        		mapcon.animateTo(lindholmenLoc);
	        	//Switch case to determine what series of locations to be drawn on map
	        	switch(setView.getInt("Show locations")){
	        	
	        	case MICROWAVEBUTTON:
	        		drawLocationList(DB_MICROWAVETABLE);
	        		break;
	        	}

	        }
		}

		
//		switch(setView.getInt("Show searchfield")){
//		
//		case LECTUREHALLBUTTON:
//			setContentView(R.layout.activity_map); 
//			assignInstances();
//	        editButton = (Button) findViewById(R.id.edittextbutton);
//	        lectureEdit = (EditText) findViewById(R.id.edittextlecture);
//	        editButton.setOnClickListener(this);
//	        break;
//		
//		case MICROWAVEBUTTON:
//			setContentView(R.layout.activity_strippedmap);
//			assignInstances();
//			drawLocationList(DB_MICROWAVETABLE);
//			break;
//		}
//		
//
//	}

/**
 * Draws locations (overlayitems) from specified table
 * @param table
 */
	private void drawLocationList(String table) {
		search.openRead();
		ArrayList<OverlayItem> locationList = search.getLocations(table);
		search.close();
		overlay.removeOverlay();
		for(OverlayItem item : locationList)
		{
			overlay.addOverlay(item);
			mapOverlays.add(overlay);

		}
		mapView.postInvalidate();
		
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
		finish();
		location_manager.removeUpdates(location_listener);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			// Register the listener with the Location Manager to receive
			// location updates
			location_manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 100, location_listener);
			location_manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 100, location_listener);
		}
		catch (Exception e) {
			//print("Couldn't use the GPS: " + e + ", " + e.getMessage());
		}
	}
	
    private void assignInstances() {

		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(false);
		mapcon = mapView.getController();
		mapOverlays = mapView.getOverlays();
		lindholmenLoc = new GeoPoint(57706434, 11937214);
		johannesbergLoc = new GeoPoint(57688678, 11977136);
		Drawable drawable = this.getResources().getDrawable(R.drawable.dot); 
		overlay = new MapItemizedOverlay(drawable, this);
		
		location_manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		location_listener = new LocationListener(){

			public void onLocationChanged(Location location) { //metod som hämtar din position genom att anropa onResume

				/*
				int longitude = (int) (location.getLongitude() * 1E6);
				int latitude = (int) (location.getLatitude() * 1E6);

				GeoPoint point = new GeoPoint(latitude, longitude);
				OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
				overlay.addOverlay(overlayitem);
				mapOverlays.add(overlay);
				 */

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

    /**
     * onClick method that takes the input from the user and queries the database with the input and draws
     * and animate to the location if found.
     */
		public void onClick(View v) {
		roomToFind = lectureEdit.getText().toString();
		roomToFind.toLowerCase().trim(); //removes white signs and converts to lower case
		roomToFind = roomToFind.replaceAll("[^a-zA-Z0-9]+",""); //Removes illegal characters to prevent sql injection
		search.openRead(); //öppnar databasen för läsafrån den
		 if(search.exists(roomToFind)){
			 GeoPoint gp = new GeoPoint(search.getLat(roomToFind),search.getLong(roomToFind)); //create a geopoint
			 mapcon.animateTo(gp);
		     mapcon.setZoom(18); //zoom level
			 OverlayItem over = new OverlayItem(gp, search.getAddress(roomToFind), search.getLevel(roomToFind)); //address and level is shown in the dialog
			 
			 search.close();
			 overlay.removeOverlay();
			 overlay.addOverlay(over);
			 mapOverlays.add(overlay);
			 mapView.postInvalidate();
			 }
		}
}
