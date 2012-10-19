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

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

/**
 * class that draws path from our direction to room on a canvas
 */
public class PathOverlay extends Overlay {
	
	private List<GeoPoint> geoList;
	private int index;
	
	
	public PathOverlay(List<GeoPoint> geoPointList){
		super();
		geoList = geoPointList;
	}

	public void draw(Canvas canvas, MapView mapview, boolean shadow){
		super.draw(canvas, mapview, shadow);
		drawPath(canvas, mapview);
		
	}
	
	/**
	 * draws path from our direction to room on a canvas
	 * @param canvas, to draw in, created in GoogleMapActivity 
	 * @param mapview, created in GoogleMapActivity 
	 */
	private void drawPath (Canvas canvas, MapView mapview) {
		Paint paint = new Paint();
		Point startPoint = new Point();
		Point endPoint = new Point();
		GeoPoint startGeopoint, endGeopoint;
		float startLat, startLong, destLat, destLong;
		Projection projection = mapview.getProjection();
		
		//set colur etc to paint with
		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setStrokeWidth(4);
		paint.setAlpha(100);
		
		//loops through the geopoint, convert from lat,lng to pixels and draw path on canvas
		for(index = 0; index<(geoList.size()-1);index++){
			startGeopoint = geoList.get(index);
			projection.toPixels(startGeopoint, startPoint);
			
			endGeopoint = geoList.get(index+1);
			projection.toPixels(endGeopoint, endPoint);
			
			startLat = startPoint.x;
			startLong = startPoint.y;
			destLat = endPoint.x;
			destLong = endPoint.y;
			
			//draws lines from geopoint to geopoint
			canvas.drawLine(startLat, startLong, destLat, destLong, paint);	
		}
	}
	
	/**
	 * @return the size of the arraylist<Geopoints>
	 */
	public int getSize(){
		return geoList.size();
	}
	
	/**
	 * 
	 * @return the size of the index, used 
	 */
	public int getIndexSize(){
		return index;
	}
}
