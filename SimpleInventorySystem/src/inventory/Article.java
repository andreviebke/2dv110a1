package inventory;

public class Article {

	private String artnr;
	
	public Article(String artnr) {
		if(null == artnr)
			throw new IllegalArgumentException();
		
		this.artnr = artnr;
	}

	public Article() {
	}

	public String getArtNr() {
		return this.artnr;
	}

}
