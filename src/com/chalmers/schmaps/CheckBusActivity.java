/*
 * Copyright [2012] [Martin Augustsson]

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


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/*************************************************
 * Class shows when Bus 16 departures from
 * Chalmers and Lindholmen
 *************************************************/

public class CheckBusActivity extends Activity {
	
	private static int NROFROWS = 5;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkbus);
        makeRows();
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_campus_menu, menu);
        return true;
    }
    
    /**
     * Delete all rows under the top row 
     **/
    public void deleteRows(){
    	//ChalmersTable.getChildCount();
    }
    
    /**
     * Makes new rows with content that shows departures
     */
    public void makeRows(){
    	TableLayout ChalmersTable = (TableLayout) findViewById(R.id.ChalmersTable);
    	TableLayout LindholmenTable = (TableLayout) findViewById(R.id.LindholmenTable);
    	
    	for(int i = 0; i<NROFROWS;i++){ 
    	TableRow tempTableRow = new TableRow(this);
    	tempTableRow.setLayoutParams(new LayoutParams( LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
    	
    	TextView textview = new TextView(this);
    	textview.setText("16");
    	tempTableRow.addView(textview);
    	
    	TextView textview2 = new TextView(this);
    	textview2.setText("Högsbohöjd");
    	tempTableRow.addView(textview2);
    	
    	TextView textview3 = new TextView(this);
    	textview3.setText("13:40");
    	tempTableRow.addView(textview3);
    	
    	TextView textview4 = new TextView(this);
    	textview4.setText("Läge A");
    	tempTableRow.addView(textview4);
    	
    	ChalmersTable.addView(tempTableRow, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
    	}
    }
    
    /**
     * Refreshes the tables that hold info about departures
     */
    public void refreshTables(){
    	
    	
    }
    
}
