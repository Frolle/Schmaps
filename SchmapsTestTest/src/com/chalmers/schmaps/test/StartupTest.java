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

import com.chalmers.schmaps.Startup;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

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
	public void testForStartingMenuActivity(){
		assertEquals("android.intent.action.MENUACTIVITY", stringIntentActivity);
	}
}
