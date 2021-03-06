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
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/**
 * class that creates MapItemizedOverlay and draws them on the googlemap, like a layot
 * shows dialogs when user taps on figures in the picture
 *
 */
public class MapItemizedOverlay extends ItemizedOverlay<OverlayItem>{
	
	private List<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	private AlertDialog alertDialog;
	

	public MapItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;	
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
	
	/**
	 * Method used to create copies of Overlay Items found
	 * in the array list of this class.
	 */
	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	/**
	 * Returns size of the array list.
	 */
	@Override
	public int size() {
		return mOverlays.size();
	}
	

	/**
	 * called when the user taps a figure on the screen, shows a dialog with information about the geopoint
	 */
	@Override
	public boolean onTap(int index) {
	  OverlayItem item = mOverlays.get(index);
	  AlertDialog.Builder builder;
	 
	  LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	  View layout = inflater.inflate(R.layout.room_dialog, null);
	  
	  builder = new AlertDialog.Builder(mContext);
	  builder.setCancelable(true);
	  
	  TextView text = (TextView) layout.findViewById(R.id.text);
	  text.setText(item.getSnippet());
	  
	  
	  ImageView image = (ImageView) layout.findViewById(R.id.image);
	  
	  image.setImageResource(R.drawable.logoschmapsmini); //shows a picture in the dialog

      builder.setCancelable(true);
	  
	  builder.setView(layout);
	  alertDialog = builder.create();
	  alertDialog.setTitle(item.getTitle());
	  alertDialog.setCanceledOnTouchOutside(true);
	  //Set the dialog field so it can be tested.
	  setAlertDialog(alertDialog);
	  alertDialog.show();
	  
	  return true;
	}
	/**
	 * Returns the alert dialog that would be shown if there is no internet
	 * connection
	 * @return AlertDialog - the dialog for no internet connection
	 */
	public AlertDialog getAlertDialog() {
		return alertDialog;
	}

	/**
	 * Set the field variable alert dialog to tell the user there is no internet
	 * connection. Method is mostly used for testing.
	 * @param alertDialog - the dialog to set.
	 */
	public void setAlertDialog(AlertDialog alertDialog) {
		this.alertDialog = alertDialog;
	}


}