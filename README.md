Schmaps - Epic Android project.
===================================

Release Notes - Release version 1.0
-----------------------------------------------
The following features are included in the release of Schmaps version 1.0: 
- Search Lecture Hall: You can search for lecture halls and other rooms on Chalmers and get the direction. By clicking on the figure you get more information about where you can find the microwave, which floor and building.

- Search Micros: Shows a map with microwaves on both campuses. By clicking on the figure you get more information about where you can find the microwave, which floor and building.

- Search for Restaurants: Shows a map with restaurants on both campuses. By clicking on the figure you get the name and more information about the restaurant. 

- Search for ATMs: Shows a map with the ATMs on both campuses. By clicking on the figure you get the address. 

- Book Study room: Takes you to the web page where you can book a study room. You login with your CID.

- Check In: You can view who is checked in and check in yourself. Writing your name is not mandatory.  By clicking on the figure you get the name of the checked in person or "Unknown" and when they checked in. The information is stored for 2 hours.

- Check Buses: Shows a timetable for bus number 16 which goes between campus Johanneberg and Lindholmen. The departure times an in real time.

The app contains database that contains information on lecture halls, other rooms, microwaves, restaurants and ATMs on Chalmers campus Lindholmen and Johanneberg.

Permissions used:
- android.permission.INTERNET: used so that the Schmpas can access Internet and get directions, buss timetable etc.
- android.permission.ACCESS_COARSE_LOCATION: used so that Schmaps can get your location using the network (triangulation).
- android.permission.ACCESS_NETWORK_STATE: used so that Schmaps can check if you have Internet connection before attempting to get directions, check in or get the bus time table.

Fixed bugs:
Bug#2 
When you choose get directions you need to path the screen or move on the map before the path is drawn. This recourse isRouteDisplayed() is not called when nothing is happening, it is triggered by events I guess.

Bug#3
GoogleMapSearchLocation crashes when started in emulator.

Bug Bugged-Button
The buttons in menu still pressed in when press return button

Bug Database in Swedish
The words and descriptions used in database are in Swedish, should be in English.

Bug4 New intent starts when phone is flipped, need to make a search again 
The data is not saved, you need to make a search again in googleMapSearchLocation

Bug5 get directions button
The get directions button appears when you press the menu button, this is not good because it is easily missed. 

Bug6 Application crashes when starting CheckInActivity without Internet

Bug7 Application crashes when pressing get directions without Internet

Bug8 App crashes when changing to landscape view if you have the mapview

Bug11 Splash screen bug
If return button is pressed when splash sceen is showing the application starts anyway, the menu is shown.

Bug12 Not possible to check in using Swedish characters

Bug14 App crashes when accessing check in or check bus without Internet


Known bugs:
Bug#1 Line in the bottom in splash screen
When starting splash a lighter line in the bottom on lg optimus 2x. On Samsung galaxy s the line is on the bottom and on the top
 	
Bug13 App crashes sometimes when trying to access Check buses
