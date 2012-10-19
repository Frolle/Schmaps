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

import com.chalmers.schmaps.MenuActivity;
import com.chalmers.schmaps.Startup;
import com.jayway.android.robotium.solo.Solo;
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Test class for testing the splash screen and that it starts the correct activity
 * @author Froll
 *
 */
public class StartupTest extends ActivityInstrumentationTestCase2<Startup> {

	private Startup startupActivity;
	private Thread threadForSplash;
	private Solo solo;
	
	public StartupTest() {
		super(Startup.class);
	}
	
	/**
	 * Set up the instance variables accordingly.
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		solo = new Solo(getInstrumentation(), getActivity());
		this.startupActivity = super.getActivity();
		}
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	/**
	 * Test that the splash screen starts the correct activity and that it shuts down
	 * when it's touched.
	 */
	public void testSplashThreadAndOnTouch(){
		try {
			Field splashThread = startupActivity.getClass().getDeclaredField("threadForSplash");
			splashThread.setAccessible(true);
			threadForSplash = (Thread) splashThread.get(this.startupActivity);
		}
		catch (Exception e) {
				e.printStackTrace();
		}
		MotionEvent evtDown, evtUp;
		evtDown = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis()+100, MotionEvent.ACTION_DOWN , 0.0f, 0.0f, 0);
		evtUp = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis()+100, MotionEvent.ACTION_UP , 0.0f, 0.0f, 0);

		assertTrue(startupActivity.onTouchEvent(evtDown));
		assertFalse(startupActivity.onTouchEvent(evtUp));

		threadForSplash.run();
		solo.clickOnScreen(65, 65);
		solo.assertCurrentActivity("Wrong class", MenuActivity.class);
	}
	/**
	 * Test for testing that the activity finishes when users clicks back.
	 */
	public void testSkipStart(){
		assertTrue(startupActivity.onKeyDown(KeyEvent.KEYCODE_BACK, null));
		assertFalse(startupActivity.onKeyDown(KeyEvent.KEYCODE_MENU, null));
	}
	
}
