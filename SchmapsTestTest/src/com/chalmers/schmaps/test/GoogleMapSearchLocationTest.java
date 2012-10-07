package com.chalmers.schmaps.test;

import java.util.*;

import junit.framework.Assert;

import com.chalmers.schmaps.GoogleMapSearchLocation;
import com.chalmers.schmaps.MapItemizedOverlay;
import com.chalmers.schmaps.R;

import com.google.android.maps.*;

//import android.sax.RootElement;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.text.style.SuperscriptSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class GoogleMapSearchLocationTest extends ActivityInstrumentationTestCase2<GoogleMapSearchLocation> {
	
	GoogleMapSearchLocation activity;
	private Button editButton;
	private EditText lectureEdit;
	private MapView mapview;
	
	public GoogleMapSearchLocationTest()
	{
		super(GoogleMapSearchLocation.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		this.activity = super.getActivity();
		this.editButton = (Button) this.activity.findViewById(R.id.edittextbutton);
		this.lectureEdit = (EditText) this.activity.findViewById(R.id.edittextlecture);
		this.mapview = (MapView) this.activity.findViewById(R.id.mapview);
	}
	
	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}

	public void testPreConditions(){
		super.assertNotNull(editButton);
		super.assertNotNull(mapview);
		super.assertNotNull(lectureEdit);
	}
	
	public void testSearchForARoom(){
		TouchUtils.tapView(this, this.lectureEdit);
		super.sendKeys("R U N A N");
		super.getInstrumentation().waitForIdleSync();
		TouchUtils.clickView(this, this.editButton);
		
		List<Overlay> overlays = mapview.getOverlays();
		MapItemizedOverlay tempTestOverlay = (MapItemizedOverlay) overlays.get(0);
		
		GeoPoint roomGP = new GeoPoint(57689329, 11973824);
		
		assertEquals(tempTestOverlay.getItem(0).getPoint(),roomGP);
		assertEquals(tempTestOverlay.getItem(0).getTitle(),"Sven Hultins Gata 2");
		assertEquals(tempTestOverlay.getItem(0).getSnippet(),"Plan 2");
		
		
	}
}