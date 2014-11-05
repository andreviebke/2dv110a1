package inventory;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestArticle {

	private static String ART_NR = "12345";
	private static double WIDTH = 123.0;
	private static double NEGATIVE_WIDTH = -1;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnNull() {
		new Article(null, 0);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnNegativeWidth()
	{
		new Article(TestArticle.ART_NR, TestArticle.NEGATIVE_WIDTH);
	}

	@Test
	public void shouldSetArtNrAndWidth() {
		Article art = new Article(TestArticle.ART_NR, TestArticle.WIDTH);
		assertEquals(art.getArtNr(), TestArticle.ART_NR);
		assertEquals(art.getWidth(), TestArticle.WIDTH, 0.001);
	}

	@Test
	public void shouldSetEmptyString() {
		Article art = new Article();
		assertEquals(art.getArtNr(), "");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnTooLargeArticleNumber()
	{
		Article art = new Article("123456789012345678901", TestArticle.WIDTH);
	}
}
