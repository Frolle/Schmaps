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
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MapItemizedOverlay extends ItemizedOverlay {
	
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	private boolean getDirection;
	
	public MapItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;	
		getDirection = false;
	}

	
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate(); //anropar createItem(int) och ItemizeOverlay
	}
	
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
	
	public boolean wantDirection(){
		return getDirection;
	}
	
	public void setDirections(boolean b) {
		getDirection = b;
		
	}
	
	@Override
	protected boolean onTap(int index) {
	  OverlayItem item = mOverlays.get(index);
	  Integer a = index;
	  String s = a.toString();
	  Log.e("onTAP", s);
	  
	  AlertDialog.Builder builder;
	  AlertDialog alertDialog;
	 
	  LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	  View layout = inflater.inflate(R.layout.room_dialog,
	                                 null);
	  builder = new AlertDialog.Builder(mContext);
	  builder.setCancelable(true);
	  
	  TextView text = (TextView) layout.findViewById(R.id.text);
	  text.setText(item.getSnippet());
	  
	  
	  ImageView image = (ImageView) layout.findViewById(R.id.image);
	  
	  if(index == 0){
	  image.setImageResource(R.drawable.dot);

	 
	  // builder.setMessage(item.getSnippet());
      builder.setCancelable(true);
      
      builder.setNeutralButton("Get directions", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
        	  getDirection = true;
          }
      });
      }else{
    	  image.setImageResource(R.drawable.tomte); 
      }
	  builder.setView(layout);
	  alertDialog = builder.create();
	  alertDialog.setTitle(item.getTitle());
	  alertDialog.show();
	  if(wantDirection() == true){
		  Log.e("wantdirections", "true");
	  }else{
		  Log.e("wantdirections", "false"); 
	  }
	  
	  return true;
	}

}

