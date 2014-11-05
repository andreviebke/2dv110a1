package inventory;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestArticle {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void shouldCreateNewInstance() {
		new Article();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnNull()
	{
		new Article(null);
	}

}
