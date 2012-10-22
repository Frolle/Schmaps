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

import android.test.ActivityInstrumentationTestCase2;

import com.chalmers.schmaps.GroupRoomActivity;

/**
 * Test class for GroupRoomActivity, contains just one test that it starts and uses
 * the correct url-link.
 * @author Froll
 *
 */
public class GroupRoomActivityTest extends ActivityInstrumentationTestCase2<GroupRoomActivity> {

	private static final String URLTOBESTARTED = "https://web.timeedit.se/chalmers_se/db1/b1/";
	private GroupRoomActivity groupRoomActivity;

	public GroupRoomActivityTest() {
		super(GroupRoomActivity.class);
	}
	/**
	 * Method for assigning all the field variables used.
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		this.groupRoomActivity = super.getActivity();		
	}
	/**
	 * Tests conditions before starting to tests
	 */
	public void testPreConditions(){
		assertEquals(GroupRoomActivity.class, groupRoomActivity.getClass());
	}
	/**
	 * Tests that it starts the correct URL, gets the field containing the url in run time
	 * and compares it to what it should be.
	 */
	public void testCorrectUrlString(){
		String urlString = groupRoomActivity.getUri().toString();
		assertEquals(URLTOBESTARTED, urlString);
		
	}

}
