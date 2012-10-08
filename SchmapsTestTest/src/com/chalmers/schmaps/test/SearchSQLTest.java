package com.chalmers.schmaps.test;

import java.io.SequenceInputStream;

import com.chalmers.schmaps.GoogleMapSearchLocation;
import com.chalmers.schmaps.R;
import com.chalmers.schmaps.SearchSQL;
import com.google.android.maps.MapView;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;
import android.widget.Button;
import android.widget.EditText;

public class SearchSQLTest extends ActivityInstrumentationTestCase2<GoogleMapSearchLocation> {
	
	private SearchSQL tester;
	GoogleMapSearchLocation activity;
	String theTestValue = "runan";
	
	public SearchSQLTest()
	{
		super(GoogleMapSearchLocation.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		this.activity = super.getActivity();
		tester = new SearchSQL(activity);
		tester.createDatabase();
		tester.openRead();
	}
	
	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		tester.close();
		activity.finish();
		super.tearDown();
	}

	
	public void testExists() {
		assertTrue(tester.exists(theTestValue));

	}


	public void testGetLat() {
		assertEquals(57689111, tester.getLat(theTestValue));
	}


	public void testGetLong() {
		assertEquals(11973517, tester.getLong(theTestValue));
	}


	public void testGetAddress() {
		
		assertEquals("Sven Hultins gata 2", tester.getAddress(theTestValue));
	}


	public void testGetLevel() {
		
		assertEquals("", tester.getLevel(theTestValue));
	}


	public void testGetLocations() {
		
		assertEquals(2, tester.getLocations("Microwaves").size());
	}

}
