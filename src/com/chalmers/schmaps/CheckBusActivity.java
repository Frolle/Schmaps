/*
 * Copyright [2012] [Martin Augustsson]

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
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/*************************************************
 * Class shows when Bus 16 departures from
 * Chalmers and Lindholmen
 *************************************************/

public class CheckBusActivity extends Activity implements View.OnClickListener {

	private static final int COLUMN_NR_4 = 3;
	private static final int COLUMN_NR_3 = 2;
	private static final int COLUMN_NR_2 = 1;
	private static final int COLUMN_NR_1 = 0;
	private static final int NR_OF_COLUMNS = 4;
	private static final int TIME_TO_SLEEP = 700;
	private static final int TEXT_SIZE = 14;
	private static final int NROFROWS = 8;
	private static final String CHALMERS_URL = "http://api.vasttrafik.se/bin/rest.exe/v1/departureBoard?authKey=2443e74a-b1cd-466a-a4e2-72ac982a62df&format=json&id=9021014001960000&direction=9021014004490000";
	private static final String LINDHOLMEN_URL= "http://api.vasttrafik.se/bin/rest.exe/v1/departureBoard?authKey=2443e74a-b1cd-466a-a4e2-72ac982a62df&format=json&id=9021014004490000&direction=9021014001960000";

	private JSONObject[] returnedJsonObject;
	private TableLayout lindholmenTable;
	private TableLayout chalmersTable;
	private List<String> chalmersLineArray = new ArrayList<String>();
	private List<String> chalmersDestArray = new ArrayList<String>();
	private List<String> chalmersTimeArray = new ArrayList<String>();
	private List<String> chalmersTrackArray = new ArrayList<String>();

	private List<String> lindholmenLineArray = new ArrayList<String>();
	private List<String> lindholmenDestArray = new ArrayList<String>();
	private List<String> lindholmenTimeArray = new ArrayList<String>();
	private List<String> lindholmenTrackArray = new ArrayList<String>();
	private Button refreshButton;

	@Override
	/**
	 * onCreate method for determining what the activity does on creation.
	 * Sets the right view for the user and assigns fields.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkbus);
		refreshButton = (Button) findViewById(R.id.refreshbutton);
		refreshButton.setOnClickListener(this);

		chalmersTable = (TableLayout) findViewById(R.id.ChalmersTable);
		lindholmenTable = (TableLayout) findViewById(R.id.LindholmenTable);
		makeRows();
	}
	/**
	 * Delete all rows under the top row 
	 **/
	public void deleteRows(){
		int chalmersRowsToDel = chalmersTable.getChildCount();
		int lindholmenRowsToDel = lindholmenTable.getChildCount();

		if( (chalmersRowsToDel-1 | lindholmenRowsToDel-1) > 0){
			for (int i=chalmersRowsToDel-1;i>0;i--){
				TableRow row = (TableRow) chalmersTable.getChildAt(i);
				chalmersTable.removeView(row);
			}

			for (int j=lindholmenRowsToDel;j>0;j--){
				TableRow row = (TableRow) lindholmenTable.getChildAt(j);
				lindholmenTable.removeView(row);
			}
		}
	}

	/**
	 * Makes new rows with content that shows departures
	 */
	public void makeRows(){
		returnedJsonObject = null;
		try{
			GetDepatures getDepatures = new GetDepatures();
			getDepatures.execute();
			parseDataToArrays();

			makeChalmersRows();
			makeLindholmenRows();
		}catch (Exception e) {
			String msg = "Failed to retrive data";
			Toast.makeText(getApplicationContext(),msg , Toast.LENGTH_SHORT).show();
		}


	}

	/**
	 * Makes the rows for the chalmerstable
	 */
	public void makeChalmersRows(){
		for(int i = 0; i<NROFROWS; i++){ 
			TableRow tempTableRow = new TableRow(this);
			tempTableRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));

			// Makes every other row light gray or white
			if(i%2 == 0){
				tempTableRow.setBackgroundColor(getResources().getColor(R.color.transp_grey));
			}else{
				tempTableRow.setBackgroundColor(getResources().getColor(R.color.transp_white));
			}

			//Makes every textview for each column and add it before starting with a new one
			for(int j = 0; j<NR_OF_COLUMNS; j++){
				TextView textview = new TextView(this);
				textview.setTextColor(Color.BLACK);
				textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE);
				//Check which content should be written in the textview
				if(j == COLUMN_NR_1){
					textview.setText(chalmersLineArray.get(i));
				}else if(j == COLUMN_NR_2){
					textview.setText(chalmersDestArray.get(i));
				}else if(j == COLUMN_NR_3){
					textview.setText(chalmersTimeArray.get(i));
				}else if(j == COLUMN_NR_4){
					textview.setText(chalmersTrackArray.get(i));
				}
				textview.setGravity(Gravity.CENTER_HORIZONTAL);
				tempTableRow.addView(textview);
			}
			chalmersTable.addView(tempTableRow, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		}
	}

	/**
	 * Makes the rows for the lindholmentable
	 */
	public void makeLindholmenRows(){
		for(int i = 0; i<NROFROWS; i++){ 
			TableRow tempTableRow = new TableRow(this);
			tempTableRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));

			// Makes every other row light gray or white
			if(i%2 == 0){
				tempTableRow.setBackgroundColor(getResources().getColor(R.color.transp_grey));
			}else{
				tempTableRow.setBackgroundColor(getResources().getColor(R.color.transp_white));
			}

			//Makes every textview for each column and add it before starting with a new one 
			for(int j = 0; j<NR_OF_COLUMNS; j++){
				TextView textview = new TextView(this);
				textview.setTextColor(Color.BLACK);
				textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE);
				//Check which content should be written in the textview
				if(j == COLUMN_NR_1){
					textview.setText(lindholmenLineArray.get(i));
				}else if(j == COLUMN_NR_2){
					textview.setText(lindholmenDestArray.get(i));
				}else if(j == COLUMN_NR_3){
					textview.setText(lindholmenTimeArray.get(i));
				}else if(j == COLUMN_NR_4){
					textview.setText(lindholmenTrackArray.get(i));
				}

				textview.setGravity(Gravity.CENTER_HORIZONTAL);
				tempTableRow.addView(textview);
			}
			lindholmenTable.addView(tempTableRow, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		}
	}


	/**
	 * Refreshes the tables when clicking on the refresh button. 
	 */
	public void onClick(View v){
		refreshTables();
	}

	/**
	 * Saves all relevant data collected from the json-response 
	 * in arrays for easy access when making table
	 */
	public void parseDataToArrays(){

		while(returnedJsonObject == null){ //if json object not returned, sleep
			try {
				Thread.sleep(TIME_TO_SLEEP); // Sleep for x seconds
			} catch (InterruptedException e1) {
			}
		}
		//Run two times, one for data for chalmerstable and second for lindholmentable
		for(int i=0;i<2;i++){

			try {
				JSONObject departureBoard = returnedJsonObject[i].getJSONObject("DepartureBoard");
				JSONArray departureArray = departureBoard.getJSONArray("Departure");
				if(departureArray.length() <= 0)
				{
					String msg = "Failed to retrive data";
					Toast.makeText(getApplicationContext(),msg , Toast.LENGTH_SHORT).show();
				}
				else
				{
					for(int count = 0;count<departureArray.length();count++){
						JSONObject depature = departureArray.getJSONObject(count);
						String line = depature.getString("name");
						String destination = depature.getString("direction");
						String time = depature.getString("rtTime");
						String track = depature.getString("track");

						if(i == 0){
							chalmersLineArray.add(count, line);
							chalmersDestArray.add(count, destination);
							chalmersTimeArray.add(count, time);
							chalmersTrackArray.add(count, track);
						}else if(i == 1){
							lindholmenLineArray.add(count,line);
							lindholmenDestArray.add(count,destination);
							lindholmenTimeArray.add(count,time);
							lindholmenTrackArray.add(count,track);
						}
					}
				}

			} catch (JSONException e) {
			}

		}
	}

	/**
	 * Refreshes the tables that hold info about departures
	 */
	public boolean refreshTables(){
		try {
			this.deleteRows();
			makeRows();
			return true;
		} catch (Exception e) {

		}
		return false;
	}


	/****************************************************************************
	 * this innerclass creates a new thread from where we can make a request
	 *  to v�sttrafik api - to get the directions
	 * 	inspired by
	 *  http://www.vogella.com/articles/AndroidPerformance/article.html
	 ********************************************************************************/
	private class GetDepatures extends AsyncTask<Void, Void, JSONObject[]>{


		/** when called makes a request to v�sttrafik api (json format) 
		 *  gets the response back
		 *  convertes the response to a jsonobject
		 */
		@Override
		protected JSONObject[] doInBackground(Void... params) {

			JSONObject[] tempJsonObject = new JSONObject[2];

			//establish a connection with v�sttrafik api
			for(int i=0; i<2; i++){
				StringBuilder response = new StringBuilder();
				InputStream is = null;
				URL url = null;
				HttpURLConnection urlConnection = null;
				String line = null;
				String jsonResponse = "";
				try {
					if(i==0){
						url = new URL(CHALMERS_URL);
					} else if(i == 1 ) {
						url = new URL(LINDHOLMEN_URL);
					}
					urlConnection = (HttpURLConnection) url.openConnection();
					urlConnection.setRequestMethod("GET");
					urlConnection.setDoOutput(true);
					urlConnection.setDoInput(true);
					is = urlConnection.getInputStream();
					urlConnection.connect();
				} catch (MalformedURLException e) {
				} catch (IOException e) {
				}
				//read from the buffer line by line and save in response (a stringbuider)
				try{
					InputStreamReader inputStream = new InputStreamReader(is);
					BufferedReader reader = new BufferedReader(inputStream);
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
					tempJsonObject[i] = new JSONObject(jsonResponse);
				}catch(JSONException e){

				}
			}
			returnedJsonObject = tempJsonObject;
			return returnedJsonObject;
		}
	}
}
