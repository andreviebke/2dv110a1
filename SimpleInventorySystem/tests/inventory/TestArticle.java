package inventory;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestArticle {

	/* Test data */
	private static String VALID_ART_NR = "12345";
	private static String TOO_LONG_ART_NR = "123456789012345678901";
	private static String NO_ART_NR = "";
	
	private static double VALID_WIDTH = 123.0;
	private static double TOO_SMALL_WIDTH = -1;
	private static double TOO_LARGE_WIDTH = 1000;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnNullArticleNumber() {
		new Article(null, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnInvalidWidth() {
		new Article(TestArticle.VALID_ART_NR, TestArticle.TOO_SMALL_WIDTH);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnTooLongArticleNumber() {
		new Article(TestArticle.TOO_LONG_ART_NR,
				TestArticle.VALID_WIDTH);
	}

	@Test
	public void shouldSetArtNrAndWidth() {
		Article art = new Article(TestArticle.VALID_ART_NR,
				TestArticle.VALID_WIDTH);
		assertEquals(art.getArtNr(), TestArticle.VALID_ART_NR);
		assertEquals(art.getWidth(), TestArticle.VALID_WIDTH, 0.001);
	}

	@Test
	public void shouldSetEmptyString() {
		Article art = new Article();
		assertEquals(art.getArtNr(), TestArticle.NO_ART_NR);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowOnTooLargeWidth()
	{
		new Article(TestArticle.VALID_ART_NR, TestArticle.TOO_LARGE_WIDTH);
	}
}
