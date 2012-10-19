/*
 * Copyright [2012] [Martin Augustsson]

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

import com.chalmers.schmaps.CheckBusActivity;
import com.chalmers.schmaps.R;
import com.jayway.android.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;
import android.widget.TableLayout;

public class CheckBusActivityTest extends ActivityInstrumentationTestCase2<CheckBusActivity> {

	private CheckBusActivity activity;
	private TableLayout chalmersTableLayout;
	private TableLayout lindholmenTableLayout;
	private Button refreshButton;

	public CheckBusActivityTest()
	{
		super(CheckBusActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		this.activity = super.getActivity();
		chalmersTableLayout = (TableLayout) this.activity.findViewById(R.id.ChalmersTable);
		lindholmenTableLayout = (TableLayout) this.activity.findViewById(R.id.LindholmenTable);
		refreshButton = (Button) this.activity.findViewById(R.id.refreshbutton);
	}

	@Override
	protected void tearDown() throws Exception {
		this.activity.finish();
		this.activity = null;
		super.tearDown();
	}

	public void testPreConditions(){
		super.assertNotNull(lindholmenTableLayout);
		super.assertNotNull(chalmersTableLayout);
		super.assertNotNull(refreshButton);
		super.assertNotNull(activity);
	}

	@UiThreadTest
	public void testDeleteRows(){
		this.activity.deleteRows();
		assertEquals(1, lindholmenTableLayout.getChildCount());
		assertEquals(1, chalmersTableLayout.getChildCount());
	}

	@UiThreadTest
	public void testMakeRows(){
		int nrOfRows = 0;
		this.activity.deleteRows();
		assertEquals(1, lindholmenTableLayout.getChildCount());
		assertEquals(1, chalmersTableLayout.getChildCount());

		this.activity.makeRows();
		try {
			Field nrOfRowsField = activity.getClass().getDeclaredField("NROFROWS");
			nrOfRowsField.setAccessible(true);
			nrOfRows = (Integer) nrOfRowsField.get(this.activity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(1+nrOfRows, lindholmenTableLayout.getChildCount());
		assertEquals(1+nrOfRows, chalmersTableLayout.getChildCount());
	}

	public void testRefresh(){
		assertFalse(this.activity.refreshTables());
	}
}
