package inventory;

public class Article {

	public Article(String artnr) {
		if(null == artnr)
			throw new IllegalArgumentException();
	}

	public Article() {
	}

}
