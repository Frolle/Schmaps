package com.chalmers.schmaps;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
	private boolean showDirections;
	
	public MapItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;	
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
	  TextView text = (TextView) layout.findViewById(R.id.text);
	  text.setText(item.getSnippet());
	  
	  
	  ImageView image = (ImageView) layout.findViewById(R.id.image);
	  
	  if(index == 0){
	  image.setImageResource(R.drawable.dot);

	 
	  // builder.setMessage(item.getSnippet());
      builder.setCancelable(true);
      
      builder.setNeutralButton("Get directions", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
          }
      });
      }else{
    	  image.setImageResource(R.drawable.tomte); 
      }
	  builder.setView(layout);
	  alertDialog = builder.create();
	  alertDialog.setTitle(item.getTitle());
	  alertDialog.show();
	 
	  return true;
	}

}

