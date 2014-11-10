package inventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

public class TestStock {

	private Stock sut;
	private static double JUST_BELOW_LOW = Stock.MIN_TEMP - 0.1;
	private static double JUST_ABOVE_HIGH = Stock.MAX_TEMP + 0.1;
	private static double JUST_BELOW_HIGH = Stock.MAX_TEMP - 0.1;
	private static double JUST_ABOVE_LOW = Stock.MIN_TEMP + 0.1;
	private static final String VALID_STORAGE_NAME = "SomeName";
	private static final String VALID_STORAGE_NAME_2 = "SomeOtherName";
	private static final String VALID_ART_NR_1 = "1";
	private static final String VALID_ART_NR_2 = "2";

	@Before
	public void setUp() throws Exception {
		this.sut = new Stock();
		MockitoAnnotations.initMocks(this);
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

	@Test
	public void shoudlReturnStorageLocationsByName() {
		LinkedList<StorageLocation> locs = this.generateStorageLocations(
				Stock.MAX_STORAGE_LOCATIONS - 1, TestStock.VALID_STORAGE_NAME);
		StorageLocation mock = generateStorageLocation(TestStock.VALID_STORAGE_NAME_2);
		locs.add(mock);

		this.sut.addStorageLocations(locs);
		LinkedList<StorageLocation> output = this.sut
				.getStorageLocationsByName(TestStock.VALID_STORAGE_NAME);

		verifyInvokeGetName(locs);
		assertEquals(Stock.MAX_STORAGE_LOCATIONS - 1, output.size());
	}

	// Add one storage location
	@Test
	public void shouldAddStorageLocation() {
		StorageLocation input = this.generateStorageLocation(null);

		this.sut.addStorageLocation(input);
		StorageLocation output = this.sut.getStorageLocations().get(0);

		assertNotNull(output);
		assertEquals(input, output);
	}

	@Test
	public void shouldNotAddDuplicatedStorageLocation() {
		StorageLocation input = this.generateStorageLocation(null);

		this.sut.addStorageLocation(input);
		this.sut.addStorageLocation(input);
		LinkedList<StorageLocation> output = (LinkedList<StorageLocation>) this.sut
				.getStorageLocations();

		assertEquals(1, output.size());
		assertEquals(input, output.get(0));
	}

	@Test(expected = TooManyStorageLocationsException.class)
	public void shouldThrowOnAddingTooManyStorageLocations() {
		LinkedList<StorageLocation> input = this.generateStorageLocations(
				Stock.MAX_STORAGE_LOCATIONS + 1, TestStock.VALID_STORAGE_NAME);

		addStorageLocationMultipleTimes(input);
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
		LinkedList<StorageLocation> input = generateStorageLocations(
				Stock.MAX_STORAGE_LOCATIONS, TestStock.VALID_STORAGE_NAME);

		this.sut.addStorageLocations(input);
		LinkedList<StorageLocation> output = (LinkedList<StorageLocation>) this.sut
				.getStorageLocations();

		assertEquals(Stock.MAX_STORAGE_LOCATIONS, output.size());
		assertEquals(input, output);
	}

	@Test(expected = TooManyStorageLocationsException.class)
	public void shouldThrowWhenAddingTooManyStorageLocationsAtOnce() {
		LinkedList<StorageLocation> input = generateStorageLocations(
				Stock.MAX_STORAGE_LOCATIONS + 1, TestStock.VALID_STORAGE_NAME);

		this.sut.addStorageLocations(input);
	}

	@Test(expected = TooManyStorageLocationsException.class)
	public void shouldThrowWhenAddingTooManyStorageLocationsAlreadyExisting() {
		LinkedList<StorageLocation> input = this.generateStorageLocations(
				Stock.MAX_STORAGE_LOCATIONS, TestStock.VALID_STORAGE_NAME);
		this.sut.addStorageLocations(input);

		input = new LinkedList<StorageLocation>();
		input.add(mock(StorageLocation.class));

		this.sut.addStorageLocations(input);
	}

	@Test
	public void shouldIgnoreDuplicationsWhenAddingMany() {
		LinkedList<StorageLocation> firstInputs = this
				.generateStorageLocations(Stock.MAX_STORAGE_LOCATIONS,
						TestStock.VALID_STORAGE_NAME);
		LinkedList<StorageLocation> secondInputs = new LinkedList<StorageLocation>();
		secondInputs.add(firstInputs.get(0));

		this.sut.addStorageLocations(firstInputs);
		this.sut.addStorageLocations(secondInputs);
		LinkedList<StorageLocation> output = (LinkedList<StorageLocation>) this.sut
				.getStorageLocations();

		assertEquals(3, this.sut.getStorageLocations().size());
		assertEquals(firstInputs, output);
	}

	@Test
	public void shouldIgnoreAddingDuplicatesInInputListWhenAddingMany() {
		LinkedList<StorageLocation> inputs = this.generateStorageLocations(
				Stock.MAX_STORAGE_LOCATIONS, TestStock.VALID_STORAGE_NAME);
		inputs.addAll(inputs);

		this.sut.addStorageLocations(inputs);
		LinkedList<StorageLocation> outputs = (LinkedList<StorageLocation>) this.sut
				.getStorageLocations();

		assertEquals(3, outputs.size());
		assertEquals(inputs.subList(0, 3), outputs);
	}

	/*
	 * Move between storage locations
	 */
	@Test
	public void shouldMoveAllArticlesFromOneStorageLocationToAnother() {
		StorageLocation s1 = this
				.generateStorageLocation(TestStock.VALID_STORAGE_NAME);
		StorageLocation s2 = this
				.generateStorageLocation(TestStock.VALID_STORAGE_NAME_2);

		LinkedList<Article> expectedBeforeArticlesListS1 = new LinkedList<Article>();
		LinkedList<Article> expectedBeforeArticlesListS2 = new LinkedList<Article>();
		LinkedList<Article> expectedAfterArticlesListS1 = new LinkedList<Article>();
		LinkedList<Article> expectedAfterArticlesListS2 = new LinkedList<Article>();

		this.populateArticleList(TestStock.VALID_ART_NR_1, expectedBeforeArticlesListS1, 2);
		this.populateArticleList(TestStock.VALID_ART_NR_1, expectedBeforeArticlesListS2, 1);
		this.populateArticleList(TestStock.VALID_ART_NR_2, expectedBeforeArticlesListS2, 1);
		this.populateArticleList(TestStock.VALID_ART_NR_1, expectedAfterArticlesListS1, 3);
		this.populateArticleList(TestStock.VALID_ART_NR_2, expectedAfterArticlesListS1, 1);
	
		when(s1.getArticles()).thenReturn(expectedBeforeArticlesListS1);
		when(s2.pickAll()).thenReturn(expectedBeforeArticlesListS2);

		this.sut.moveAllArticles(s1, s2);

		verify(s1).getArticles();
		verify(s2).pickAll();

		LinkedList<Article> afterArticlesInS1 = (LinkedList<Article>) s1
				.getArticles();
		LinkedList<Article> afterArticlesInS2 = (LinkedList<Article>) s2
				.getArticles();

		assertEquals(expectedAfterArticlesListS1.size(),
				afterArticlesInS1.size());
		assertEquals(expectedAfterArticlesListS2.size(),
				afterArticlesInS2.size());
	}

	@Test
	public void shouldMoveArtilcesMatchingArtilceIdFromOneLocationToAnother() {
		StorageLocation s1 = this
				.generateStorageLocation(TestStock.VALID_STORAGE_NAME);
		StorageLocation s2 = this
				.generateStorageLocation(TestStock.VALID_STORAGE_NAME_2);

		LinkedList<Article> expectedBeforeArticlesListS1 = new LinkedList<Article>();
		LinkedList<Article> expectedBeforeArticlesListS2 = new LinkedList<Article>();
		LinkedList<Article> expectedAfterArticlesListS1 = new LinkedList<Article>();
		LinkedList<Article> expectedAfterArticlesListS2 = new LinkedList<Article>();

		this.populateArticleList(TestStock.VALID_ART_NR_1,
				expectedBeforeArticlesListS1, 2);
		this.populateArticleList(TestStock.VALID_ART_NR_1,
				expectedBeforeArticlesListS2, 1);
		this.populateArticleList(TestStock.VALID_ART_NR_2,
				expectedBeforeArticlesListS2, 1);
		this.populateArticleList(TestStock.VALID_ART_NR_1,
				expectedAfterArticlesListS1, 3);
		this.populateArticleList(TestStock.VALID_ART_NR_2,
				expectedAfterArticlesListS2, 1);

		when(s1.getArticles()).thenReturn(expectedBeforeArticlesListS1)
				.thenReturn(expectedAfterArticlesListS1);
		when(s2.pickAll(TestStock.VALID_ART_NR_1)).thenReturn(
				expectedBeforeArticlesListS2);
		when(s2.getArticles()).thenReturn(expectedAfterArticlesListS2);

		this.sut.moveAllArticles(s1, s2, TestStock.VALID_ART_NR_1);

		verify(s1).getArticles();
		verify(s2).pickAll(TestStock.VALID_ART_NR_1);

		LinkedList<Article> afterArticlesInS1 = (LinkedList<Article>) s1
				.getArticles();
		LinkedList<Article> afterArticlesInS2 = (LinkedList<Article>) s2
				.getArticles();

		assertEquals(expectedAfterArticlesListS1.size(),
				afterArticlesInS1.size());
		assertEquals(expectedAfterArticlesListS2.size(),
				afterArticlesInS2.size());
	}

	/*
	 * Search articles
	 */
	@Test
	public void shouldNotFindAnyArticles() {
		StorageLocation s1 = this
				.generateStorageLocation(TestStock.VALID_STORAGE_NAME);
		StorageLocation s2 = this
				.generateStorageLocation(TestStock.VALID_STORAGE_NAME_2);

		LinkedList<Article> articleListS1 = new LinkedList<Article>();
		LinkedList<Article> articleListS2 = new LinkedList<Article>();
		this.populateArticleList(TestStock.VALID_ART_NR_1, articleListS1, 3);
		this.populateArticleList(TestStock.VALID_ART_NR_1, articleListS2, 1);
		
		when(s1.getArticles(TestStock.VALID_ART_NR_1)).thenReturn(articleListS1);
		when(s2.getArticles(TestStock.VALID_ART_NR_1)).thenReturn(articleListS2);
		
		this.sut.addStorageLocation(s1);
		this.sut.addStorageLocation(s2);
		LinkedList<Article> output = this.sut
				.findArticles(TestStock.VALID_ART_NR_2);

		verify(s1).getArticles(TestStock.VALID_ART_NR_2);
		verify(s2).getArticles(TestStock.VALID_ART_NR_2);

		assertEquals(0, output.size());
	}

	/*
	 * Helper methods
	 */
	private void populateArticleList(String name, LinkedList<Article> articles,
			int count) {
		for (int i = 0; i < count; i++)
			articles.add(this.generateArticle(name));
	}

	private LinkedList<StorageLocation> generateStorageLocations(int count,
			String name) {
		LinkedList<StorageLocation> locs = new LinkedList<StorageLocation>();

		for (int i = 0; i < count; i++) {
			StorageLocation mock = mock(StorageLocation.class);
			when(mock.getName()).thenReturn(name);
			locs.add(mock);
		}

		return locs;
	}

	private StorageLocation generateStorageLocation(String name) {
		StorageLocation mock = mock(StorageLocation.class);
		if (null != name)
			when(mock.getName()).thenReturn(name);
		return mock;
	}

	private void addStorageLocationMultipleTimes(
			LinkedList<StorageLocation> locs) {
		for (int i = 0; i < locs.size(); i++)
			this.sut.addStorageLocation(locs.get(i));
	}

	private void verifyInvokeGetName(LinkedList<StorageLocation> locs) {
		for (int i = 0; i < Stock.MAX_STORAGE_LOCATIONS - 1; i++) {
			verify(locs.get(i)).getName();
		}
	}

	private Article generateArticle(String name) {
		Article mock = mock(Article.class);
		when(mock.getArtNr()).thenReturn(name);
		return mock;
	}

}

