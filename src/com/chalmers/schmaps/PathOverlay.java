package com.chalmers.schmaps;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class PathOverlay extends Overlay {
	
	private ArrayList<GeoPoint> geoList;
	
	public PathOverlay(ArrayList<GeoPoint> geoPointList){
		super();
		geoList = geoPointList;
	}

	public void draw(Canvas canvas, MapView mapview, boolean shadow){
		super.draw(canvas, mapview, shadow);
		drawPath(canvas, mapview);
		
	}
	
	private void drawPath (Canvas canvas, MapView mapview) {
		// TODO Auto-generated method stub
		
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setStrokeWidth(4);
		paint.setAlpha(100);
		
		Point point1 = new Point();
		Point point2 = new Point();
		GeoPoint startpoint, endpoint;
		float startLat, startLong, destLat, destLong;
		Projection projection = mapview.getProjection();
		
		for(int index = 0; index<(geoList.size()-1);index++){
			startpoint = geoList.get(index);
			projection.toPixels(startpoint, point1);
			
			endpoint = geoList.get(index+1);
			projection.toPixels(endpoint, point2);
			
			startLat = point1.x;
			startLong =point1.y;
			destLat = point2.x;
			destLong = point2.y;
			canvas.drawLine(startLat, startLong, destLat, destLong, paint);
			
		}
	}
}
