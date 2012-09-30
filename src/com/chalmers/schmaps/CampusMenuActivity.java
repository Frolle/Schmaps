package com.chalmers.schmaps;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class CampusMenuActivity extends Activity implements View.OnClickListener {
	private static final int JOHANNESBERG = 40;
	private static final int LINDHOLMEN = 42;
	
	private Intent startMapActivity;
	private Button johannesbergButton;
	private Button lindholmenButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_menu);
        assignInstances();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_campus_menu, menu);
        return true;
    }
    
	private void assignInstances() {
		johannesbergButton = (Button) findViewById(R.id.johannesbergButton);
		johannesbergButton.setOnClickListener(this);
		lindholmenButton = (Button) findViewById(R.id.lindholmenButton);
		lindholmenButton.setOnClickListener(this);
	}
	
	/**
	 * Starts GoogleMapActivity with different parameters depending on what the user
	 * entered.
	 */
	public void onClick(View arg0) {
		switch (arg0.getId())
		{
		case R.id.johannesbergButton:
			startMapActivity = new Intent("android.intent.action.GOOGLEMAPACTIVITY");
			startMapActivity.putExtra("Show searchfield", false);
			startMapActivity.putExtra("Campus", JOHANNESBERG);
			startActivity(startMapActivity);
			
		case R.id.lindholmenButton:
			startMapActivity = new Intent("android.intent.action.GOOGLEMAPACTIVITY");
			startMapActivity.putExtra("Show searchfield", false);
			startMapActivity.putExtra("Campus", LINDHOLMEN);
			startActivity(startMapActivity);
		}
	}
}
