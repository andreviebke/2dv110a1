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

	private static String VALIDSTORAGE_NAME = "MyStorageLocation";
	private static String NON_EXISTING_ART_NR = "123456";
	private static int VALID_NUM_ARTICLES = 1;
	private static double TOO_LARGE_WIDTH = 1000.0;
	private static double TOO_SMALL_WIDTH = -0.1;
	private static String ARTICLE_NAME = "articleName";
	private StorageLocation sut;

	@Before
	public void beforeClass() {
		this.sut = new StorageLocation(TestStorageLocation.VALIDSTORAGE_NAME);
		MockitoAnnotations.initMocks(TestStorageLocation.class);
	}

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
		StorageLocation s = new StorageLocation(
				TestStorageLocation.VALIDSTORAGE_NAME, articles);
		verifyInvokeGetWidth(articles);
		assertEquals(articles, s.getArticles());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnTooManyArticles() {
		LinkedList<Article> articles = this.generateArticles(
				StorageLocation.MAX_ARTICLES + 1, 0.0);
		new StorageLocation(TestStorageLocation.VALIDSTORAGE_NAME, articles);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnTooLargeWidth() {
		LinkedList<Article> articles = this.generateArticles(1,
				TestStorageLocation.TOO_LARGE_WIDTH);
		new StorageLocation(TestStorageLocation.VALIDSTORAGE_NAME, articles);
		this.verifyInvokeGetWidth(articles);
	}

	@Test
	public void shouldReturnNoArticles() {
		LinkedList<Article> output = (LinkedList<Article>) this.sut
				.getArticles();
		assertEquals(0, output.size());
	}

	@Test
	public void shouldReturnNoArticlesWhenInvalidArticleNumber() {
		LinkedList<Article> output = this.sut
				.getArticles(TestStorageLocation.NON_EXISTING_ART_NR);
		assertEquals(0, output.size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnNullArticleNumber() {
		this.sut.getArticles(null);
	}

	@Test
	public void shoudInsertOneArticle() {
		Article mock = mock(Article.class);
		when(mock.getArtNr()).thenReturn(TestStorageLocation.ARTICLE_NAME);
		this.sut.insert(mock);

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

	@Test
	public void shouldInsertSeveralArticles() {
		insert5Articles();

		assertEquals(5, this.sut.getArticles().size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowWhenInsertNullArticleList() {
		this.sut.insertMany(null);
	}

	@Test
	public void shouldInsertTwice() {
		insert5Articles();
		insert5Articles();

		assertEquals(10, this.sut.getArticles().size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnInsertOneArticleWithTooLargeTotalWidth() {
		Article mock = mock(Article.class);
		when(mock.getWidth()).thenReturn(TestStorageLocation.TOO_LARGE_WIDTH);
		this.sut.insert(mock);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnInsertOneArticleWithTooSmallTotalWidth() {
		Article mock = mock(Article.class);
		when(mock.getWidth()).thenReturn(TestStorageLocation.TOO_SMALL_WIDTH);
		this.sut.insert(mock);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnInsertManyArticlesWithTooLargeTotalWidth() {
		LinkedList<Article> articles = this.generateArticles(5,
				TestStorageLocation.TOO_LARGE_WIDTH / 5);
		this.sut.insertMany(articles);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnInsertManyArticlesWithTooSmallTotalWidth() {
		LinkedList<Article> articles = this.generateArticles(5,
				TestStorageLocation.TOO_SMALL_WIDTH / 5);
		this.sut.insertMany(articles);
	}

	@Test
	public void shouldPickAll() {
		insert5Articles();

		LinkedList<Article> pickedArticles = this.sut.pickAll();
		assertEquals(5, pickedArticles.size());
		assertEquals(0, this.sut.getArticles().size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowWhenPickNullArticleNumber() {
		this.sut.pickAll(null);
	}

	@Test
	public void shouldPickAllArticlesWithArticleNumber() {

		LinkedList<Article> articles = insert5Articles();

		when(articles.get(0).getArtNr()).thenReturn("articleNumber");
		when(articles.get(1).getArtNr()).thenReturn("articleNumber");
		when(articles.get(2).getArtNr()).thenReturn("articleNumber");
		when(articles.get(3).getArtNr()).thenReturn("articleNumber2");
		when(articles.get(4).getArtNr()).thenReturn("articleNumber2");

		LinkedList<Article> pickedArticles = this.sut.pickAll("articleNumber");

		this.verifyInvokeGetArtNr(articles);

		assertEquals(3, pickedArticles.size());
		assertEquals(2, this.sut.getArticles().size());
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
}
