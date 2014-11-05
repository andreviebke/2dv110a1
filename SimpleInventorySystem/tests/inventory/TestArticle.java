package inventory;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestArticle {

	private static String ART_NR = "12345";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnNull() {
		new Article(null);
	}

	@Test
	public void shouldSetArtNrAndWidth() {
		Article art = new Article(TestArticle.ART_NR, 123.0);
		assertEquals(art.getArtNr(), TestArticle.ART_NR);
		assertEquals(art.getWidth(), 123.0, 0.001);
	}

	@Test
	public void shouldSetEmptyStringOnNoArticleNumber()
	{
		Article art = new Article();
		assertEquals(art.getArtNr(), "");
	}
}
