package inventory;

import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestStorageLocation {

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
	public void shouldCreateInstanceWithName()
	{
		StorageLocation s = new StorageLocation("MyStorageLocation");
		assertEquals(s.getName(), "MyStorageLocation");
	}
	
	@Test
	public void shouldCreateInstanceWithASetOfArticles()
	{
		@SuppressWarnings("unchecked")
		List<Article> input = mock(List.class);
		
		Article a1 = mock(Article.class);
		Article a2 = mock(Article.class);		
		input.add(a1);
		input.add(a2);
		
		StorageLocation s = new StorageLocation("MyStorageLocation", input);
		assertEquals(input, s.getArticles());
	}

}
