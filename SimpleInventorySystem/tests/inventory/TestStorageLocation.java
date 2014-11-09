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
	public void shouldThrowWhenInsertNullArticle()
	{		
		this.sut.insert(null);
	}
	
	@Test
	public void shouldInsertSeveralArticles()
	{		
		LinkedList<Article> articles = this.generateArticles(5, 10);
		this.sut.insertMany(articles);
		
		assertEquals(5, this.sut.getArticles().size());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowWhenInsertNullArticleList()
	{
		this.sut.insertMany(null);
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
}
