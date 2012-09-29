package com.chalmers.schmaps;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CampusMenuActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_campus_menu, menu);
        return true;
    }
}
