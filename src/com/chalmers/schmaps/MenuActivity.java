package com.chalmers.schmaps;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Button searchHall = (Button) findViewById(R.id.button1);
        searchHall.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				Intent startMapActivity = new Intent("android.intent.action.MAPACTIVITY");
				startActivity(startMapActivity);
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }
}
