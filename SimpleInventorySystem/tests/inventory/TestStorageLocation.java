package inventory;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestStorageLocation {

	private static String VALIDSTORAGE_NAME = "MyStorageLocation";
	private static String NON_EXISTING_ART_NR = "123456";
	private static int VALID_NUM_ARTICLES = 1;
	private static double TOO_LARGE_WIDTH = 1000.0;
	private static StorageLocation SUT;

	@BeforeClass
	public static void beforeClass() {

		TestStorageLocation.SUT = new StorageLocation(
				TestStorageLocation.VALIDSTORAGE_NAME);
		MockitoAnnotations.initMocks(TestStorageLocation.class);

	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnNullName() {

		new StorageLocation(null);
	}

	@Test
	public void shouldCreateInstanceWithName() {
		assertEquals(TestStorageLocation.SUT.getName(),
				TestStorageLocation.VALIDSTORAGE_NAME);
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

		LinkedList<Article> output = (LinkedList<Article>) TestStorageLocation.SUT
				.getArticles();
		assertEquals(0, output.size());
	}

	@Test
	public void shouldReturnNoArticlesWhenInvalidArticleNumber() {

		LinkedList<Article> output = TestStorageLocation.SUT
				.getArticles(TestStorageLocation.NON_EXISTING_ART_NR);
		assertEquals(0, output.size());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnNullArticleNumber()
	{
		TestStorageLocation.SUT.getArticles(null);
	}
	
	@Test
	public void shoudAddOneArticle()
	{
		Article mock = mock(Article.class);
		when(mock.getArtNr()).thenReturn("articleName");
		TestStorageLocation.SUT.insert(mock);
		
		assertEquals(1, TestStorageLocation.SUT.getArticles("articleName").size());
		assertEquals("articleName", TestStorageLocation.SUT.getArticles("articleName").get(0).getArtNr());
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
