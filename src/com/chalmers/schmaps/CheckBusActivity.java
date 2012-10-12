package com.chalmers.schmaps;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;


public class CheckBusActivity extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkbus);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_campus_menu, menu);
        return true;
    }

}
