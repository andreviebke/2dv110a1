package inventory;

import static org.junit.Assert.assertEquals;
import inventory.exceptions.InvalidNameException;
import inventory.exceptions.InvalidWidthException;

import org.junit.*;

public class TestArticle {

	/* Test data */
	private static final String VALID_ART_NR = "12345";
	private static final String TOO_LONG_ART_NR = "123456789012345678901";
	private static final String EMPTY_ART_NR = "";
	private static final double VALID_WIDTH = 123.0;
	private static final double TOO_SMALL_WIDTH = -1;
	private static final double TOO_LARGE_WIDTH = 1000;

	private Article sut;

	@Before
	public void setUp() {
		this.sut = new Article();
	}

	/*
	 * Creation
	 */
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowWhenNullArticleNumber() {
		new Article(null, TestArticle.VALID_WIDTH);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowWhenTooLongArticleNumber() {
		new Article(TestArticle.TOO_LONG_ART_NR, TestArticle.VALID_WIDTH);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowWhenTooSmallWidth() {
		new Article(TestArticle.VALID_ART_NR, TestArticle.TOO_SMALL_WIDTH);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowWhenCreatingWithTooLargeWidth() {
		new Article(TestArticle.VALID_ART_NR, TestArticle.TOO_LARGE_WIDTH);
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
		assertEquals(this.sut.getArtNr(), TestArticle.EMPTY_ART_NR);
	}

	/*
	 * Getters and setters
	 */

	@Test
	public void shouldSetArtNr() {
		this.sut.setArtNr(TestArticle.VALID_ART_NR);
	}

	@Test
	public void shouldSetWidth() {
		this.sut.setWidth(TestArticle.VALID_WIDTH);
	}

	@Test(expected = InvalidWidthException.class)
	public void shouldThrowWhenTooLargeWidth() {
		this.sut.setWidth(TestArticle.TOO_LARGE_WIDTH);
	}

	@Test(expected = InvalidNameException.class)
	public void shouldThrowWhenTooLongArtNr() {
		this.sut.setArtNr(TestArticle.TOO_LONG_ART_NR);
	}

	@Test(expected = InvalidNameException.class)
	public void shouldThrowWhenNullArtNr() {
		this.sut.setArtNr(null);
	}
}
