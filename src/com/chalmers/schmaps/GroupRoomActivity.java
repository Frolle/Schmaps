/*
 * Copyright [2012] [Mei Ha]

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

import android.app.Activity;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * GroupRoomActivity starts a URL link, where students can book group room at Chalmers.
 */
public class GroupRoomActivity extends Activity{
	   
	 @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_menu);
	    getUrlLink();
	  }
	 
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			getMenuInflater().inflate(R.menu.activity_menu, menu);
			return true;
		}
/**
 * Start the URL-link 
 */
		private void getUrlLink() {
			 Uri uri = Uri.parse("https://web.timeedit.se/chalmers_se/db1/b1/"); 
			 Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			 startActivity(intent);
			 finish();//take back to previous activity
		}
		
}