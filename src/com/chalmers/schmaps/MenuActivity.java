package com.chalmers.schmaps;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends Activity implements View.OnClickListener {
	Button searchHall;
	Button groupRoom;
	Button findRestaurants;
	public static final int RESTAURANTS = 1 ;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        assignInstances();
    }

    private void assignInstances() {
        searchHall = (Button) findViewById(R.id.hButton);
        groupRoom = (Button) findViewById(R.id.gButton);
        findRestaurants = (Button) findViewById(R.id.fButton);
        findRestaurants.setOnClickListener(this);
        groupRoom.setOnClickListener(this);
        searchHall.setOnClickListener(this);
		
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }

	public void onClick(View v) {

		if(v==searchHall){  
			searchHall.getBackground().setColorFilter(new LightingColorFilter(0x000000, 0x4682b4)); //graphics for the button
			Intent startMapActivity = new Intent("android.intent.action.GOOGLEMAPACTIVITY");
			startMapActivity.putExtra("Map", true); //setting flag to know that the activity_map.xml should be run.
			startActivity(startMapActivity);
		}
		
		
		else if(v==findRestaurants){
			findRestaurants.getBackground().setColorFilter(new LightingColorFilter(0x000000, 0x4682b4));
			Intent campusMenu= new Intent("android.intent.action.CAMPUSMENUACTIVITY");
			campusMenu.putExtra("Search", RESTAURANTS);	//Setting an id which the next Activity can use.
			startActivity(campusMenu);
			
		}

	}
}
