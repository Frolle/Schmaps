package com.chalmers.schmaps;

import android.app.Activity;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CampusMenu extends Activity implements View.OnClickListener{
	
	Button jberg;
	Button lholmen;
	SearchSQL sql;
	Bundle b; 
	int i;
	
	public static final int JOHANNEBERG = 1;
	public static final int LINDHOLMEN = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);
         assignInstances();
    }

    private void assignInstances() {   	
    	b = getIntent().getExtras();
    	i = b.getInt("Search");
    	jberg =(Button) findViewById(R.id.jButton);
    	lholmen =(Button) findViewById(R.id.lButton);   
        jberg.setOnClickListener(this);
        lholmen.setOnClickListener(this);
		
	}
    

	public void onClick(View v) {
		
		if(v == jberg){
			jberg.getBackground().setColorFilter(new LightingColorFilter(0x000000, 0x4682b4));
			
		}
		if(v==lholmen){
			lholmen.getBackground().setColorFilter(new LightingColorFilter(0x000000, 0x4682b4));
			
		}
		Intent startMapActivity = new Intent("android.intent.action.GOOGLEMAPACTIVITY");
		startActivity(startMapActivity);
		
	}
	
	
	

}
