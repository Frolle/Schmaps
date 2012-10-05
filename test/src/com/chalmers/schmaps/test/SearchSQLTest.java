

import com.chalmers.schmaps.*;

import android.test.AndroidTestCase;
import android.test.ActivityInstrumentationTestCase2;

public class SearchSQLTest extends ActivityInstrumentationTestCase2<GoogleMapSearchLocation> {
	private SearchSQL tester;
	private String theTestValue;

	public SearchSQLTest() {
		super(GoogleMapSearchLocation.class);
	}
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		GoogleMapSearchLocation goolie = new GoogleMapSearchLocation();
		SearchSQL tester = new SearchSQL(goolie);
		String theTestValue = new String("runan"); //"runan" exists within the database
	}


	public void testExists() {
		assertTrue(tester.exists(theTestValue));

	}


	public void testGetLat() {
		assertEquals(57689329, tester.getLat(theTestValue));
	}


	public void testGetLong() {
		assertEquals(11973824, tester.getLong(theTestValue));
	}


	public void testGetAddress() {
		
		assertEquals("Sven Hultins Gata 2", tester.getAddress(theTestValue));
	}


	public void testGetLevel() {
		
		assertEquals("Plan 2", tester.getLevel(theTestValue));
	}


	public void testGetLocations() {
		
		assertEquals(2, tester.getLocations("Microwave").size());
	}

}
