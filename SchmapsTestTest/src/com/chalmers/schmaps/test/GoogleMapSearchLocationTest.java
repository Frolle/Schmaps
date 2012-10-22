/*
 * Copyright [2012] [Simon Fransson]

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

import java.lang.reflect.Field;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;

import com.chalmers.schmaps.GoogleMapSearchLocation;
import com.chalmers.schmaps.MapItemizedOverlay;
import com.chalmers.schmaps.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
/**
 * Test class for testing GoogleMapSearchLocation, tests the function to search for rooms,
 * regex and what happens if room is not found.
 * @author Frolle
 *
 */
public class GoogleMapSearchLocationTest extends ActivityInstrumentationTestCase2<GoogleMapSearchLocation> {
	
	private static final int ARBITRARYLATPOS = 57689111;
	private static final int ARBITRARYLONGPOS = 11973517;
	private GoogleMapSearchLocation activity;
	private Button editButton, directionsButton;
	private EditText lectureEdit;
	private MapView mapview;
	private String roomToFindString;
	private Dialog showingDialog;
	public GoogleMapSearchLocationTest()
	{
		super(GoogleMapSearchLocation.class);
	}
	/**
	 * Set up instance variables
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		this.activity = super.getActivity();
		this.editButton = (Button) this.activity.findViewById(R.id.edittextbutton);
		this.lectureEdit = (EditText) this.activity.findViewById(R.id.edittextlecture);
		this.directionsButton = (Button) this.activity.findViewById(R.id.directionbutton);
		this.mapview = (MapView) this.activity.findViewById(R.id.mapview);
	}
	/**
	 * Tests conditions before starting all the tests.
	 */
	public void testPreConditions(){
		super.assertNotNull(editButton);
		super.assertNotNull(directionsButton);
		super.assertNotNull(mapview);
		super.assertNotNull(lectureEdit);
		super.assertNotNull(activity);
	}
	
	
	/**
	 * Searches for a room that is known to exist in the database and tests
	 * that what is drawn on the map has the same attributes as the one that
	 * was queried.
	 */
	public void testSearchForARoom(){
		TouchUtils.tapView(this, this.lectureEdit);
		super.sendKeys("R U N A N");
		super.getInstrumentation().waitForIdleSync();
		TouchUtils.clickView(this, this.editButton);
		super.getInstrumentation().waitForIdleSync();
		List<Overlay> overlays = mapview.getOverlays();
		
		//Test case for when u dont have a position from user
		//MapItemizedOverlay tempTestOverlay = (MapItemizedOverlay) overlays.get(0);
		
		//Test case for when u get a position for the user
		MapItemizedOverlay tempTestOverlay = (MapItemizedOverlay) overlays.get(2);
		
		GeoPoint roomGP = new GeoPoint(ARBITRARYLATPOS, ARBITRARYLONGPOS);
		
		assertEquals(roomGP,tempTestOverlay.getItem(0).getPoint());
		assertEquals("Sven Hultins gata 2",tempTestOverlay.getItem(0).getTitle());
		assertEquals("Floor 1",tempTestOverlay.getItem(0).getSnippet());
	}
	
	
	/**
	 * Tests that a dialog is shown if a room is not found by querying for
	 * something that does not exist within the database and confirms that
	 * a dialog is shown upon the query.
	 */
	public void testDialogRoomNotFound(){
		TouchUtils.tapView(this, this.lectureEdit);
		super.sendKeys(KeyEvent.KEYCODE_BACKSLASH);
		super.sendKeys("R O O M THATDOESNOTEXIST");
		super.sendKeys(KeyEvent.KEYCODE_POUND);
		super.getInstrumentation().waitForIdleSync();
		TouchUtils.clickView(this, this.editButton);
		super.getInstrumentation().waitForIdleSync();
		try {
			Field dialogRoomNotFound = activity.getClass().getDeclaredField("dialog");
			dialogRoomNotFound.setAccessible(true);
			showingDialog = (Dialog) dialogRoomNotFound.get(this.activity);
		}
		catch (Exception e) {
		}
		assertTrue(showingDialog.isShowing());
	}
	
	
	/**
	 * Makes sure that the regex function works as intended by 
	 * inserting spaces and special characters into the query
	 * and confirming that still the right input was queried.
	 */
	public void testRegexForRoom(){
		TouchUtils.tapView(this, this.lectureEdit);
		super.sendKeys(KeyEvent.KEYCODE_BACKSLASH);
		super.sendKeys("R U N A N ");
		super.sendKeys(KeyEvent.KEYCODE_POUND);
		super.getInstrumentation().waitForIdleSync();
		TouchUtils.clickView(this, this.editButton);
		super.getInstrumentation().waitForIdleSync();
		try {
			Field roomToFindField = activity.getClass().getDeclaredField("roomToFind");
			roomToFindField.setAccessible(true);
			roomToFindString = (String) roomToFindField.get(this.activity);
			Log.e("GMSLtest", roomToFindString);
		} catch (Exception e) {
		}
		assertEquals("runan", roomToFindString);
	}
	
	/**
	 * Tests if walkingDirections parses the jsonobject in the right way
	 * @throws JSONException
	 */
	public void testParseJson() throws JSONException{

		String jsonresponse = "{\"routes\":[{\"legs\":[{\"steps\":[{\"end_location\":{\"lat\":57.715350,\"lng\":11.999310},\"start_location\":{\"lat\":57.71545000000001,\"lng\":11.999690}}]}]}]}";
				
		JSONObject jsonobject = new JSONObject(jsonresponse);


		activity.parseJson(jsonobject);


		assertEquals(2,activity.returnNrOfGeopoints());
	}
	
	/**
	 * Tests that the database is connected and that a response from the google directions api is recieved
	 * If the jsonobject is received the boolean running is set to true
	 */
	public void testConnectionToDirections(){
		TouchUtils.tapView(this, this.lectureEdit);
		super.sendKeys("R U N A N ");
		super.getInstrumentation().waitForIdleSync();
		TouchUtils.clickView(this, this.editButton);
		super.getInstrumentation().waitForIdleSync();

		activity.walkningDirections();
		
		assertEquals(true,activity.getIsAsyncTaskRunning());
	}
	
	/**
	 * Tests that the getdirectionsbutton is working, that the asynctask method has executed
	 */
	public void testDirectionsButton(){
		TouchUtils.tapView(this, this.lectureEdit);
		super.sendKeys("R U N A N ");
		super.getInstrumentation().waitForIdleSync();
		TouchUtils.clickView(this, this.editButton);
		super.getInstrumentation().waitForIdleSync();
		
		TouchUtils.clickView(this, this.directionsButton);
		super.getInstrumentation().waitForIdleSync();
		
		assertEquals(true,activity.getIsAsyncTaskRunning());
	}
}
