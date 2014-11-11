package inventory;

import inventory.exceptions.InvalidNameException;
import inventory.exceptions.InvalidWidthException;

public class Article {

	public static int MAX_ARTNR_LENGTH = 20;
	public static double MIN_WIDTH = 0;
	public static double MAX_WIDTH = 999;

	private String artnr;
	private double width;

	/**
	 * Constructor
	 */
	public Article() {
		this.artnr = "";
	}

	/**
	 * Constructor
	 * @param artnr - article number
	 * @param width - width
	 */
	public Article(String artnr, double width) {

		if (null == artnr)
			throw new IllegalArgumentException();

		if (artnr.length() > Article.MAX_ARTNR_LENGTH)
			throw new IllegalArgumentException();

		if (width < Article.MIN_WIDTH)
			throw new IllegalArgumentException();

		if (width > Article.MAX_WIDTH)
			throw new IllegalArgumentException();

		this.artnr = artnr;
		this.width = width;
	}

	/**
	 * Returns article number
	 * @return article number
	 */
	public String getArtNr() {
		return this.artnr;
	}

	/**
	 * Returns width
	 * @return width
	 */
	public double getWidth() {
		return this.width;
	}

	/**
	 * Sets width
	 * @param width to set
	 */
	public void setWidth(double width) {
		if (width > Article.MAX_WIDTH)
			throw new InvalidWidthException();

		this.width = width;
	}

	/**
	 * Sets article number
	 * @param artNr - article number to set
	 */
	public void setArtNr(String artNr) {
		if (null == artNr)
			throw new InvalidNameException();

		if (artNr.length() > Article.MAX_ARTNR_LENGTH)
			throw new InvalidNameException();

		this.artnr = artNr;
	}

}
