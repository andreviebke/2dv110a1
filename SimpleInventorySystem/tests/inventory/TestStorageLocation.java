package inventory;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;

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
	private static final double TOO_SMALL_WIDTH = -0.1;
	private static final int OVER_PICK = 4;
	private static final int UNDER_PICK = 2;
	private static final String ARTICLE_NAME = "articleName";
	private StorageLocation sut;

	@Before
	public void beforeClass() {
		MockitoAnnotations.initMocks(TestStorageLocation.class);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnNullName() {
		new StorageLocation(null);
	}

	/*
	 * Creation
	 */

	@Test
	public void shouldCreateInstanceWithName() {
		this.createSUTWithValidName();
		assertEquals(this.sut.getName(), TestStorageLocation.VALIDSTORAGE_NAME);
	}

	@Test
	public void shouldCreateInstanceWithArticles() {
		LinkedList<Article> articles = this.generateArticles(
				TestStorageLocation.VALID_NUM_ARTICLES, 0.0);
		StorageLocation s = this
				.createSUTWithValidStorageNameValidArticles(articles);
		verifyInvokeGetWidth(articles);
		assertEquals(articles, s.getArticles());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnTooManyArticles() {
		LinkedList<Article> articles = this.generateArticles(
				StorageLocation.MAX_ARTICLES + 1, 0.0);
		this.createSUTWithValidStorageNameValidArticles(articles);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnTooLargeWidth() {
		LinkedList<Article> articles = this.generateArticles(1,
				TestStorageLocation.TOO_LARGE_WIDTH);
		this.createSUTWithValidStorageNameValidArticles(articles);
		this.verifyInvokeGetWidth(articles);
	}

	/*
	 * Get
	 */
	@Test
	public void shouldGetNoArticles() {
		this.createSUTWithValidName();
		LinkedList<Article> output = (LinkedList<Article>) this.sut
				.getArticles();
		assertEquals(0, output.size());
	}

	@Test
	public void shouldGetNoArticlesWhenNoneExistingArticleNumber() {
		this.createSUTWithValidName();
		LinkedList<Article> output = this.sut
				.getArticles(TestStorageLocation.NONE_EXISTING_ART_NR);
		assertEquals(0, output.size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnGetNullArticleNumber() {
		this.createSUTWithValidName();
		this.sut.getArticles(null);
	}

	/*
	 * Insert
	 */

	@Test
	public void shoudInsertOneArticle() {
		this.createSUTWithValidName();

		Article mock = this.createMockArticle(TestStorageLocation.VALID_WIDTH,
				TestStorageLocation.ARTICLE_NAME);
		this.sut.insert(mock);

		assertEquals(1, this.sut.getArticles(TestStorageLocation.ARTICLE_NAME)
				.size());
		assertEquals(TestStorageLocation.ARTICLE_NAME,
				this.sut.getArticles(TestStorageLocation.ARTICLE_NAME).get(0)
						.getArtNr());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowWhenInsertNullArticle() {
		this.createSUTWithValidName();

		this.sut.insert(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnInsertOneArticleWithTooLargeTotalWidth() {
		this.createSUTWithValidName();

		Article mock = this.createMockArticle(
				TestStorageLocation.TOO_LARGE_WIDTH,
				TestStorageLocation.ARTICLE_NAME);

		this.sut.insert(mock);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnInsertOneArticleWithTooSmallTotalWidth() {
		this.createSUTWithValidName();

		Article mock = this.createMockArticle(
				TestStorageLocation.TOO_SMALL_WIDTH,
				TestStorageLocation.ARTICLE_NAME);

		this.sut.insert(mock);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowWhenInsertTooManyArticlesAfterOneAnother() {
		this.createSUTWithValidName();

		for (int i = 0; i < StorageLocation.MAX_ARTICLES + 1; i++) {
			Article mock = this.createMockArticle(
					TestStorageLocation.VALID_WIDTH,
					TestStorageLocation.ARTICLE_NAME);

			this.sut.insert(mock);
		}

	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowWhenAddingTooLargeWidthToExistingArticles() {
		this.createSUTWithValidName();

		Article mock1 = this.createMockArticle(
				TestStorageLocation.TOO_LARGE_WIDTH / 2,
				TestStorageLocation.ARTICLE_NAME);

		Article mock2 = this.createMockArticle(
				TestStorageLocation.TOO_LARGE_WIDTH / 2 + 0.1,
				TestStorageLocation.ARTICLE_NAME);

		this.sut.insert(mock1);
		verify(mock1).getWidth();
		this.sut.insert(mock2);
		verify(mock2).getWidth();
	}

	@Test
	public void shouldInsertManyArticles() {
		this.createSUTWithValidName();

		insert5Articles();

		assertEquals(5, this.sut.getArticles().size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowWhenInsertNullArticleList() {
		this.createSUTWithValidName();
		this.sut.insertMany(null);
	}

	@Test
	public void shouldInsertManyArticlesTwice() {
		this.createSUTWithValidName();

		insert5Articles();
		insert5Articles();

		assertEquals(10, this.sut.getArticles().size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnInsertManyArticlesWithTooLargeTotalWidth() {
		this.createSUTWithValidName();

		LinkedList<Article> articles = this.generateArticles(5,
				TestStorageLocation.TOO_LARGE_WIDTH / 5);

		this.sut.insertMany(articles);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnInsertManyArticlesWithTooSmallTotalWidth() {
		this.createSUTWithValidName();

		LinkedList<Article> articles = this.generateArticles(5,
				TestStorageLocation.TOO_SMALL_WIDTH / 5);

		this.sut.insertMany(articles);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnInsertTooManyArticlesAtOnce() {
		this.createSUTWithValidName();

		LinkedList<Article> articles = this.generateArticles(
				StorageLocation.MAX_ARTICLES + 1,
				TestStorageLocation.VALID_WIDTH);

		this.sut.insertMany(articles);

		this.verifyInvokeGetWidth(articles);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnInsertTooManyArticlesWhenExisting() {
		this.createSUTWithValidName();

		LinkedList<Article> articles = this.insert5Articles();
		articles.addAll(this.generateArticles(StorageLocation.MAX_ARTICLES
				- (articles.size() - 1), TestStorageLocation.VALID_WIDTH));
		this.sut.insertMany(articles);

		this.verifyInvokeGetWidth(articles);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnInsertManyArticlesWhenTooLargeWidthWithExisting() {
		this.createSUTWithValidName();

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
		this.createSUTWithValidName();

		insert5Articles();

		LinkedList<Article> pickedArticles = this.sut.pickAll();

		assertEquals(5, pickedArticles.size());
		assertEquals(0, this.sut.getArticles().size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowWhenPickNullArticleNumber() {

		this.createSUTWithValidName();

		this.sut.pickAll(null);
	}

	@Test
	public void shouldPickAllArticlesWithArticleNumber() {
		this.createSUTWithValidName();

		LinkedList<Article> articles = insert5Articles();
		generateArticlesWithName(articles);
		LinkedList<Article> pickedArticles = this.sut
				.pickAll(TestStorageLocation.ARTICLE_NAME);
		this.verifyInvokeGetArtNr(articles);

		assertEquals(3, pickedArticles.size());
		assertEquals(2, this.sut.getArticles().size());
	}

	@Test
	public void shouldPickGivenCountOfArticlesWithIdWhenCountIsSmallerThantTotalArticles() {
		this.createSUTWithValidName();

		LinkedList<Article> articles = insert5Articles();
		generateArticlesWithName(articles);
		LinkedList<Article> pickedList = this.sut.pick(
				TestStorageLocation.ARTICLE_NAME,
				TestStorageLocation.UNDER_PICK);

		this.verifyInvokeGetArtNr(articles);
		assertEquals(TestStorageLocation.UNDER_PICK, pickedList.size());
	}

	@Test
	public void shouldPickAsManyAsPossibleWhenCountIsLargerThanTotalArticles() {
		this.createSUTWithValidName();

		LinkedList<Article> articles = insert5Articles();
		generateArticlesWithName(articles);
		LinkedList<Article> pickedList = this.sut
				.pick(TestStorageLocation.ARTICLE_NAME,
						TestStorageLocation.OVER_PICK);

		this.verifyInvokeGetArtNr(articles);
		assertEquals(3, pickedList.size());
	}

	/*
	 * Helper methods
	 */
	private void generateArticlesWithName(LinkedList<Article> articles) {
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

		for (Article a : articles)
			verify(a).getWidth();
	}

	private void verifyInvokeGetArtNr(LinkedList<Article> articles) {

		for (Article a : articles)
			verify(a).getArtNr();
	}

	private void createSUTWithValidName() {
		this.sut = new StorageLocation(TestStorageLocation.VALIDSTORAGE_NAME);
	}

	private StorageLocation createSUTWithValidStorageNameValidArticles(
			LinkedList<Article> articles) {
		StorageLocation s = new StorageLocation(
				TestStorageLocation.VALIDSTORAGE_NAME, articles);
		return s;
	}

	private Article createMockArticle(double width, String name) {
		Article mock = mock(Article.class);
		when(mock.getArtNr()).thenReturn(name);
		when(mock.getWidth()).thenReturn(width);
		return mock;
	}

}
