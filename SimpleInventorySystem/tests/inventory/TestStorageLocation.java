package inventory;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class TestStorageLocation {

	private static String STORAGE_NAME = "MyStorageLocation";
	private static int MAX_ARTICLES = 10;
	private static int MIN_ARTICLES = 1;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnNull() {
		new StorageLocation(null);
	}

	@Test
	public void shouldCreateInstanceWithName() {
		StorageLocation s = new StorageLocation(
				TestStorageLocation.STORAGE_NAME);
		assertEquals(s.getName(), TestStorageLocation.STORAGE_NAME);
	}

	@Test
	public void shouldCreateInstanceWithArticles() {
		LinkedList<Article> articles = this
				.createArticles(TestStorageLocation.MIN_ARTICLES);

		StorageLocation s = new StorageLocation(
				TestStorageLocation.STORAGE_NAME, articles);
		assertEquals(articles, s.getArticles());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnTooManyArticles() {
		LinkedList<Article> articles = this
				.createArticles(TestStorageLocation.MAX_ARTICLES + 1);

		new StorageLocation(TestStorageLocation.STORAGE_NAME, articles);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnTooLargeWidth() {
		Article a = mock(Article.class);
		when(a.getWidth()).thenReturn(1000.0);
		
		LinkedList<Article> articles = new LinkedList<Article>();
		articles.add(a);
		
		new StorageLocation(TestStorageLocation.STORAGE_NAME, articles);
		
		verify(a.getWidth(), times(1));		
	}
	
	public LinkedList<Article> createArticles(int count) {
		LinkedList<Article> articles = new LinkedList<Article>();

		for (int i = 0; i < count; i++) {
			articles.add(mock(Article.class));
		}

		return articles;
	}

}
