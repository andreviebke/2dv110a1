package inventory;

public class Article {

	public static int MAX_ARTNR_LENGTH = 20;
	public static double MIN_WIDTH = 0;
	public static double MAX_WIDTH = 999;
	
	private String artnr;
	private double width;
	
	public Article() {
		this.artnr = "";
	}

	public Article(String artnr, double width) {
		
		if(null == artnr)
			throw new IllegalArgumentException();
		
		if(artnr.length() > Article.MAX_ARTNR_LENGTH)
			throw new IllegalArgumentException();
		
		if(width < Article.MIN_WIDTH)
			throw new IllegalArgumentException();
		
		if(width > Article.MAX_WIDTH)
			throw new IllegalArgumentException();
		
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
