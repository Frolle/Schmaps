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

import com.chalmers.schmaps.GoogleMapSearchLocation;
import com.chalmers.schmaps.SearchSQL;
/**
 * Test class for testing the SQL class. Different test cases tests so that the
 * get methods get the proper data from the database given an already known query.
 * @author Froll
 *
 */
public class SearchSQLTest extends ActivityInstrumentationTestCase2<GoogleMapSearchLocation> {
	private static final int RETURNVALUEIFNOTFOUND = 0;
	private static final String VALUETHATDOESNOTEXISTINDATABASE = "ValueThatDoesNotExist";
	private static final int LATOFTESTVALUE = 57689111;
	private static final int LONGOFTESTVALUE = 11973517;
	private static final String ADDRESSOFTESTVALUE = "Sven Hultins gata 2";
	private static final int SIZEOFMICROWAVETABLE = 9;
	private static final String LEVELOFTESTVALUE = "Floor 1";
	private SearchSQL tester;
	private GoogleMapSearchLocation activity;
	private String theTestValue = "runan";
	
	public SearchSQLTest()
	{
		super(GoogleMapSearchLocation.class);
	}
	
	/**
	 * Setup method for instantiating the variables.
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		this.activity = super.getActivity();
		tester = new SearchSQL(activity);
		tester.createDatabase();
		tester.openRead();
	}
	
	/**
	 * Method called after each test to safely close them down.
	 */
	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		tester.close();
		activity.finish();
		super.tearDown();
	}

	/**
	 * Test that all fields are assigned correct class.
	 */
	public void testPreConditions(){
		assertEquals(GoogleMapSearchLocation.class, activity.getClass());
	}
	/**
	 * Tests the method that checks if the value exists in the database
	 */
	public void testExists() {
		assertTrue(tester.exists(theTestValue));
		assertFalse(tester.exists(VALUETHATDOESNOTEXISTINDATABASE));
	}
	/**
	 * Tests the method that checks if the value exists in the database
	 */
	public void testGetLat() {
		assertEquals(LATOFTESTVALUE, tester.getLat(theTestValue));
		assertEquals(RETURNVALUEIFNOTFOUND, tester.getLat(VALUETHATDOESNOTEXISTINDATABASE));
	}

	/**
	 * Tests the method that checks if the value exists in the database
	 */
	public void testGetLong() {
		assertEquals(LONGOFTESTVALUE, tester.getLong(theTestValue));
		assertEquals(RETURNVALUEIFNOTFOUND, tester.getLong(VALUETHATDOESNOTEXISTINDATABASE));

	}

	/**
	 * Tests the method that checks if the value exists in the database
	 */
	public void testGetAddress() {
		
		assertEquals(ADDRESSOFTESTVALUE, tester.getAddress(theTestValue));
		assertNull(tester.getAddress(VALUETHATDOESNOTEXISTINDATABASE));
	}

	/**
	 * Tests the method that checks if the value exists in the database
	 */
	public void testGetLevel() {
		
		assertEquals(LEVELOFTESTVALUE, tester.getLevel(theTestValue));
		assertNull(tester.getLevel(VALUETHATDOESNOTEXISTINDATABASE));

	}

	/**
	 * Tests the method that checks if the value exists in the database
	 */
	public void testGetLocations() {
		
		assertEquals(SIZEOFMICROWAVETABLE, tester.getLocations("Microwaves").size());
	}

}
