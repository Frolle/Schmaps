package com.chalmers.schmaps;

import java.util.HashMap;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MapActivity extends Activity implements View.OnClickListener {
	HashMap<String, Integer> lectureHashMap;
	Button editButton;
	EditText lectureEdit;
	TextView showLecture;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        assignInstances();
    }

    private void assignInstances() {
        lectureHashMap = new HashMap();
        editButton = (Button) findViewById(R.id.edittextbutton);
        lectureEdit = (EditText) findViewById(R.id.edittextlecture);
        editButton.setOnClickListener(this);
        lectureHashMap.put("Matsalen", 42);
        showLecture = (TextView) findViewById(R.id.showLectureText);
        showLecture.setText("Hi!");
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_map, menu);
        return true;
    }

	public void onClick(View v) {
		String lectureText = lectureEdit.getText().toString();
		if(lectureHashMap.containsKey(lectureText))
				{
					showLecture.setText(lectureHashMap.get(lectureText).toString());
				}
		else
			showLecture.setText("WTF?!");
		
	}
}
