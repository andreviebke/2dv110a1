package inventory;

import java.util.List;

public class StorageLocation {

	private String name;
	private List<Article> articles;

	public StorageLocation(String name) {
		if (null == name)
			throw new IllegalArgumentException();

		this.name = name;
	}

	public StorageLocation(String name, List<Article> input) {

		if (input.size() > 9)
			throw new IllegalArgumentException();		

		double totWidth = 0;
		
		for(Article a : input)
			totWidth += a.getWidth();
		
		if(totWidth > 999)
			throw new IllegalArgumentException();
			
		this.articles = input;
	}

	public String getName() {
		return this.name;
	}

	public List<Article> getArticles() {
		return this.articles;
	}

}
