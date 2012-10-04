package com.chalmers.schmaps;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class CheckoutMenu extends Activity implements View.OnClickListener {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_menu);
        assignInstances();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_checkout_menu, menu);
        return true;
    }
    
	private void assignInstances() {

	}

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

}
