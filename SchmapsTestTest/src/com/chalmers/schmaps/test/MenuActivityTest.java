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

import com.chalmers.schmaps.MenuActivity;
import com.chalmers.schmaps.R;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.widget.Button;

/**
 * Test case to see that all the menu buttons in MenuActivity starts the
 * correct intent when user touches a button.
 * @author Froll
 *
 */
public class MenuActivityTest extends ActivityInstrumentationTestCase2<MenuActivity> {
	private Button searchHall, groupRoom,atmButton,microwaveButton,findRestaurantsButton, checkInButton;
	private MenuActivity menuActivity;

	public MenuActivityTest() {
		super(MenuActivity.class);
	}
	
	/**
	 * Setup method for instantiating the variables.
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		this.menuActivity = super.getActivity();
		searchHall = (Button) menuActivity.findViewById(R.id.searchHallButton);
		groupRoom = (Button) menuActivity.findViewById(R.id.groupRoomButton);
		atmButton = (Button) menuActivity.findViewById(R.id.atmButton);
		microwaveButton = (Button) menuActivity.findViewById(R.id.microwaveButton);
		findRestaurantsButton = (Button) menuActivity.findViewById(R.id.findRestaurantsButton);
		checkInButton = (Button) menuActivity.findViewById(R.id.checkinButton);
	}
	
	public void testPreConditions(){
		super.assertNotNull(searchHall);
		super.assertNotNull(groupRoom);
		super.assertNotNull(atmButton);
		super.assertNotNull(microwaveButton);
		super.assertNotNull(findRestaurantsButton);
	}
	
	/**
	 * Tests the "Search hall" button to start the correct activity by comparing the intent.
	 * Tests below works in the same manner but for different buttons and different intents.
	 */
	public void testSearchHallButton(){
		TouchUtils.clickView(this, this.searchHall);
		super.getInstrumentation().waitForIdleSync();
		assertEquals("android.intent.action.GOOGLEMAPSEARCHLOCATION", menuActivity.getActivityString());
		this.sendKeys(KeyEvent.KEYCODE_BACK);
	}
	/**
	 * See test comments for test above.
	 */
	public void testGroupRoomButton(){
		TouchUtils.clickView(this, this.groupRoom);
		super.getInstrumentation().waitForIdleSync();
		assertEquals("GroupRoomButton", menuActivity.getActivityString());
		this.sendKeys(KeyEvent.KEYCODE_BACK);
	}
	
	/**
	 * See test comments for test above.
	 */
	public void testAtmButton(){
		TouchUtils.clickView(this, this.atmButton);
		super.getInstrumentation().waitForIdleSync();
		assertEquals("android.intent.action.CAMPUSMENUACTIVITY", menuActivity.getActivityString());
		this.sendKeys(KeyEvent.KEYCODE_BACK);
	}
	
	/**
	 * See test comments for test above.
	 */
	public void testMicrowaveButton(){
		TouchUtils.clickView(this, this.microwaveButton);
		super.getInstrumentation().waitForIdleSync();
		assertEquals("android.intent.action.CAMPUSMENUACTIVITY", menuActivity.getActivityString());
		this.sendKeys(KeyEvent.KEYCODE_BACK);
	}
	
	/**
	 * See test comments for test above.
	 */
	public void testFindRestaurantsButton(){
		TouchUtils.clickView(this, this.findRestaurantsButton);
		super.getInstrumentation().waitForIdleSync();
		assertEquals("android.intent.action.CAMPUSMENUACTIVITY", menuActivity.getActivityString());
		this.sendKeys(KeyEvent.KEYCODE_BACK);
	}
	
	/**
	 * See test comments for test above.
	 */
	public void testCheckInButton(){
		TouchUtils.clickView(this, this.checkInButton);
		super.getInstrumentation().waitForIdleSync();
		assertEquals("android.intent.action.CHECKINACTIVITY", menuActivity.getActivityString());
		this.sendKeys(KeyEvent.KEYCODE_BACK);
	}

	public void tearDown() throws Exception{
		super.tearDown();
	}
	
}
