package com.chalmers.schmaps.test;

import java.lang.reflect.Field;

import com.chalmers.schmaps.GroupRoomActivity;

import android.test.ActivityInstrumentationTestCase2;

public class GroupRoomActivityTest extends ActivityInstrumentationTestCase2<GroupRoomActivity> {

	private static final String URLTOBESTARTED = "https://web.timeedit.se/chalmers_se/db1/b1/";
	private GroupRoomActivity groupRoomActivity;
	private String urlString;

	public GroupRoomActivityTest() {
		super(GroupRoomActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		this.groupRoomActivity = super.getActivity();		
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testPreConditions(){
		assertNotNull(groupRoomActivity);
	}
	public void testCorrectUrlString(){
		Field urlStringField;
		try {
			urlStringField = groupRoomActivity.getClass().getDeclaredField("uri");
			urlStringField.setAccessible(true);
			urlString = (String)urlStringField.get(this.groupRoomActivity).toString();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(URLTOBESTARTED, urlString);
		
	}

}
