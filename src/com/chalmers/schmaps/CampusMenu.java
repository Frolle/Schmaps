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
	Bundle b; 
	int i;
	Intent intent;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);
         assignInstances();
    }

    private void assignInstances() {   	
    	intent = new Intent("android.intent.action.GOOGLEMAPACTIVITY");
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
			intent.putExtra("Johanneberg", i);
			
			
			
		}
		else if(v==lholmen){
			lholmen.getBackground().setColorFilter(new LightingColorFilter(0x000000, 0x4682b4));
			intent.putExtra("Lindholmen", i);
			
		}
		intent.putExtra("Map", false);
		startActivity(intent);
		
	}
	
	
	

}
