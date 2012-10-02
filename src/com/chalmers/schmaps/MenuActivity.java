/*
 * Copyright [2012] []

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

package com.chalmers.schmaps;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends Activity implements View.OnClickListener {
	Button searchHall;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		assignInstances();
	}

	private void assignInstances() {
		searchHall = (Button) findViewById(R.id.hButton);
		searchHall.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_menu, menu);
		return true;
	}

	public void onClick(View v) {

		if(v==searchHall){
			searchHall.getBackground().setColorFilter(new LightingColorFilter(0x000000, 0x4682b4)); //graphics for the button
		}

		Intent startMapActivity = new Intent("android.intent.action.GOOGLEMAPSEARCHLOCATION");
		startActivity(startMapActivity);	
	}
}
