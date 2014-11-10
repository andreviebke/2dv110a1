package inventory;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TestStock {

	private Stock sut;
	private static double JUST_BELOW_LOW = Stock.MIN_TEMP - 0.1;
	private static double JUST_ABOVE_HIGH = Stock.MAX_TEMP + 0.1;
	private static double JUST_BELOW_HIGH = Stock.MAX_TEMP - 0.1;
	private static double JUST_ABOVE_LOW = Stock.MIN_TEMP + 0.1;

	@Before
	public void setUp() throws Exception {
		this.sut = new Stock();
	}

	/*
	 * Temperature
	 */
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowErrorOnABitTooCold() {
		this.sut.setTemperature(TestStock.JUST_BELOW_LOW);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowErrorOnABitTooWarm() {
		this.sut.setTemperature(TestStock.JUST_ABOVE_HIGH);
	}

	@Test
	public void shouldSetTemparatureWhenJustWarmEnough() {
		this.sut.setTemperature(TestStock.JUST_ABOVE_LOW);
		assertEquals(TestStock.JUST_ABOVE_LOW, this.sut.getTemperature());
	}

	@Test
	public void shouldSetTemparatureWhenJustColdEnough() {
		this.sut.setTemperature(TestStock.JUST_BELOW_HIGH);
		assertEquals(TestStock.JUST_BELOW_HIGH, this.sut.getTemperature());
	}

	@Test
	public void shoudlGetIntMinTemperatureWhenNotSet() {
		assertEquals(Double.MIN_VALUE, this.sut.getTemperature());
	}

	/*
	 * Storage locations
	 */
	// Get
	@Test
	public void shouldReturnNoStorageLocations() {
		List<StorageLocation> locs = this.sut.getStorageLocations();
		assertEquals(0, locs.size());
	}

	// Add one storage location
	@Test
	public void shouldAddStorageLocation() {
		StorageLocation loc = mock(StorageLocation.class);
		this.sut.addStorageLocation(loc);
		assertEquals(1, this.sut.getStorageLocations().size());
	}

	@Test
	public void shouldNotAddDuplicatedStorageLocation() {
		StorageLocation loc = mock(StorageLocation.class);
		this.sut.addStorageLocation(loc);
		this.sut.addStorageLocation(loc);
		assertEquals(1, this.sut.getStorageLocations().size());
	}
	
	@Test(expected = TooManyStorageLocationsException.class)
	public void shouldThrowOnAddingTooManyStorageLocations() {
		LinkedList<StorageLocation> locs = this
				.generateStorageLocations(Stock.MAX_STORAGE_LOCATIONS +1);
		for (int i = 0; i < locs.size(); i++)
			this.sut.addStorageLocation(locs.get(i));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowWhenAddingOneNullStorageLocation() {
		this.sut.addStorageLocation(null);
	}
	
	// Add many storage locations
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowWhenAddingManyNullStorageLocation() {
		this.sut.addStorageLocations(null);
	}

	@Test
	public void shouldAddManyStorageLocations() {
		LinkedList<StorageLocation> locs = generateStorageLocations(Stock.MAX_STORAGE_LOCATIONS);
		this.sut.addStorageLocations(locs);
	}

	@Test(expected = TooManyStorageLocationsException.class)
	public void shouldThrowWhenAddingTooManyStorageLocationsAtOnce() {
		LinkedList<StorageLocation> locs = generateStorageLocations(Stock.MAX_STORAGE_LOCATIONS + 1);
		this.sut.addStorageLocations(locs);
	}

	@Test(expected = TooManyStorageLocationsException.class)
	public void shouldThrowWhenAddingTooManyStorageLocationsAlreadyExisting() {
		LinkedList<StorageLocation> locs = this
				.generateStorageLocations(Stock.MAX_STORAGE_LOCATIONS);
		this.sut.addStorageLocations(locs);

		locs = new LinkedList<StorageLocation>();
		locs.add(mock(StorageLocation.class));

		this.sut.addStorageLocations(locs);
	}

	@Test
	public void shouldIgnoreDuplicationsWhenAddingMany() {
		LinkedList<StorageLocation> firstLocations = this
				.generateStorageLocations(Stock.MAX_STORAGE_LOCATIONS);
		this.sut.addStorageLocations(firstLocations);

		LinkedList<StorageLocation> secondLocations = new LinkedList<StorageLocation>();
		secondLocations.add(firstLocations.get(0));

		this.sut.addStorageLocations(secondLocations);
		assertEquals(3, this.sut.getStorageLocations().size());
	}
	
	@Test
	public void shouldIgnoreAddingDuplicatesInInputSetWhenAddingMany()
	{
		LinkedList<StorageLocation> locations = this
				.generateStorageLocations(Stock.MAX_STORAGE_LOCATIONS);
		locations.addAll(locations);
		this.sut.addStorageLocations(locations);
		
		assertEquals(3, this.sut.getStorageLocations().size());
	}

	private LinkedList<StorageLocation> generateStorageLocations(int count) {
		LinkedList<StorageLocation> locs = new LinkedList<StorageLocation>();

		for (int i = 0; i < count; i++)
		{
			StorageLocation mock = mock(StorageLocation.class);			
			locs.add(mock);
		}

		return locs;
	}

}
