package inventory;

public class Article {

	private String artnr;
	private double width;

	public Article(String artnr) {
		if (null == artnr)
			throw new IllegalArgumentException();

		this.artnr = artnr;
	}

	public Article() {
		this.artnr = "";
	}

	public Article(String artnr, double width) {
		this.artnr = artnr;
		this.width = width;
	}

	public String getArtNr() {
		return this.artnr;
	}

	public double getWidth() {
		return this.width;
	}

}
