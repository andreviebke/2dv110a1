package inventory;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

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
	@Test
	public void shouldReturnNoStorageLocations() {
		List<StorageLocation> locs = this.sut.getStorageLocations();
		assertEquals(0, locs.size());
	}

	@Test
	public void shouldAddStorageLocation() {
		StorageLocation loc = mock(StorageLocation.class);
		this.sut.addStorageLocation(loc);
		assertEquals(1, this.sut.getStorageLocations().size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowWhenAddingOneNullStorageLocation() {
		this.sut.addStorageLocation(null);
	}

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
	public void shouldNotAddManyDuplicatedStorageLocations() {
		LinkedList<StorageLocation> firstLocations = this
				.generateStorageLocations(2);
		this.sut.addStorageLocations(firstLocations);

		LinkedList<StorageLocation> secondLocations = new LinkedList<StorageLocation>();
		secondLocations.add(firstLocations.get(0));

		this.sut.addStorageLocations(secondLocations);
		assertEquals(2, this.sut.getStorageLocations().size());
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

	private LinkedList<StorageLocation> generateStorageLocations(int count) {
		LinkedList<StorageLocation> locs = new LinkedList<StorageLocation>();

		for (int i = 0; i < count; i++)
			locs.add(mock(StorageLocation.class));

		return locs;
	}

}
