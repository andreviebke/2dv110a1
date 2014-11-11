package inventory;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class TestStorageLocation {

	private static final String ARTICLE_NAME_2 = "articleName2";
	private static final String VALIDSTORAGE_NAME = "MyStorageLocation";
	private static final String NONE_EXISTING_ART_NR = "123456";
	private static final int VALID_NUM_ARTICLES = 1;
	private static final double TOO_LARGE_WIDTH = 1000.0;
	private static final double VALID_WIDTH = 50;
	private static final int OVER_PICK = 4;
	private static final int UNDER_PICK = 2;
	private static final String ARTICLE_NAME = "articleName";

	private StorageLocation sut;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(TestStorageLocation.class);
		this.createSUTWithValidName();
	}

	/*
	 * Creation
	 */

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnNullName() {
		new StorageLocation(null);
	}

	@Test
	public void shouldCreateInstanceWithName() {
		assertEquals(this.sut.getName(), TestStorageLocation.VALIDSTORAGE_NAME);
	}

	@Test
	public void shouldCreateInstanceWithArticles() {
		LinkedList<Article> articles = this.generateArticles(
				TestStorageLocation.VALID_NUM_ARTICLES, 0.0);
		this.createSUTWithValidStorageName(articles);

		this.verifyInvokeGetWidth(articles);

		assertEquals(articles, this.sut.getArticles());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnTooManyArticles() {
		LinkedList<Article> articles = this.generateArticles(
				StorageLocation.MAX_ARTICLES + 1, 0.0);

		this.createSUTWithValidStorageName(articles);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnTooLargeWidth() {
		LinkedList<Article> articles = this.generateArticles(1,
				TestStorageLocation.TOO_LARGE_WIDTH);

		this.createSUTWithValidStorageName(articles);

		this.verifyInvokeGetWidth(articles);
	}

	/*
	 * Get
	 */
	@Test
	public void shouldGetNoArticles() {
		LinkedList<Article> output = (LinkedList<Article>) this.sut
				.getArticles();

		assertEquals(0, output.size());
	}

	@Test
	public void shouldGetNoArticlesWhenArticleNumberDoesNotExist() {
		LinkedList<Article> output = this.sut
				.getArticles(TestStorageLocation.NONE_EXISTING_ART_NR);

		assertEquals(0, output.size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnGetWhenUsingNullArticleNumber() {
		this.sut.getArticles(null);
	}

	/*
	 * Insert
	 */

	@Test
	public void shoudInsertOneArticle() {
		Article mock = this.createMockArticle(TestStorageLocation.VALID_WIDTH,
				TestStorageLocation.ARTICLE_NAME);
		this.sut.insert(mock);

		verify(mock).getWidth();
		
		assertEquals(1, this.sut.getArticles(TestStorageLocation.ARTICLE_NAME)
				.size());
		assertEquals(TestStorageLocation.ARTICLE_NAME,
				this.sut.getArticles(TestStorageLocation.ARTICLE_NAME).get(0)
						.getArtNr());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowWhenInsertNullArticle() {
		this.sut.insert(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowWhenInsertingOneArticleWithTooLargeWidth() {
		Article mock = this.createMockArticle(
				TestStorageLocation.TOO_LARGE_WIDTH,
				TestStorageLocation.ARTICLE_NAME);

		this.sut.insert(mock);
		
		verify(mock).getWidth();
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowWhenInsertingTooManyArticlesInSequence() {
		for (int i = 0; i < StorageLocation.MAX_ARTICLES + 1; i++) {
			Article mock = this.createMockArticle(
					TestStorageLocation.VALID_WIDTH,
					TestStorageLocation.ARTICLE_NAME);

			this.sut.insert(mock);
			
			verify(mock).getWidth();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowWhenAddingTooLargeWidthToAlreadyExistingArticles() {
		Article mock1 = this.createMockArticle(
				TestStorageLocation.TOO_LARGE_WIDTH / 2,
				TestStorageLocation.ARTICLE_NAME);

		Article mock2 = this.createMockArticle(
				TestStorageLocation.TOO_LARGE_WIDTH / 2 + 0.1,
				TestStorageLocation.ARTICLE_NAME);

		this.sut.insert(mock1);
		this.sut.insert(mock2);
		
		verify(mock1).getWidth();
		verify(mock2).getWidth();
	}

	@Test
	public void shouldInsertManyArticles() {
		List<Article> input = this.insert5Articles();

		assertEquals(5, this.sut.getArticles().size());
		
		this.verifyInvokeGetWidth((LinkedList<Article>) input);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowWhenInsertNullArticleList() {
		this.sut.insertMany(null);
	}

	@Test
	public void shouldInsertManyArticlesTwice() {
		LinkedList<Article> input1 = insert5Articles();
		LinkedList<Article> input2 = insert5Articles();
		
		this.verifyInvokeGetWidth(input1, 2);
		this.verifyInvokeGetWidth(input2);

		LinkedList<Article> output = (LinkedList<Article>) this.sut.getArticles();		
		
		assertEquals(input1.size() + input2.size(), output.size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnInsertManyArticlesWithTooLargeTotalWidth() {
		LinkedList<Article> input = this.generateArticles(5,				
				TestStorageLocation.TOO_LARGE_WIDTH / 5);		
		
		this.sut.insertMany(input);
		
		this.verifyInvokeGetWidth(input);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnInsertTooManyArticlesAtOnce() {
		LinkedList<Article> input = this.generateArticles(
				StorageLocation.MAX_ARTICLES + 1,
				TestStorageLocation.VALID_WIDTH);

		this.sut.insertMany(input);

		this.verifyInvokeNeverArticles(input);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnInsertTooManyArticlesWhenExisting() {
		LinkedList<Article> input = this.insert5Articles();
		input.addAll(this.generateArticles(StorageLocation.MAX_ARTICLES
				- (input.size() - 1), TestStorageLocation.VALID_WIDTH));
		
		this.sut.insertMany(input);
		
		this.verifyInvokeNeverArticles(input);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnInsertManyArticlesWhenTooLargeWidthWithExisting() {
		this.insert5Articles();

		LinkedList<Article> articles = new LinkedList<Article>();
		articles.add(this.createMockArticle(StorageLocation.MAX_WIDTH,
				TestStorageLocation.ARTICLE_NAME));
		
		this.sut.insertMany(articles);

		this.verifyInvokeGetWidth(articles);
	}

	/*
	 * Pick
	 */

	@Test
	public void shouldPickAll() {
		LinkedList<Article> input = this.insert5Articles();

		LinkedList<Article> output = this.sut.pickAll();

		assertEquals(input.size(), output.size());
		assertEquals(0, this.sut.getArticles().size());
		assertEquals(input, output);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowWhenPickNullArticleNumber() {
		this.sut.pickAll(null);
	}

	@Test
	public void shouldPickAllArticlesWithArticleNumber() {
		LinkedList<Article> input = this.insert5Articles();
		this.addSampleArtNrFor5Articles(input);
		
		LinkedList<Article> output = this.sut
				.pickAll(TestStorageLocation.ARTICLE_NAME);
		
		this.verifyInvokeGetArtNr(input);

		assertEquals(3, output.size());
		assertEquals(2, this.sut.getArticles().size());
		assertEquals(input.subList(0, 3), output);
	}

	@Test
	public void shouldPickGivenAmountWhenUnderPick() {
		LinkedList<Article> input = this.insert5Articles();
		this.addSampleArtNrFor5Articles(input);
		
		LinkedList<Article> output = this.sut.pick(
				TestStorageLocation.ARTICLE_NAME,
				TestStorageLocation.UNDER_PICK);

		this.verifyInvokeGetArtNr(input);
		
		assertEquals(TestStorageLocation.UNDER_PICK, output.size());
		assertEquals(1, this.sut.getArticles(TestStorageLocation.ARTICLE_NAME).size());
		assertEquals(input.subList(0, 2), output);
		assertEquals(input.subList(2, 3), this.sut.getArticles(TestStorageLocation.ARTICLE_NAME));
	}

	@Test
	public void shouldPickAsManyAsAvailableWhenOverPick() {
		LinkedList<Article> input = this.insert5Articles();
		this.addSampleArtNrFor5Articles(input);
		
		LinkedList<Article> output = this.sut
				.pick(TestStorageLocation.ARTICLE_NAME,
						TestStorageLocation.OVER_PICK);

		this.verifyInvokeGetArtNr(input);
		
		assertEquals(3, output.size());
		assertEquals(0, this.sut.getArticles(TestStorageLocation.ARTICLE_NAME).size());
		assertEquals(input.subList(0, 3), output);		
	}

	/*
	 * Helper methods
	 */
	private void addSampleArtNrFor5Articles(LinkedList<Article> articles) {
		when(articles.get(0).getArtNr()).thenReturn(
				TestStorageLocation.ARTICLE_NAME);
		when(articles.get(1).getArtNr()).thenReturn(
				TestStorageLocation.ARTICLE_NAME);
		when(articles.get(2).getArtNr()).thenReturn(
				TestStorageLocation.ARTICLE_NAME);
		when(articles.get(3).getArtNr()).thenReturn(
				TestStorageLocation.ARTICLE_NAME_2);
		when(articles.get(4).getArtNr()).thenReturn(
				TestStorageLocation.ARTICLE_NAME_2);
	}

	private LinkedList<Article> insert5Articles() {
		LinkedList<Article> articles = this.generateArticles(5, 10);
		this.sut.insertMany(articles);
		return articles;
	}

	private LinkedList<Article> generateArticles(int count, double width) {

		LinkedList<Article> articles = new LinkedList<Article>();

		for (int i = 0; i < count; i++) {
			Article a = mock(Article.class);
			when(a.getWidth()).thenReturn(width);
			articles.add(a);
		}

		return articles;
	}
	
	private void verifyInvokeGetWidth(LinkedList<Article> articles) {
		this.verifyInvokeGetWidth(articles, 1);
	}
	
	private void verifyInvokeGetWidth(LinkedList<Article> articles, int times) {

		for (Article a : articles)
			verify(a, times(times)).getWidth();
	}

	private void verifyInvokeGetArtNr(LinkedList<Article> articles) {

		for (Article a : articles)
			verify(a).getArtNr();
	}

	private void createSUTWithValidName() {
		this.sut = new StorageLocation(TestStorageLocation.VALIDSTORAGE_NAME);
	}

	private void createSUTWithValidStorageName(LinkedList<Article> articles) {
		this.sut = new StorageLocation(TestStorageLocation.VALIDSTORAGE_NAME,
				articles);
	}

	private Article createMockArticle(double width, String name) {
		Article mock = mock(Article.class);
		when(mock.getArtNr()).thenReturn(name);
		when(mock.getWidth()).thenReturn(width);
		return mock;
	}

	private void verifyInvokeNeverArticles(LinkedList<Article> articles) {
		for(Article a : articles)
			verify(a, never()).getWidth();
	}
}
