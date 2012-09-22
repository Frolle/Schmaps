package com.chalmers.schmaps;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.content.Intent;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * A Startup activity for showing a splash screen, uses a thread to determine the length of the splash screen.
 * @author Frolle
 * @version 1.1
 */
public class Startup extends Activity {

    private ImageView myView;
	private Animation fadeInAnimation;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        
        //Use a thread for the splash screen to assign its lifetime.
        assignInstances();
        Thread timer = new Thread(){
            public void run (){
            	try{
            		sleep(5000);
            		Intent startMenuActivity = new Intent("android.intent.action.MENUACTIVITY");
            		startActivity(startMenuActivity);
            	}
            	catch(InterruptedException e){
            		e.printStackTrace();
            	}
            }
          };

        timer.start();
    }
	/**
	 * Method to assign the instance variables.
	 */
    private void assignInstances() {
               myView = (ImageView) findViewById(R.id.imageView1);
               fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.appear);
               myView.startAnimation(fadeInAnimation);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_startup, menu);
        return true;
    }
    
	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}
    

}
