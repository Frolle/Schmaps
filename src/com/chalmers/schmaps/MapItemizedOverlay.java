/*
 * Copyright [2012] [Dina Zuko]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. 
   */

package com.chalmers.schmaps;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/**
 * class that creates MapItemizedOverlay and draws them and dialogs when user taps
 * @author dina
 *
 */
public class MapItemizedOverlay extends ItemizedOverlay<OverlayItem> {
	
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	private boolean getDirection;
	
	public MapItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;	
		getDirection = false;
	}

	/**
	 * adds a overlay to the arraylist mOverlays
	 * @param overlay, created in GoogleMapActivity with a figure, text etc
	 */
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate(); //calls createItem(int) and ItemizeOverlay
	}
	
	/**
	 * clears the overlay from the arraylist mOverlays
	 */
	public void removeOverlay(){
		mOverlays.clear();
		populate();
	}
	
	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
	
	/**
	 * @return boolean, true if user wants directions, false if user does not want directions
	 */
	public boolean wantDirection(){
		return getDirection;
	}
	/**
	 * sets the variable getDirections to true if user wants directions
	 * and false if user does not wants directions
	 * @param b, false or true
	 */
	public void setDirections(boolean b) {
		getDirection = b;
		
	}
	/**
	 * called when the user taps a figure on the screen, shows a dialog with information about the geopoint
	 */
	@Override
	protected boolean onTap(int index) {
	  OverlayItem item = mOverlays.get(index);
	  AlertDialog.Builder builder;
	  AlertDialog alertDialog;
	 
	  LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	  View layout = inflater.inflate(R.layout.room_dialog, null);
	  
	  builder = new AlertDialog.Builder(mContext);
	  builder.setCancelable(true);
	  
	  TextView text = (TextView) layout.findViewById(R.id.text);
	  text.setText(item.getSnippet());
	  
	  
	  ImageView image = (ImageView) layout.findViewById(R.id.image);
	  
	  if(index == 0){
	  image.setImageResource(R.drawable.dot); //shows a picture in the dialog

	  // builder.setMessage(item.getSnippet());
      builder.setCancelable(true);
      
      builder.setNeutralButton("Get directions", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
        	  getDirection = true; //sets variable getDirections to truth if we click the button getdirections
          }
      });
      }else{
    	  image.setImageResource(R.drawable.tomte); //shows a picture in the dialog
      }
	  
	  builder.setView(layout);
	  alertDialog = builder.create();
	  alertDialog.setTitle(item.getTitle());
	  alertDialog.show();
	  
	  return true;
	}

}