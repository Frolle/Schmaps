/*
 * Copyright [2012] [Dina Zuko]

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

package com.chalmers.schmaps.test;


import org.json.JSONException;
import org.json.JSONObject;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;

import com.chalmers.schmaps.CheckInActivity;
import com.chalmers.schmaps.R;
import com.google.android.maps.MapView;

/**
 * Test class for testing the Checkin activity by testing connection to database,
 * Reg ex functions and adding JSON objects.
 * @author Teafrog
 *
 */
public class CheckInActivityTest extends ActivityInstrumentationTestCase2<CheckInActivity> {


	private static final int SIZEOFJSONARRAY = 3;
	private CheckInActivity activity;
	private Button checkInButton;
	private EditText nameEdit;
	private MapView mapview;

	
	public CheckInActivityTest()
	{
		super(CheckInActivity.class);
	}
	
	/**
	 * Simple setUp method that runs before each test to set fields and such.
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		this.activity = super.getActivity();
		this.checkInButton = (Button) this.activity.findViewById(R.id.checkinbutton);
		this.nameEdit = (EditText) this.activity.findViewById(R.id.entername);
		this.mapview = (MapView) this.activity.findViewById(R.id.mapview);
	}
	/**
	 * Method called after each test to safely close them down.
	 */
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		activity.finish();
		super.tearDown();
	}

	/**
	 * Tests conditions before starting to tests
	 */
	public void testPreConditions(){
		super.assertNotNull(checkInButton);
		super.assertNotNull(mapview);
		super.assertNotNull(nameEdit);
	}
	
	/**
	 * Sends a jsonobject to the method and checks that it can parse it correctly
	 * @throws JSONException
	 */
	public void testParseJsonAndDraw() throws JSONException{

		String jsonresponse = "{\"result\":[{\"time\":\"2012-10-10 12:48:11\",\"name\":\"Arne\",\"lat\":\"57685535\",\"lng\":\"11977898\"},{\"time\":\"2012-10-10 12:48:41\",\"name\":\"Anna\",\"lat\":\"57685534\",\"lng\":\"11977897\"},{\"time\":\"2012-10-10 12:57:36\",\"name\":\"Kalle\",\"lat\":\"57685533\",\"lng\":\"11977896\"}]}";
		
		JSONObject jsonobject = new JSONObject(jsonresponse);
		
		activity.parseJsonAndDraw(jsonobject);
		
		assertEquals(SIZEOFJSONARRAY,activity.getSizeOfJsonArray());
	}
	
	/**
	 * Is testing that is testing that the regex is working
	 * Only allowing letters and numbers
	 */
	public void testAddNameRegex(){
		TouchUtils.tapView(this, this.nameEdit);
		
		super.sendKeys("R U N E ");
		super.getInstrumentation().waitForIdleSync();
		super.sendKeys(KeyEvent.KEYCODE_APOSTROPHE);
		
		super.getInstrumentation().waitForIdleSync();
		TouchUtils.clickView(this, this.checkInButton);
		super.getInstrumentation().waitForIdleSync();
		
		activity.getInputName();
		assertEquals("rune", activity.getInputName());
		
	}
	
	/**
	 * Tests that the database is connected and that a response from the database is recieved
	 * If the jsonobject is received the boolean running is set to true
	 */
	public void testConnectionToDatabase(){
		activity.connectExternalDatabase();
		
		assertEquals(true,activity.getIsAsyncTaskRunning());
	}
	
	
}
