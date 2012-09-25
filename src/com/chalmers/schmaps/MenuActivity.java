package com.chalmers.schmaps;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
/**
 * An activity that functions as a menu, it implements OnClickListener for assigning actions to buttons when clicked.
 * 
 * @author Frolle
 *
 */
public class MenuActivity extends Activity implements View.OnClickListener {
	Button searchHall;
	Button groupRoom;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        assignInstances();
    }
    /**
     * Simple method to assign instance variables.
     */
    private void assignInstances() {
        searchHall = (Button) findViewById(R.id.hButton);
        groupRoom = (Button) findViewById(R.id.gButton);
        groupRoom.setOnClickListener(this);
        searchHall.setOnClickListener(this);
		
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }
	/**
	 * Determine what the buttons on the menu will do on click.
	 */
	public void onClick(View v) {

		if(v==searchHall){  
			searchHall.getBackground().setColorFilter(new LightingColorFilter(0x000000, 0x4682b4)); //graphics for the button
		}
		if(v==groupRoom){
			 groupRoom.getBackground().setColorFilter(new LightingColorFilter(0x000000, 0x4682b4)); //graphics for the button
			 Uri uri = Uri.parse("https://web.timeedit.se/chalmers_se/db1/b1/");
			 Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			 startActivity(intent);
			
		}

		Intent startMapActivity = new Intent("android.intent.action.GOOGLEMAPACTIVITY");
		startActivity(startMapActivity);		
	}
}
