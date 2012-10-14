/*
 * Copyright [2012] [Mei Ha, Martin Augustsson, Simon Fransson]

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

import android.R.color;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.view.Menu;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.Toast;
/**
 * MenuActivity contains buttons on the menu and determine which activity will start
 * when the buttons are pressed.
 */

public class MenuActivity extends Activity implements View.OnClickListener {
	private static final int MICROWAVEBUTTON = 1;
	private static final int RESTAURANTBUTTON = 2;
	private static final int ATMBUTTON = 3;
	private static final int LECTUREHALLBUTTON = 4;
	private static final int BOOKINGKEY = 5;
	private static final int BUSKEY = 6;
	private static final int CHECKIN = 7;


	private Intent startMapActivity;

	private Button searchHall, groupRoom,atmButton,microwaveButton,findRestaurantsButton, checkin, bus;


	private String activityString;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		assignInstances();
	}

	private void assignInstances() {
		searchHall = (Button) findViewById(R.id.searchHallButton);
		searchHall.setOnClickListener(this);
		microwaveButton = (Button) findViewById(R.id.microwaveButton);
		microwaveButton.setOnClickListener(this);
		findRestaurantsButton = (Button) findViewById(R.id.findRestaurantsButton);
		findRestaurantsButton.setOnClickListener(this);
		groupRoom = (Button) findViewById(R.id.groupRoomButton);
		groupRoom.setOnClickListener(this);
		atmButton = (Button) findViewById(R.id.atmButton);
		atmButton.setOnClickListener(this);
		checkin = (Button) findViewById(R.id.checkinButton);
		checkin.setOnClickListener(this);
		bus = (Button) findViewById(R.id.checkbusButton);
		bus.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_menu, menu);
		return true;
	}

	/**
	 * onClick method for determining which activity will start through the use of view ID's and
	 * a switch case to start correct activity with correct variables.
	 */
	public void onClick(View v) {
		switch(v.getId()){

		case R.id.searchHallButton:
			startMapActivity = new Intent("android.intent.action.GOOGLEMAPSEARCHLOCATION");
			setActivityString(startMapActivity.getAction());
			startActivity(startMapActivity);
			break;

		case R.id.microwaveButton:
			startMapActivity = new Intent("android.intent.action.CAMPUSMENUACTIVITY");
			startMapActivity.putExtra("Show locations", MICROWAVEBUTTON);
			setActivityString(startMapActivity.getAction());
			startActivity(startMapActivity);
			break;

		case R.id.findRestaurantsButton:
			startMapActivity = new Intent("android.intent.action.CAMPUSMENUACTIVITY");
			startMapActivity.putExtra("Show locations", RESTAURANTBUTTON);
			setActivityString(startMapActivity.getAction());
			startActivity(startMapActivity);
			break;

		case R.id.atmButton:
			startMapActivity = new Intent("android.intent.action.CAMPUSMENUACTIVITY");
			startMapActivity.putExtra("Show locations", ATMBUTTON);
			setActivityString(startMapActivity.getAction());
			startActivity(startMapActivity);	
			break;

		case R.id.groupRoomButton:
			//Start the group room activity
			startMapActivity = new Intent(this,GroupRoomActivity.class);
			setActivityString("GroupRoomButton");
			startActivity(startMapActivity);	
			break;


		case R.id.checkinButton:
			Intent startCheckIn = new Intent("android.intent.action.CHECKINACTIVITY");
			startActivity(startCheckIn);
			break;

		case R.id.checkbusButton:
			if(gotInternetConnection()){
				startMapActivity = new Intent("android.intent.action.CHECKBUSACTIVITY");
				setActivityString(startMapActivity.getAction());
				startActivity(startMapActivity);
			}
			else
			{
				Context context = getApplicationContext();
				Toast.makeText(context, "Internet connection needed for this option", Toast.LENGTH_LONG).show();
			}
			break;

		}
	}
	public String getActivityString() {
		return activityString;
	}

	public void setActivityString(String activityString) {
		this.activityString = activityString;
	}

	/**
	 * Check if the device is connected to internet.
	 * Need three if-statements because getActiveNetworkInfo() may return null
	 * and end up with a force close. So thats the last thing to check.
	 * @return true if there is an internet connection
	 */
	public boolean gotInternetConnection()
	{
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null && wifiNetwork.isConnected()) {
			return true;
		}

		NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetwork != null && mobileNetwork.isConnected()) {
			return true;
		}

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			return true;
		}

		return false;
	}



}