/*
 * Copyright [2012] []

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

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.widget.Button;

import com.chalmers.schmaps.CampusMenuActivity;
import com.chalmers.schmaps.R;
/**
 * Test case for CampusMenuActivity to test that it starts the correct activities 
 * from the user's input.
 * @author Froll
 *
 */
public class CampusMenuActivityTest extends ActivityInstrumentationTestCase2<CampusMenuActivity> {

	private static final int MICROWAVEBUTTON = 1;
	private CampusMenuActivity campusMenuActivity;
	private Button johannebergButton;
	private Button lindholmenButton;
	
	public CampusMenuActivityTest() {
		super(CampusMenuActivity.class);
	}
	/**
	 * Set up method for instantiating the variables.
	 */
	@Override
	protected void setUp()  {
		try {
			super.setUp();
		} catch (Exception e) {
		}
		//Simulate the intent from the previous activities which is usually rendered from the user's inputs in the app.
		setActivityIntent(new Intent("android.intent.action.CAMPUSMENUACTIVITY").putExtra("Show locations", MICROWAVEBUTTON));
		setActivityInitialTouchMode(false);
		this.campusMenuActivity = super.getActivity();
		johannebergButton = (Button) campusMenuActivity.findViewById(R.id.johannebergButton);
		lindholmenButton = (Button) campusMenuActivity.findViewById(R.id.lindholmenButton);
	}

	/**
	 * Tests conditions before starting to tests
	 */
	public void testPreConditions(){
		super.assertNotNull(johannebergButton);
		super.assertNotNull(lindholmenButton);
	}
		
	/**
	 * Test to see that the button for Lindholmen works correctly
	 * by starting the correct intent.
	 */
	public void testLindholmenButton(){
		TouchUtils.clickView(this, this.lindholmenButton);
		super.getInstrumentation().waitForIdleSync();
		assertEquals("android.intent.action.GOOGLEMAPSHOWLOCATION", campusMenuActivity.getActionString());
		this.sendKeys(KeyEvent.KEYCODE_BACK);
	}
	/**
	 * Works the same way as the test above.
	 */
	public void testJohannebergButton(){
		TouchUtils.clickView(this, this.johannebergButton);
		super.getInstrumentation().waitForIdleSync();
		assertEquals("android.intent.action.GOOGLEMAPSHOWLOCATION", campusMenuActivity.getActionString());
		this.sendKeys(KeyEvent.KEYCODE_BACK);
	}


}
