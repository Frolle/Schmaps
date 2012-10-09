package com.chalmers.schmaps.test;

import com.chalmers.schmaps.Startup;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

/**
 * Test class for testing the splash screen and that it starts the correct activity
 * @author Froll
 *
 */
public class StartupTest extends ActivityInstrumentationTestCase2<Startup> {

	private Startup startupActivity;
	private String stringIntentActivity;
	private Intent startupIntent;

	public StartupTest() {
		super(Startup.class);
	}
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		this.startupActivity = super.getActivity();
		this.startupIntent = startupActivity.getIntentForMenuActivity();
		stringIntentActivity = startupIntent.getAction();	
		}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	/**
	 * Test so that the correct activity is started.
	 */
	public void testForStartingMenuActivity(){
		assertEquals("android.intent.action.MENUACTIVITY", stringIntentActivity);
	}
}
