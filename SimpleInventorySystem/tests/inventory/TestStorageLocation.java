package inventory;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;

import org.junit.Test;

public class TestStorageLocation {

	private static String VALIDSTORAGE_NAME = "MyStorageLocation";
	private static int VALID_NUM_ARTICLES = 1;

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnNull() {
		new StorageLocation(null);
	}

	@Test
	public void shouldCreateInstanceWithName() {
		StorageLocation s = new StorageLocation(
				TestStorageLocation.VALIDSTORAGE_NAME);
		assertEquals(s.getName(), TestStorageLocation.VALIDSTORAGE_NAME);
	}

	@Test
	public void shouldCreateInstanceWithArticles() {
		LinkedList<Article> articles = this.createArticles(
				TestStorageLocation.VALID_NUM_ARTICLES, 0.0);

		StorageLocation s = new StorageLocation(
				TestStorageLocation.VALIDSTORAGE_NAME, articles);

		verifyInvokeGetWidth(articles);

		assertEquals(articles, s.getArticles());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnTooManyArticles() {
		LinkedList<Article> articles = this.createArticles(
				StorageLocation.MAX_ARTICLES + 1, 0.0);

		new StorageLocation(TestStorageLocation.VALIDSTORAGE_NAME, articles);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnTooLargeWidth() {
		LinkedList<Article> articles = this.createArticles(1, 1000.0);

		new StorageLocation(TestStorageLocation.VALIDSTORAGE_NAME, articles);

		this.verifyInvokeGetWidth(articles);
	}

	public LinkedList<Article> createArticles(int count, double width) {
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
