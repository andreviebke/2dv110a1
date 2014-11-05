package inventory;

public class Article {

	private String artnr;
	private double width;
	
	public Article() {
		this.artnr = "";
	}

	public Article(String artnr, double width) {
		
		if(null == artnr)
			throw new IllegalArgumentException();
		
		if(artnr.length() > 20)
			throw new IllegalArgumentException();
		
		if(width < 0)
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
