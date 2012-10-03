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
 * A link starts when the book group room button is clicked
 * @author -Mei
 *
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