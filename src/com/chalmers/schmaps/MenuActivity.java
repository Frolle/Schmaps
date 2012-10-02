package com.chalmers.schmaps;

import android.R.color;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends Activity implements View.OnClickListener {
	
	public static int ATM = 1;
	
	Button searchHall,groupRoom,atmButton;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		assignInstances();
	}

	private void assignInstances() {
		searchHall = (Button) findViewById(R.id.hallButton);
		searchHall.setOnClickListener(this);
		groupRoom = (Button) findViewById(R.id.groupRoomButton);
		groupRoom.setOnClickListener(this);
		atmButton = (Button) findViewById(R.id.atmButton);
		atmButton.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_menu, menu);
		return true;
	}

	public void onClick(View v) {
		switch(v.getId()){
		case R.id.hallButton:
		
			searchHall.setBackgroundColor(Color.DKGRAY); //change button color when button is clicked
			Intent startMapActivity = new Intent("android.intent.action.GOOGLEMAPACTIVITY");
			startActivity(startMapActivity);	
			break;
						
		case R.id.groupRoomButton:
			groupRoom.setBackgroundColor(Color.DKGRAY); //change button color when button is clicked
			//Start the group room activity
			Intent startGroupRoomActivity = new Intent(this,GroupRoomActivity.class);
			startActivity(startGroupRoomActivity);	
			break;

		case R.id.atmButton:
			atmButton.setBackgroundColor(Color.DKGRAY); //change button color when button is clicked
			//Start the group room activity
			Intent startAtm = new Intent(this,GoogleMapActivity.class);
			startAtm.putExtra(null, 1);
			startActivity(startAtm);	
			break;
		}
	}
	
}