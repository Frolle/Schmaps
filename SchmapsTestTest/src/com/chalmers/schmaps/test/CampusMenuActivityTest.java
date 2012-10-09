package com.chalmers.schmaps.test;

import com.chalmers.schmaps.CampusMenuActivity;
import com.chalmers.schmaps.R;

import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.widget.Button;
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
	private Bundle extras;

	public CampusMenuActivityTest() {
		super(CampusMenuActivity.class);
	}
	/**
	 * Set up method for instantiating the variables.
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		//Simulate the intent from the previous activities which is usually rendered from the user's inputs in the app.
		setActivityIntent(new Intent("android.intent.action.CAMPUSMENUACTIVITY").putExtra("Show locations", MICROWAVEBUTTON));
		setActivityInitialTouchMode(false);
		this.campusMenuActivity = super.getActivity();
		johannebergButton = (Button) campusMenuActivity.findViewById(R.id.johannebergButton);
		lindholmenButton = (Button) campusMenuActivity.findViewById(R.id.lindholmenButton);
	}

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
