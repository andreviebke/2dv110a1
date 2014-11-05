package inventory;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestStorageLocation {

	private static String STORAGE_NAME = "MyStorageLocation";

	@Before
	public void setUp() throws Exception {
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
	public void shouldCreateInstanceWithASetOfArticles() {
		@SuppressWarnings("unchecked")
		List<Article> input = mock(List.class);

		Article a1 = mock(Article.class);
		Article a2 = mock(Article.class);
		input.add(a1);
		input.add(a2);

		StorageLocation s = new StorageLocation(
				TestStorageLocation.STORAGE_NAME, input);
		assertEquals(input, s.getArticles());
	}

}
