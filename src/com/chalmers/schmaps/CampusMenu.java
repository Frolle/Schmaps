package com.chalmers.schmaps;

import android.app.Activity;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CampusMenu extends Activity implements View.OnClickListener{
	
	private Button jberg;
	private Button lholmen;
	public Bundle b; 
	private int i;
	private Intent intent;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);
         assignInstances();
    }

    private void assignInstances() {   	
    	intent = new Intent("android.intent.action.GOOGLEMAPACTIVITY");
    	b = getIntent().getExtras();	//Get the extra info from the MenuActivity
    	i = b.getInt("Search");	//get the button-id from the MenuActivity
    	jberg =(Button) findViewById(R.id.jButton);
    	lholmen =(Button) findViewById(R.id.lButton);   
        jberg.setOnClickListener(this);
        lholmen.setOnClickListener(this);
		
	}
    

	public void onClick(View v) {
		
		if(v == jberg){
			jberg.getBackground().setColorFilter(new LightingColorFilter(0x000000, 0x4682b4));
			intent.putExtra("Campus", "Johanneberg");
			intent.putExtra("Johanneberg", i);	//send "Johanneberg" + button-id to create the correct map later on in the GoogleMapActivity
			
			
			
		}
		else if(v==lholmen){
			lholmen.getBackground().setColorFilter(new LightingColorFilter(0x000000, 0x4682b4));
			intent.putExtra("Campus", "Lindholmen");
			intent.putExtra("Lindholmen", i);
			
		}
		intent.putExtra("Map", false); //Setting a boolean for use in GoogleMapActivity
		startActivity(intent);
		
	}
	
	
	

}
