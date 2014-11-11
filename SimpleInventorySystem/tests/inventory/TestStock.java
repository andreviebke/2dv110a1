package inventory;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import inventory.exceptions.InvalidTemperatureException;
import inventory.exceptions.TooManyStorageLocationsException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class TestStock {

	private static final double JUST_BELOW_MIN = Stock.MIN_TEMP - 0.1;
	private static final double JUST_ABOVE_MAX = Stock.MAX_TEMP + 0.1;
	private static final double JUST_BELOW_MAX = Stock.MAX_TEMP - 0.1;
	private static final double JUST_ABOVE_MIN = Stock.MIN_TEMP + 0.1;
	private static final String VALID_STORAGE_NAME = "SomeName";
	private static final String VALID_STORAGE_NAME_2 = "SomeOtherName";
	private static final String VALID_ART_NR_1 = "1";
	private static final String VALID_ART_NR_2 = "2";

	private Stock sut;

	@Before
	public void setUp() {
		this.sut = new Stock();
		MockitoAnnotations.initMocks(this);
	}

	/*
	 * Temperature
	 */
	@Test(expected = InvalidTemperatureException.class)
	public void shouldThrowErrorOnABitTooCold() {
		this.sut.setTemperature(TestStock.JUST_BELOW_MIN);
	}

	@Test(expected = InvalidTemperatureException.class)
	public void shouldThrowErrorOnABitTooWarm() {
		this.sut.setTemperature(TestStock.JUST_ABOVE_MAX);
	}

	@Test
	public void shouldSetTemparatureWhenJustWarmEnough() {
		this.sut.setTemperature(TestStock.JUST_ABOVE_MIN);

		assertEquals(TestStock.JUST_ABOVE_MIN, this.sut.getTemperature());
	}

	@Test
	public void shouldSetTemparatureWhenJustColdEnough() {
		this.sut.setTemperature(TestStock.JUST_BELOW_MAX);

		assertEquals(TestStock.JUST_BELOW_MAX, this.sut.getTemperature());
	}

	@Test
	public void shoudlGetMinTemperatureWhenNotSet() {
		assertEquals(Double.MIN_VALUE, this.sut.getTemperature());
	}

	/*
	 * Storage locations
	 */
	//
	// Get storage locations
	//
	@Test
	public void shouldReturnNoStorageLocations() {
		List<StorageLocation> input = this.sut.getStorageLocations();

		assertEquals(0, input.size());
	}

	// Note: Several storage locations may have the same names but not be the
	// same object
	@Test
	public void shoudlReturnStorageLocationsByName() {
		LinkedList<StorageLocation> inputs = this.createStorageLocations(
				Stock.MAX_STORAGE_LOCATIONS - 1, TestStock.VALID_STORAGE_NAME);
		StorageLocation input = this
				.createStorageLocations(TestStock.VALID_STORAGE_NAME_2);
		inputs.add(input);

		this.sut.addStorageLocations(inputs);

		LinkedList<StorageLocation> output = this.sut
				.getStorageLocationsByName(TestStock.VALID_STORAGE_NAME);

		verifyInvokeGetName(inputs);
		assertEquals(inputs.subList(0, Stock.MAX_STORAGE_LOCATIONS - 1), output);
	}

	// TODO: Add test for get all storage locations

	//
	// Adding one storage location
	//
	@Test
	public void shouldAddStorageLocation() {
		StorageLocation input = this.createStorageLocations(null);

		this.sut.addStorageLocation(input);

		StorageLocation output = this.sut.getStorageLocations().get(0);

		assertEquals(input, output);
	}

	@Test
	public void shouldNotAddDuplicatedStorageLocation() {
		StorageLocation input = this.createStorageLocations(null);

		this.sut.addStorageLocation(input);
		this.sut.addStorageLocation(input);

		LinkedList<StorageLocation> output = (LinkedList<StorageLocation>) this.sut
				.getStorageLocations();

		assertEquals(1, output.size());
		assertEquals(input, output.get(0));
	}

	@Test(expected = TooManyStorageLocationsException.class)
	public void shouldThrowOnAddingTooManyStorageLocations() {
		LinkedList<StorageLocation> input = this.createStorageLocations(
				Stock.MAX_STORAGE_LOCATIONS + 1, TestStock.VALID_STORAGE_NAME);

		this.addStorageLocationMultipleTimes(input);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowWhenAddingNullStorageLocation() {
		this.sut.addStorageLocation(null);
	}

	//
	// Adding many storage locations
	//
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowWhenAddingManyNullStorageLocation() {
		this.sut.addStorageLocations(null);
	}

	@Test
	public void shouldAddManyStorageLocations() {
		LinkedList<StorageLocation> input = createStorageLocations(
				Stock.MAX_STORAGE_LOCATIONS, TestStock.VALID_STORAGE_NAME);

		this.sut.addStorageLocations(input);

		LinkedList<StorageLocation> output = (LinkedList<StorageLocation>) this.sut
				.getStorageLocations();

		assertEquals(input, output);
	}

	@Test(expected = TooManyStorageLocationsException.class)
	public void shouldThrowWhenAddingTooManyStorageLocationsAtOnce() {
		LinkedList<StorageLocation> input = createStorageLocations(
				Stock.MAX_STORAGE_LOCATIONS + 1, TestStock.VALID_STORAGE_NAME);

		this.sut.addStorageLocations(input);
	}

	@Test(expected = TooManyStorageLocationsException.class)
	public void shouldThrowWhenAddingTooManyStorageLocationsAlreadyExisting() {
		LinkedList<StorageLocation> input = this.createStorageLocations(
				Stock.MAX_STORAGE_LOCATIONS, TestStock.VALID_STORAGE_NAME);

		this.sut.addStorageLocations(input);

		input = new LinkedList<StorageLocation>();
		input.add(this.createStorageLocations(TestStock.VALID_STORAGE_NAME));

		this.sut.addStorageLocations(input);
	}

	@Test
	public void shouldIgnoreDuplicationsWhenAddingManyExisting() {
		LinkedList<StorageLocation> firstInputs = this.createStorageLocations(
				Stock.MAX_STORAGE_LOCATIONS, TestStock.VALID_STORAGE_NAME);
		LinkedList<StorageLocation> secondInputs = new LinkedList<StorageLocation>();
		secondInputs.add(firstInputs.get(0));

		this.sut.addStorageLocations(firstInputs);
		this.sut.addStorageLocations(secondInputs);

		LinkedList<StorageLocation> output = (LinkedList<StorageLocation>) this.sut
				.getStorageLocations();

		assertEquals(firstInputs, output);
	}

	@Test
	public void shouldIgnoreAddingDuplicatesInInputListWhenAddingMany() {
		LinkedList<StorageLocation> inputs = this.createStorageLocations(
				Stock.MAX_STORAGE_LOCATIONS, TestStock.VALID_STORAGE_NAME);
		inputs.addAll(inputs);

		this.sut.addStorageLocations(inputs);

		LinkedList<StorageLocation> outputs = (LinkedList<StorageLocation>) this.sut
				.getStorageLocations();

		assertEquals(inputs.subList(0, 3), outputs);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionOnNullStorageLocationsWhenMerging()
	{
		this.sut.mergeStorageLocations(null, null);;
	}

	/*
	 * Move between storage locations
	 */
	@Test
	public void shouldMoveAllArticlesBetweenStorageLocations() {
		StorageLocation input1 = this
				.createStorageLocations(TestStock.VALID_STORAGE_NAME);
		StorageLocation input2 = this
				.createStorageLocations(TestStock.VALID_STORAGE_NAME_2);

		LinkedList<Article> inputArticles1 = this.createArticleList(
				TestStock.VALID_ART_NR_1, 0, 2);
		LinkedList<Article> inputArticles2 = this.createArticleList(
				TestStock.VALID_ART_NR_1, 0, 1);
		this.populateArticleList(TestStock.VALID_ART_NR_2, inputArticles2, 0, 1);

		when(input1.getArticles()).thenReturn(inputArticles1);
		when(input2.getArticles()).thenReturn(inputArticles2);
		when(input2.pickAll()).thenReturn(inputArticles2);

		this.sut.moveAllArticles(input1, input2);

		this.verifyInvokeGetArticles(input1, input2);
		verify(input2).pickAll();
		verify(input1).insertMany(inputArticles2);
		this.verifyInvokeArticleWidth(inputArticles1);
		this.verifyInvokeArticleWidth(inputArticles2);
	}

	@Test
	public void shouldMoveAllMatchingArticleNumbersBetweenStorageLocations() {
		StorageLocation input1 = this
				.createStorageLocations(TestStock.VALID_STORAGE_NAME);
		StorageLocation input2 = this
				.createStorageLocations(TestStock.VALID_STORAGE_NAME_2);

		LinkedList<Article> inputArticles1 = this.createArticleList(
				TestStock.VALID_ART_NR_1, 2);
		LinkedList<Article> inputArticles2 = this.createArticleList(
				TestStock.VALID_ART_NR_1, 1);

		when(input1.getArticles()).thenReturn(inputArticles1);
		when(input2.getArticles(TestStock.VALID_ART_NR_1)).thenReturn(
				inputArticles2);
		when(input2.pickAll(TestStock.VALID_ART_NR_1)).thenReturn(
				inputArticles2);

		this.sut.moveAllArticles(input1, input2, TestStock.VALID_ART_NR_1);

		this.verifyInvokeGetArticles(input1);
		this.verifyInvokeGetArticles(TestStock.VALID_ART_NR_1, input2);
		verify(input2).pickAll(TestStock.VALID_ART_NR_1);
		verify(input1).insertMany(inputArticles2);
		this.verifyInvokeArticleWidth(inputArticles1);
		this.verifyInvokeArticleWidth(inputArticles2);
	}

	@Test
	public void shouldNotMoveArticlesIfTooLargeTotalWidth() {
		StorageLocation s1 = this
				.createStorageLocations(TestStock.VALID_STORAGE_NAME);
		StorageLocation s2 = this
				.createStorageLocations(TestStock.VALID_STORAGE_NAME_2);

		LinkedList<Article> expectedBeforeArticlesListS1 = new LinkedList<Article>();
		LinkedList<Article> expectedAfterArticlesListS1 = new LinkedList<Article>();
		LinkedList<Article> expectedBeforeArticlesListS2 = this
				.createArticleList(TestStock.VALID_ART_NR_2,
						StorageLocation.MAX_WIDTH, 2);
		LinkedList<Article> expectedAfterArticlesListS2 = this
				.createArticleList(TestStock.VALID_ART_NR_2,
						StorageLocation.MAX_WIDTH, 2);

		when(s1.getArticles()).thenReturn(expectedBeforeArticlesListS1)
				.thenReturn(expectedAfterArticlesListS1);
		when(s2.getArticles()).thenReturn(expectedBeforeArticlesListS2)
				.thenReturn(expectedAfterArticlesListS2);

		this.sut.moveAllArticles(s1, s2);

		this.verifyInvokeGetArticles(s1, s2);

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
	public void shouldNotMoveArtilcesIfTooManyArticles() {
		StorageLocation s1 = this
				.createStorageLocations(TestStock.VALID_STORAGE_NAME);
		StorageLocation s2 = this
				.createStorageLocations(TestStock.VALID_STORAGE_NAME_2);

		LinkedList<Article> expectedBeforeArticlesListS1 = new LinkedList<Article>();
		LinkedList<Article> expectedAfterArticlesListS1 = new LinkedList<Article>();
		LinkedList<Article> expectedBeforeArticlesListS2 = this
				.createArticleList(TestStock.VALID_ART_NR_2,
						StorageLocation.MAX_ARTICLES + 1);
		LinkedList<Article> expectedAfterArticlesListS2 = this
				.createArticleList(TestStock.VALID_ART_NR_2,
						StorageLocation.MAX_ARTICLES + 1);

		when(s1.getArticles()).thenReturn(expectedBeforeArticlesListS1)
				.thenReturn(expectedAfterArticlesListS1);
		when(s2.getArticles()).thenReturn(expectedBeforeArticlesListS2)
				.thenReturn(expectedAfterArticlesListS2);

		this.sut.moveAllArticles(s1, s2);

		this.verifyInvokeGetArticles(s1, s2);

		LinkedList<Article> afterArticlesInS1 = (LinkedList<Article>) s1
				.getArticles();
		LinkedList<Article> afterArticlesInS2 = (LinkedList<Article>) s2
				.getArticles();

		assertEquals(expectedAfterArticlesListS1.size(),
				afterArticlesInS1.size());
		assertEquals(expectedAfterArticlesListS2.size(),
				afterArticlesInS2.size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionOnNullStorageLocation() {
		
		this.sut.moveAllArticles(null, null);
	}

	/*
	 * Search articles
	 */
	@Test
	public void shouldNotFindAnyArticles() {
		StorageLocation input1 = this
				.createStorageLocations(TestStock.VALID_STORAGE_NAME);
		StorageLocation input2 = this
				.createStorageLocations(TestStock.VALID_STORAGE_NAME_2);

		LinkedList<Article> inputArticles1 = this.createArticleList(
				TestStock.VALID_ART_NR_1, 3);
		LinkedList<Article> inputArticles2 = this.createArticleList(
				TestStock.VALID_ART_NR_1, 1);

		when(input1.getArticles(TestStock.VALID_ART_NR_1)).thenReturn(
				inputArticles1);
		when(input2.getArticles(TestStock.VALID_ART_NR_1)).thenReturn(
				inputArticles2);

		this.sut.addStorageLocation(input1);
		this.sut.addStorageLocation(input2);

		LinkedList<Article> output = this.sut
				.findArticles(TestStock.VALID_ART_NR_2);

		this.verifyInvokeGetArticles(TestStock.VALID_ART_NR_2, input1);
		this.verifyInvokeGetArticles(TestStock.VALID_ART_NR_2, input2);
		assertEquals(0, output.size());
	}

	/*
	 * Merge storage locations
	 */
	@Test
	public void shouldNotMergeIfTooLargeWidth() {
		StorageLocation input1 = this
				.createStorageLocations(TestStock.VALID_STORAGE_NAME);
		StorageLocation input2 = this
				.createStorageLocations(TestStock.VALID_STORAGE_NAME_2);

		LinkedList<Article> inputArticles1 = this.createArticleList(
				TestStock.VALID_ART_NR_1, StorageLocation.MAX_WIDTH / 2, 2);
		LinkedList<Article> inputArticles2 = this.createArticleList(
				TestStock.VALID_ART_NR_2, 0.1, 1);

		when(input1.getArticles()).thenReturn(inputArticles1);
		when(input2.getArticles()).thenReturn(inputArticles2);

		this.sut.mergeStorageLocations(input1, input2);

		this.verifyInvokeGetArticles(input1, input2);
		this.verifyInvokeArticleWidth(inputArticles1);
		this.verifyInvokeArticleWidth(inputArticles2);
	}

	@Test
	public void shouldNotMergeIfTooManyArticles() {
		StorageLocation input1 = this
				.createStorageLocations(TestStock.VALID_STORAGE_NAME);
		StorageLocation input2 = this
				.createStorageLocations(TestStock.VALID_STORAGE_NAME_2);

		LinkedList<Article> inputArticles1 = this.createArticleList(
				TestStock.VALID_ART_NR_2, StorageLocation.MAX_ARTICLES + 1);
		LinkedList<Article> inputArticles2 = new LinkedList<Article>();

		when(input1.getArticles()).thenReturn(inputArticles1);
		when(input2.getArticles()).thenReturn(inputArticles2);

		this.sut.mergeStorageLocations(input1, input2);

		this.verifyInvokeGetArticles(input1, input2);
		this.verifyNotInvokingGetWidth(inputArticles1);
		this.verifyNotInvokingGetWidth(inputArticles2);
	}

	@Test
	public void shouldMerge() {
		StorageLocation input1 = this
				.createStorageLocations(TestStock.VALID_STORAGE_NAME);
		StorageLocation input2 = this
				.createStorageLocations(TestStock.VALID_STORAGE_NAME_2);

		LinkedList<Article> inputArticles1 = this.createArticleList(
				TestStock.VALID_ART_NR_2, StorageLocation.MAX_WIDTH / 4, 1);
		LinkedList<Article> inputArticles2 = this.createArticleList(
				TestStock.VALID_ART_NR_2, StorageLocation.MAX_WIDTH / 4, 1);

		when(input1.getArticles()).thenReturn(inputArticles1);
		when(input2.getArticles()).thenReturn(inputArticles2);
		when(input2.pickAll()).thenReturn(inputArticles2);

		this.sut.mergeStorageLocations(input1, input2);

		this.verifyInvokeGetArticles(input1, input1, input2, input2);
		verify(input2).pickAll();
		verify(input1).insertMany(inputArticles2);
		this.verifyInvokeArticleWdith(inputArticles1, 2);
		this.verifyInvokeArticleWdith(inputArticles2, 2);
	}

	/*
	 * Move between storage locations
	 */
	@Test
	public void shouldNotMoveArticlesWhenTooMany() {
		StorageLocation input1 = this
				.createStorageLocations(TestStock.VALID_STORAGE_NAME);
		StorageLocation input2 = this
				.createStorageLocations(TestStock.VALID_STORAGE_NAME_2);

		LinkedList<Article> inputArticles1 = this.createArticleList(
				TestStock.VALID_ART_NR_2, StorageLocation.MAX_ARTICLES + 1);
		LinkedList<Article> inputArticles2 = this.createArticleList(
				TestStock.VALID_ART_NR_2, StorageLocation.MAX_ARTICLES + 1);

		when(input1.getArticles()).thenReturn(inputArticles1);
		when(input2.getArticles(anyString())).thenReturn(inputArticles2);
		when(input2.pickAll()).thenReturn(inputArticles2);

		this.sut.moveAllArticles(input1, input2, TestStock.VALID_ART_NR_2);

		this.verifyInvokeGetArticles(TestStock.VALID_ART_NR_2, input2);
		this.verifyInvokeGetArticles(input1);
		this.verifyNotInvokingGetWidth(inputArticles1);
		this.verifyNotInvokingGetWidth(inputArticles2);
	}

	@Test
	public void shouldNotMoveArticlesWithArticleIdWhenTooLargeWidth() {
		StorageLocation input1 = this
				.createStorageLocations(TestStock.VALID_STORAGE_NAME);
		StorageLocation input2 = this
				.createStorageLocations(TestStock.VALID_STORAGE_NAME_2);

		LinkedList<Article> inputArticles1 = new LinkedList<Article>();
		LinkedList<Article> inputArticles2 = this.createArticleList(
				TestStock.VALID_ART_NR_2, StorageLocation.MAX_WIDTH / 2 + 0.1,
				2);

		when(input1.getArticles()).thenReturn(inputArticles1);
		when(input2.getArticles(anyString())).thenReturn(inputArticles2);

		this.sut.moveAllArticles(input1, input2, TestStock.VALID_ART_NR_2);

		this.verifyInvokeGetArticles(TestStock.VALID_ART_NR_2, input2);
		this.verifyInvokeGetArticles(input1);
		this.verifyInvokeArticleWidth(inputArticles1);
		this.verifyInvokeArticleWidth(inputArticles2);
	}

	/*
	 * Helper methods
	 */

	//
	// SUT
	//
	private void addStorageLocationMultipleTimes(
			LinkedList<StorageLocation> locs) {
		for (int i = 0; i < locs.size(); i++)
			this.sut.addStorageLocation(locs.get(i));
	}

	//
	// Verifications
	//

	private void verifyInvokeGetName(LinkedList<StorageLocation> locs) {
		for (int i = 0; i < Stock.MAX_STORAGE_LOCATIONS - 1; i++) {
			verify(locs.get(i)).getName();
		}
	}

	private void verifyInvokeGetArticles(StorageLocation... storageLocations) {
		HashMap<StorageLocation, Integer> locations = new HashMap<StorageLocation, Integer>();
		for (int i = 0; i < storageLocations.length; i++)
			if (locations.containsKey(storageLocations[i]))
				locations.put(storageLocations[i],
						locations.get(storageLocations[i]) + 1);
			else
				locations.put(storageLocations[i], 1);

		for (Map.Entry<StorageLocation, Integer> s : locations.entrySet())
			verify(s.getKey(), times(s.getValue())).getArticles();
	}

	private void verifyInvokeGetArticles(String name,
			StorageLocation... storageLocations) {
		HashMap<StorageLocation, Integer> locations = new HashMap<StorageLocation, Integer>();
		for (int i = 0; i < storageLocations.length; i++)
			if (locations.containsKey(storageLocations[i]))
				locations.put(storageLocations[i],
						locations.get(storageLocations[i]) + 1);
			else
				locations.put(storageLocations[i], 1);

		for (Map.Entry<StorageLocation, Integer> s : locations.entrySet())
			verify(s.getKey(), times(s.getValue())).getArticles(name);
	}

	private void verifyInvokeArticleWidth(LinkedList<Article> articles) {
		verifyInvokeArticleWdith(articles, 1);
	}

	private void verifyInvokeArticleWdith(LinkedList<Article> articles,
			int times) {
		for (Article art : articles)
			verify(art, times(times)).getWidth();
	}

	private void verifyNotInvokingGetWidth(LinkedList<Article> articles) {
		for (Article art : articles)
			verify(art, never()).getWidth();
	}

	//
	// Creations
	//
	private LinkedList<Article> createArticleList(String name, int count) {
		LinkedList<Article> articles = new LinkedList<Article>();
		for (int i = 0; i < count; i++)
			articles.add(this.generateArticle(name));
		return articles;
	}

	private LinkedList<Article> createArticleList(String name, double width,
			int count) {
		LinkedList<Article> articles = new LinkedList<Article>();
		for (int i = 0; i < count; i++)
			articles.add(this.generateArticle(name, width));
		return articles;
	}

	private Article generateArticle(String name, double width) {
		Article mock = mock(Article.class);
		when(mock.getArtNr()).thenReturn(name);
		when(mock.getWidth()).thenReturn(width);
		return mock;
	}

	private Article generateArticle(String name) {
		Article mock = mock(Article.class);

		if (name == TestStock.VALID_ART_NR_1)
			when(mock.getWidth()).thenReturn(Article.MAX_WIDTH / 3);
		else
			when(mock.getWidth()).thenReturn(Article.MAX_WIDTH / 2);

		when(mock.getArtNr()).thenReturn(name);

		return mock;
	}

	private StorageLocation createStorageLocations(String name) {
		StorageLocation mock = mock(StorageLocation.class);
		if (null != name)
			when(mock.getName()).thenReturn(name);
		return mock;
	}

	private void populateArticleList(String name, LinkedList<Article> articles,
			int count, double width) {
		for (int i = 0; i < count; i++)
			articles.add(this.generateArticle(name, width));
	}

	private LinkedList<StorageLocation> createStorageLocations(int count,
			String name) {
		LinkedList<StorageLocation> locs = new LinkedList<StorageLocation>();

		for (int i = 0; i < count; i++) {
			StorageLocation mock = mock(StorageLocation.class);
			when(mock.getName()).thenReturn(name);
			locs.add(mock);
		}

		return locs;
	}
}
