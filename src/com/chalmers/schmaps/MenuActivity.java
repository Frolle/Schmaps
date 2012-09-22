package com.chalmers.schmaps;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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
		Intent startMapActivity = new Intent("android.intent.action.GOOGLEMAPACTIVITY");
		startActivity(startMapActivity);		
	}
}
